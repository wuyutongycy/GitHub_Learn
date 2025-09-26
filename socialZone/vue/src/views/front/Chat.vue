<template>
  <div class="chat-container">
    <div class="chat-sidebar">
      <!-- 会话列表 -->
      <div class="session-header">
        <h3>私信</h3>
        <el-button size="small" @click="refreshSessions">
          <el-icon><Refresh /></el-icon>
        </el-button>
      </div>
      
      <div class="session-list">
        <div v-if="!sessions || sessions.length === 0" class="empty-sessions">
          <el-empty description="暂无会话" />
        </div>
        <template v-for="(session, index) in sessions" :key="session?.userId || index">
          <div
            v-if="session && session.userId"
            class="session-item"
            :class="{ 'active': currentSession?.userId === session.userId }"
            @click="selectSession(session)"
          >
            <div class="session-avatar">
              <img :src="session.avatarUrl || '/default-avatar.png'" :alt="session.nickname || '未知用户'">
              <div v-if="session.unreadCount && session.unreadCount > 0" class="unread-badge">{{ session.unreadCount }}</div>
            </div>
            <div class="session-info">
              <div class="session-name">{{ session.nickname || '未知用户' }}</div>
              <div class="session-last-message">{{ session.lastMessage || '开始聊天吧...' }}</div>
              <div class="session-time">{{ formatTime(session.lastTime) }}</div>
            </div>
          </div>
        </template>
      </div>
    </div>

    <div class="chat-main">
      <div v-if="!currentSession" class="no-session">
        <el-empty description="选择一个会话开始聊天" />
      </div>
      
      <div v-else class="chat-content">
        <!-- 聊天头部 -->
        <div class="chat-header">
          <div class="chat-user-info">
            <img :src="currentSession.avatarUrl" :alt="currentSession.nickname" class="chat-avatar">
            <span class="chat-user-name">{{ currentSession.nickname }}</span>
          </div>
          <div class="chat-actions">
            <el-button size="small" @click="viewProfile(currentSession.userId)">查看资料</el-button>
          </div>
        </div>

        <!-- 消息列表 -->
        <div ref="messagesContainer" class="messages-container">
          <div v-if="loadingMessages" class="loading-messages">
            <el-skeleton :rows="3" animated />
          </div>
          
          <div v-for="message in messages" :key="message.id" class="message-item">
            <div
              class="message-bubble"
              :class="{
                'own-message': message.fromUserId === account.id,
                'other-message': message.fromUserId !== account.id
              }"
            >
              <div v-if="message.fromUserId !== account.id" class="message-avatar">
                <img :src="currentSession.avatarUrl" :alt="currentSession.nickname">
              </div>
              
              <div class="message-content">
                <div class="message-text">{{ message.content }}</div>
                <div class="message-time">{{ formatTime(message.sendTime) }}</div>
              </div>
              
              <div v-if="message.fromUserId === account.id" class="message-avatar">
                <img :src="account.avatarUrl" :alt="account.nickname">
              </div>
            </div>
          </div>
        </div>

        <!-- 输入框 -->
        <div class="chat-input-area">
          <div class="input-toolbar">
            <!-- 可以添加表情、文件等功能 -->
          </div>
          <div class="input-container">
            <el-input
              v-model="newMessage"
              type="textarea"
              placeholder="输入消息..."
              :rows="3"
              resize="none"
              @keydown.ctrl.enter="sendMessage"
            />
            <el-button
              type="primary"
              :disabled="!newMessage.trim()"
              @click="sendMessage"
              class="send-button"
            >
              发送
            </el-button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, nextTick, onUnmounted } from 'vue'
import { ElMessage } from 'element-plus'
import { Refresh } from '@element-plus/icons-vue'
import request from '@/utils/request'
import { useRoute, useRouter } from 'vue-router'

const route = useRoute()
const router = useRouter()

// 当前用户信息
const account = ref(JSON.parse(localStorage.getItem('account') || '{}'))

// 响应式数据
const sessions = ref([])
const currentSession = ref(null)
const messages = ref([])
const newMessage = ref('')
const loadingMessages = ref(false)

// DOM引用
const messagesContainer = ref(null)

// 轮询定时器
let pollTimer = null

// 加载会话列表
const loadSessions = async () => {
  try {
    console.log('正在加载会话列表...')
    const res = await request.get('/privateMessage/sessions')
    console.log('会话列表响应:', res)
    if (res.code === '200') {
      console.log('会话数据:', res.data)
      // 详细输出每个会话的结构
      res.data.forEach((session, index) => {
        console.log(`会话${index}:`, session)
        console.log(`会话${index} userId:`, session.userId)
        console.log(`会话${index} nickname:`, session.nickname)
        console.log(`会话${index} avatarUrl:`, session.avatarUrl)
      })
      sessions.value = res.data || []
    } else {
      console.error('获取会话列表失败:', res.msg)
      ElMessage.error('获取会话列表失败: ' + res.msg)
    }
  } catch (error) {
    console.error('加载会话列表出错:', error)
    ElMessage.error('加载会话列表失败')
  }
}

// 刷新会话
const refreshSessions = () => {
  loadSessions()
}

// 选择会话
const selectSession = async (session) => {
  currentSession.value = session
  await loadMessages()
  startPolling()
  
  // 标记消息已读
  try {
    await request.put(`/privateMessage/markRead/${session.userId}`)
    session.unreadCount = 0
  } catch (error) {
    console.error('标记已读失败', error)
  }
}

// 加载消息列表
const loadMessages = async () => {
  if (!currentSession.value) return
  
  loadingMessages.value = true
  try {
    const res = await request.get(`/privateMessage/conversation/${currentSession.value.userId}`, {
      params: { pageNum: 1, pageSize: 50 }
    })
    if (res.code === '200') {
      messages.value = res.data.records || []
      scrollToBottom()
    }
  } catch (error) {
    ElMessage.error('加载消息失败')
  } finally {
    loadingMessages.value = false
  }
}

// 发送消息
const sendMessage = async () => {
  if (!newMessage.value.trim() || !currentSession.value) return
  
  try {
    const res = await request.post('/privateMessage/send', {
      toUserId: currentSession.value.userId,
      content: newMessage.value.trim()
    })
    
    if (res.code === '200') {
      newMessage.value = ''
      await loadMessages()
      updateSessionLastMessage(currentSession.value.userId, newMessage.value.trim())
    } else {
      ElMessage.error(res.msg)
    }
  } catch (error) {
    ElMessage.error('发送失败')
  }
}

// 更新会话最后消息
const updateSessionLastMessage = (userId, message) => {
  const session = sessions.value.find(s => s.userId === userId)
  if (session) {
    session.lastMessage = message
    session.lastTime = new Date().toISOString()
  }
}

// 滚动到底部
const scrollToBottom = () => {
  nextTick(() => {
    if (messagesContainer.value) {
      messagesContainer.value.scrollTop = messagesContainer.value.scrollHeight
    }
  })
}

// 开始轮询
const startPolling = () => {
  stopPolling()
  
  pollTimer = setInterval(() => {
    if (currentSession.value) {
      loadMessages()
      loadSessions() // 也更新会话列表
    }
  }, 30000) // 30秒轮询
}

// 停止轮询
const stopPolling = () => {
  if (pollTimer) {
    clearInterval(pollTimer)
    pollTimer = null
  }
}

// 查看用户资料
const viewProfile = (userId) => {
  router.push(`/front/user?id=${userId}`)
}

// 格式化时间
const formatTime = (timeStr) => {
  if (!timeStr) return ''
  
  const time = new Date(timeStr)
  const now = new Date()
  const diff = now - time
  
  if (diff < 60000) return '刚刚'
  if (diff < 3600000) return `${Math.floor(diff / 60000)}分钟前`
  if (diff < 86400000) return `${Math.floor(diff / 3600000)}小时前`
  
  // 同一天显示时间
  if (time.toDateString() === now.toDateString()) {
    return time.toTimeString().slice(0, 5)
  }
  
  // 显示日期
  return time.toLocaleDateString()
}

// 从URL参数打开指定用户的对话
const openChatFromUrl = async () => {
  const userId = route.query.userId
  if (userId) {
    // 先加载会话列表
    await loadSessions()
    
    // 查找对应会话
    let session = sessions.value.find(s => s.userId == userId)
    
    // 如果没有现有会话，创建新会话
    if (!session) {
      try {
        const userRes = await request.get(`/user/${userId}`)
        if (userRes.code === '200') {
          session = {
            userId: userRes.data.id,
            nickname: userRes.data.nickname,
            avatarUrl: userRes.data.avatarUrl,
            lastMessage: '',
            lastTime: null,
            unreadCount: 0
          }
          sessions.value.unshift(session)
        }
      } catch (error) {
        ElMessage.error('用户不存在')
        return
      }
    }
    
    selectSession(session)
  }
}

// 页面初始化
onMounted(() => {
  loadSessions().then(() => {
    openChatFromUrl()
  })
})

// 页面销毁
onUnmounted(() => {
  stopPolling()
})
</script>

<style scoped lang="scss">
.chat-container {
  display: flex;
  height: calc(100vh - 60px);
  background: #fff;
}

.chat-sidebar {
  width: 300px;
  border-right: 1px solid #e0e0e0;
  display: flex;
  flex-direction: column;

  .session-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 16px;
    border-bottom: 1px solid #e0e0e0;
    background: #f8f9fa;

    h3 {
      margin: 0;
      font-size: 18px;
      color: #333;
    }
  }

  .session-list {
    flex: 1;
    overflow-y: auto;

    .empty-sessions {
      padding: 40px 20px;
      text-align: center;
    }

    .session-item {
      display: flex;
      align-items: center;
      padding: 16px;
      cursor: pointer;
      border-bottom: 1px solid #f0f0f0;
      transition: background-color 0.2s ease;

      &:hover {
        background-color: #f5f5f5;
      }

      &.active {
        background-color: #e6f7ff;
        border-right: 3px solid #1890ff;
      }

      .session-avatar {
        position: relative;
        margin-right: 12px;

        img {
          width: 48px;
          height: 48px;
          border-radius: 50%;
          object-fit: cover;
        }

        .unread-badge {
          position: absolute;
          top: -4px;
          right: -4px;
          background: #ff4d4f;
          color: #fff;
          font-size: 10px;
          padding: 2px 6px;
          border-radius: 10px;
          min-width: 16px;
          text-align: center;
        }
      }

      .session-info {
        flex: 1;
        min-width: 0;

        .session-name {
          font-size: 14px;
          font-weight: 600;
          color: #333;
          margin-bottom: 4px;
        }

        .session-last-message {
          font-size: 12px;
          color: #666;
          overflow: hidden;
          text-overflow: ellipsis;
          white-space: nowrap;
          margin-bottom: 2px;
        }

        .session-time {
          font-size: 10px;
          color: #999;
        }
      }
    }
  }
}

.chat-main {
  flex: 1;
  display: flex;
  flex-direction: column;

  .no-session {
    flex: 1;
    display: flex;
    align-items: center;
    justify-content: center;
    background: #fafafa;
  }

  .chat-content {
    flex: 1;
    display: flex;
    flex-direction: column;
  }
}

.chat-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px 20px;
  border-bottom: 1px solid #e0e0e0;
  background: #fff;

  .chat-user-info {
    display: flex;
    align-items: center;

    .chat-avatar {
      width: 36px;
      height: 36px;
      border-radius: 50%;
      margin-right: 12px;
      object-fit: cover;
    }

    .chat-user-name {
      font-size: 16px;
      font-weight: 600;
      color: #333;
    }
  }
}

.messages-container {
  flex: 1;
  overflow-y: auto;
  padding: 20px;
  background: #f5f5f5;

  .loading-messages {
    padding: 20px 0;
  }

  .message-item {
    margin-bottom: 16px;

    .message-bubble {
      display: flex;
      align-items: flex-end;
      gap: 8px;

      &.own-message {
        justify-content: flex-end;
        
        .message-content {
          order: -1;
          background: #1890ff;
          color: #fff;
          text-align: right;

          .message-time {
            color: rgba(255, 255, 255, 0.7);
          }
        }
      }

      &.other-message {
        justify-content: flex-start;

        .message-content {
          background: #fff;
          color: #333;
        }
      }

      .message-avatar {
        width: 32px;
        height: 32px;
        flex-shrink: 0;

        img {
          width: 100%;
          height: 100%;
          border-radius: 50%;
          object-fit: cover;
        }
      }

      .message-content {
        max-width: 60%;
        padding: 12px 16px;
        border-radius: 18px;
        box-shadow: 0 1px 2px rgba(0, 0, 0, 0.1);

        .message-text {
          font-size: 14px;
          line-height: 1.4;
          word-break: break-word;
          margin-bottom: 4px;
        }

        .message-time {
          font-size: 10px;
          opacity: 0.7;
        }
      }
    }
  }
}

.chat-input-area {
  border-top: 1px solid #e0e0e0;
  background: #fff;

  .input-toolbar {
    padding: 8px 20px;
    border-bottom: 1px solid #f0f0f0;
    // 可以放置表情、文件等按钮
  }

  .input-container {
    display: flex;
    padding: 16px 20px;
    gap: 12px;

    :deep(.el-textarea) {
      flex: 1;

      .el-textarea__inner {
        border-radius: 8px;
        border: 1px solid #d9d9d9;
        resize: none;
        font-size: 14px;

        &:focus {
          border-color: #1890ff;
          box-shadow: 0 0 0 2px rgba(24, 144, 255, 0.2);
        }
      }
    }

    .send-button {
      align-self: flex-end;
      height: 40px;
      padding: 0 24px;
      border-radius: 8px;
    }
  }
}

// 响应式设计
@media (max-width: 768px) {
  .chat-container {
    flex-direction: column;
  }

  .chat-sidebar {
    width: 100%;
    height: 200px;
  }

  .messages-container .message-bubble .message-content {
    max-width: 80%;
  }
}
</style>
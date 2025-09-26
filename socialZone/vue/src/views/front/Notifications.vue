<template>
  <div class="notification-center">
    <div class="notification-header">
      <h2>消息通知</h2>
      <div class="header-actions">
        <el-button size="small" @click="markAllAsRead">全部标记已读</el-button>
        <el-button size="small" @click="showSettings = true">通知设置</el-button>
      </div>
    </div>

    <!-- 通知分类标签 -->
    <el-tabs v-model="activeCategory" @tab-change="loadNotifications">
      <el-tab-pane label="全部" name="">
        <template #label>
          <span>全部</span>
          <el-badge v-if="unreadCounts.total > 0" :value="unreadCounts.total" class="badge-small"></el-badge>
        </template>
      </el-tab-pane>
      <el-tab-pane label="互动消息" name="social">
        <template #label>
          <span>互动消息</span>
          <el-badge v-if="unreadCounts.socialCount > 0" :value="unreadCounts.socialCount" class="badge-small"></el-badge>
        </template>
      </el-tab-pane>
      <el-tab-pane label="系统消息" name="system">
        <template #label>
          <span>系统消息</span>
          <el-badge v-if="unreadCounts.systemCount > 0" :value="unreadCounts.systemCount" class="badge-small"></el-badge>
        </template>
      </el-tab-pane>
      <el-tab-pane label="私信" name="private">
        <template #label>
          <span>私信</span>
          <el-badge v-if="unreadCounts.privateCount > 0" :value="unreadCounts.privateCount" class="badge-small"></el-badge>
        </template>
      </el-tab-pane>
    </el-tabs>

    <!-- 通知列表 -->
    <div class="notification-list">
      <div v-if="loading" class="loading-container">
        <el-skeleton :rows="5" animated />
      </div>
      
      <div v-else-if="notifications.length === 0" class="empty-state">
        <el-empty description="暂无消息"></el-empty>
      </div>

      <div v-else>
        <div
          v-for="notification in notifications"
          :key="notification.id"
          class="notification-item"
          :class="{ 'unread': !notification.isRead }"
          @click="handleNotificationClick(notification)"
        >
          <div class="notification-avatar">
            <img :src="notification.fromAvatarUrl" :alt="notification.fromNickname">
          </div>
          
          <div class="notification-content">
            <div class="notification-text">
              <span class="user-name">{{ notification.fromNickname }}</span>
              <span class="action-text">{{ getActionText(notification.type) }}</span>
            </div>
            <div class="notification-time">{{ formatTime(notification.time) }}</div>
          </div>

          <div class="notification-actions">
            <el-icon v-if="!notification.isRead" class="unread-dot"></el-icon>
            <el-button
              size="small"
              text
              @click.stop="markAsRead(notification.id)"
              v-if="!notification.isRead"
            >
              标记已读
            </el-button>
          </div>
        </div>

        <!-- 分页 -->
        <el-pagination
          v-if="total > pageSize"
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          :total="total"
          layout="total, prev, pager, next"
          @current-change="loadNotifications"
          class="pagination"
        />
      </div>
    </div>

    <!-- 通知设置对话框 -->
    <el-dialog v-model="showSettings" title="通知设置" width="500px">
      <el-form :model="notificationSettings" label-width="120px">
        <el-form-item label="点赞通知">
          <el-switch v-model="notificationSettings.likeNotify" />
        </el-form-item>
        <el-form-item label="关注通知">
          <el-switch v-model="notificationSettings.followNotify" />
        </el-form-item>
        <el-form-item label="评论通知">
          <el-switch v-model="notificationSettings.commentNotify" />
        </el-form-item>
        <el-form-item label="收藏通知">
          <el-switch v-model="notificationSettings.collectNotify" />
        </el-form-item>
        <el-form-item label="提及通知">
          <el-switch v-model="notificationSettings.mentionNotify" />
        </el-form-item>
        <el-form-item label="私信通知">
          <el-switch v-model="notificationSettings.privateNotify" />
        </el-form-item>
        <el-form-item label="好友申请通知">
          <el-switch v-model="notificationSettings.friendRequestNotify" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showSettings = false">取消</el-button>
        <el-button type="primary" @click="saveSettings">保存设置</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { ElMessage } from 'element-plus'
import request from '@/utils/request'
import { useRouter } from 'vue-router'

const router = useRouter()

// 响应式数据
const activeCategory = ref('')
const notifications = ref([])
const loading = ref(false)
const showSettings = ref(false)

// 分页参数
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

// 未读数量
const unreadCounts = ref({
  total: 0,
  systemCount: 0,
  socialCount: 0,
  privateCount: 0
})

// 通知设置
const notificationSettings = ref({
  likeNotify: true,
  followNotify: true,
  commentNotify: true,
  collectNotify: true,
  mentionNotify: true,
  privateNotify: true,
  friendRequestNotify: true
})

// 加载通知列表
const loadNotifications = async (resetPage = false) => {
  if (resetPage) {
    currentPage.value = 1
  }
  
  loading.value = true
  try {
    const res = await request.get('/message/list', {
      params: {
        category: activeCategory.value,
        pageNum: currentPage.value,
        pageSize: pageSize.value
      }
    })
    
    if (res.code === '200') {
      notifications.value = res.data.records
      total.value = res.data.total
    }
  } catch (error) {
    ElMessage.error('加载通知失败')
  } finally {
    loading.value = false
  }
}

// 加载未读数量
const loadUnreadCounts = async () => {
  try {
    const res = await request.get('/message/unreadCount')
    if (res.code === '200') {
      unreadCounts.value = {
        total: res.data.systemCount + res.data.socialCount + res.data.privateCount,
        ...res.data
      }
    }
  } catch (error) {
    console.error('加载未读数量失败', error)
  }
}

// 标记单个消息已读
const markAsRead = async (messageId) => {
  try {
    const res = await request.put(`/message/read/${messageId}`)
    if (res.code === '200') {
      // 更新本地状态
      const notification = notifications.value.find(n => n.id === messageId)
      if (notification) {
        notification.isRead = true
      }
      loadUnreadCounts()
    }
  } catch (error) {
    ElMessage.error('标记失败')
  }
}

// 全部标记已读
const markAllAsRead = async () => {
  try {
    const res = await request.put('/message/readAll', null, {
      params: { category: activeCategory.value }
    })
    if (res.code === '200') {
      ElMessage.success('已全部标记为已读')
      loadNotifications()
      loadUnreadCounts()
    }
  } catch (error) {
    ElMessage.error('操作失败')
  }
}

// 处理通知点击
const handleNotificationClick = (notification) => {
  // 标记为已读
  if (!notification.isRead) {
    markAsRead(notification.id)
  }
  
  // 根据通知类型跳转
  switch (notification.type) {
    case 'like':
    case 'comment':
    case 'collect':
      if (notification.itemId) {
        // 跳转到博文详情
        router.push(`/front/blog/${notification.itemId}`)
      }
      break
    case 'follow':
      // 跳转到用户主页
      router.push(`/front/user?id=${notification.fromUserId}`)
      break
    case 'friend_request':
    case 'friend_accept':
      // 跳转到好友管理页面
      router.push('/front/friends?tab=requests')
      break
    case 'private':
      // 跳转到私信页面
      router.push(`/front/chat?userId=${notification.fromUserId}`)
      break
  }
}

// 获取动作文本
const getActionText = (type) => {
  const actionMap = {
    like: '点赞了你的笔记',
    follow: '关注了你',
    comment: '评论了你的笔记',
    collect: '收藏了你的笔记',
    mention: '在评论中提到了你',
    friend_request: '请求添加你为好友',
    friend_accept: '同意了你的好友申请',
    private: '给你发送了私信'
  }
  return actionMap[type] || '与你互动'
}

// 格式化时间
const formatTime = (timeStr) => {
  const time = new Date(timeStr)
  const now = new Date()
  const diff = now - time
  
  if (diff < 60000) return '刚刚'
  if (diff < 3600000) return `${Math.floor(diff / 60000)}分钟前`
  if (diff < 86400000) return `${Math.floor(diff / 3600000)}小时前`
  if (diff < 2592000000) return `${Math.floor(diff / 86400000)}天前`
  
  return time.toLocaleDateString()
}

// 加载通知设置
const loadSettings = async () => {
  try {
    const res = await request.get('/message/settings')
    if (res.code === '200') {
      notificationSettings.value = res.data
    }
  } catch (error) {
    console.error('加载设置失败', error)
  }
}

// 保存通知设置
const saveSettings = async () => {
  try {
    const res = await request.put('/message/settings', notificationSettings.value)
    if (res.code === '200') {
      ElMessage.success('设置已保存')
      showSettings.value = false
    } else {
      ElMessage.error(res.msg)
    }
  } catch (error) {
    ElMessage.error('保存失败')
  }
}

// 页面初始化
onMounted(() => {
  loadNotifications()
  loadUnreadCounts()
  loadSettings()
})
</script>

<style scoped lang="scss">
.notification-center {
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
}

.notification-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding-bottom: 16px;
  border-bottom: 1px solid #e0e0e0;

  h2 {
    margin: 0;
    font-size: 24px;
    color: #333;
  }

  .header-actions {
    display: flex;
    gap: 12px;
  }
}

:deep(.el-tabs) {
  .el-tabs__header {
    margin-bottom: 20px;
  }
  
  .badge-small {
    margin-left: 8px;
    
    .el-badge__content {
      font-size: 10px;
      height: 16px;
      line-height: 16px;
      padding: 0 4px;
      min-width: 16px;
    }
  }
}

.notification-list {
  .loading-container {
    padding: 20px 0;
  }

  .empty-state {
    padding: 60px 0;
    text-align: center;
  }
}

.notification-item {
  display: flex;
  align-items: flex-start;
  padding: 16px;
  border: 1px solid #e0e0e0;
  border-radius: 8px;
  margin-bottom: 12px;
  cursor: pointer;
  transition: all 0.3s ease;
  background: #fff;

  &:hover {
    box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  }

  &.unread {
    border-color: #1890ff;
    background: #f0f7ff;
  }

  .notification-avatar {
    width: 40px;
    height: 40px;
    margin-right: 12px;
    flex-shrink: 0;

    img {
      width: 100%;
      height: 100%;
      border-radius: 50%;
      object-fit: cover;
    }
  }

  .notification-content {
    flex: 1;
    min-width: 0;

    .notification-text {
      font-size: 14px;
      line-height: 1.5;
      color: #333;
      margin-bottom: 4px;

      .user-name {
        font-weight: 600;
        color: #1890ff;
      }

      .action-text {
        margin-left: 4px;
      }
    }

    .notification-time {
      font-size: 12px;
      color: #999;
    }
  }

  .notification-actions {
    display: flex;
    align-items: center;
    gap: 8px;
    flex-shrink: 0;

    .unread-dot {
      color: #1890ff;
      font-size: 8px;
    }
  }
}

.pagination {
  margin-top: 20px;
  text-align: center;
}
</style>
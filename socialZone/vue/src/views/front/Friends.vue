<template>
  <div class="friend-management">
    <!-- 标签页切换 -->
    <el-tabs v-model="activeTab" @tab-change="handleTabChange">
      <el-tab-pane label="我的好友" name="friends">
        <div class="friend-list">
          <div v-if="friends.length === 0" class="empty-state">
            <el-empty description="暂无好友"></el-empty>
          </div>
          <div v-else class="user-cards">
            <div v-for="friend in friends" :key="friend.id" class="user-card">
              <img :src="friend.avatarUrl" :alt="friend.nickname" class="user-avatar">
              <div class="user-info">
                <h4 class="user-name">{{ friend.nickname }}</h4>
                <p class="user-desc">{{ friend.info || '这个人很懒，什么都没写' }}</p>
              </div>
              <div class="user-actions">
                <el-button size="small" @click="sendMessage(friend.id)">私信</el-button>
                <el-button size="small" type="danger" @click="removeFriend(friend.id)">解除</el-button>
              </div>
            </div>
          </div>
        </div>
      </el-tab-pane>

      <el-tab-pane label="好友申请" name="requests">
        <el-tabs v-model="requestTab" type="card">
          <el-tab-pane label="收到的申请" name="received">
            <div class="request-list">
              <div v-if="receivedRequests.length === 0" class="empty-state">
                <el-empty description="暂无新申请"></el-empty>
              </div>
              <div v-else>
                <div v-for="request in receivedRequests" :key="request.id" class="request-item">
                  <img :src="request.fromAvatarUrl" :alt="request.fromNickname" class="request-avatar">
                  <div class="request-info">
                    <h4>{{ request.fromNickname }}</h4>
                    <p class="request-message">{{ request.message || '请求添加你为好友' }}</p>
                    <p class="request-time">{{ request.createTime }}</p>
                  </div>
                  <div class="request-actions" v-if="request.status === 0">
                    <el-button size="small" type="primary" @click="handleRequest(request.id, 1)">同意</el-button>
                    <el-button size="small" @click="handleRequest(request.id, 2)">拒绝</el-button>
                  </div>
                  <div v-else class="request-status">
                    <el-tag :type="request.status === 1 ? 'success' : 'danger'">
                      {{ request.status === 1 ? '已同意' : '已拒绝' }}
                    </el-tag>
                  </div>
                </div>
              </div>
            </div>
          </el-tab-pane>

          <el-tab-pane label="发出的申请" name="sent">
            <div class="request-list">
              <div v-if="sentRequests.length === 0" class="empty-state">
                <el-empty description="暂无申请记录"></el-empty>
              </div>
              <div v-else>
                <div v-for="request in sentRequests" :key="request.id" class="request-item">
                  <img :src="request.toAvatarUrl" :alt="request.toNickname" class="request-avatar">
                  <div class="request-info">
                    <h4>{{ request.toNickname }}</h4>
                    <p class="request-message">{{ request.message || '申请添加好友' }}</p>
                    <p class="request-time">{{ request.createTime }}</p>
                  </div>
                  <div class="request-actions" v-if="request.status === 0">
                    <el-button size="small" type="danger" @click="withdrawRequest(request.id)">撤回</el-button>
                  </div>
                  <div v-else class="request-status">
                    <el-tag :type="request.status === 1 ? 'success' : 'info'">
                      {{ request.status === 1 ? '已同意' : '已拒绝' }}
                    </el-tag>
                  </div>
                </div>
              </div>
            </div>
          </el-tab-pane>
        </el-tabs>
      </el-tab-pane>

      <el-tab-pane label="关注列表" name="follows">
        <div class="follow-list">
          <div v-if="followingList.length === 0" class="empty-state">
            <el-empty description="暂无关注的用户"></el-empty>
          </div>
          <div v-else class="user-cards">
            <div v-for="follow in followingList" :key="follow.id" class="user-card">
              <img :src="follow.avatarUrl" :alt="follow.nickname" class="user-avatar">
              <div class="user-info">
                <h4 class="user-name">{{ follow.nickname }}</h4>
                <p class="user-desc">{{ follow.info || '这个人很懒，什么都没写' }}</p>
              </div>
              <div class="user-actions">
                <el-button size="small" @click="sendMessage(follow.itemId)">私信</el-button>
                <el-button size="small" type="danger" @click="unfollowUser(follow.itemId)">取消关注</el-button>
              </div>
            </div>
          </div>
        </div>
      </el-tab-pane>

      <el-tab-pane label="粉丝列表" name="followers">
        <div class="follower-list">
          <div v-if="followersList.length === 0" class="empty-state">
            <el-empty description="暂无粉丝"></el-empty>
          </div>
          <div v-else class="user-cards">
            <div v-for="follower in followersList" :key="follower.id" class="user-card">
              <img :src="follower.avatarUrl" :alt="follower.nickname" class="user-avatar">
              <div class="user-info">
                <h4 class="user-name">{{ follower.nickname }}</h4>
                <p class="user-desc">{{ follower.info || '这个人很懒，什么都没写' }}</p>
              </div>
              <div class="user-actions">
                <el-button size="small" @click="sendMessage(follower.userId)">私信</el-button>
                <el-button 
                  v-if="!isFollowing(follower.userId)" 
                  size="small" 
                  type="primary" 
                  @click="followUser(follower.userId)"
                >
                  回关
                </el-button>
                <span v-else class="already-following">已关注</span>
              </div>
            </div>
          </div>
        </div>
      </el-tab-pane>
    </el-tabs>

    <!-- 发送好友申请对话框 -->
    <el-dialog v-model="sendRequestVisible" title="发送好友申请" width="400px">
      <el-form :model="requestForm" label-width="80px">
        <el-form-item label="申请消息">
          <el-input
            v-model="requestForm.message"
            type="textarea"
            placeholder="说点什么吧..."
            :rows="3"
            maxlength="200"
            show-word-limit
          ></el-input>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="sendRequestVisible = false">取消</el-button>
        <el-button type="primary" @click="confirmSendRequest">发送申请</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import request from '@/utils/request'
import { useRouter } from 'vue-router'

const router = useRouter()

// 当前活动标签
const activeTab = ref('friends')
const requestTab = ref('received')

// 数据列表
const friends = ref([])
const receivedRequests = ref([])
const sentRequests = ref([])
const followingList = ref([])
const followersList = ref([])

// 对话框控制
const sendRequestVisible = ref(false)
const requestForm = ref({
  toUserId: null,
  message: '你好，我想和你成为好友！'
})

// 切换标签
const handleTabChange = (tabName) => {
  switch (tabName) {
    case 'friends':
      loadFriends()
      break
    case 'requests':
      loadRequests()
      break
    case 'follows':
      loadFollows()
      break
    case 'followers':
      // 同时加载关注列表和粉丝列表，以便判断回关状态
      loadFollows()
      loadFollowers()
      break
  }
}

// 加载好友列表
const loadFriends = async () => {
  try {
    const res = await request.get('/friend/list')
    if (res.code === '200') {
      friends.value = res.data
    }
  } catch (error) {
    ElMessage.error('加载好友列表失败')
  }
}

// 加载好友申请
const loadRequests = async () => {
  try {
    const [receivedRes, sentRes] = await Promise.all([
      request.get('/friend/requests?type=received'),
      request.get('/friend/requests?type=sent')
    ])
    
    if (receivedRes.code === '200') {
      receivedRequests.value = receivedRes.data
    }
    if (sentRes.code === '200') {
      sentRequests.value = sentRes.data
    }
  } catch (error) {
    ElMessage.error('加载申请列表失败')
  }
}

// 处理好友申请
const handleRequest = async (requestId, action) => {
  try {
    const res = await request.put(`/friend/request/${requestId}?action=${action}`)
    if (res.code === '200') {
      ElMessage.success(res.data || (action === 1 ? '已同意申请' : '已拒绝申请'))
      loadRequests()
      if (action === 1) {
        loadFriends() // 同意后刷新好友列表
      }
    } else {
      ElMessage.error(res.msg)
    }
  } catch (error) {
    ElMessage.error('操作失败')
  }
}

// 撤回申请
const withdrawRequest = async (requestId) => {
  try {
    await ElMessageBox.confirm('确定要撤回这个申请吗？', '确认操作', {
      type: 'warning'
    })
    
    const res = await request.delete(`/friend/request/${requestId}`)
    if (res.code === '200') {
      ElMessage.success('申请已撤回')
      loadRequests()
    } else {
      ElMessage.error(res.msg)
    }
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('撤回失败')
    }
  }
}

// 解除好友关系
const removeFriend = async (friendId) => {
  try {
    await ElMessageBox.confirm('确定要解除好友关系吗？', '确认操作', {
      type: 'warning'
    })
    
    const res = await request.delete(`/friend/${friendId}`)
    if (res.code === '200') {
      ElMessage.success('已解除好友关系')
      loadFriends()
    } else {
      ElMessage.error(res.msg)
    }
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('操作失败')
    }
  }
}

// 发送私信
const sendMessage = (userId) => {
  router.push(`/front/chat?userId=${userId}`)
}

// 加载关注列表
const loadFollows = async () => {
  try {
    const res = await request.get('/follow/following')
    if (res.code === '200') {
      followingList.value = res.data
    }
  } catch (error) {
    ElMessage.error('加载关注列表失败')
  }
}

// 加载粉丝列表
const loadFollowers = async () => {
  try {
    const res = await request.get('/follow/followers')
    if (res.code === '200') {
      followersList.value = res.data
    }
  } catch (error) {
    ElMessage.error('加载粉丝列表失败')
  }
}

// 取消关注
const unfollowUser = async (userId) => {
  try {
    await ElMessageBox.confirm('确定要取消关注吗？', '确认操作', {
      type: 'warning'
    })
    
    const res = await request.delete(`/follow/unfollow/${userId}`)
    if (res.code === '200') {
      ElMessage.success('已取消关注')
      loadFollows()
    } else {
      ElMessage.error(res.msg)
    }
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('操作失败')
    }
  }
}

// 关注用户
const followUser = async (userId) => {
  try {
    const res = await request.post('/follow', { itemId: userId })
    if (res.code === '200') {
      ElMessage.success('关注成功')
      loadFollowers() // 刷新粉丝列表（如果在粉丝页面）
    } else {
      ElMessage.error(res.msg)
    }
  } catch (error) {
    ElMessage.error('关注失败')
  }
}

// 检查是否已经关注了某个用户
const isFollowing = (userId) => {
  return followingList.value.some(follow => follow.itemId === userId)
}

// 发送好友申请
const sendFriendRequest = (userId) => {
  requestForm.value.toUserId = userId
  sendRequestVisible.value = true
}

// 确认发送申请
const confirmSendRequest = async () => {
  try {
    const res = await request.post('/friend/request', requestForm.value)
    if (res.code === '200') {
      ElMessage.success('申请已发送')
      sendRequestVisible.value = false
      loadRequests()
    } else {
      ElMessage.error(res.msg)
    }
  } catch (error) {
    ElMessage.error('发送失败')
  }
}

// 页面初始化
onMounted(() => {
  loadFriends()
})
</script>

<style scoped lang="scss">
.friend-management {
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;

  :deep(.el-tabs__header) {
    margin-bottom: 20px;
  }
}

.user-cards {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 16px;
}

.user-card {
  display: flex;
  align-items: center;
  padding: 16px;
  border: 1px solid #e0e0e0;
  border-radius: 8px;
  background: #fff;
  transition: all 0.3s ease;

  &:hover {
    box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  }

  .user-avatar {
    width: 50px;
    height: 50px;
    border-radius: 50%;
    margin-right: 12px;
  }

  .user-info {
    flex: 1;
    min-width: 0;

    .user-name {
      font-size: 16px;
      font-weight: 600;
      margin: 0 0 4px 0;
      color: #333;
    }

    .user-desc {
      font-size: 12px;
      color: #666;
      margin: 0;
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;
    }
  }

  .user-actions {
    display: flex;
    gap: 8px;
    align-items: center;
  }
  
  .already-following {
    color: #909399;
    font-size: 13px;
    padding: 6px 12px;
    background: #f4f4f5;
    border-radius: 4px;
    border: 1px solid #e4e7ed;
  }
}

.request-list {
  .request-item {
    display: flex;
    align-items: center;
    padding: 16px;
    border: 1px solid #e0e0e0;
    border-radius: 8px;
    margin-bottom: 12px;
    background: #fff;

    .request-avatar {
      width: 48px;
      height: 48px;
      border-radius: 50%;
      margin-right: 16px;
    }

    .request-info {
      flex: 1;
      min-width: 0;

      h4 {
        font-size: 16px;
        font-weight: 600;
        margin: 0 0 4px 0;
        color: #333;
      }

      .request-message {
        font-size: 14px;
        color: #666;
        margin: 0 0 4px 0;
      }

      .request-time {
        font-size: 12px;
        color: #999;
        margin: 0;
      }
    }

    .request-actions {
      display: flex;
      gap: 8px;
    }

    .request-status {
      display: flex;
      align-items: center;
    }
  }
}

.empty-state {
  padding: 40px 0;
  text-align: center;
}

.follow-list,
.follower-list {
  padding: 20px 0;
  text-align: center;
}
</style>
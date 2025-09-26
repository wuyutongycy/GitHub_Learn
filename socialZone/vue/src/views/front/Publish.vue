<script setup>
import {ref, reactive, onMounted, nextTick, shallowRef} from 'vue'
import { ElMessage } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'

import '@wangeditor/editor/dist/css/style.css'
import { Editor, Toolbar } from '@wangeditor/editor-for-vue'

import axios from 'axios'
import request from '../../utils/request'
import { serverHost } from '../../../config/config.default'

// 响应式数据
const form = reactive({
  name: '',
  typeId: '',
  content: '',
  img: '',
  video: '',
  category: '',
  visibility: 'public' // 默认公开可见
})

// 内容富文本
const htmlContent = ref('');
const editorRefContent = shallowRef();

// 可见性选项
const visibilityOptions = [
  { value: 'public', label: '全部可见', description: '所有用户都可以看到此内容' },
  { value: 'friends', label: '好友可见', description: '只有我的好友可以看到此内容' },
  { value: 'followers', label: '关注我的可见', description: '只有关注我的用户可以看到此内容' },
  { value: 'private', label: '仅自己可见', description: '只有我自己可以看到此内容' }
]

const ruleFormRef = ref(null)
const videoRef = ref(null)
const canvasRef = ref(null)

// 新增：视频封面预览和提交状态
const videoCoverPreview = ref('') // 用于预览的封面图片（base64）
const isSubmitting = ref(false) // 提交状态

// 表单验证规则
const rules = reactive({
  name: [
    { required: true, message: '请输入标题', trigger: 'blur' }
  ],
  typeId: [
    { required: true, message: '请选择分类', trigger: 'blur' }
  ]
})

// 自定义上传方法
const customUpload = (file, insertFn) => {
  const formData = new FormData()
  formData.append('file', file)
  axios({
    url: `${serverHost}/web/upload`,
    method: 'post',
    data: formData,
    headers: {'Content-Type': 'multipart/form-data'},
  }).then(res => {
    insertFn(res.data)
  })
}

// wangEditor 配置
const editorConfig = {
  placeholder: '请输入内容...',
  MENU_CONF: {
    uploadImage: {
      customUpload: (file, insertFn) => {
        customUpload(file, insertFn)
      },
    },
    uploadVideo: {
      customUpload: (file, insertFn) => {
        customUpload(file, insertFn)
      },
    },
  }
}

// 获取可见性图标
const getVisibilityIcon = (visibility) => {
  const iconMap = {
    'public': 'el-icon-view',
    'friends': 'el-icon-user',
    'followers': 'el-icon-s-custom',
    'private': 'el-icon-lock'
  }
  return iconMap[visibility] || 'el-icon-view'
}

// 切换分类标签
const changeCategory = () => {
  form.img = ''
  form.video = ''
  videoCoverPreview.value = ''
}

// 重置表单
const resetForm = () => {
  Object.keys(form).forEach(key => {
    if (key === 'visibility') {
      form[key] = 'public' // 重置为默认值
    } else {
      form[key] = ''
    }
  })
  htmlContent.value = ''
  videoCoverPreview.value = ''
}

// 提交表单
const submitForm = () => {
  request.post('/blog', form).then(res => {
    if (res.code === '200') {
      ElMessage.success('发布成功')
      resetForm()
    } else {
      ElMessage.error(res.msg || '发布失败')
    }
    isSubmitting.value = false
  })
}

// 生成视频封面（仅用于预览）
const generateVideoCoverPreview = () => {
  const video = videoRef.value
  const canvas = canvasRef.value

  if (!video || !canvas) return

  const ctx = canvas.getContext('2d')
  video.crossOrigin = 'anonymous'

  video.onloadeddata = () => {
    video.currentTime = 0
  }

  video.onseeked = () => {
    canvas.width = video.videoWidth || video.clientWidth
    canvas.height = video.videoHeight || video.clientHeight
    ctx.drawImage(video, 0, 0, canvas.width, canvas.height)
    videoCoverPreview.value = canvas.toDataURL('image/png')
  }
}

const uploadVideoCoverAndSubmit = () => {

  // 将预览图转换为 Blob
  const imgSrcBase64 = videoCoverPreview.value
  const byteString = window.atob(imgSrcBase64.split(',')[1])
  const mimeString = imgSrcBase64.split(',')[0].split(':')[1].split(';')[0]
  const ab = new ArrayBuffer(byteString.length)
  const ia = new Uint8Array(ab)

  for (let i = 0; i < byteString.length; i++) {
    ia[i] = byteString.charCodeAt(i)
  }

  const blob = new Blob([ab], {type: mimeString})

  // 组装文件上传对象
  const formData = new FormData()
  formData.append('file', blob, 'cover.png')

  // 通过axios发送网络请求到后端上传接口
  axios({
    url: `${serverHost}/web/upload`,
    method: 'post',
    data: formData,
    headers: {'Content-Type': 'multipart/form-data'}
  }).then(res => {
    form.img = res.data
    submitForm()
  })
}

// 保存表单
const save = () => {
  ruleFormRef.value.validate((valid) => {
    if (!valid) return

    isSubmitting.value = true
    form.content = htmlContent.value

    // 确保visibility字段被正确提交
    console.log('提交的表单数据:', form)
    console.log('可见性设置:', form.visibility)

    if (form.category === '视频' && form.video && !form.img) {
      uploadVideoCoverAndSubmit()
    } else {
      submitForm()
    }
  })
}

// 图片上传成功回调
const handleImgUploadSuccess = (res) => {
  form.img = res
  form.category = '图片'
  videoCoverPreview.value = ''
}

// 视频上传成功回调
const handleVideoUploadSuccess = (res) => {
  form.video = res
  form.category = '视频'
  form.img = ''

  nextTick(() => {
    generateVideoCoverPreview()
  })
}

const types = ref([])
const loadType = () => {
  request.get('/type').then(res => {
    types.value = res.data
  })
}

// 生命周期钩子
onMounted(() => {
  loadType()
})
</script>

<template>
  <div class="main-content">
    <el-card>
      <div class="title">创作服务平台</div>
      <el-form label-width="100px" size="default" style="max-width: 800px; margin: 0 auto" :model="form" :rules="rules" ref="ruleFormRef">
        <!-- 媒体上传区域 -->
        <el-form-item label="媒体内容" class="media-upload-section">
          <el-tabs @tab-click="changeCategory" class="upload-tabs">
            <el-tab-pane label="上传图片">
              <el-upload
                  class="img-uploader"
                  :action="`${serverHost}/web/upload`"
                  :show-file-list="false"
                  :on-success="handleImgUploadSuccess"
              >
                <img v-if="form.img" :src="form.img" class="uploaded-img">
                <el-icon v-else class="upload-icon"><Plus /></el-icon>
              </el-upload>
            </el-tab-pane>
            <el-tab-pane label="上传视频">
              <el-upload
                  class="img-uploader"
                  :action="`${serverHost}/web/upload`"
                  :show-file-list="false"
                  :on-success="handleVideoUploadSuccess"
              >
                <img v-if="videoCoverPreview" :src="videoCoverPreview" class="uploaded-img">
                <el-icon v-else class="upload-icon"><Plus /></el-icon>
              </el-upload>
              <div v-if="form.video" class="video-preview">
                <video ref="videoRef" controls :src="form.video" class="video-player"></video>
                <canvas ref="canvasRef" style="display: none"></canvas>
              </div>
            </el-tab-pane>
          </el-tabs>
        </el-form-item>

        <!-- 基本信息 -->
        <div class="form-section">
          <h3 class="section-title">基本信息</h3>
          <el-form-item prop="name" label="标题" class="title-input">
            <el-input 
              v-model="form.name" 
              autocomplete="off" 
              placeholder="好的创作值得一个好名字！"
              maxlength="100"
              show-word-limit
            ></el-input>
          </el-form-item>

          <el-form-item prop="typeId" label="分类" class="category-select">
            <el-select v-model="form.typeId" placeholder="请选择分类" style="width: 100%">
              <el-option
                  v-for="item in types"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id">
              </el-option>
            </el-select>
          </el-form-item>
        </div>
        <!-- 可见性设置 -->
        <div class="form-section visibility-section">
          <h3 class="section-title">可见性设置</h3>
          <el-form-item prop="visibility">
            <el-radio-group v-model="form.visibility" class="visibility-radio-group-compact">
              <el-radio 
                v-for="option in visibilityOptions" 
                :key="option.value" 
                :label="option.value"
                class="visibility-radio-compact"
              >
                <div class="visibility-option-compact">
                  <i :class="getVisibilityIcon(option.value)"></i>
                  <span>{{ option.label }}</span>
                </div>
              </el-radio>
            </el-radio-group>
          </el-form-item>
        </div>

        <!-- 内容编辑 -->
        <div class="form-section">
          <h3 class="section-title">内容编辑</h3>
          <el-form-item prop="content">
            <div class="editor-container">
              <Toolbar class="editor-toolbar" :editor="editorRefContent" :defaultConfig="editorConfig" mode="default" />
              <Editor class="editor-content" v-model="htmlContent" :defaultConfig="editorConfig" mode="default" @onCreated="editorRefContent = $event" />
            </div>
          </el-form-item>
        </div>
      </el-form>

      <div style="margin-top: 10px;display: flex;justify-content: space-around">
        <el-button type="success" size="large" @click="save" :loading="isSubmitting">
          {{ isSubmitting ? '提交中...' : '立即发布' }}
        </el-button>
      </div>
    </el-card>
  </div>
</template>

<style lang="scss" scoped>
.main-content {
  display: flex;
  background-color: #fff;
  padding: 30px 20px;
  flex-direction: column;
  
  .title {
    font-size: 28px;
    font-weight: 600;
    color: #2c3e50;
    text-align: center;
    margin-bottom: 30px;
    &::after {
      content: '';
      width: 60px;
      height: 4px;
      background: linear-gradient(135deg, #409eff, #67c23a);
      display: block;
      margin: 15px auto;
      border-radius: 2px;
    }
  }
}

// 表单区域样式
.form-section {
  margin-bottom: 30px;
  padding: 20px;
  background: #fafbfc;
  border-radius: 12px;
  border: 1px solid #e4e7ed;
  
  .section-title {
    font-size: 18px;
    font-weight: 600;
    color: #303133;
    margin: 0 0 20px 0;
    padding-bottom: 10px;
    border-bottom: 2px solid #409eff;
    display: inline-block;
  }
}

// 媒体上传区域
.media-upload-section {
  margin-bottom: 30px;
  
  .upload-tabs {
    :deep(.el-tabs__header) {
      margin-bottom: 20px;
    }
  }
}

// 上传组件样式优化
.img-uploader {
  text-align: center;
  padding-bottom: 10px;
  
  :deep(.el-upload) {
    border: 2px dashed #d9d9d9;
    border-radius: 8px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
    transition: all 0.3s;
    
    &:hover {
      border-color: #409EFF;
      background-color: #f0f8ff;
    }
  }
}

.upload-icon {
  font-size: 32px;
  color: #8c939d;
  width: 250px;
  height: 180px;
  line-height: 180px;
  text-align: center;
  transition: color 0.3s;
}

.uploaded-img {
  width: 250px;
  height: 180px;
  display: block;
  object-fit: cover;
  border-radius: 6px;
}

.video-preview {
  width: 100%;
  max-width: 400px;
  margin: 20px auto;
  display: flex;
  justify-content: center;

  .video-player {
    width: 100%;
    max-width: 350px;
    height: auto;
    border-radius: 8px;
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  }
}

// 表单项样式
.title-input {
  :deep(.el-input) {
    font-size: 16px;
    
    .el-input__inner {
      height: 48px;
      line-height: 48px;
      font-size: 16px;
    }
  }
}

.category-select {
  :deep(.el-select) {
    .el-input__inner {
      height: 42px;
      line-height: 42px;
      font-size: 15px;
    }
  }
}

// 可见性设置紧凑样式
.visibility-section {
  background: #f8f9fa;
}

.visibility-radio-group-compact {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
  
  .visibility-radio-compact {
    margin: 0;
    
    .visibility-option-compact {
      display: flex;
      align-items: center;
      gap: 8px;
      padding: 8px 16px;
      border: 1px solid #e4e7ed;
      border-radius: 6px;
      background: #fff;
      transition: all 0.3s;
      cursor: pointer;
      font-size: 14px;
      
      &:hover {
        border-color: #409eff;
        background: #f0f8ff;
      }
      
      i {
        font-size: 16px;
        color: #409eff;
      }
    }
  }
  
  :deep(.visibility-radio-compact.is-checked) .visibility-option-compact {
    border-color: #409eff;
    background: #409eff;
    color: #fff;
    
    i {
      color: #fff;
    }
  }
}

// 编辑器样式
.editor-container {
  border: 1px solid #e4e7ed;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  
  .editor-toolbar {
    border-bottom: 1px solid #e4e7ed;
    background: #fafbfc;
  }
  
  .editor-content {
    height: 350px;
    overflow-y: hidden;
    background: #fff;
  }
}

// 表单标签样式
:deep(.el-form-item__label) {
  font-size: 15px;
  font-weight: 500;
  color: #606266;
}

// 发布按钮区域
.submit-section {
  text-align: center;
  padding: 30px 0;
  
  .el-button {
    padding: 12px 40px;
    font-size: 16px;
    font-weight: 600;
    border-radius: 8px;
  }
}
</style>
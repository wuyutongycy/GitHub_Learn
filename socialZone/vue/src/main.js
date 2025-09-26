import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
const app = createApp(App)
app.use(router)


//引入element-plus
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
//引入element-plus中文库
import zhCn from 'element-plus/es/locale/lang/zh-cn'
app.use(ElementPlus, {locale: zhCn,size: 'small'})
//引入瀑布流组件
import waterfall from 'vue-waterfall2';
app.use(waterfall);

//引入全局样式
import './style/index.scss'

app.mount('#app')

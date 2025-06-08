import { createApp } from 'vue';
import App from './App.vue';
import router from './router/index.js';
import ElementPlus from 'element-plus';
import 'element-plus/dist/index.css';

const app = createApp(App);

app.use(router);

// 使用 ElementPlus 插件
app.use(ElementPlus);

app.mount('#app');
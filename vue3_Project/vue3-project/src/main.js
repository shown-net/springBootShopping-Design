import {createApp} from 'vue'
import App from './App.vue'
import ElementPlus from 'element-plus'
import router from "@/router/index.js";
import 'element-plus/dist/index.css'
import {createPinia} from "pinia";
import * as ElementPlusIconsVue from '@element-plus/icons-vue'

const pinia = createPinia();
const app = createApp(App)
app.use(ElementPlus)
app.use(router)
app.use(pinia)
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    app.component(key, component)
}
app.mount('#app')


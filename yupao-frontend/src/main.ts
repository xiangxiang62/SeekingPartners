import { createApp } from 'vue'
import Vant from 'vant';
import App from './App.vue'
import * as VueRouter from 'vue-router';
import routes from "./config/route.ts";
import 'vant/lib/index.css';


const app = createApp(App);
// 3. 注册你需要的组件
app.use(Vant);


// 3. 创建路由实例并传递 `routes` 配置

const router = VueRouter.createRouter({
    // 4. 内部提供了 history 模式的实现。为了简单起见，我们在这里使用 hash 模式。
    history: VueRouter.createWebHashHistory(),
    routes, // `routes: routes` 的缩写
})

app.use(router)

app.mount('#app')

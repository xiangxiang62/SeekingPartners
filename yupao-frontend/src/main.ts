import { createApp } from 'vue'
// 1. 引入你需要的组件
import {Button, Icon, NavBar} from 'vant';
import App from './App.vue'



const app = createApp(App);
// 3. 注册你需要的组件
app.use(Button);
app.use(NavBar);
app.use(Icon);

app.mount("#app")
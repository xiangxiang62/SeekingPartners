// 2. 定义一些路由
import team from "../pages/team.vue";
import Index from "../pages/index.vue";
import User from "../pages/User.vue";
import Search from "../pages/Search.vue";
import userEditPage from "../pages/userEditPage.vue";
import SearchResultPage from "../pages/SearchResultPage.vue";

const routes = [
    { path: '/', component: Index },
    { path: '/team', component: team },
    { path: '/user', component: User },
    { path: '/search', component: Search },
    { path: '/user/edit', component: userEditPage },
    { path: '/user/list', component: SearchResultPage },
]

export default routes;
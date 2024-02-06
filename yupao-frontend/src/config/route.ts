// 2. 定义一些路由
import team from "../pages/team.vue";
import Index from "../pages/index.vue";
import User from "../pages/User.vue";
import Search from "../pages/Search.vue";
import userEditPage from "../pages/userEditPage.vue";
import SearchResultPage from "../pages/SearchResultPage.vue";
import UserLoginPage from "../pages/UserLoginPage.vue";
import TeamAddPage from "../pages/teamAddPage.vue";
import TeamUpdatePage from "../pages/TeamUpdatePage.vue";
import UserUpdatePage from "../pages/UserUpdatePage.vue";
import UserTeamJoinPage from "../pages/UserTeamJoinPage.vue";
import UserTeamCreatePage from "../pages/UserTeamCreatePage.vue";

const routes = [
    { path: '/', component: Index },
    { path: '/team', component: team },
    { path: '/user', component: User },
    { path: '/search', component: Search },
    { path: '/user/edit', component: userEditPage },
    { path: '/user/list', component: SearchResultPage },
    { path: '/user/update', component: UserUpdatePage },
    { path: '/user/team/join', component: UserTeamJoinPage },
    { path: '/user/team/create', component: UserTeamCreatePage },
    { path: '/user/login', component: UserLoginPage },
    { path: '/team/add', component: TeamAddPage },
    { path: '/team/update', component: TeamUpdatePage },
]

export default routes;
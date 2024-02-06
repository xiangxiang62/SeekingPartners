<script setup lang="ts">
import {useRouter} from "vue-router";
import {onMounted, ref} from "vue";
import myAxios from "../plugins/myAxios.ts";

/*const user = {
  id: 1,
  username: 'string',
  userAccount: 'string',
  avatarUrl: 'https://img1.baidu.com/it/u=2129525223,626456209&fm=253&app=138&size=w931&n=0&f=JPEG&fmt=auto?sec=1706720400&t=d02bf7ed2a912cd2bc8aac533e1b551b',
  gender: '男',
  phone: 'string',
  email: 'string',
  planeCode: 'string',
  createTime: new Date(),
}*/

const router = useRouter();

const user = ref({});

onMounted(async () => {
const res = await myAxios.get('/user/get/login');
if (res.code === 0){
  user.value = res.data;
}
console.log(user.value)
})

const toEdit = (editKey: string, editName: string, currentValue: string) => {
  router.push({
    path: '/user/edit',
    query: {
      editKey,
      editName,
      currentValue,
    }
  })
}
</script>

<template v-if="user">
  <van-cell title="当前用户"  :value="user.userAccount" />
  <van-cell title="修改信息" is-link to="/user/update" />
  <van-cell title="我创建的队伍" is-link to="/user/team/create" />
  <van-cell title="我加入的队伍" is-link to="/user/team/join" />

</template>

<style scoped>

</style>
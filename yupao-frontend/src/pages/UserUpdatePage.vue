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

<template>
  <van-cell title="昵称" is-link :value="user.username" @click="toEdit('username','昵称',user.username)"/>
  <van-cell title="账号" :value="user.userAccount"/>
  <van-cell title="头像" is-link  @click="toEdit('avatarUrl','头像',user.avatarUrl)">
    <img style="height: 48px" :src="user.avatarUrl"/>
  </van-cell>
  <van-cell title="性别" is-link :value="user.gender" @click="toEdit('gender','性别',user.gender)"/>
  <van-cell title="电话" is-link  :value="user.phone" @click="toEdit('phone','电话',user.phone)"/>
  <van-cell title="邮箱" is-link  :value="user.email" @click="toEdit('email','邮箱',user.email)"/>
  <van-cell title="星球编号" :value="user.planeCode"/>
  <van-cell title="注册时间" :value="user.createTime"/>
</template>

<style scoped>

</style>
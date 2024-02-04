<script setup>
import {onMounted, ref} from 'vue';
import {useRoute, useRouter} from "vue-router";
import myAxios from "../plugins/myAxios.ts";
import {Toast} from "vant";

const route = useRoute();
const {tags} = route.query;
const router = useRouter();
const userList = ref([])

onMounted(async () => {
  const userListData = await myAxios.get('/user/recommend', {
    params: {
      pageSize: 8,
      pageNum: 1
    },
  })
      .then(function (response) {
        console.log('/user/recommend successd', response.data.data);
        // Toast.success("请求成功")
        return response?.data?.records;
      })
      .catch(function (error) {
        console.log('/user/recommend error', error);
        router.replace("/user/login");
        // Toast.fail("请求失败")
      })

  if (userListData){
    userListData.forEach(user => {
      if (user.tags){
        user.tags = JSON.parse(user.tags);
      }
    })
    userList.value = userListData;
  }
})
</script>

<template>
  <van-card
      v-for="user in userList"
      :desc="user.profile"
      :title="`${user.username}(${user.planeCode})`"
      :thumb="user.avatarUrl"
  >
    <template #tags>
      <van-tag plain type="primary" v-for="tag in user.tags" style="margin-right: 8px;margin-top: 6px">{{ tag }}
      </van-tag>
    </template>
    <template #footer>
      <van-button size="mini">联系我</van-button>
    </template>
  </van-card>
  <van-empty v-if="!userList || userList.length < 1" description="数据为空" />

</template>

<style scoped>

</style>
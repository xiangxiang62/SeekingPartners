<script setup>
import {onMounted, ref} from 'vue';
import {useRoute, useRouter} from "vue-router";
import myAxios from "../plugins/myAxios.ts";
import {showToast, Toast} from "vant";

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

  if (userListData) {
    userListData.forEach(user => {
      if (user.tags) {
        user.tags = JSON.parse(user.tags);
      }
    })
    userList.value = userListData;
  }
})

const doMatch = async () => {
  const num = 5;
  const userListData = await myAxios.get('/team/match', {
    params: {
      num,
    },
  })
      .then(function (response) {
        console.log('/user/match successd', response.data.data);
        showToast("请求成功")
        return response?.data;
      })
      .catch(function (error) {
        console.log('/user/match error', error);
        showToast("请求失败")
      })

  if (userListData) {
    userListData.forEach(user => {
      if (user.tags) {
        user.tags = JSON.parse(user.tags);
      }
    })
    userList.value = userListData;
  }

}
</script>

<template>
  <van-skeleton title avatar :row="3" :loading="false">
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
    <van-button size="mini" @click="doMatch">匹配用户</van-button>
    <van-empty v-if="!userList || userList.length < 1" description="数据为空"/>
  </van-skeleton>
</template>

<style scoped>

</style>
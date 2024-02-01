<script setup>
import {onMounted, ref} from 'vue';
import {useRoute} from "vue-router";
import myAxios from "../plugins/myAxios.ts";
import {Toast} from "vant";

const route = useRoute();
const {tags} = route.query;
import qs from 'qs';

const userList = ref([])

onMounted(async () => {
  const userListData = await myAxios.get('/user/search/tags', {
    params: {
      tagNameList: tags
    },
    paramsSerializer: params => {
      return qs.stringify(params, {indices: false})
    }

  })
      .then(function (response) {
        console.log('/user/search/tags successd', response.data.data);
        // Toast.success("请求成功")
        return response?.data;
      })
      .catch(function (error) {
        console.log('/user/search/tags error', error);
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


/*const mockUser = {
  id: 1,
  username: '香香',
  userAccount: 'xiangxiang',
  avatarUrl: 'https://img1.baidu.com/it/u=2129525223,626456209&fm=253&app=138&size=w931&n=0&f=JPEG&fmt=auto?sec=1706720400&t=d02bf7ed2a912cd2bc8aac533e1b551b',
  gender: '男',
  profile: '我是香香，你好你好 HelloKitty',
  phone: '1333333333',
  email: '3123213@qq.com',
  userRole: 0,
  planeCode: '1234',
  tags: ['java', 'emo', '求职', '春招'],
  createTime: new Date(),
}*/


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
  <van-empty v-if="!userList || userList.length < 1" description="暂无符合要求的用户" />

</template>

<style scoped>

</style>
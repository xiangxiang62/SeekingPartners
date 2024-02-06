<script setup lang="ts">
import {useRouter} from "vue-router";

const router = useRouter();
// 跳转到加入队伍页
const doJoinTeam1 = () => {
  router.push({
    path: "/team/add"
  })
}


import {onMounted, ref} from 'vue';
import {useRoute} from "vue-router";
import myAxios from "../plugins/myAxios.ts";
import {showToast} from 'vant';
import {teamStatusEnum} from "../cnstants/teamStatusEnum.ts";

const route = useRoute();
const teamList = ref([])
const user = ref({});

onMounted(async () => {
  const res = await myAxios.get('/team/list/my/create')
  if (res?.code === 0) {
    teamList.value = res.data;
  } else {
    showToast("查询失败")
  }
  const res1 = await myAxios.get('/user/get/login');
  if (res1.code === 0) {
    user.value = res1.data;
  }
})

const doJoinTeam = async (id) => {
  listTeam("");
}

/**
 * listTeam
 * @param val
 * @returns {Promise<void>}
 */
const listTeam = async (val) => {
  const res = await myAxios.get('/team/list/my/create', {
    params: {
      searchText: val,
      pageNum: 1,
    },
  });
  if (res?.code === 0) {
    teamList.value = res.data;
  } else {
    showToast("shibaile")
  }
}

const onSearch = (val) => {
  listTeam(val);
};
const searchText = ref("")

const doUpdateTeam = (id:number) => {
  router.push({
    path: '/team/update',
    query: {
      id,
    }
  })
  console.log(id);
}
</script>

<template>
  <div>
    <van-search v-model="searchText" @search="onSearch" placeholder="搜素队伍"/>
    <van-button type="primary" @click="doJoinTeam1">创建队伍</van-button>
    <van-card
        v-for="team in teamList"
        :desc="team.description"
        :title="`${team.name}`"
        thumb="https://img1.baidu.com/it/u=1947255442,3860275857&fm=253&app=138&size=w931&n=0&f=JPEG&fmt=auto?sec=1707325200&t=31f9f317fbe21633420f0255079e3535"
    >
      <template #tags>
        <van-tag plain type="primary" style="margin-right: 8px;margin-top: 6px">{{ teamStatusEnum[team.status] }}
        </van-tag>
      </template>
      <template #footer>
        <van-button size="mini" @click="doJoinTeam(team.id)">加入队伍</van-button>
        <van-button v-if="team.userId === user.id" size="mini" @click="doUpdateTeam(team.id)">更新队伍</van-button>
      </template>
    </van-card>
    <van-empty v-if="!teamList || teamList.length < 1" description="数据为空"/>

  </div>
</template>

<style scoped>

</style>
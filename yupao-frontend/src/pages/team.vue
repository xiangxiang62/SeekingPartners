<script setup>
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
import { showToast } from 'vant';
import {teamStatusEnum} from "../cnstants/teamStatusEnum.ts";
const route = useRoute();
const teamList = ref([])

onMounted(async () => {
  const res = await myAxios.get('/team/list')
  if (res?.code === 0){
    teamList.value = res.data;
  }else {
    showToast("shibaile")
  }

  console.log(1)
  console.log(1)
})

const doJoinTeam = async (id) => {
  const res = await myAxios.post("/team/join",{
    teamId:Number(id),
  });
  if (res?.code === 0){
    showToast("加入成功")
  }else {
    showToast("加入失败")
  }
  console.log("idididid" + id)
}


</script>

<template>
  <div>
    <van-button type="primary" @click="doJoinTeam">创建队伍</van-button>111
    <van-card
        v-for="team in teamList"
        :desc="team.description"
        :title="`${team.name}`"
    >
      <template #tags>
        <van-tag plain type="primary"  style="margin-right: 8px;margin-top: 6px">{{ teamStatusEnum[team.status] }}
        </van-tag>
      </template>
      <template #footer>
        <van-button size="mini" @click="doJoinTeam(team.id)">加入队伍</van-button>
      </template>
    </van-card>
    <van-empty v-if="!teamList || teamList.length < 1" description="数据为空" />

  </div>
</template>

<style scoped>

</style>
<script setup lang="ts">

import {useRoute, useRouter} from "vue-router";
import {ref} from "vue";
import myAxios from "../plugins/myAxios.ts";

const route = useRoute();
const router = useRouter();
const editUser = ref({
  editKey:route.query.editKey,
  currentValue: route.query.currentValue,
  editName: route.query.editName,
})

const onSubmit = async () => {
  const res = await myAxios.post('/user/update',{
    'id': 5,
     [editUser.value.editKey]:editUser.value.currentValue
  })
  console.log(res.code)
  console.log(res.data)
  if (res.code == 0 && res.data == 0){
    router.back();
  }else {
    console.log("登录失败")
  }
};
</script>

<template>
  <van-form @submit="onSubmit">
    <van-cell-group inset>
      <van-field
          v-model="editUser.currentValue"
          :name="editUser.editKey"
          :label="editUser.editName"
          :placeholder="`请输入${editUser.editName}`"
      />
    </van-cell-group>
    <div style="margin: 16px;">
      <van-button round block type="primary" native-type="submit">
        提交
      </van-button>
    </div>
  </van-form>

</template>

<style scoped>

</style>
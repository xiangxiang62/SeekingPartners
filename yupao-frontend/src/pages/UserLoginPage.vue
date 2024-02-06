<script setup lang="ts">
import {useRouter} from "vue-router";

const router = useRouter();
import {ref} from "vue";
import myAxios from "../plugins/myAxios.ts";
import {showToast} from "vant";

const userAccount = ref('');
const userPassword = ref('');
const onSubmit = async () => {
  const res = await myAxios.post('/user/login',{
    userAccount:userAccount.value,
    userPassword:userPassword.value
  })
  console.log(res)
  if (res.code === 0 && res.data){
    router.replace("/");
    showToast(res.message)
    showToast("登录成功")
    console.log("登录成功")
  }else {
    showToast(res.message)
    console.log("登录失败")
  }
};
</script>

<template>
  <van-form @submit="onSubmit">
    <van-cell-group inset>
      <van-field
          v-model="userAccount"
          name="账号"
          label="账号"
          placeholder="请输入账号"
          :rules="[{ required: true, message: '请填写账号' }]"
      />
      <van-field
          v-model="userPassword"
          type="password"
          name="userPassword"
          label="密码"
          placeholder="请输入密码"
          :rules="[{ required: true, message: '请填写密码' }]"
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
<script setup>

import {ref} from "vue";
// import MyAxios from "../plugins/myAxios.js";


const initFormData = {
  "description": "",
  "expireTime": "",
  "maxNum": 0,
  "name": "",
  "password": "",
  "status": 0,
  "userId": 0
}
const result = ref('');
const showPicker = ref(false);
/*const onConfirm = ({ selectedValues }) => {
  result.value = selectedValues.join('/');
  showPicker.value = false;
  addTeamData.value.expireTime = result.value;
};*/
const onConfirm = ({ selectedValues }) => {
  const year = selectedValues[0];
  const month = selectedValues[1].padStart(2, '0'); // 补零操作
  const day = selectedValues[2].padStart(2, '0');

  const formattedDate = `${year}-${month}-${day}`;
  result.value = formattedDate;
  showPicker.value = false;
  addTeamData.value.expireTime = formattedDate;
  console.log(addTeamData.value.expireTime)
};

// 用户填写的表单数据
const addTeamData = ref({...initFormData});
import { showToast } from 'vant';
import {useRouter} from "vue-router";
import MyAxios from "../plugins/myAxios.ts";
const router = useRouter();

const onSubmit = async () => {
  const postData = {
    ...addTeamData.value,
    status:Number(addTeamData.value.status)
  }
  const res = await MyAxios.post("/team/add",postData);
  if (res?.code === 0 && res.data){
    showToast('添加成功');
    router.push({
      path: '/team',
      replace: true,
    });
  }else {
    showToast("添加失败")
  }
}
</script>

<template>
  <div>
    <van-form @submit="onSubmit">
      <van-cell-group inset>
        <van-field
            v-model="addTeamData.name"
            name="name"
            label="队伍名"
            placeholder="请输入队伍名"
            :rules="[{ required: true, message: '请填写队伍名' }]"
        />
        <van-field
            v-model="addTeamData.description"
            rows="1"
            autosize
            label="队伍描述"
            type="textarea"
            placeholder="请输入队伍描述"
            :rules="[{ required: true, message: '请输入队伍描述' }]"
        />

        <van-field
            v-model="addTeamData.expireTime"
            name="datePicker"
            label="时间选择"
            :placeholder="addTeamData.expireTime ?? '点击选择时间'"
            @click="showPicker = true"
        />
        <van-popup v-model:show="showPicker" position="bottom">
          <van-date-picker @confirm="onConfirm" @cancel="showPicker = false" />
        </van-popup>

        <van-field name="stepper" label="最大人数">
          <template #input>
            <van-stepper v-model="addTeamData.maxNum" min="3" max="10" />
          </template>
        </van-field>

        <van-field name="radio" label="队伍状态">
          <template #input>
            <van-radio-group v-model="addTeamData.status" direction="horizontal">
              <van-radio name="0">公开</van-radio>
              <van-radio name="1">私有</van-radio>
              <van-radio name="2">加密</van-radio>
            </van-radio-group>
          </template>
        </van-field>

        <van-field
            v-if="Number(addTeamData.status) === 2"
            v-model="addTeamData.password"
            type="password"
            name="password"
            label="密码"
            placeholder="请输入队伍密码"
            :rules="[{ required: true, message: '请填写密码' }]"
        />










      </van-cell-group>
      <div style="margin: 16px;">
        <van-button round block type="primary" native-type="submit">
          提交
        </van-button>
      </div>
    </van-form>
  </div>
</template>

<style scoped>

</style>
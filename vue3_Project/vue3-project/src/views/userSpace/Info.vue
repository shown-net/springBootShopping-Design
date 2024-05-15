<template>
  <el-form class="space-container" label-position="right" size="large"
           :model="sizeForm" ref="formRef" :rules="rules" value-width="80px">
    <el-form-item class="space-item" label="用户名" prop="accountName">
      <!--禁止修改用户名-->
      <el-input placeholder="用户名" v-model="sizeForm.accountName" :disabled="true"></el-input>
    </el-form-item>
    <el-form-item v-if="userStore.accountType==='user'" class="space-item" label="性别" prop="gender">
      <el-radio v-model="sizeForm.gender" value="男">男</el-radio>
      <el-radio v-model="sizeForm.gender" value="女">女</el-radio>
    </el-form-item>
    <el-form-item v-if="userStore.accountType==='user'" class="space-item" label="年龄" prop="age">
      <el-input-number placeholder="年龄" :min="1" :max="120" v-model.number="sizeForm.age">
      </el-input-number>
    </el-form-item>
    <el-form-item v-if="userStore.accountType==='user'" class="space-item" label="邮箱" prop="email">
      <el-input placeholder="邮箱" v-model="sizeForm.email"></el-input>
    </el-form-item>
    <el-form-item v-if="userStore.accountType==='user'" class="space-item" label="电话号码" prop="phoneNumber">
      <el-input placeholder="电话号码" v-model="sizeForm.phoneNumber"></el-input>
    </el-form-item>
    <el-form-item size="large">
      <el-button type="primary" @click="submitForm">更新账号信息</el-button>
    </el-form-item>
  </el-form>
</template>
<script>

import instance from "@/utils/request.js";
import {onMounted, reactive, ref, watchEffect} from "vue";
import {ElMessage} from "element-plus";
import {useUserStore} from "@/utils/store.js";
import {useRouter} from "vue-router";

export default {
  setup() {
    // 创建响应式表单数据
    const sizeForm = reactive({
      accountName: '',
      // password: '',
      gender: '男',
      age: 20,
      email: '',
      phoneNumber: ''
    });
    const rules = {
      // password: [
      //   {required: false, message: '密码可以为空', trigger: 'blur'},
      //   {min: 8, max: 20, message: '长度在 8 到 20 个字符', trigger: 'blur'}
      // ],
      email: [
        {required: true, message: '邮箱不能为空', trigger: 'blur',},
        {type: "email", required: true, message: '邮箱格式错误', trigger: ['blur', 'change']}
      ]
    }
    const userStore = useUserStore();
    const userInfo = ref(null);
    const router = useRouter();
    onMounted(() => {
      // 如果用户本地数据不存在
      if (localStorage.getItem('userInfo') == null) {
        // 重定向到登录界面
        ElMessage.error("用戶未登录");
        router.push('/rl')
      } else {
        // 给userStore全局变量赋值
        userStore.updateAllData();
        //获取个人信息表单
        instance.get('/' + userStore.accountType + "/selectInfo?accountName=" + userStore.accountName)
            .then(result => {
              if (result.code === 0) {
                sizeForm.accountName = result.data.accountName
                // 前端界面不读取password字段
                // sizeForm.password = result.data.password
                sizeForm.gender = result.data.gender
                sizeForm.age = result.data.age
                sizeForm.email = result.data.email
                sizeForm.phoneNumber = result.data.phoneNumber
              } else {
                //说明令牌失效或者不存在该用户
                localStorage.removeItem('userInfo')
                localStorage.removeItem('Authorization')
                localStorage.removeItem('cart')
                // userStore所有成员设置为null
                userStore.setNullAllData()
                ElMessage.error(result.message)
                // 重定向到登录界面
                router.push('/rl/login')
              }
            })
      }
    });
    const formRef = ref(null);
    const submitForm = () => {  //更新账号信息
      formRef.value.validate((valid) => {
        if (valid) {
          instance.put(`/${userStore.accountType}/updateInfo`, sizeForm).then(result => {
            ElMessage.success(result.data);
          })
        } else {
          ElMessage.error("表单验证不通过，请重试");
        }
      })
    }
    return {
      sizeForm,
      userInfo,
      rules,
      formRef,
      submitForm,
      userStore
    }
  }
}
</script>
<style scoped>

.space-container {
  width: 90%;
  height: 75%;
  border-radius: 10px;
  margin: 0px 0px;
  padding: 20px 20px 10px 20px;
  text-align: left;
}

.space-item {
//width: 100%; //height: 100px
}


</style>
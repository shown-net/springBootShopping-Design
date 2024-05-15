<template>
  <el-form class="login-container" :rules="rules" ref="formRef" size="large"
           label-position="top" :model="sizeForm" label-width="auto">
    <el-form-item aria-label="用户名" prop="accountName">
      <el-input placeholder="请设置用户名" v-model="sizeForm.accountName"></el-input>
    </el-form-item>
    <el-form-item aria-label="密码" prop="password">
      <el-input placeholder="请设置密码" v-model="sizeForm.password" show-password></el-input>
    </el-form-item>
    <el-form-item aria-label="账号类型">
      <el-radio-group v-model="sizeForm.accountType">
        <el-radio-button value="user">消费者</el-radio-button>
        <el-radio-button value="salesMan">商家</el-radio-button>
        <el-radio-button value="manager">管理者</el-radio-button>
      </el-radio-group>
    </el-form-item>
    <el-form-item size="large">
      <el-button type="primary" @click="onSubmit()">登录</el-button>
      <el-button>取消</el-button>
    </el-form-item>
  </el-form>
</template>
<script>

import instance from "@/utils/request.js";
import {ElMessage} from "element-plus";
import {reactive, ref} from "vue";
import {useRouter} from "vue-router";

export default {
  setup() {
    const router = useRouter();
    const sizeForm = reactive({
      accountName: '',
      password: '',
      accountType: "user"
    })
    const rules = reactive({
          accountName: [
            {required: true, message: '用户名不能为空', trigger: 'blur'},
            {min: 3, max: 20, message: '长度在 3 到 20 个字符', trigger: 'blur'}],
          password: [
            {required: true, message: '密码不能为空', trigger: 'blur'},
            {min: 8, max: 20, message: '长度在 8 到 20 个字符', trigger: 'blur'}]
        }
    )
    const formRef = ref(null);
    const onSubmit = () => {
      formRef.value.validate((valid) => {
        if (valid) {
          instance.post(`/${sizeForm.accountType}/login`, sizeForm)
              .then(result => {
                if (result.code === 0) {
                  //登录成功,保存登录之后的令牌和用户名和账号类型
                  let jwt = result.data;
                  localStorage.setItem("Authorization", jwt);
                  localStorage.setItem("userInfo", JSON.stringify({
                    'accountName': sizeForm.accountName,
                    "accountType": sizeForm.accountType
                  }))
                  //网页重定向到个人空间
                  ElMessage.success("登录成功，已进入个人空间")
                  router.push("/userSpace")
                } else {
                  ElMessage.error(result.message)
                }
              })
        } else {
          ElMessage.error("表单验证错误，请重试")
        }
      })
    }
    return {
      formRef,
      sizeForm,
      rules,
      onSubmit
    }
  }
};
</script>
<style scoped>

.login-container {
  width: 90%;
  height: 75%;
  border-radius: 10px;
  padding: 20px 20px 10px 20px;
  text-align: left;
}


</style>
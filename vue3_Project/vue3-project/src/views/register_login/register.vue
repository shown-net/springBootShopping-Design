<template>
  <el-form class="register-container" :rules="rules" size="large"
           label-position="right" ref="sizeForm" :model="sizeForm" label-width="auto">
    <el-form-item label="用户名" prop="accountName">
      <el-input placeholder="请设置用户名" v-model="sizeForm.accountName"></el-input>
    </el-form-item>
    <el-form-item label="密码" prop="password">
      <el-input placeholder="请设置密码" v-model="sizeForm.password" show-password></el-input>
    </el-form-item>
    <el-form-item label="邮箱" prop="email">
      <el-input placeholder="请输入邮箱" v-model="sizeForm.email"></el-input>
    </el-form-item>
    <el-form-item label="账号类型">
      <el-radio-group v-model="sizeForm.accountType">
        <el-radio-button value="消费者">消费者</el-radio-button>
        <el-radio-button value="商家">商家</el-radio-button>
      </el-radio-group>
    </el-form-item>
    <el-form-item>
      <el-button type="primary" @click="onSubmit('sizeForm')">立即注册</el-button>
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
    const sizeForm = reactive({
      accountName: '',
      password: '',
      email: '',
      // 账户类型，消费者||商家
      accountType: '消费者'
    })
    const rules = reactive({
      accountName: [
        {required: true, message: '用户名不能为空', trigger: 'blur'},
        {min: 3, max: 20, message: '长度在 3 到 20 个字符', trigger: 'blur'}
      ],
      password: [
        {required: true, message: '密码不能为空', trigger: 'blur'},
        {min: 8, max: 20, message: '长度在 8 到 20 个字符', trigger: 'blur'}
      ],
      email: [
        {required: true, message: '邮箱不能为空', trigger: 'blur',},
        {type: "email", required: true, message: '邮箱格式错误', trigger: ['blur', 'change']}
      ]
    })
    const formRef = ref(null);
    const router = useRouter();
    const onSubmit = () => {
      formRef.value.validate((valid) => {
        if (!valid) {
          ElMessage.error("表单验证错误，请重试")
        } else {
          instance.post(`/${sizeForm.accountType}/register`, sizeForm
          ).then(result => {
            if (result.code === 1) {
              //存在重复用户名，重新注册
              ElMessage.error(result.message)
            } else {
              //注册成功
              ElMessage.success(result.data)
              router.push('/rl/login')
            }
          })
        }
      });
    }
    return {
      sizeForm,
      rules,
      onSubmit
    }
  }
}
;
</script>
<style scoped>

.register-container {
  width: 90%;
  height: 100%;
  border-radius: 10px;
  padding: 20px 20px 10px 20px;
  text-align: left;
}

.el-form-item {
  height: 50px;
}


</style>
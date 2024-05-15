<template>
  <div class="main">
    <div class="left-content">
      <!--启用router模式，其index将作为router的path值-->
      <el-menu class="contain_Menu" active-text-color="#ffd04b" router>
        <el-menu-item-group title="个人空间">
          <el-menu-item index="/userSpace/info">首页</el-menu-item>
          <el-menu-item index="/userSpace/info">个人信息</el-menu-item>
          <el-menu-item index="/userSpace/cart" v-if="userStore.accountType==='user'">购物车</el-menu-item>
          <el-menu-item index="/userSpace/catalogue" v-if="userStore.accountType==='salesMan'">商品目录</el-menu-item>
          <el-menu-item index="/userSpace/statistics/salesMan" v-if="userStore.accountType==='salesMan'">销售统计
          </el-menu-item>
          <el-menu-item index="/userSpace/order" v-if="userStore.accountType==='user'">订单记录</el-menu-item>
          <el-menu-item index="/userSpace/statistics/manager" v-if="userStore.accountType==='manager'">管理者统计
          </el-menu-item>
          <el-menu-item index="/userSpace/salesManAccountController" v-if="userStore.accountType==='manager'">
            销售账号管理
          </el-menu-item>
          <el-sub-menu v-if="userStore.accountType==='salesMan'">
            <template #title>
              <el-icon>
                <Collection/>
              </el-icon>
              <span>浏览/购买日志</span>
            </template>
            <el-menu-item index="/userSpace/userBrowseLog">浏览商品日志
            </el-menu-item>
            <el-menu-item index="/userSpace/order">购买商品日志</el-menu-item>
          </el-sub-menu>
          <el-menu-item index="/userSpace/operatorLog" v-if="userStore.accountType!=='user'">操作日志</el-menu-item>
        </el-menu-item-group>
      </el-menu>
    </div>
    <div class="right-content">
      <div class="contain">
        <div class="space-nav">
          <h4 class="title">个人中心</h4>
          <el-button type="primary" icon="el-icon-right" @click="logout">退出账号</el-button>
          <el-button v-if="userStore.accountType!=='salesMan'" plain @click="dialogFormVisible = true">更新密码
          </el-button>
          <el-dialog v-model="dialogFormVisible" title="更新密码所需表单" width="600">
            <el-form :model="passwordForm" :rules="rules">
              <el-form-item label="旧密码" :label-width="formLabelWidth" prop="oldPassword">
                <el-input v-model="passwordForm.oldPassword" autocomplete="off" show-password/>
              </el-form-item>
              <el-form-item label="新密码" :label-width="formLabelWidth" prop="newPassword">
                <el-input v-model="passwordForm.newPassword" autocomplete="off" show-password/>
              </el-form-item>
              <el-form-item label="输入重复新密码" :label-width="formLabelWidth" prop="repeatPassword">
                <el-input v-model="passwordForm.repeatPassword" autocomplete="off" show-password/>
              </el-form-item>
            </el-form>
            <template #footer>
              <div class="dialog-footer">
                <el-button @click="dialogFormVisible = false">取消操作</el-button>
                <el-button type="primary" @click="updatePassword()">确定更新</el-button>
              </div>
            </template>
          </el-dialog>
          <el-button plain type="danger" @click="LogOff" v-if="userStore.accountType!=='manager'">注销账号</el-button>
        </div>
        <router-view></router-view>
      </div>
    </div>
  </div>
</template>
<script>
import instance from "@/utils/request.js";
import {onMounted, reactive, ref} from "vue";
import {ElMessage, ElMessageBox} from "element-plus";
import {useUserStore} from "@/utils/store.js";
import {useRouter} from "vue-router";

export default {
  setup() {
    const userStore = useUserStore();
    const router = useRouter();
    onMounted(() => {
      // 如果用户本地数据不存在
      if (localStorage.getItem('userInfo') == null) {
        // 重定向到登录界面
        ElMessage.error("用戶未登录");
        router.push('/rl')
      } else {
        userStore.updateAllData()
      }
    })
    // 更新密码的对话框表单是否可见
    const dialogFormVisible = ref(false)
    const passwordForm = reactive({
      oldPassword: '',
      newPassword: '',
      repeatPassword: '',
    })
    const formLabelWidth = '140px'
    const rules = {
      oldPassword: [
        {required: true, message: '旧密码不可以为空', trigger: 'blur'},
        {min: 8, max: 20, message: '长度在 8 到 20 个字符', trigger: 'blur'}
      ],
      newPassword: [
        {required: true, message: '新密码不可以为空', trigger: 'blur'},
        {min: 8, max: 20, message: '长度在 8 到 20 个字符', trigger: 'blur'}
      ],
      repeatPassword: [
        {required: true, message: '确认密码不可以为空', trigger: 'blur'},
        {min: 8, max: 20, message: '长度在 8 到 20 个字符', trigger: 'blur'}
      ]
    }
    const updatePassword = () => {
      if (passwordForm.newPassword !== passwordForm.repeatPassword) {
        ElMessage.error("新密码与输入的重复新密码不一致")
      } else {
        dialogFormVisible.value = false
        instance.put(`/${userStore.accountType}/updatePassword`, {
          accountName: userStore.accountName,
          oldPassword: passwordForm.oldPassword,
          newPassword: passwordForm.newPassword
        }).then(result => {
          if (result.code === 0) {
            ElMessage.success(result.data);
            logout();
          } else {
            ElMessage.error(result.message);
          }
        })
      }
    }
    //退出登录状态
    const logout = async () => {
      let result = await instance.post(`/${userStore.accountType}/logout`).then(result => {
        console.log(result)
      })
      // 删除本地保存的令牌信息
      localStorage.removeItem('userInfo')
      localStorage.removeItem('Authorization')
      localStorage.removeItem('cart')
      // userStore所有成员设置为null
      userStore.setNullAllData()
      //重定向到登录页面
      await router.push("/rl/login")
    }
    //注销账号
    const LogOff = () => {
      ElMessageBox.confirm(
          '确定要注销该账号吗？',
          'Warning',
          {
            confirmButtonText: 'OK',
            cancelButtonText: 'Cancel',
            type: 'warning',
          }
      ).then(() => {
        return instance.delete(`/${userStore.accountType}/LogOff`)
            .then(result => {
              if (result.code === 0) {
                ElMessage.success(result.data);
                logout();
              } else {
                ElMessage.error(result.message);
              }
            })
      }).catch(() => {
        ElMessage({
          type: 'info',
          message: '操作取消',
        })
      })
    }
    return {
      userStore,
      dialogFormVisible,
      passwordForm,
      formLabelWidth,
      rules,
      updatePassword,
      logout,
      LogOff,
    }
  }
}
</script>
<style scoped>
.main {
//background: url("../../assets/sign_bg.jpg"); width: 100%; height: 100%; background-size: cover; position: fixed;
}

.left-content {
  float: left;
  width: 12.5%;
  height: 100%;
  text-align: center;

}

.contain_Menu {
  width: 100%;
  height: 100%;
  margin: 0% 0% 20% 0%;
//background-color:#545c64; background-color: #B3C0D1;
}

.right-content {
  float: right;
  width: 87.5%;
  height: 100%;
  padding-bottom: 3%;
}

.contain {
  width: 100%;
  height: 100%;
  border-radius: 10px;
  padding: 10px 10px 10px 10px;
  background-color: rgba(255, 255, 255, 0.8);
  text-align: left;
  box-shadow: 0 0 20px 2px rgba(0, 0, 0, 1.0);
}

.space-nav {
  display: flex;
  align-items: center; /* 垂直居中 */
  justify-content: flex-start; /* 水平靠左对齐 */
}

.title {
  margin: 20px 100px 20px 20px;
  text-align: left;
  font-size: 30px;
}

.el-button {
  margin-right: 100px;
}


</style>
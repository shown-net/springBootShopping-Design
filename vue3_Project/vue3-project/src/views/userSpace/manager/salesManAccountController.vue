<template>
  <div>
    <el-table height="500" :data="accountTable" border
              :highlight-current-row="true" :row-style="{height:'100px'}">
      <el-table-column label="账号ID" prop="id"></el-table-column>
      <el-table-column label="账号名" prop="accountName"></el-table-column>
      <el-table-column label="账号邮箱" prop="email"></el-table-column>
      <el-table-column label="操作" prop="id">
        <template #default="{row}">
          <el-button plain @click="updatePassword(row.id)">更新该销售账号密码</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>
<script>

import instance from "@/utils/request.js";
import {ElMessage, ElMessageBox} from "element-plus";
import {onMounted, ref} from "vue";
import {useUserStore} from "@/utils/store.js";

export default {
  setup() {
    // 账号表格
    const accountTable = ref([])
    // 对话框可见标志
    const formLabelWidth = ('120px')
    const userStore = useUserStore();
    onMounted(() => {
      userStore.updateAllData()
      instance.get(`/${userStore.accountType}/getSalesManAccount`)
          .then(result => {
            // result = Result类对象，其data字段为Product类对象列表
            accountTable.value = result.data
          })
    })
    const updatePassword = (ID) => {
      ElMessageBox.prompt('请输入新密码', 'Tip', {
        confirmButtonText: '确认更新',
        cancelButtonText: '取消操作',
        inputPattern: /^(?!\s).{8,16}$/,
        inputErrorMessage: '密码长度在8到16个字符之间，且没有空格。',
      })
          .then(async ({value}) => {
            const result = await instance.post(`/${userStore.accountType}/updateSalesManAccount`, {
              id: ID,
              password: value
            })
            if (result.code === 0) {
              ElMessage({
                type: 'success',
                message: result.data,
              })
            } else {
              ElMessage({
                type: 'error',
                message: result.message,
              })
            }
          })
          .catch(() => {
            ElMessage({
              type: 'info',
              message: '操作被取消',
            })
          })
    }
    return {
      userStore,
      accountTable,
      formLabelWidth,
      updatePassword
    }
  }
}
;
</script>
<style scoped>


div {
  box-sizing: content-box;
}

el-table-column {
  text-align: center;
}

</style>
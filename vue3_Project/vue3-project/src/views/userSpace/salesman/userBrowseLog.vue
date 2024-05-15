<template>
  <div style="height:400px">
    <el-table height="500" :data="LogRecord" border
              :highlight-current-row="true" :row-style="{height:'100px'}">
      <el-table-column label="日志ID" prop="id"></el-table-column>
      <el-table-column label="用户ID" prop="userID"></el-table-column>
      <el-table-column label="商品ID" prop="productID"></el-table-column>
      <el-table-column label="浏览时间" prop="browseTime"></el-table-column>
      <el-table-column label="操作" prop="id">
        <template #default="{row}">
          <el-popconfirm
              title="确定要删除该日志记录？"
              @confirm="deleteLog(row)">
            <template #reference>
              <el-button type="danger">删除</el-button>
            </template>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>
<script>

import instance from "@/utils/request.js";
import {onMounted, onUnmounted, ref} from "vue";
import {useUserStore} from "@/utils/store.js";
import {ElMessage} from "element-plus";

export default {
  setup() {
    const LogRecord = ref([]); // 日志记录
    const deleteIDList = ref([]); // 删除的日志id
    onMounted(() => {
      instance.get("/salesMan/getUserBrowseLog").then(result => {
        LogRecord.value = result.data
      })
    });
    onUnmounted(() => {
      // 提交删除日志请求到后端数据库
      if (deleteIDList.value.length > 0) {
        instance.post("/order/deleteById", deleteIDList.value)
      }
    });
    const deleteLog = (row) => {
      LogRecord.value = LogRecord.value.filter(item => item.id !== row.id)
      deleteIDList.value.push(row.id)
    }
    return {
      LogRecord,
      deleteIDList,
      deleteLog
    }
  }

}
;
</script>
<style scoped>

div {
  box-sizing: content-box;
}


</style>
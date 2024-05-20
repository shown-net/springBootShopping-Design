<template>
  <div style="height:400px">
    <el-table height="500" :data="LogRecord" border
              :highlight-current-row="true" :row-style="{height:'100px'}">
      <el-table-column label="日志ID" prop="id"></el-table-column>
      <el-table-column label="用户ID" prop="userID"></el-table-column>
      <el-table-column label="商品类别" prop="kind"></el-table-column>
      <el-table-column label="浏览时间" prop="browseTime"></el-table-column>
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
    return {
      LogRecord,
      deleteIDList,
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
<template>
  <div style="height:500px">
    <el-table :data="currentPageItems" border
              :highlight-current-row="true" :row-style="{height:'75px'}">
      <el-table-column label="ID" prop="id"></el-table-column>
      <el-table-column label="操作结果" prop="operationRes"></el-table-column>
      <el-table-column label="操作内容" prop="content"></el-table-column>
      <el-table-column label="IP地址" prop="ipAddress"></el-table-column>
      <el-table-column label="操作时间" prop="operatorTime"></el-table-column>
      <el-table-column label="操作" prop="id">
        <template #default="{row}">
          <el-popconfirm
              title="确定要删除该记录吗？"
              @confirm="deleteLog(row)">
            <template #reference>
              <el-button type="danger">删除</el-button>
            </template>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>
  </div>
  <el-pagination style="text-align: center;height: 50px"
                 background
                 layout="prev, pager, next,jumper"
                 :total=operationLog_List.length
                 :current-page="currentPage"
                 :page-size="itemsPerPage"
                 @current-change="handlePageChange">
  </el-pagination>
</template>
<script>
import {onMounted, ref} from "vue";
import instance from "@/utils/request.js";
import {useUserStore} from "@/utils/store.js";

export default {
  setup() {
    const userStore = useUserStore();
    // 展示的原始日志列表
    const operationLog_List = ref([])
    // 组件创建时加载本地的购物车数据
    onMounted(() => {
      userStore.updateAllData()
      //添加商品列表信息到表单
      instance.get(`/${userStore.accountType}/getOperationLog`)
          .then(result => {
            // result.data = Result类对象，其data字段为Product类对象列表
            operationLog_List.value = result.data
            handlePageChange(1)
          })
    })
    // 当前页码
    const currentPage = ref(1)
    // 每页显示的项目数量
    const itemsPerPage = 6
    // 当前页的项目数组
    const currentPageItems = ref([])
    const handlePageChange = (newPage) => {
      currentPage.value = newPage;
      currentPageItems.value = getPaginatedItems(newPage);
    }
    const getPaginatedItems = (page) => {
      const start = (page - 1) * itemsPerPage;
      const end = start + itemsPerPage;
      return operationLog_List.value.slice(start, end);
    }
    const deleteLog = (row) =>{

    }
    return {
      operationLog_List,
      currentPage,
      itemsPerPage,
      currentPageItems,
      handlePageChange,
      getPaginatedItems,
      deleteLog
    }
  }
}

</script>
<style scoped>

</style>
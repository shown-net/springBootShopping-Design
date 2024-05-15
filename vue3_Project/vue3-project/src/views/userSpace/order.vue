<template>
  <div style="height:400px">
    <el-table height="500" :data="orderRecord" border
              :highlight-current-row="true" :row-style="{height:'100px'}">
      <el-table-column label="商品ID" prop="productID"></el-table-column>
      <el-table-column label="商品种类" prop="kind"></el-table-column>
      <el-table-column label="商品名" prop="productName"></el-table-column>
      <el-table-column label="下单时间" prop="buyTime"></el-table-column>
      <el-table-column label="收货人ID" prop="userID"></el-table-column>
      <el-table-column label="购买数量" prop="quantity"></el-table-column>
      <el-table-column label="购买单价" prop="price"></el-table-column>
      <el-table-column label="操作" prop="id">
        <template #default="{row}">
          <el-popconfirm
              title="确定要删除该订单吗？"
              @confirm="deleteOrder(row)">
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
    const orderRecord = ref([]); // 订单记录
    const deleteIDList = ref([]); // 删除的订单id
    const userStore = useUserStore();
    onMounted(async () => {
      userStore.updateAllData();
      try {
        let res;
        if (userStore.accountType === 'salesMan') {
          res = await instance.get("/order/getInfoBySalesMan")
        } else {
          res = await instance.get("/order/getInfoByUser")
        }
        orderRecord.value = res.data
      } catch (e) {
        console.log(e)
      }
    });
    onUnmounted(() => {
      // 提交删除订单请求到后端数据库
      if (deleteIDList.value.length > 0) {
        console.log(deleteIDList)
        instance.post("/order/deleteById", deleteIDList.value)
      }
    });
    const deleteOrder = (row) => {
      orderRecord.value = orderRecord.value.filter(item => item.id !== row.id)
      deleteIDList.value.push(row.id)
    }
    return {
      userStore,
      orderRecord,
      deleteIDList,
      deleteOrder
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
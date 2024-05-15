<template>
  <div>
    <el-table height="500" :data="stats" border
              :highlight-current-row="true" :row-style="{height:'100px'}">
      <el-table-column label="商品种类" prop="kind"></el-table-column>
      <el-table-column label="销售总量" prop="quantity"></el-table-column>
      <el-table-column label="销售额(￥)" prop="price"></el-table-column>
    </el-table>
  </div>
</template>
<script>

import instance from "@/utils/request.js";
import {ElMessage} from "element-plus";
import {onMounted, ref} from "vue";

export default {
  setup() {
    // 统计数据
    const stats = ref([])
    // 对话框可见标志
    const formLabelWidth = ('120px')
    onMounted(() => {
      instance.get("/order/getStatisticsByManager")
          .then(result => {
            // result = Result类对象，其data字段为Product类对象列表
            stats.value = result.data
          })
    })
    return {
      stats,
      formLabelWidth,
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
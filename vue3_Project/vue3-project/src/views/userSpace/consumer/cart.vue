<template>
  <el-table height="400" :data="cart" border @select-all="selectAll()"
            :highlight-current-row="true"
            :row-style="{height:'75px'}">
    <el-table-column width="40px" type="selection">
      <template #default="{row}">
        <el-checkbox v-model="row.selected"></el-checkbox>
      </template>
    </el-table-column>
    <el-table-column label="商品信息" prop="name"></el-table-column>
    <el-table-column label="商品种类" prop="kind"></el-table-column>
    <el-table-column label="购买数量" prop="quantity">
      <template #default="{row}">
        <el-input-number :min="1" v-model="row.quantity"></el-input-number>
      </template>
    </el-table-column>
    <el-table-column label="单价(￥)" prop="price"></el-table-column>
    <el-table-column label="小计">
      <template #default="{row}"> {{ row.price * row.quantity }}</template>
    </el-table-column>
    <el-table-column label="操作">
      <template #default="{row}">
        <el-popconfirm
            title="确定要删除这行吗？"
            @confirm="deleteGoods(row.name)"
        >
          <template #reference>
            <el-button type="danger">删除</el-button>
          </template>
        </el-popconfirm>
      </template>
    </el-table-column>
  </el-table>
  <div class="settlement" style="height: 50px;border: 5px solid #B3C0D1;">
    <div class="amount-sum">已选择{{ countSum }}种商品</div>
    <div class="price-sum-amount">￥总价{{ priceSum }}</div>
    <div class="btn-area">
      <el-popconfirm title="确定要提交这次订单吗？" @confirm="submitOrder">
        <template #reference>
          <el-button size=default>提交订单</el-button>
        </template>
      </el-popconfirm>
    </div>
  </div>
</template>
<script>

import instance from "@/utils/request.js";
import {ElMessage} from "element-plus";
import {computed, onMounted, onUnmounted, ref} from "vue";

export default {
  setup() {
    const cart = ref([])          //购物车存放的商品信息
    const all_checked = ref(false)// 全选按钮是否被选中
    const currentTime = ref(null) //当地时间
    onMounted(() => {
      const cartData = localStorage.getItem('cart')
      // 如果本地数据有购物车信息，加载数据
      if (cartData) {
        cart.value = JSON.parse(cartData)
      }
    });
    onUnmounted(() => {
      // 更新本地cart数据
      localStorage.setItem('cart', JSON.stringify(cart.value))
    })
    const priceSum = computed(() => { // 选中商品的总价格
      return cart.value.reduce((sum, e) => sum + (e.selected ? 1 : 0) * e.price * e.quantity, 0)
    });
    const countSum = computed(() => { // 选中商品的数量
      return cart.value.reduce((sum, e) => sum + (e.selected ? 1 : 0), 0)
    });
    const deleteGoods = (name) => {  // 从购物车删除某个商品
      cart.value = cart.value.filter(item => item.name !== name) // 从表格数据中移除行
    };
    const selectAll = () => { // 全选或取消全选所有商品
      if (cart.value.length > 0) {
        let flag = cart.value[0].selected
        cart.value.forEach(e => {
          e.selected = !flag
        })
      }
    }
    const submitOrder = () => { //提交用户订单
      let OrderList = []
      let date = new Date();
      date = date.toLocaleString('zh-CN', {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit',
        hour: '2-digit',
        minute: '2-digit',
        second: '2-digit'
      });
      cart.value.forEach(goods => {
        // 将要购买的商品加入到信息列表，并从购物车删除
        if (goods.selected === true) {
          OrderList.push({
            productID: goods.productID, kind: goods.kind, productName: goods.name,
            price: goods.price, quantity: goods.quantity, buyTime: date
          })
          // 在购物车删除该商品
          deleteGoods(goods.name)
        }
      })
      if (OrderList.length === 0) {
        ElMessage.error("选择数量为0，请选择要购买的商品");
      } else {
        instance.post("/order/add", OrderList)
        instance.post("/order/sendEmail", OrderList);
        ElMessage.success("提交订单成功")
      }
    }
    return {
      cart,
      all_checked,
      currentTime,
      priceSum,
      countSum,
      deleteGoods,
      selectAll,
      submitOrder,
    }
  }
}
;
</script>
<style scoped>


div {
  box-sizing: content-box;
}


.settlement div {
  float: left;
  display: inline-block;
  font-size: 30px;
  text-align: center;
  margin: 0 2% 0 1%;
  width: 30%;
}


</style>
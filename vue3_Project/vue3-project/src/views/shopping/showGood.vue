<template>
  <div class="product-intro">
    <div v-if="productItem.imageUrl!=null" class="preview-img">
      <el-image :src=(baseImageUrl+productItem.imageUrl) class="img_Product">

      </el-image>
    </div>
    <div class="itemInfo-wrap">
      <div class="item-name" style="font-size: 20px">{{ productItem.name }}</div>
      <div class="summary-line"></div>
      <div class="summary-price">
        <div class="dt">价格</div>
        <div class="dd">
          <div class="p-price">
            <span>￥</span>
            <span class="p-price-value">{{ productItem.price }}</span>
          </div>
        </div>
      </div>
      <div class="summary-line"></div>
      <div class="dt">状态: {{ productItem.state }}</div>
      <div class="summary-line"></div>
      <div class="action-button-wrapper">
        <div class="item-countSum-wrapper">
          <el-input-number v-model="targetCount" :min="1" :max="200"/>
        </div>
        <el-button size="large" @click="addToCart()" type="danger">加入购物车</el-button>
      </div>
    </div>
  </div>
</template>

<script>

import {onMounted, onUnmounted, reactive, ref} from "vue";
import instance from "@/utils/request.js";
import {ElMessage} from "element-plus";
import {useRouter} from "vue-router";
import {useUserStore} from "@/utils/store.js";

export default {
  setup() {
    const router = useRouter()
    const userStore = useUserStore();
    // 商品的购买数量
    const targetCount = ref(0);
    //
    const baseImageUrl = import.meta.env.VITE_APP_BASE_URL + '/images/download/'
    // 用来存储计时器ID
    let timer;
    // 该商品的浏览时长
    const browseTime = ref(0)
    // 启动计时器
    const startTimer = () => {
      timer = setInterval(() => {
        browseTime.value++; // 每秒增加1
      }, 1000);
    };
    // 发送浏览时长到后端
    const sendBrowseTime = () => {
      if (browseTime.value >= 5) {
        // 只发送浏览时间大于5秒的记录
        instance.post(`/${userStore.accountType}/sendBrowserTime`, {
          kind:productItem.kind,
          browseTime: browseTime.value
        })
      }
    }
    // 停止计时器
    const stopTimer = () => {
      clearInterval(timer);
    };
    // 组件创建时加载本地的购物车数据
    onMounted(() => {
      startTimer();
      const routeParams = router.currentRoute.value.query;
      if (routeParams.productID) {
        productItem.id = routeParams.productID
      }
      //添加商品列表信息到表单
      instance.get("/product/getInfoByProductID?productID=" + productItem.id)
          .then(result => {
            if (result.code === 0) {
              productItem.kind = result.data.kind
              productItem.name = result.data.name
              productItem.price = result.data.price
              productItem.imageUrl = result.data.imageUrl
              productItem.state = result.data.state
            } else {
              ElMessage.error(result.message + ' ' + "请重试")
            }
          })
      if (localStorage.getItem('cart')) {
        // 数据格式转换
        cart.value = JSON.parse(localStorage.getItem('cart'))
      }
    })
    onUnmounted(() => {
      stopTimer();
      sendBrowseTime();
      // 更新本地数据
      localStorage.setItem('cart', JSON.stringify(cart.value))
    })
    // 购物车存放的商品信息
    const cart = ref([])
    // 添加商品至购物车
    const addToCart = () => {
      // 如果用户本地数据不存在
      if (localStorage.getItem('userInfo') == null) {
        // 重定向到登录界面
        ElMessage.error("用戶未登录,请登录后再添加商品至购物车");
        router.push('/rl')
        return;
      }
      if (productItem.state === '无货') {
        ElMessage.error("暂时没有余货，请选择其他商品")
      } else if (productItem.state === '有货') {
        const existingProduct = cart.value.find(item => item.productID === productItem.id);
        if (existingProduct) {
          existingProduct.quantity += targetCount.value;
        } else {
          cart.value.push({
            productID: productItem.id,
            name: productItem.name,
            kind: productItem.kind,
            price: productItem.price,
            quantity: targetCount.value,
            selected: false
          });
        }
        // 更新本地数据
        localStorage.setItem('cart', JSON.stringify(cart.value))
        ElMessage.success("已添加至购物车")
      } else {
        ElMessage.error("该商品还未开始售卖")
      }
    }
    const productItem = reactive({
      id: null,
      kind: null,
      name: null,
      price: null,
      imageUrl: null,
      state: null
    })
    return {
      productItem,
      targetCount,
      userStore,
      addToCart,
      baseImageUrl,
      startTimer,
      stopTimer,
      sendBrowseTime
    }
  }
}

</script>
<style scoped>
.product-intro {
  position: relative;
  float: left;
  height: 100%;
  margin: 100px 0px 100px 100px;
  padding-bottom: 10px;
}

.preview-img {
  width: 500px;
  float: left;
  margin: 100px 50px 100px 100px;
}

.img_Product {
  width: 400px;
  height: 400px;
  display: block;
  text-align: center;
  cursor: pointer;
  overflow: hidden;
  border-bottom: 1px solid #ebebeb;
  padding-bottom: 25px;
  margin: 15px auto 0;
  position: relative;
}

.itemInfo-wrap .dt {
  padding-left: 10px;
  font-family: simsun;
  font-size: 20px;
  color: #999;
}

.itemInfo-wrap .dd {
  font-size: 40px;
  margin-left: 70px;
}

.itemInfo-wrap {
  float: left;
  width: 590px;
  line-height: 125px;
  margin-left: 50px;
  margin-bottom: 10px;
}

.item-name {
  font: 700 16px Arial, "microsoft yahei";
  color: #666;
  padding-top: 10px;
  line-height: 28px;
  margin-bottom: 5px;
}

.summary-price {
  display: flex;
  margin-top: 10px;
}

.action-button-wrapper {
  display: flex;
  text-align: center;
}

.item-countSum-wrapper {
  margin-right: 100px;
  display: flex;
}

.summary-line {
  height: 0;
  overflow: hidden;
  border-bottom: 5px dotted #dfdfdf;
  margin-bottom: 15px;
}
</style>
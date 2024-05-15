<template>
  <div class="main">
    <ul>
      <li v-for="v in product_List" v-bind:key="v.id">
        <el-image v-if="v.imageUrl!=null" :src=(baseImageUrl+v.imageUrl) alt="" class="img_Product"></el-image>
        <h4>{{ v.name }}</h4>
        <p>{{ "￥" + v.price }}</p>
        <el-button @click="moveItemShow(v.id)" type="danger">查看商品详情</el-button>
      </li>
    </ul>
  </div>
</template>
<script>
import instance from "@/utils/request.js";
import {onMounted, onUnmounted, ref} from "vue";
import {ElMessage} from "element-plus";
import {useRouter} from "vue-router";
import {useUserStore} from "@/utils/store.js";


export default {
  setup() {
    const router = useRouter();
    const userStore = useUserStore();
    // 组件创建时加载本地的购物车数据
    onMounted(() => {
      userStore.updateAllData();
      //添加商品列表信息到表单
      instance.get(`/${userStore.accountType}/getPreferredProduct`)
          .then(result => {
            // result.data = Result类对象，其data字段为Product类对象列表
            product_List.value = result.data
          })
    })
    // 跳转单个商品展示页面
    const moveItemShow = (ID) => {
      router.push({path: `item-show`, query: {productID: ID}})
    }
    const baseImageUrl = import.meta.env.VITE_APP_BASE_URL + '/images/download/'
    // 展示的原始商品列表
    const product_List = ref([])

    return {
      baseImageUrl,
      product_List,
      moveItemShow
    }
  }
}

</script>
<style scoped>

.main {
  display: flex;
  background-color: #f5f5f5;
}

ul li {
  display: block; /* 将ul的行内元素转换为块元素 */
  float: left; /* 增加了左浮动属性*/
  width: 290px;
  height: 385px;
  background-color: #fff;
  margin-right: 2%;
  margin-bottom: 2%;
  vertical-align: top;
  padding: 10px 10px 10px 10px;
  position: relative;
  overflow: hidden;
}

.img_Product {
  width: 200px;
  height: 200px;
  display: block;
  text-align: center;
  cursor: pointer;
  overflow: hidden;
  border-bottom: 1px solid #ebebeb;
  padding-bottom: 25px;
  margin: 15px auto 0;
  position: relative;
}

</style>
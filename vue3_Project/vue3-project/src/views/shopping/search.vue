<template>
  <div class="main">
    <el-container>
      <el-header>
        <el-checkbox-group v-model="checkKinds" :min="0" :max="6" @change="handleCheckedKindsChange">
          <el-checkbox-button v-for="kind in kinds" :value="kind" :key="kind">
            {{ kind }}
          </el-checkbox-button>
        </el-checkbox-group>
      </el-header>
      <el-main class="productDetail">
        <ul>
          <li v-for="v in currentPageItems" v-bind:key="v.id">
            <el-image v-if="v.imageUrl!=null" :src=(baseImageUrl+v.imageUrl) alt="" class="img_Product"></el-image>
            <h4>{{ v.name }}</h4>
            <p>{{ "￥" + v.price }}</p>
            <el-button @click="moveItemShow(v.id)" type="danger">查看商品详情</el-button>
          </li>
        </ul>
      </el-main>
      <el-pagination style="text-align: center;height: 50px"
                     background
                     layout="prev, pager, next,jumper"
                     :total=product_List.length
                     :current-page="currentPage"
                     :page-size="itemsPerPage"
                     @current-change="handlePageChange">
      </el-pagination>
    </el-container>
  </div>
</template>
<script>
import instance from "@/utils/request.js";
import {onMounted, onUnmounted, ref} from "vue";
import {ElMessage} from "element-plus";
import {useRouter} from "vue-router";


export default {
  setup() {
    const kind_Options = ['笔记本电脑', '台式电脑', '平板电脑', '手机通信', '智能产品', '配件办公']
    const router = useRouter();
    // 组件创建时加载本地的购物车数据
    onMounted(() => {
      //添加商品列表信息到表单
      instance.post("/product/getProductInfo", [])
          .then(result => {
            // result.data = Result类对象，其data字段为Product类对象列表
            product_List.value = result.data
            handlePageChange(1)
          })
    })
    // 跳转单个商品展示页面
    const moveItemShow = (ID) => {
      router.push({path: `item-show`, query: {productID: ID}})
    }
    const baseImageUrl = import.meta.env.VITE_APP_BASE_URL + '/images/download/'
    // 展示的原始商品列表
    const product_List = ref([])
    // 选择的商品种类
    const checkKinds = ref([])
    // 总共的商品种类
    const kinds = ref(kind_Options)
    // 当前页码
    const currentPage = ref(1)
    // 每页显示的项目数量
    const itemsPerPage = 8
    // 当前页的项目数组
    const currentPageItems = ref([])
    const handleCheckedKindsChange = () => {
      let kindList = checkKinds.value
      if (kindList.length === kind_Options.length) {
        // 如果选择的种类数组为全满，则直接发送空数组表示不进行商品种类筛选
        kindList = []
      }
      // 根据商品种类更新后端筛选后的商品列表
      instance.post("/product/getProductInfo", kindList)
          .then(result => {
            // result = Result类对象，其data字段为Product类对象列表
            product_List.value = result.data
            currentPageItems.value = getPaginatedItems(currentPage.value);
          })
    }
    const handlePageChange = (newPage) => {
      currentPage.value = newPage;
      currentPageItems.value = getPaginatedItems(newPage);
    }
    const getPaginatedItems = (page) => {
      const start = (page - 1) * itemsPerPage;
      const end = start + itemsPerPage;
      return product_List.value.slice(start, end);
    }

    return {
      baseImageUrl,
      product_List,
      checkKinds,
      kinds,
      currentPage,
      itemsPerPage,
      currentPageItems,
      handleCheckedKindsChange,
      handlePageChange,
      getPaginatedItems,
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
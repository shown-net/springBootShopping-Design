<template>
  <el-menu
      mode="horizontal"
      background-color="#545c64"
      text-color="#fff"
      active-text-color="#ffd04b"
      router>
    <el-menu-item>logo-test</el-menu-item>
    <el-menu-item index="/" v-if="userStore.accountType==='user'||userStore.accountType===null">商城主页</el-menu-item>
    <el-menu-item index="/shopping" v-if="userStore.accountType==='user'||userStore.accountType===null">商品搜索
    </el-menu-item>
    <el-menu-item index="/item-recommend" v-if="userStore.accountType==='user'">商品推荐</el-menu-item>
    <el-sub-menu index=" 3
      ">
      <template #title>
        {{ currentAccountName }}
      </template>
      <el-menu-item index="/userSpace/info">空间首页</el-menu-item>
      <el-menu-item index="/userSpace/info">个人信息</el-menu-item>
      <el-menu-item index="/userSpace/cart" v-if="userStore.accountType==='user'">购物车</el-menu-item>
      <el-menu-item index="/userSpace/catalogue" v-if="userStore.accountType==='salesMan'">商品目录</el-menu-item>
      <el-menu-item index="/userSpace/order">订单记录</el-menu-item>
    </el-sub-menu>
  </el-menu>
  <router-view></router-view>
</template>
<script>
import {useUserStore} from "@/utils/store.js";
import {computed, onMounted, ref} from "vue";

export default {
  setup() {
    // 创建一个响应式引用来存储用户信息
    const userStore = useUserStore();
    const userInfo = ref(null);
    onMounted(() => {
      userStore.updateAllData();
    })
    const currentAccountName = computed(() => { // 当前账户名
      if (userStore.accountName == null) {
        return '账户未登录';
      } else if (userStore.accountType === 'user') {
        return '用户' + userStore.accountName;
      } else if (userStore.accountType === 'salesMan') {
        return '商家' + userStore.accountName;
      } else {
        return '管理者' + userStore.accountName;
      }
    });
    // 返回需要暴露给模板的属性
    return {
      userStore,
      userInfo,
      currentAccountName,
      onMounted
    };
  }
}
</script>
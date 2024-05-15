<template>
  <div class="main">
    <div class="left-content">
      <div class="logo-bg"></div>
    </div>
    <div class="right-content">
      <div class="contain">
        <h3 class="title" style="font-size: 40px">欢迎注册|登录</h3>
        <br>
        <router-link to="/rl/register">注册账户</router-link>
        ||
        <router-link to="/rl/login">登录账户</router-link>
        <router-view></router-view>
      </div>
    </div>
  </div>

</template>
<script>

export default {

}

</script>
<style scoped>

.main {
  background: url("../../assets/sign_bg.jpg");
  width: 100%;
  height: 100%;
  background-size: cover;
  position: fixed;
}

.left-content {
  float: left;
  width: 50%;
  height: 100%;

}

.logo-bg {
  width: 40%;
  height: 25%;
  background-image: url("../../assets/sign_logo.png");
  background-repeat: no-repeat;
  background-position: center;
  background-size: 160px, 90px;
}

.right-content {
  float: right;
  width: 50%;
  height: 100%;
  padding-top: 3%;
  //padding-bottom: 5%;
}

.contain {
  width: 70%;
  height: 80%;
  border-radius: 10px;
  margin: 0px auto;
  padding: 20px 20px 10px 20px;
  background-color: rgba(255, 255, 255, 0.8);
  text-align: left;
  box-shadow: 0 0 20px 2px rgba(0, 0, 0, 0.1);
}

.title {
  margin: 0px 0px auto auto;
  text-align: left;
}


</style>
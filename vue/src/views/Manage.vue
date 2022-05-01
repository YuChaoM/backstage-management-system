<template>

  <el-container style="height: 100vh">

    <!--左边导航栏start-->
    <el-aside :width="sideWidth + 'px'"
              style="background-color: rgb(238, 241, 246); box-shadow: 2px 0 6px rgb(0 21 41 / 35%);">
      <Aside :isCollapse="isCollapse" :logoTextShow="logoTextShow" />
    </el-aside>
    <!--左边导航栏end-->

    <el-container>
      <el-header style="border-bottom: 1px solid #ccc;">
        <Header :collapseBtnClass="collapseBtnClass" :user="user"/>
      </el-header>

      <el-main>
        <!-- 表示当前页面的子路由会在 <router-view /> 里面展示-->
        <router-view @refreshUser="getUser"/>
      </el-main>
    </el-container>
  </el-container>
</template>

<script>


import Aside from "../components/Aside";
import Header from "../components/Header";

export default {
  name: 'Home',
  data() {
    return {
      collapseBtnClass: 'el-icon-s-fold',
      isCollapse: false,
      sideWidth: 200,
      logoTextShow: true,
      // user:localStorage.getItem("user")?JSON.parse(localStorage.getItem("user")):{},
      user: {}

    }
  },
  components: {
    Aside,
    Header
  },
  created() {
    this.getUser()//一开始就从后台过去数据
  },
  provide() {//用于把父组件的方法传到字子组件
    return {
      fatherMethod: this.collapse,
    };
  },
  methods: {
    collapse() {  // 点击收缩按钮触发
      this.isCollapse = !this.isCollapse
      if (this.isCollapse) {  // 收缩
        this.sideWidth = 64
        this.collapseBtnClass = 'el-icon-s-unfold'//换为展开的图标
        this.logoTextShow = false
      } else {   // 展开
        this.sideWidth = 200
        this.collapseBtnClass = 'el-icon-s-fold'
        this.logoTextShow = true
      }
    },
    getUser() {
      let username = localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")).username : ""
      this.request.get("user/username/" + username).then(res => {
        this.user = res.data//把最新的user传给header，不是浏览器缓存的了
        console.log(res)
      })
    }
  }

}
</script>

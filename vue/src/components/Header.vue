<template>
  <div style="line-height: 60px; display: flex">
    <div style="flex: 1;">
      <span :class="collapseBtnClass" style="cursor: pointer; font-size: 18px" @click="collapse"></span>
      <el-breadcrumb separator="/" style="display: inline-block; margin-left: 10px">
        <el-breadcrumb-item :to="'/'">首页</el-breadcrumb-item>
        <el-breadcrumb-item>{{ currentPathName }}</el-breadcrumb-item>
      </el-breadcrumb>
    </div>

    <el-dropdown style="width: 150px;cursor: pointer">
      <div style="display: inline-block">
<!--        <img :src="user.avatarUrl"-->
<!--             style="width: 30px;border-radius: 50%;position:relative;top: 10px;right: 5px">-->
        <span style="right: 10px; position:relative;font-size: 16px;">{{ user.nickname }}</span>
        <el-avatar :src="user.avatarUrl" fit="contain"  style="position:relative;top: 12px;"></el-avatar>
<!--        <i class="el-icon-arrow-down" style="margin-left: 5px"></i>-->
      </div>
      <el-dropdown-menu slot="dropdown" style=" width: 100px; text-align: center">
        <!--        <el-dropdown-item >个人信息</el-dropdown-item>-->
        <el-dropdown-item style="font-size: 14px; padding: 5px 0">
          <router-link to="/person" style="text-decoration: none">个人信息</router-link>
        </el-dropdown-item>
        <el-dropdown-item style="font-size: 14px; padding: 5px 0">
          <router-link to="/password" style="text-decoration: none">修改密码</router-link>
        </el-dropdown-item>
        <el-dropdown-item style="font-size: 14px; padding: 10px 0">
          <span style="text-decoration: none" @click="logout">退出</span>
        </el-dropdown-item>
      </el-dropdown-menu>
    </el-dropdown>
  </div>

</template>

<script>
import user from "../views/User";

export default {
  name: "Header",
  props: {//定义的变量用来接受manage传过来的数据
    collapseBtnClass: String,
    user: Object,
  },
  computed: {
    currentPathName() {//页签功能
      let msg = this.$store.state.currentPathName//需要监听的数据
      if (msg == "首页") {
        return
      } else {
        return msg
      }
      // return this.$store.state.currentPathName;
    }
  },
  data() {
    return {
      //user已经传过来了，不需要在获取了
      // user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {}
    }
  },
  metaInfo() {
    return {
      meta: [
        {name: "referrer", content: "no-referrer"},
      ],
    };
  },
  inject: ["fatherMethod"],//注入进来
  methods: {
    collapse() {
      this.$emit("asideCollapse")
      this.fatherMethod();
    },
    logout() {
      // this.$router.push("/login")
      // localStorage.removeItem("user")//退出后删除信息
      // localStorage.removeItem("menus")//退出后删除信息
      this.$store.commit("logout")
      this.$message.success("退出成功")
    }
  }

}
</script>

<style scoped>

</style>
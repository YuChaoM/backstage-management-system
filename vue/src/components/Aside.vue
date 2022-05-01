<template>
  <el-menu :default-openeds="opens" style="height: 100%;overflow-x: hidden; "
           background-color="rgb(48,65,86)"
           text-color="#fff"
           active-text-color="#ffd04b"
           :collapse-transition="false"
           :collapse="isCollapse"
           router
           @select="handleSelect"
  >
    <div style="height: 60px;line-height: 60px;text-align: center">
      <img src="../assets/img.png" alt="" style="width: 25px;position: relative;top: 6px;margin-right: 5px">
      <b style="color: white" v-show="logoTextShow">后台管理系统</b>
    </div>

    <div v-for="item in menus" :key="item.id" >
      <!--      有path就是一级菜单-->
      <div v-if="item.path">
        <!--    index指定要跳转的路由-->
        <el-menu-item :index="item.path">
          <i :class=item.icon></i><span slot="title">{{ item.name }}</span>
        </el-menu-item>
      </div>
      <!--      二级菜单没有path-->
      <div v-else>
        <el-submenu :index="item.id + ''">
          <template slot="title"><i :class="item.icon"></i><span slot="title">{{ item.name }}</span></template>
          <div v-for="subItem in item.children" :key="subItem.id">
            <el-menu-item :index="subItem.path">
              <i :class=subItem.icon></i><span slot="title">{{ subItem.name }}</span>
            </el-menu-item>
          </div>

        </el-submenu>
      </div>
    </div>
    <!--    <el-menu-item index="/user"><i class="el-icon-s-custom"></i><span slot="title">用户管理</span></el-menu-item>-->
    <!--    <el-menu-item index="/role"><i class="el-icon-s-custom"></i><span slot="title">角色管理</span></el-menu-item>-->
    <!--    <el-menu-item index="/menu"><i class="el-icon-s-custom"></i><span slot="title">菜单管理</span></el-menu-item>-->
    <!--    <el-menu-item index="/file"><i class="el-icon-document"></i><span slot="title">文件管理</span></el-menu-item>-->

  </el-menu>
</template>


<script>
export default {
  name: "Aside",
  data() {
    return {
      menus: localStorage.getItem("menus") ? JSON.parse(localStorage.getItem("menus")) : [],
      opens:localStorage.getItem("menus") ? JSON.parse(localStorage.getItem("menus")).map(v => v.id + '') : null
    }
  },
  props: {
    isCollapse: Boolean,
    logoTextShow: Boolean,
  },
  methods: {
    handleSelect(index) {
      console.log(index)
      console.log(this.$router.options.routes)
    }
  }
}
</script>

<style scoped>
.fontSize18{
  font-size: 18px;
}
.el-menu--collapse span {
  visibility: hidden;
}

</style>
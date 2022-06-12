<template>
  <div style="color: #666;font-size: 14px;">
    <div style="padding-bottom: 20px">
      <b>欢迎你！{{ user.nickname }}</b>
    </div>
    <el-card>
      大数据三班
      <el-divider />
      虎虎生威，虎年大吉
    </el-card>
    <div style="height: 1px; background: #ddd; margin: 20px 0"/>

    <el-row :gutter="30">
      <el-col :span="12">
        <el-card>
          <div slot="header" class="clearfix">
            <span>得闲饮茶</span>
          </div>
          <div style="text-align: center">
            <img style="height: 300px" src="https://fastly.jsdelivr.net/gh/YuChaoM/images/Golang/11213917c59e902b48a0825090ac018.jpg" alt="">
            <div class="pd-10">关注我</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <div style="padding: 20px 0; font-size: 20px">项目使用的技术栈</div>
        <el-row>
          <el-col :span="12" style="line-height: 30px">
            <div><b>后端</b></div>
            <div>SpringBoot2</div>
            <div>Hutool</div>
            <div>Poi</div>
            <div>Lombok</div>
            <div>Mybatis/Mybatis-plus</div>
          </el-col>
          <el-col :span="12" style="line-height: 30px">
            <b>前端</b>
            <div>Vue2</div>
            <div>Vue-Router</div>
            <div>VueX</div>
            <div>ElementUI</div>
            <div>Axios</div>
          </el-col>
        </el-row>
      </el-col>
    </el-row>

    <el-row style="margin: 20px 0;">
      <el-col :span="12">
        <div class="m-10" style="margin: 10px 0;">
          <el-button type="danger"><i class="el-icon-coin"></i><a style="color: #fff" href="http://120.25.172.243">免费开源资料仓库</a></el-button>
          <el-button type="primary"><i class="el-icon-link"></i> <a style="color: #fff" href="https://gitee.com/meng-yuchao/manage-system">Gitee源码</a></el-button>

        </div>
      </el-col>
    </el-row>

      <el-dialog title="温馨提示" :visible.sync="Visit" width="30%" v-if="user.role == 'ROLE_USER' ">
        <div >请联系管理员开通权限:mengyuchaotide@gmail.com</div>
      </el-dialog>
  </div>

</template>

<script>
export default {
  name: "Home",
  data() {
    return {
      Visit: true,
      user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {}
    }
  }
}
</script>

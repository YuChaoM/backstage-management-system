<template>
  <el-card style="width: 500px;margin: auto">
    <el-form label-width="80px" size="small">
<!--      <el-form-item label="用户名">-->
<!--        <el-input v-model="form.username" autocomplete="off"></el-input>-->
<!--      </el-form-item>-->
      <el-form-item label="昵称">
        <el-input v-model="form.nickname" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="邮箱">
        <el-input v-model="form.email" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="电话">
        <el-input v-model="form.phone" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="地址">
        <el-input v-model="form.address" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item >
        <el-button type="primary" @click="save()">确 定</el-button>
      </el-form-item>
    </el-form>
<!--    <div slot="footer" class="dialog-footer">-->
<!--      -->
<!--    </div>-->
  </el-card>
</template>

<script>
export default {
  name: "person",
  data(){
    return{
      form:{},
      //当前登录用户浏览器保存的用户信息，不全
      user:localStorage.getItem("user")?JSON.parse(localStorage.getItem("user")):{},
    }
  },
  created() {//用来请求后台接口的
    this.request.get("user/username/"+this.user.username).then(res =>{
      if (res.code === '200'){
          this.form = res.data
      }
    })
  },
  methods:{
    save() {
      //.then()就是后台返回的结果 ，url在request.js添加了前缀
      this.request.post("/user", this.form).then(res => {
        console.log(res)
        if (res.data) {
          this.$message.success("保存成功")
        } else {
          this.$message.error("失败")
        }
      })
    },
  }
}
</script>

<style scoped>

</style>
<template>
  <el-card style="width: 500px;margin: auto">
    <el-form label-width="80px" size="small">
      <el-upload
          class="avatar-uploader"
          action="http://localhost:9090/file/upload"
          :show-file-list="false"
          :on-success="handleAvatarSuccess">
        <img v-if="form.avatarUrl" :src="form.avatarUrl" class="avatar">
        <i v-else class="el-icon-plus avatar-uploader-icon"></i>
      </el-upload>

      <el-form-item label="用户名" >
        <el-input v-model="form.username" disabled autocomplete="off"></el-input>
      </el-form-item>
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
        <el-input type="textarea" v-model="form.address" autocomplete="off"></el-input>
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
      // //当前登录用户浏览器保存的用户信息，返回的是dto
      user:localStorage.getItem("user")?JSON.parse(localStorage.getItem("user")):{},
    }
  },
  created() {//用来请求后台接口的,一进个人信息页就渲染页面数据
    this.getUser().then(res =>{
      console.log(res)
      this.form= res
    })
  },
  methods:{
    async getUser(){
      return (await this.request.get("user/username/"+this.user.username)).data
    },
    save() {
      //.then()就是后台返回的结果 ，url在request.js添加了前缀
      this.request.post("/user", this.form).then(res => {
        console.log(res)
        if (res.code === '200') {
          this.$message.success("保存成功")

          //触发父级更新user的方法,子(person)传父(manage)在父传子(header)
          this.$emit("refreshUser")
          this.getUser().then(res =>{//更新头像后，重新在数据库获取user
            res.token = JSON.parse(localStorage.getItem("user")).token
            localStorage.setItem("user",JSON.stringify(res))//重新存到浏览器
          })
        } else {
          this.$message.error("失败")
        }
      })
    },
    handleAvatarSuccess(res){//文件上传成功后，后台返回url
      console.log(res)
        this.form.avatarUrl = res//赋值一下，点确认会掉用save方法
    },
  }
}
</script>

<style scoped>
.avatar-uploader{
  text-align: center;
  padding-bottom: 15px;
}
.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;

}
.avatar-uploader .el-upload:hover {
  border-color: #409EFF;

}
.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  line-height: 178px;
  text-align: center;
}
.avatar {
  width: 138px;
  height: 138px;
  display: block;
}
</style>
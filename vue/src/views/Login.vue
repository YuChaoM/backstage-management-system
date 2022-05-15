<template>
  <div class="wrapper">
    <div
        style="margin:auto;background-color: #fff; width: 350px; height: 400px; padding: 20px; border-radius: 10px">
      <div style="margin: 20px 0; text-align: center; font-size: 24px"><b>登 录</b></div>
      <el-form :model="user" :rules="rules" ref="userForm">
        <el-form-item prop="username">
          <el-input size="medium" style="margin: 10px 0" prefix-icon="el-icon-user" v-model="user.username"></el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input size="medium" style="margin: 10px 0" prefix-icon="el-icon-lock" show-password
                    v-model="user.password"></el-input>
        </el-form-item>
        <el-form-item prop="captcha">
          <el-input size="medium" style="margin: 10px 0; width: 180px; vertical-align:middle" prefix-icon="el-icon-lock"
                    v-model="user.captchaCode"></el-input>
          <img :src="captchaUrl" style="padding: 0px 10px 0px 10px; vertical-align:middle" @click="updateCaptcha">
        </el-form-item>
        <el-form-item style="margin: 10px 0; text-align: right">
          <el-button type="warning" size="small" autocomplete="off" @click="$router.push('/register')">注册</el-button>
          <el-button type="primary" size="small" autocomplete="off" @click="login">登录</el-button>
        </el-form-item>
        <el-form-item style="margin: 10px 0; text-align: right">
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
import {setRoutes} from "@/router";

export default {
  name: "Login",
  data() {
    return {
      user: {},//user对象发送到后台校验
      rules: {//表单校验规则
        username: [
          {required: true, message: '请输入用户名', trigger: 'blur'},
          {min: 3, max: 10, message: '长度在 3 到 5 个字符', trigger: 'blur'}
        ],
        password: [
          {required: true, message: '请输入密码', trigger: 'blur'},
          {min: 6, max: 20, message: '长度在 6 到 20 个字符', trigger: 'blur'}
        ],
      },
      captchaUrl: '',
      key: '',
    }
  },
  created() {
    this.updateCaptcha()
  },
  methods: {
    updateCaptcha() {
      this.request.get("/captcha/getkey").then(res => {
        if (res.code === '200') {
          console.log(res)
          this.key = res.data
          this.captchaUrl = 'http://localhost:9090/captcha?key=' + this.key
        }
      })
    },
    login() {
      this.$refs['userForm'].validate((valid) => {//校验不合法时不会发请求
        if (valid) {  // 表单校验合法
          this.request.post("/user/login", this.user, {
            params: {
              key: this.key
            }
          }).then(res => {
            console.log(res)
            if (res.code === '200') {
              localStorage.setItem("user", JSON.stringify(res.data))  // 存储用户信息到浏览器,包含token
              localStorage.setItem("menus", JSON.stringify(res.data.menus))  // 存储用户菜单信息到浏览器，只有登录的接口才会处理菜单信息
              // // 动态设置当前用户的路由
              setRoutes()
              // this.$router.push("/")
              this.$message.success("登录成功")
              // debugger
              if (res.data.role === 'ROLE_STUDENT') {//后台首页
                this.$router.push("/front/home")
              } else {//后台首页
                this.$router.push("/")
              }
            } else {
              this.$message.error(res.msg)//报后台返回的信息
              this.updateCaptcha()
            }

          })
        }
      })
    }
  }
}
</script>

<style>
.wrapper {
  height: 100vh; /*撑满窗口*/
  display: flex;
  justify-content: center;
  align-items: center;
  /*background-image: linear-gradient(to bottom right, #FC466B, #3F5EFB); !*渐变色*!*/
  background-image: url("../assets/login.jpg");
  background-size: cover;
  /*background-image: linear-gradient(-20deg, #d558c8 0%, #24d292 100%);*/
  overflow: hidden;
}

</style>

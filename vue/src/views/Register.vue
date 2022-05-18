<template>
  <div class="wrapper">
    <div
        style="margin: 350px auto; background-color: #fff; width: 360px; height: 500px; padding: 20px; border-radius: 10px">
      <div style="margin: 20px 0; text-align: center; font-size: 24px"><b>注 册</b></div>
      <el-form :model="user" :rules="rules" ref="userForm">
        <el-form-item prop="username" :error="errorMsg">
          <el-input placeholder="请输入账号" size="medium" style="margin: 5px 0" prefix-icon="el-icon-user"
                    v-model="user.username" @blur="checkMobile"></el-input>
        </el-form-item>
        <el-form-item prop="email">
          <el-input placeholder="请输入账号" size="medium" style="margin: 10px 0" prefix-icon="el-icon-message"
                    v-model="user.email"></el-input>
        </el-form-item>
        <el-form-item prop="code">
          <el-input size="medium" style="margin: 10px 0; width: 195px" prefix-icon="el-icon-lock"
                    v-model="user.captchaCode"></el-input>
          <el-button type="primary" size="small" class="ml-5" @click="sendCode(1)" :disabled="disabled">
            {{ btnText }}
          </el-button>
        </el-form-item>
        <el-form-item prop="password">
          <el-input placeholder="请输入密码" size="medium" style="margin: 5px 0" prefix-icon="el-icon-lock"
                    show-password v-model="user.password"></el-input>
        </el-form-item>
        <el-form-item prop="confirmPassword">
          <el-input placeholder="请确认密码" size="medium" style="margin: 5px 0" prefix-icon="el-icon-lock"
                    show-password v-model="user.confirmPassword"></el-input>
        </el-form-item>
        <el-form-item style="margin: 5px 0; text-align: right">
          <el-button type="primary" size="small" autocomplete="off" @click="register">注册</el-button>
          <el-button type="warning" size="small" autocomplete="off" @click="$router.push('/login')">返回登录</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
import user from "./User";

export default {
  name: "Login",
  data() {
    return {
      user: {},
      rules: {
        username: [
          {required: true, message: '请输入用户名', trigger: 'blur'},
          {min: 3, max: 20, message: '长度在 3 到 20 个字符', trigger: 'blur'}
        ],
        password: [
          {required: true, message: '请输入密码', trigger: 'blur'},
          {min: 6, max: 20, message: '长度在 6 到 20 个字符', trigger: 'blur'}
        ],
        confirmPassword: [
          {required: true, message: '请输入密码', trigger: 'blur'},
          {min: 6, max: 20, message: '长度在 6 到 20 个字符', trigger: 'blur'}
        ],
      },
      errorMsg: "",
      flag: true,//不允许注册
      disabled: false,
      totalCount: 0,
    }
  },
  created() {
    this.request.get("/captcha/getkey").then(res => {
      if (res.code === '200') {
        this.user.key = res.data
      }
    })
  },
  computed: {
    btnText() {
      return this.totalCount !== 0 ? `${this.totalCount}秒再次获取` : "获取验证码"
    }
  },
  methods: {
    // 鼠标失焦旧校验用户名
    checkMobile() {
      this.request.post("/user/check", this.user).then(res => {
        console.log(res)
        if (res.code === '600') {
          this.flag = false
          this.errorMsg = "账号已存在，请换一个"
          this.$message.warning(res.msg)
        } else {
          this.flag = true
        }
      })

    },
    register() {
      this.$refs['userForm'].validate((valid) => {
        if (valid) {  // 表单校验合法
          if (this.user.password !== this.user.confirmPassword) {
            this.$message.error("两次输入的密码不一致")
            return false
          }
          console.log(this.flag)
          if (this.flag) {//发送注册请求
            this.request.post("/user/register", this.user).then(res => {
              if (res.code === '200') {
                this.$message.success("注册成功")
              } else {
                this.$message.warning(res.msg)
              }
            })
          } else {
            this.user.username = ""
            this.user.password = ""
            this.user.confirmPassword = ""
          }
        }
      })
    },
    sendCode(type) {
      // 按钮60秒倒计时
      this.totalCount = 60
      this.disabled = true
      this.sendEmailCode(type) //60秒过倒计时过后才能调用的事件
      this.interval = setInterval(() => {
        this.totalCount--
        if (this.totalCount === 0) {
          clearInterval(this.interval)
          this.disabled = false
        }
      }, 1000);
    },
    sendEmailCode(type) {
      let email = this.user.email
      console.log(email)
      if (!email) {
        this.$message.warning("请输入邮箱")
        return;
      }
      if (!/^\w+((.\w+)|(-\w+))@[A-Za-z0-9]+((.|-)[A-Za-z0-9]+).[A-Za-z0-9]+$/.test(email)) {
        this.$message.warning("请输入正确的邮箱账号")
        return
      }
      //发送邮箱验证码
      this.request.get("/user/email/" + email + "/" + type, {
        params: {
          key: this.user.key
        }
      }).then(res => {
        if (res.code === '200') {
          this.$message.success("发送成功")
        } else {
          this.$message.error(res.msg)
        }
      })
    },
  }
}
</script>

<style>
.wrapper {
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  /*background-image: linear-gradient(to bottom right, #FC466B , #3F5EFB);*/
  /*background-image: linear-gradient(-20deg, #b721ff 0%, #21d4fd 100%);*/
  background-image: url("../assets/login.jpg");
  background-size: cover;
  overflow: hidden;
}
</style>

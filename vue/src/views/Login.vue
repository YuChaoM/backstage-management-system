<template>
  <div class="wrapper">
    <div style="border-radius: 10px;background-color: #fff;">
      <el-tabs v-model="activeName" type="card" @tab-click="handleClick">
        <el-tab-pane label="密码登录" name="first">
          <div
              style="margin:0px auto; width: 350px; height: 450px;padding: 0px 20px;">
            <el-avatar :src="avatarUrl" :size="70" style="margin:20px 175px 20px 115px "></el-avatar>
            <el-form :model="user" :rules="rules" ref="userForm">
              <el-form-item prop="username">
                <el-input size="medium" style="margin: 10px 0" prefix-icon="el-icon-user" placeholder="请输入用户名"
                          v-model="user.username" @blur="getAvatarUrl(1)"></el-input>
              </el-form-item>
              <el-form-item prop="password">
                <el-input size="medium" style="margin: 10px 0" prefix-icon="el-icon-lock" show-password
                          v-model="user.password"></el-input>
              </el-form-item>
              <el-form-item prop="captcha">
                <el-input size="medium" style="margin: 10px 0; width: 180px; vertical-align:middle"
                          prefix-icon="el-icon-lock"
                          v-model="user.captchaCode" @keydown.enter.native="login"></el-input>
                <img :src="captchaUrl" style="padding: 0px 10px 0px 10px; vertical-align:middle" @click="updateCaptcha">
              </el-form-item>
              <el-form-item style="margin: 10px 0; text-align: right">
                <el-button type="warning" size="small" autocomplete="off" @click="$router.push('/register')">注册
                </el-button>
                <el-button type="primary" size="small" autocomplete="off" @click="login">登录</el-button>
              </el-form-item>
              <el-form-item style="margin: 10px 0; text-align: right">
                <el-button type="text" size="medium" autocomplete="off" @click="handlePass">找回密码</el-button>
              </el-form-item>
            </el-form>
          </div>
        </el-tab-pane>
        <el-tab-pane label="邮箱登录" name="second">
          <div
              style="margin:0px auto; width: 350px; height: 360px;padding: 0px 20px;">
            <el-avatar :src="avatarUrl" :size="70" style="margin:20px 175px 20px 115px "></el-avatar>
            <el-form :model="user" :rules="rules" ref="userForm">
              <el-form-item prop="email">
                <el-input size="medium" style="margin: 10px 0" prefix-icon="el-icon-message"
                          v-model="user.email" @blur="getAvatarUrl(2)"></el-input>
              </el-form-item>
              <el-form-item prop="captchaCode">
                <el-input size="medium" style="margin: 10px 0; width: 195px" prefix-icon="el-icon-lock"
                          v-model="user.captchaCode" @keydown.enter.native="loginByEmail"></el-input>
                <el-button type="primary" size="small" class="ml-5" @click="sendCode(1)" :disabled="disabled">
                  {{ btnText }}
                </el-button>
              </el-form-item>
              <el-form-item style="margin: 10px 0; text-align: right">
                <el-button type="warning" size="small" autocomplete="off" @click="$router.push('/register')">注册
                </el-button>
                <el-button type="primary" size="small" autocomplete="off" @click="loginByEmail">登录</el-button>
              </el-form-item>

            </el-form>
          </div>
        </el-tab-pane>

      </el-tabs>
    </div>


    <el-dialog title="找回密码" :visible.sync="dialogFormVisible" width="30%">
      <el-form label-width="100px" size="small" :model="pass" :rules="rules2" ref="passFrom">
        <el-form-item label="邮箱">
          <el-input size="medium" v-model="pass.email" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="新密码" prop="newPassword">
          <el-input v-model="pass.newPassword" autocomplete="off" show-password></el-input>
        </el-form-item>
        <el-form-item label="确认新密码" prop="confirmPassword">
          <el-input v-model="pass.confirmPassword" autocomplete="off" show-password></el-input>
        </el-form-item>
        <el-form-item label="验证码">
          <el-input size="medium" style="margin: 10px 0; width: 190px" prefix-icon="el-icon-lock"
                    v-model="pass.captchaCode"></el-input>
          <el-button type="primary" size="small" class="ml-5" @click="sendCode(2)" :disabled="disabled">{{
              btnText
            }}
          </el-button>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="passwordBack">重置密码</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {setRoutes} from "@/router";
import {serverIp} from "../../public/config";

export default {
  name: "Login",
  data() {
    return {
      activeName: 'first',
      disabled: false,
      interval: undefined,
      totalCount: 0,
      dialogFormVisible: false,
      pass: {},
      user: {},//user对象发送到后台校验
      rules: {//表单校验规则
        username: [
          {required: true, message: '请输入用户名', trigger: 'blur'},
          {min: 3, max: 10, message: '长度在 3 到 20 个字符', trigger: 'blur'}
        ],
        password: [
          {required: true, message: '请输入密码', trigger: 'blur'},
          {min: 6, max: 20, message: '长度在 6 到 20 个字符', trigger: 'blur'}
        ],
      },
      rules2: {
        newPassword: [
          {required: true, message: '请输入新密码', trigger: 'blur'},
          {min: 6, message: '长度不少于6位', trigger: 'blur'}
        ],
        confirmPassword: [
          {required: true, message: '请输入密码', trigger: 'blur'},
          {min: 6, message: '长度不少于6位', trigger: 'blur'}
        ],
      },
      captchaUrl: '',
      avatarUrl: '',
    }
  },
  metaInfo() {
    return {
      title: "login",
      meta: [
        {name: "referrer", content: "no-referrer"},
      ],
    };
  },
  computed: {
    btnText() {
      return this.totalCount !== 0 ? `${this.totalCount}秒再次获取` : "获取验证码"
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
          this.user.key = res.data
          this.captchaUrl = `http://${serverIp}:9090/captcha?key=` + res.data
        }
      })
    },
    handleClick(tab) {
      this.activeName = tab.name
      if (this.activeName === 'first') {
        this.updateCaptcha()
      }
    },
    handlePass() {
      this.dialogFormVisible = true
      this.pass = {}
    },
    passwordBack() {
      this.$refs['passFrom'].validate((valid) => {
        console.log(valid)
        if (valid) {
          if (this.pass.newPassword !== this.pass.confirmPassword) {
            this.$message.error("2次输入的新密码不相同")
            return false
          }
          this.pass.key = this.user.key
          console.log(this.pass.key)
          this.request.put("/user/rest", this.pass).then(res => {
            if (res.code === '200') {
              this.$message.success("密码重置成功，")
              this.dialogFormVisible = false
              this.updateCaptcha()//更新验证码
            } else {
              this.$message.error(res.msg)
            }
          })
        }
      })
    },
    login() {
      this.$refs['userForm'].validate((valid) => {//校验不合法时不会发请求
        console.log(valid) //失效了
        if (valid) {  // 表单校验合法
          this.request.post("/user/login", this.user).then(res => {
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
    },
    loginByEmail() {
      if (!this.user.email) {
        this.$message.warning("请输入邮箱")
        return
      }
      if (!this.user.captchaCode) {
        this.$message.warning("请输入验证码")
        return
      }
      this.request.post("/user/loginByEmail", this.user).then(res => {
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
        }
      })
    },
    sendCode(type) {
      // 按钮60秒倒计时
      this.disabled = true
      this.totalCount = 60
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
      let email;
      if (type === 1) {
        email = this.user.email
      } else if (type === 2) {
        email = this.pass.email
      }
      console.log(email)
      if (!email) {
        this.$message.warning("请输入邮箱")
        return;
      }
      if (!/^\w+((.\w+)|(-\w+))@[A-Za-z0-9]+((.|-)[A-Za-z0-9]+).[A-Za-z0-9]+$/.test(email)) {
        this.$message.warning("请输入正确的邮箱账号")
        return
      }
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
    getAvatarUrl(type) {
      let data = '';
      if (type === 1) {
        data = this.user.username
      } else if (type === 2) {
        data = this.user.email
      }
      this.request.get("/user/getavatar", {
        params: {
          type: type,
          temp: data,
        }
      }).then(res => {
        this.avatarUrl = res.data
      })
    },
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
  background-image: url("https://fastly.jsdelivr.net/gh/YuChaoM/images/OS/login.32c09ed0.jpg");
  background-size: cover;
  /*background-image: linear-gradient(-20deg, #d558c8 0%, #24d292 100%);*/
  overflow: hidden;
  /*border-radius: 10px;*/
}

</style>

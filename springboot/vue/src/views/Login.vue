<template>
  <div class="wrapper" >
    <div style="height: 60px; line-height: 60px; font-size: 20px; padding-left: 50px; color: white;
      background-color: rgba(0,0,0,0.2)">会议人脸识别签到管理系统</div>
    <div style="margin-right:120px ; margin-top: 80px ;float: right; background-color: #fff; width: 350px; height: 400px; padding: 20px; border-radius: 10px">
      <div style="margin: 20px 0; text-align: center; font-size: 24px"><b>登 录</b></div>
      <el-form :model="user" :rules="rules" ref="userForm">
        <el-form-item prop="username">
          <el-input size="medium" prefix-icon="el-icon-user" v-model="user.username" placeholder = "账号"></el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input size="medium" prefix-icon="el-icon-lock" show-password v-model="user.password"  placeholder = "密码"></el-input>
        </el-form-item>
        <el-form-item>
          <el-row :span="24">
            <el-col :span="12">
              <el-input  v-model="code" auto-complete="off" placeholder="请输入验证码" size="mid" @keyup.enter.native="login()"></el-input>
            </el-col>
            <el-col :span="12">
              <div class="login-code" @click="refreshCode" style="cursor: pointer;">
                <!--验证码组件-->
                <identify :identifyCode="identifyCode"></identify>
              </div>
            </el-col>
          </el-row>
        </el-form-item>
        <el-form-item>
          <p style="font-size:12px;line-height:30px;color:#eaeaea;">Tips : 请输入账号密码登陆</p>
        </el-form-item>
        <el-form-item style="margin: 20px 0;">
          <el-button type="primary" size="small"  autocomplete="off" @click="login" style="width: 310px;">登录</el-button>
        </el-form-item>


        <el-form-item style="margin: 10px 0;text-align: center">
          <el-button type="text" size="mid"  autocomplete="off" @click="youke" >游客登录</el-button>
          <el-button type="text" size="mid"  autocomplete="off" @click="handlePass">忘记密码？</el-button>
          <el-button type="text" size="mid"   autocomplete="off" @click="$router.push('/register')">前往注册</el-button>
        </el-form-item>
      </el-form>
    </div>


    <el-dialog title="找回密码" :visible.sync="dialogFormVisible" width="30%" >
      <el-form label-width="100px" size="small">
        <el-form-item label="用户名">
          <el-input v-model="pass.username" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="手机号">
          <el-input v-model="pass.phone" autocomplete="off"></el-input>
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
import {resetRouter, setRoutes} from "@/router";
import Identify from "@/components/Identify";
import user from "@/views/User";

export default {
  name: "Login",
  data() {
    return {
      user: {},
      pass: {},

      code: '',
      dialogFormVisible: false,
      // 图片验证码
      identifyCode: '',
      // 验证码规则
      identifyCodes: '3456789ABCDEFGHGKMNPQRSTUVWXY',
      rules: {
        username: [
          { required: true, message: '请输入用户名', trigger: 'blur' },
          { min: 3, max: 10, message: '长度在 3 到 10 个字符', trigger: 'blur' }
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' },
          { min: 1, max: 20, message: '长度在 1 到 20 个字符', trigger: 'blur' }
        ],
      }
    }
  },
  components: {Identify},
  mounted() {
    // 重置路由
    resetRouter()
    this.refreshCode()
  },
  methods: {

    youke(){
      this.$router.push("/front/home")
    },


    login() {
      if (this.code !== this.identifyCode.toLowerCase()&&this.code !== this.identifyCode) {
        console.log("验证码是",this.identifyCode)
        console.log("你输入的是",this.code)
        this.$message({
          type: "error",
          message: "验证码错误"
        })
        this.refreshCode()
        return;
      }
      this.$refs['userForm'].validate((valid) => {
        if (valid) {  // 表单校验合法
          this.request.post("/user/login", this.user).then(res => {
            console.log("登录res=========================",res)
            if(res.code === '200') {

              localStorage.setItem("user", JSON.stringify(res.data))  // 存储用户信息到浏览器
              localStorage.setItem("menus", JSON.stringify(res.data.menus))  // 存储用户信息到浏览器

              if(res.data.role === 'ROLE_ADMIN'){
                // 动态设置当前用户的路由
                console.log(res.data.role)
                setRoutes()
                this.$router.push("/")
                this.$message.success("登录成功")
              }else{
                console.log(res.data.role)
                this.$router.push("/front/home")
              }

            } else {
              this.$message.error(res.msg)
            }
          })
        }
      });
    },

    // 切换验证码
    refreshCode() {
      this.identifyCode = ''
      this.makeCode(this.identifyCodes, 4)
    },


    // 生成随机验证码
    makeCode(o, l) {
      for (let i = 0; i < l; i++) {
        this.identifyCode += this.identifyCodes[Math.floor(Math.random() * (this.identifyCodes.length))]
      }
    },


    handlePass() {
      this.dialogFormVisible = true
      this.pass = {}
      console.log(this.user)
    },

    passwordBack() {
      this.request.put("/user/reset", this.pass).then(res => {
        if (res.code === '200') {
          this.$message.success("重置密码成功，新密码为：123，请尽快修改密码")
          this.dialogFormVisible = false
        } else {
          this.$message.error(res.msg)
        }
      })
    }
  }
}
</script>

<style>
.wrapper {
  height: 100vh;
  /*background-image: linear-gradient(to bottom right, #4169E1 , 	#87CEFA);*/
  background-image:url("~@/assets/img/loginLOGO.jpg");
  background-size: cover;
  overflow: hidden;
}
</style>

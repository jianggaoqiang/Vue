<template>
  <div class ="wrapper">
<!--    头部-->
    <div style="display: flex; height: 60px; line-height: 60px; border-bottom: 1px solid #eee">
      <div style="width: 300px; display: flex; padding-left: 30px">
        <div style="width: 60px">
          <img src="../../assets/logo1.jpg" alt="" style="width: 50px; position: relative; top: 5px; right: 0">
        </div>
        <div style="flex: 1">欢迎来到会议人脸签到</div>
      </div>
      <div style="flex: 1">
        <el-menu :default-active="'1'" class="el-menu-demo" mode="horizontal" router>
          <el-menu-item index="/front/home">首页</el-menu-item>
          <el-menu-item index="/front/AddConference">发起会议</el-menu-item>
          <el-menu-item index="/front/JoinConference">加入会议</el-menu-item>
          <el-menu-item index="/front/person">个人资料</el-menu-item>
          <el-submenu index="">
            <template slot="title">会议列表</template>
            <el-menu-item index="/front/ConferenceList">我发起的会议</el-menu-item>
            <el-menu-item index="/front/JoinConferenceList">我加入的会议</el-menu-item>

          </el-submenu>

<!--          <el-menu-item index="/front/AddFace">人脸录入</el-menu-item>-->
        </el-menu>
      </div>
      <div style="width: 200px">
        <div v-if="!user.username" style="text-align: right; padding-right: 30px">
          <el-button @click="$router.push('/login')">登录</el-button>
          <el-button @click="$router.push('/register')">注册</el-button>
        </div>
        <div v-else>
          <el-dropdown style="width: 150px; cursor: pointer; text-align: right">
            <div style="display: inline-block">
              <img :src="user.avatarUrl" alt=""
                   style="width: 30px; border-radius: 50%; position: relative; top: 10px; right: 5px">
              <span>{{ user.nickname }}</span><i class="el-icon-arrow-down" style="margin-left: 5px"></i>
            </div>
            <el-dropdown-menu slot="dropdown" style="width: 100px; text-align: center">
              <el-dropdown-item style="font-size: 14px; padding: 5px 0">
                <router-link to="/front/password">修改密码</router-link>
              </el-dropdown-item>
              <el-dropdown-item style="font-size: 14px; padding: 5px 0">
                <router-link to="/front/person">个人信息</router-link>
              </el-dropdown-item>
              <el-dropdown-item style="font-size: 14px; padding: 5px 0">
                <span style="text-decoration: none" @click="logout">退出</span>
              </el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </div>
      </div>



    </div>

<!--    主体-->
    <div style="width: 1000px; margin: 0 auto">
      <router-view  @refreshUser="getUser" />
    </div>


  </div>
</template>

<script>

export default {
  name: "Front",
  data() {
    return {
      user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {}
    }
  },
  created() {

  },
  methods: {
    logout() {
      console.log(this.user)
      localStorage.removeItem("user")
      localStorage.removeItem("menus")
      // this.$store.commit("logout")
      // resetRouter()
      this.$message.success("退出成功")
      location.reload()

    },
    getUser(){
      let username = localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")).username : ""
      if (username) {
        // 从后台获取User数据
        this.request.get("/user/username/" + username).then(res => {
          // 重新赋值后台的最新User数据
          this.user = res.data
        })
      }
    }
  },

}
</script>

<style>
.item a {
  color: 	#1E90FF;
}
/*.wrapper{*/
/*  height: 100vh;*/
/*  background-image:url("~@/assets/xue.jpg");*/
/*  background-size: cover;*/
/*  overflow: hidden;*/
/*}*/

</style>

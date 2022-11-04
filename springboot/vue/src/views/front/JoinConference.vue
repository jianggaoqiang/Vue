<template>
  <div>
    <div class="header" >
      <el-card style="height:150px" >
        <h2 style="padding-top: 5vh">加入会议</h2>
      </el-card>
      <el-card>
        <p>请输入会议号</p>
        <el-divider />
        <el-input v-model="form.conferenceId"  prefix-icon="el-icon-search" style="width: 25%" ></el-input>
        <el-divider />
        <el-button type="primary" @click="addConference" >加入会议</el-button>
      </el-card>
    </div>
    <div>
    </div>
  </div>

</template>

<script>
export default {
  name: "JoinConference",
  data(){
    return{
      input:'',
      form:{
        conferenceId: '',
        userId:'',
        sign:0,
        signTime:'',
        createTime:''
      },


    }
  },
  created() {
    let user=  localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {}
    this.form.userId = user.id
  },
  methods:{

    addConference(){
      if(this.form.conferenceId.length === 0){
        console.log(this.form.conferenceId.length)
        this.$message.error("请输入会议号")
      }else{
        this.request.post("/conferenceDetail", this.form).then(res => {
          console.log(res)
          if (res.code === '200') {
            this.$message({
              message:"会议加入成功",
              showClose:true,
              type:"success"
            })
            this.$router.push("/front/JoinConferenceList")
          } else {
            this.$message.error(res.msg)
          }
        })
      }

    },




  }

}
</script>





<style scoped>
*{
  text-align:center;
}


</style>
<template>
  <div style="margin-top: 40px">

    <el-form label-width="120px" size="small" style="width: 80%; margin: 0 auto">
      <el-form-item label="会议名称">
        <el-input v-model="form.conferenceName" autocomplete="off"></el-input>
      </el-form-item>

      <el-form-item label="开始时间" prop="startDateTime">
        <el-date-picker v-model="form.beginTime" type="datetime" value-format="yyyy-MM-dd HH:mm:ss" placeholder="选择日期时间" :picker-options="pickerOptions"></el-date-picker>
      </el-form-item>
      <el-form-item label="结束时间" prop="endDateTime">
        <el-date-picker v-model="form.endTime" type="datetime" value-format="yyyy-MM-dd HH:mm:ss" placeholder="选择日期时间"  :picker-options="pickerOptionsEnd"></el-date-picker>
      </el-form-item>


<!--      <el-form-item label="发起人">-->
<!--        <el-input v-model="form.initiator" autocomplete="off"></el-input>-->
<!--      </el-form-item>-->
      <el-form-item label="场所" >
        <el-select v-model="form.room" placeholder="请选择会议场所">
          <el-option  v-for="item in rooms"  :key = "item.id"  :value="item.roomName" >{{item.roomName}}</el-option>
        </el-select>
      </el-form-item>

      <el-form-item label="会议简介">
        <el-input v-model="form.introduce" type="textarea" :rows="2" placeholder="请输入会议简介"></el-input>
      </el-form-item>


    </el-form>

    <div slot="footer" class="dialog-footer" style="right: 100%;padding-left: 50%">
      <el-button>返回</el-button>
      <el-button type="primary" @click="save">预定会议</el-button>
    </div>

  </div>

</template>

<script>
export default {
  name: "ConferenceList",
  data(){
    return{
      user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {},
      rooms:[],
      form:{},
      pickerOptions: {
        disabledDate(time) {
          return time.getTime() < Date.now() - 8.64e7;
        }
      },
      pickerOptionsEnd: {
        disabledDate: time => {
          let beginTime = this.form.beginTime;
          if (beginTime) {
            return time.getTime() < Date.now() - 8.64e7;
            // return time.getTime()  < new Date(beginTime).getTime() ;
          }
        }
      },
    }

  },
  created() {
    this.getRoom();
  },
  methods: {
    load() {

    },

    getRoom() {
      this.request.get("/room/pagebystate", {
        params: {
          pageNum: 1,
          pageSize: 10,
        }
      }).then(res => {
        // console.log("================room", res)
        this.rooms = res.data.records
        console.log("=====2222", this.rooms)
      })
    },

    save() {
      if(this.form.endTime <= this.form.beginTime){
        this.$message.warning("结束时间应晚于开始时间")
        return
      }
      this.form.initiator = this.user.username
      this.request.post("/conference", this.form).then(res => {
        console.log(res,"===2==")
        if (res.code === '200') {
          this.$message.success("会议添加成功")
          this.$router.push("/front/ConferenceList")
        } else {
          this.$message.error(res.msg)
        }
      })
    },


  }

}
</script>

<style scoped>

</style>
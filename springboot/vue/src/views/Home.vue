<template>
  <div style="color: #666;font-size: 14px;">

    <el-card shadow="hover" class="mgb20" style="height: 252px">
      <div class="user-info">
        <img :src="user.avatarUrl" class="user-avator" alt />
        <div class="user-info-cont">
          <div class="user-info-name">{{ user.username }} 你好，欢迎来到会议签到系统</div>
          <div>{{ user.role }}</div>
        </div>
      </div>
      <div class="user-info-list">
        上次登录时间：
        <span>2022-11-01</span>
      </div>
      <div class="user-info-list">
        上次登录地点：
        <span>常州</span>
      </div>
    </el-card>

    <el-row :gutter="10" style="margin-bottom: 60px">
      <el-col :span="6">
        <el-card style="color: #409EFF">
          <div><i class="el-icon-user-solid" /> 用户总数</div>
          <div style="padding: 10px 0; text-align: center; font-weight: bold">
            {{usertotal}}人
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card style="color: #F56C6C">
          <div><i class="el-icon-user" /> 管理员</div>
          <div style="padding: 10px 0; text-align: center; font-weight: bold">
            {{admin}}人
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card style="color: #67C23A">
          <div><i class="el-icon-s-home" /> 会议场数</div>
          <div style="padding: 10px 0; text-align: center; font-weight: bold">
            {{conferenceList}}场
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card style="color: #E6A23C">
          <div><i class="el-icon-meet" /> 签到记录 </div>
          <div style="padding: 10px 0; text-align: center; font-weight: bold">
            {{ conferenceDetailList }}次
          </div>
        </el-card>
      </el-col>
    </el-row>
    <el-row>
      <el-col :span = "12">
        <div id="pie" style="width: 500px; height: 400px"></div>
      </el-col>
      <el-col :span = "12">

      </el-col>

    </el-row>
  </div>
</template>

<script>
import * as echarts from 'echarts'
export default {
  name: "Home",
  data() {
    return {
      user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {},
      admin:0,
      usertotal:0,
      conferenceDetailList:0,
      conferenceList :0,
    }
  },
  created() {
    this.getdata1();
    console.log(this.user,"============users")
  },
  methods:{
    //获取数据
    getdata1(){
      this.request.get("/sysinfo/findAll").then(res => {
        this.admin = res.data.admin
        this.usertotal = res.data.total
        this.conferenceList = res.data.conferenceList
        this.conferenceDetailList = res.data.conferenceDetailList
      })
    },




  },

  mounted() {
    this.$nextTick(()=>{
      setInterval(this.getdata1,50000)
    })

    const pieOption = {
      title: {
        text: '用户角色占比',
        left: 'center'
      },
      tooltip: {
        trigger: 'item',
        formatter: '{a} <br/>{b} : {c}人 ({d}%)'
      },
      legend: {
        orient: 'vertical',
        left: 'left'
      },
      series: [
        {
          name: "用户角色占比图",
          type: 'pie',
          radius: '55%',
          center: ['25%', '70%'],
          label:{            //饼图图形上的文本标签
            normal:{
              show:true,
              position:'inner', //标签的位置
              textStyle : {
                fontWeight : 300 ,
                fontSize : 14,    //文字的字体大小
                color: "#fff"
              },
              formatter:'{c}({d}%)'
            }
          },
          data: [{value:this.admin,name:'管理员'},
                  {value: 280,name: '普通用户'}],  // 填空
          emphasis: {
            itemStyle: {
              shadowBlur: 10,
              shadowOffsetX: 0,
              shadowColor: 'rgb(0,245,255)'
            }
          }
        },
      ],

    };
    var pieDom = document.getElementById('pie')
    var pieChart = echarts.init(pieDom);
    this.request.get("/sysinfo/pieAll").then(res => {
      console.log(res)
      pieOption.series[0].data = [
        {name: "普通用户", value: res.data.putong},
        {name: "管理员", value: res.data.admin},
      ]
      console.log(res.data.putong)
      pieChart.setOption(pieOption)
    })



  }

}
</script>


<style scoped>



.user-info {
  display: flex;
  align-items: center;
  padding-bottom: 20px;
  border-bottom: 2px solid #ccc;
  margin-bottom: 20px;
}

.user-avator {
  width: 120px;
  height: 120px;
  border-radius: 50%;
}

.user-info-cont {
  padding-left: 50px;
  flex: 1;
  font-size: 14px;
  color: #999;
}

.user-info-cont div:first-child {
  font-size: 30px;
  color: #222;
}

.user-info-list {
  font-size: 14px;
  color: #999;
  line-height: 25px;
}

.user-info-list span {
  margin-left: 70px;
}

.mgb20 {
  margin-bottom: 20px;
}




</style>

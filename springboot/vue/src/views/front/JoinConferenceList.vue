<template>

  <div style="margin-top: 40px">
    <h2>我参加的会议</h2>

    <el-tabs v-model="activeName"   >

      <el-tab-pane label="未签到的会议" name="first"  >
        <br style="padding-top: 10%" >
        <div style="margin-top: auto" class="test" >
          <el-table  :data="tableData" border stripe :header-cell-class-name="'headerBg'"  @selection-change="handleSelectionChange">
            <el-table-column label="序号" width="70" align ="center">
              <template slot-scope="scope">
                {{ (pageNum -1) * pageSize +scope.$index +1}}
              </template>
            </el-table-column>
            <el-table-column prop="conferenceId" label="会议号"></el-table-column>
            <el-table-column prop="conferenceName" label="会议名称"></el-table-column>
            <el-table-column prop="initiator" label="会议发起者"></el-table-column>
            <el-table-column prop="beginTime" label="会议开始时间"></el-table-column>
            <el-table-column prop="endTime" label="会议结束时间"></el-table-column>
            <el-table-column  label="签到">
              <template slot-scope="scope">
                {{scope.row.sign == 0 ? "未签":"已签"}}
              </template>
            </el-table-column>
            <el-table-column label="操作"  width="180" align="center">
              <template slot-scope="scope">
                <el-button type="primary" @click="sign(scope.row.id)">签到</el-button>
                <el-popconfirm
                    class="ml-5"
                    confirm-button-text='确定'
                    cancel-button-text='我再想想'
                    icon="el-icon-info"
                    icon-color="red"
                    title="您确定删除该签到记录吗？"
                    @confirm="del(scope.row.id)"
                >
                  <el-button type="danger" slot="reference">取消预约会议 <i class="el-icon-remove-outline"></i></el-button>
                </el-popconfirm>
              </template>
            </el-table-column>
          </el-table>

          <div style="padding: 10px 0">
            <el-pagination
                @size-change="handleSizeChange"
                @current-change="handleCurrentChange"
                :current-page="pageNum"
                :page-sizes="[2, 5, 10, 20]"
                :page-size="pageSize"
                layout="total, sizes, prev, pager, next, jumper"
                :total="total">
            </el-pagination>

          </div>
        </div>


      </el-tab-pane>


      <el-dialog ref="dialog" title="会议签到" :visible.sync="dialogFormVisible" width="40%" :close-on-click-modal="false">

        <div class="login-div cen">
          <div id="face-box">
            <video id="video"></video>
          </div>
          <div id="overflow">
          </div>
          <div style="display: none;">
            <canvas id="canvas" ></canvas>
          </div>
        </div>

        <div slot="footer" class="dialog-footer">
          <el-button @click="dialogFormVisible = false">取 消</el-button>
          <el-button type="primary" id = "sign" >签到</el-button>
        </div>


      </el-dialog>


      <el-tab-pane label="已签到的会议" name="second"  >
        <br style="padding-top: 10%" >
        <div style="margin-top: auto"  class="test">
          <el-table :data="overtableData" border stripe :header-cell-class-name="'headerBg'"  @selection-change="handleSelectionChange">

            <el-table-column label="序号" width="70" align ="center">
              <template slot-scope="scope">
                {{ (pageNum -1) * pageSize +scope.$index +1}}
              </template>
            </el-table-column>
            <el-table-column prop="conferenceId" label="会议号"></el-table-column>
            <el-table-column prop="conferenceName" label="会议名称"></el-table-column>
            <el-table-column prop="initiator" label="会议发起者"></el-table-column>
            <el-table-column prop="beginTime" label="会议开始时间"></el-table-column>
            <el-table-column prop="endTime" label="会议结束时间"></el-table-column>
            <el-table-column  label="签到">
              <template slot-scope="scope">
                {{scope.row.sign == 0 ? "未签":"已签"}}
              </template>
            </el-table-column>
            <el-table-column prop="signTime" label="签到时间"></el-table-column>
            <el-table-column label="操作"   align="center">
              <template slot-scope="scope">
                <el-popconfirm
                    class="ml-5"
                    confirm-button-text='确定'
                    cancel-button-text='我再想想'
                    icon="el-icon-info"
                    icon-color="red"
                    title="您确定取消该会议吗？"
                    @confirm="del(scope.row.id)">
                  <el-button type="text" slot="reference">删除</el-button>
                </el-popconfirm>
                <el-button type="text" size="mini" @click = "$router.push('/front/conferenceListDetail/'+scope.row.id)">会议详情</el-button>
                <el-dropdown  style="padding-left: 20px;cursor: pointer;">
                  <span class="el-dropdown-link" >
                    更多<i class="el-icon-arrow-down el-icon--right"></i>
                  </span>
                  <el-dropdown-menu>
                    <el-dropdown-item>导出参会人员信息</el-dropdown-item>
                    <el-dropdown-item>导出签到人员信息</el-dropdown-item>
                  </el-dropdown-menu>

                </el-dropdown>
              </template>



            </el-table-column>
          </el-table>
          <div style="padding: 10px 0">
            <el-pagination
                @size-change="handleSizeChange"
                @current-change="handleCurrentChange"
                :current-page="pageNum"
                :page-sizes="[2, 5, 10, 20]"
                :page-size="pageSize"
                layout="total, sizes, prev, pager, next, jumper"
                :total="total">
            </el-pagination>
          </div>

        </div>

      </el-tab-pane>

    </el-tabs>
  </div>
</template>

<style lang="scss" scoped>

.test /deep/  .el-table, .el-table__expanded-cell {
  background-color: transparent;
}
.test /deep/ .el-table tr {
  background-color: transparent!important;
}
.test /deep/ .el-table th{
  background-color: transparent!important;
}
.test /deep/  .el-table--enable-row-transition .el-table__body td, .el-table .cell{
  background-color: transparent;
}
.test /deep/  .el-table__row>td{
  border: none;
  //border-color: red;
}


.login-div {
  margin: auto auto;
  /*border: 1px solid black;*/
  height: 300px;
  width: 300px;
}

#face-box {
  /*border: 1px solid deepskyblue;*/
  background: royalblue;
  height: 300px;
  width: 300px;
  padding-top: 10px;
  border-radius: 300px;
  overflow: hidden;
}

#video {
  margin-left: -100px;
  margin-top: -10px;
  transform: rotateY(180deg);

}


</style>

<script>

import $ from "jquery";
import {set} from "vue";
export default {
  name: "ConferenceList",

  data(){
    return{
      activeName:"first",
      total: 0,
      tableData: [],
      overtableData:[],
      pageNum: 1,
      pageSize: 10,
      userId:'',
      user:{},
      rooms:[],
      form:{
        beginTime:'',
        room:'',
        endTime:''

      },
      dialogFormVisible: false,
      userName:'',
      Cancas: null,
      Context: null,
      Video: null,
      img:'',

    }

  },

  created() {
    //让弹窗内容组件提前渲染
    this.load();
  },
  mounted() {
  },

  methods:{

    init(id) {

      //成功回调函数
      var video = document.getElementById("video");
      var canvas = document.getElementById("canvas");
      var context = canvas.getContext("2d");
      var mediaStreamTrack = null;

      function success(stream) {
        //兼容webkit核心浏览器
        // var CompatibleURL = window.URL || window.webkitURL;
        //将视频流转化为video的源
        mediaStreamTrack = stream;
        try {
          // video.src = CompatibleURL.createObjectURL(stream);
          video.srcObject = stream;
        } catch (e) {
          console.log("访问用户媒体设备失败：", error.name, error.message);
        }

        video.play();//播放视频

        //将视频绘制到canvas上
      }

      //错误回调函数
      function error(error) {
        console.log('访问用户媒体失败：', error.name, error.message);
      }

      function getUserMediaToPhoto(constraints, success, error) {
        if (navigator.mediaDevices.getUserMedia) {
          //最新标准API
          navigator.mediaDevices.getUserMedia(constraints).then(success).catch(error);
        } else if (navigator.webkitGetUserMedia) {
          //webkit核心浏览器
          navigator.webkitGetUserMedia(constraints, success, error);
        } else if (navigator.mozGetUserMedia) {
          //firefox浏览器
          navigator.mozGetUserMedia(constraints, success, error);
        } else if (navigator.getUserMedia) {
          //旧版API
          navigator.getUserMedia(constraints, success, error);
        }
      }

      function getFace() {
        context.drawImage(video, 0, 0, 180, 150);
        var img = canvas.toDataURL('image/jpg')
        //获取完整的base64编码
        img = img.split(',')[1];
        console.log('照片收集完成')
        // console.log(img)
        return img;
      }

      function openUserMedia() {
        if (navigator.mediaDevices.getUserMedia || navigator.webkitGetUserMedia || navigator.mozGetUserMedia || navigator.getUserMedia) {
          getUserMediaToPhoto({video: {width: 480, height: 320, facingMode: "user"}}, success, error);
        } else {
          alert('你的浏览器不支持访问用户媒体设备');
        }
      }

      function offUserMedia() {
        if (mediaStreamTrack != null)
          mediaStreamTrack.getTracks()[0].stop();
      }

      $(function () {
        $("#face-box").show();
        openUserMedia();
        var sign = $("#sign").click(function () {
          // alert("开始签到")
          FaceSign();

        })

      })

      function FaceSign() {
        setTimeout(function () {
          let user = localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {}
          this.userName = user.username
          console.log("faceSign============",this.userName)
          var img = getFace();
          $.ajax({
            type: "post",
            url: "http://localhost:9090/face/sign",//后台接口
            data: {
              "imgId": this.userName,
              "imgStr": img,
              "imgType": "BASE64",
              "conferenceDetailId":id
            },
            dataType: "json",
            success: function (data) {
              console.log(data)
              console.log("签到数据===========================",data)
              alert("签到成功")
            },
            error: function () {
              alert("签到失败")
            },
            async: true
          })
        }, 500);
      }




    },
    

    sign(id){
      console.log(this.$refs.dialog.rendered,"===================dialog")
      this.$refs.dialog.rendered = true
      console.log(this.$refs.dialog.rendered,"===================dialog2")
      this.dialogFormVisible = true
      setTimeout(()=>{
        this.init(id);
      },1000)


    },


    load() {
      this.user=  localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {}
      this.userId = this.user.id
      this.request.get("/conferenceDetail/page", {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          userId: this.userId,
          sign:0,
        }
      }).then(res => {
        console.log(res,"未签到================");
        this.tableData = res.data
        this.total = res.data.length
      })

      this.request.get("/conferenceDetail/page", {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          userId: this.userId,
          sign:1,
        }
      }).then(res => {
        console.log(res,"已签到================");
        this.overtableData = res.data
        this.total = res.data.length
      })


    },

    del(id) {
      console.log(id,"id============")
      this.request.delete("/conferenceDetail/" + id).then(res => {
        if (res.code === '200') {
          this.$message.success("取消成功")
          this.load()
        } else {
          this.$message.error("取消失败")
        }
      })
    },



    handleSelectionChange(val) {
      console.log(val)
      this.multipleSelection = val
    },
    handleSizeChange(pageSize) {
      console.log(pageSize)
      this.pageSize = pageSize
      this.load()
    },
    handleCurrentChange(pageNum) {
      console.log(pageNum)
      this.pageNum = pageNum
      this.load()
    },
    handleFileUploadSuccess(res) {
      this.form.file = res
    },
    handleImgUploadSuccess(res) {
      this.form.img = res
    },

  },

}
</script>


<template>
  <div>
    <div v-if="!user.username">
      <el-empty :image-size="200" description="暂无数据"></el-empty>
    </div>
    <div v-else>
      <el-row>
        <el-col :span="12">
          <el-card  style="width: 500px;">
            <el-form label-width="80px" size="small">
              <el-upload
                  class="avatar-uploader"
                  :action="'http://localhost:9090/file/upload'"
                  :show-file-list="false"
                  :on-success="handleAvatarSuccess"
              >
                <img v-if="form.avatarUrl" :src="form.avatarUrl" class="avatar">
                <i v-else class="el-icon-plus avatar-uploader-icon"></i>
              </el-upload>

              <el-form-item label="用户名">
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
              <el-form-item label="人脸上传" >

                <el-button type="warning" v-if="form.face == 0? true:false"  @click="register">注册人脸</el-button>
                <el-button type="success" v-else  @click="register" disabled>已注册人脸</el-button>
              </el-form-item>
              <el-form-item style="text-align: right">
                <el-button type="success" @click="save">保存</el-button>
              </el-form-item>
            </el-form>
          </el-card>
        </el-col>
        <el-col :span="12" >
          <el-card >
            <img :src = "bing" class="image" width="400px" height="520px">
          </el-card>
        </el-col>
      </el-row>




      <el-dialog ref="dialog" title="上传人脸" :visible.sync="dialogFormVisible" width="40%" :close-on-click-modal="false">

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
          <el-button type="primary" id = "register" >注册</el-button>
        </div>


      </el-dialog>

    </div>


  </div>



</template>

<script>

import $ from "jquery";
import bing from "@/assets/bing.jpg";
export default {
  name: "Person",
  data() {
    return {
      bing:bing,
      dialogFormVisible:false,
      form: {},
      user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {},
      userName:'',
      userId:'',
    }
  },
  created() {
    this.getUser().then(res => {
      console.log(res)
      this.form = res
    })
  },
  methods: {
    init() {

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
        var register = $("#register").click(function () {
          alert("开始注册")
          FaceRegister();
        })

      })
      function FaceRegister() {
        setTimeout(function () {
          let user = localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {}
          this.userName = user.username
          this.userId= user.id;
          var img = getFace();
          $.ajax({
            type:"post",
            url:"http://localhost:9090/face/register",//后台接口
            data:{
              "imgId":this.userName,
              "imgStr":img,
              "imgType":"BASE64",
              "userId":this.userId,
            },
            dataType:"json",
            success:function (data) {
              console.log(data,"注册成功的data");
              alert(data.msg)

            },
            error:function () {
              alert("连接服务器失败")
            },
            async:true
          })
        },500);
      }





    },

    register(){
      this.dialogFormVisible = true
      setTimeout(()=>{
        this.init()
      },1000)

    },
    async getUser() {
      return (await this.request.get("/user/username/" + this.user.username)).data
    },
    save() {
      this.request.post("/user", this.form).then(res => {
        if (res.code === '200') {
          this.$message.success("保存成功")
          // 触发父级更新User的方法
          this.$emit("refreshUser")

          // 更新浏览器存储的用户信息
          this.getUser().then(res => {
            res.token = JSON.parse(localStorage.getItem("user")).token
            localStorage.setItem("user", JSON.stringify(res))
          })

        } else {
          this.$message.error("保存失败")
        }
      })
    },
    handleAvatarSuccess(res) {
      this.form.avatarUrl = res
    }
  }
}
</script>

<style>
.avatar-uploader {
  text-align: center;
  padding-bottom: 10px;
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
  width: 138px;
  height: 138px;
  line-height: 138px;
  text-align: center;
}
.avatar {
  width: 138px;
  height: 138px;
  display: block;
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

<template>
  <div className="container">

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


    <button id="register" >--注册--</button>
    <el-button id = "sign"> 签到</el-button>
  </div>
</template>

<script>
import $ from 'jquery';

export default {
  name: "AddFace",
  data() {
    return {
      msg: '获取您的身份信息是',
      userName:'',
      user: {}
    }
  },
  created() {
    let user = localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {}
    this.userName = user.username
    console.log("created========================",this.userName)
  },
  mounted() {
    console.log("mounted========================",this.userName)
    this.init();
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
        var sign = $("#sign").click(function () {
          alert("开始签到")
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
              "imgType": "BASE64"
            },
            dataType: "json",
            success: function (data) {
              console.log("签到数据===========================",data)
              // if(data == true){
              //   alert('签到成功')
              // }else{
              //   alert('签到失败')
              // }
            },
            error: function () {
              alert("签到失败")
            },
            async: true
          })
        }, 500);
      }


    }

  },
}
</script>

<style scoped>
html {
  height: 100%;
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


#register {
  display: block;
  margin: 0 auto
}
</style>

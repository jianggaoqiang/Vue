<template>
  <div>
    <h2>会议详情</h2>
    <el-tabs v-model="activeName"  type="card" :stretch="tabsStretch" >
      <el-tab-pane label="会议概览" name="first"  style="display: flex">

          <el-card class="box-card" style="width: 500px;height: 300px; flex: 1" >
            <h1>参加人数</h1>
            <p>{{total}}</p>

          </el-card>
          <el-card class="box-card" style="width: 500px;height: 300px;float: right; flex: 1" >
            <h1 style="float: right">签到人数</h1><br><br>

            <p >{{signNum}}</p>

          </el-card>

      </el-tab-pane>

      <el-tab-pane label="用户数据" name="second">
        <el-table :data="tableData" border stripe :header-cell-class-name="'headerBg'"  @selection-change="handleSelectionChange">
          <el-table-column label="序号" width="70" align ="center">
            <template slot-scope="scope">
              {{ (pageNum -1) * pageSize +scope.$index +1}}
            </template>
          </el-table-column>
          <el-table-column prop="username" label="用户名"></el-table-column>
          <el-table-column label="用户头像">
            <template slot-scope="scope">
              <el-image style="width: 70px;height: 70px" :src = "scope.row.avatarUrl" :fit = "fit"></el-image>
            </template>
          </el-table-column>
          <el-table-column prop="joinTime" label="预定会议时间"></el-table-column>
          <el-table-column prop="sign" label="签到">
            <template slot-scope="scope">
              {{scope.row.sign === 1 ?"已签":"未签"}}
            </template>
          </el-table-column>
          <el-table-column prop="signTime" label="签到时间"></el-table-column>
          <el-table-column prop="email" label="邮箱"></el-table-column>

          <el-table-column label="操作"  width="180" align="center">
            <template slot-scope="scope">
              <el-button type="success" @click="handleEdit(scope.row)">代签<i class="el-icon-edit"></i></el-button>

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

        <el-dialog title="信息" :visible.sync="dialogFormVisible" width="40%" :close-on-click-modal="false">
          <el-form label-width="120px" size="small" style="width: 80%; margin: 0 auto">
            <el-form-item label="会议ID">
              <el-input v-model="form.conferenceId" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="用户ID">
              <el-input v-model="form.userId" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="签到">
              <el-input v-model="form.sign" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="签到时间">
              <el-date-picker v-model="form.signTime" type="datetime" value-format="yyyy-MM-dd HH:mm:ss" placeholder="选择日期时间"></el-date-picker>
            </el-form-item>
            <el-form-item label="加入会议时间">
              <el-date-picker v-model="form.joinTime" type="datetime" value-format="yyyy-MM-dd HH:mm:ss" placeholder="选择日期时间"></el-date-picker>
            </el-form-item>

          </el-form>
          <div slot="footer" class="dialog-footer">
            <el-button @click="dialogFormVisible = false">取 消</el-button>
            <el-button type="primary" @click="save">确 定</el-button>
          </div>
        </el-dialog>

      </el-tab-pane>
    </el-tabs>

  </div>
</template>

<script>

import ConferenceDetailAPI from '@/api/conferenceDetail'
export default {
  name: "ConferenceDetail",
  data() {
    return {
      tabsStretch:true,
      tableData: [],
      // joinNum:0,
      signNum:0,
      total: 0,
      pageNum: 1,
      pageSize: 10,
      name:'',
      fit:"fill",
      conferenceId: "",
      conferenceDetailId:"",
      form: {},
      dialogFormVisible: false,
      multipleSelection: [],
      activeName:"first"
      // user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {}
    }
  },
  created() {
    this.load()
  },
  methods: {
    load() {
      if (this.$route.params && this.$route.params.id){
        this.request.get(`/conferenceDetail/conferenceuserdetail/${this.$route.params.id}`,{
          params:{
            conferenceId:this.$route.params.id
          }
        }).then(res => {
          console.log(res,"============res")
          this.tableData = res.data.items
          this.total = res.data.items.length
          this.signNum = res.data.total
          console.log(this.tableData)


        })
      }
    },

    save() {
      this.request.post("/conferenceDetail", this.form).then(res => {
        if (res.code === '200') {
          this.$message.success("保存成功")
          this.dialogFormVisible = false
          this.load()
        } else {
          this.$message.error("保存失败")
        }
      })
    },

    handleEdit(row) {
      console.log(row,'row=========================')
      this.conferenceDetailId = row.conferenceDetailId
      ConferenceDetailAPI.sign(this.conferenceDetailId).then(res=>{
        console.log(res,"qiandaoshuju ")
        if(res.data == "该用户已签到"){
          // this.$message.success(res.data)
          this.$message({
            showClose: true,
            message:res.data,
            type:"warning"
          })
        }

      })
      this.load();
    },

    del(id) {
      this.request.delete("/conferenceDetail/" + id).then(res => {
        if (res.code === '200') {
          this.$message.success("删除成功")
          this.load()
        } else {
          this.$message.error("删除失败")
        }
      })
    },
    handleSelectionChange(val) {
      console.log(val)
      this.multipleSelection = val
    },
    delBatch() {
      if (!this.multipleSelection.length) {
        this.$message.error("请选择需要删除的数据")
        return
      }
      let ids = this.multipleSelection.map(v => v.id)  // [{}, {}, {}] => [1,2,3]
      this.request.post("/conferenceDetail/del/batch", ids).then(res => {
        if (res.code === '200') {
          this.$message.success("批量删除成功")
          this.load()
        } else {
          this.$message.error("批量删除失败")
        }
      })
    },
    reset() {
      this.name = ""
      this.load()
    },

    handleSizeChange(pageSize) {

      this.pageSize = pageSize
      this.load()
      console.log(pageSize)
    },
    handleCurrentChange(pageNum) {
      // console.log(pageNum)
      this.pageNum = pageNum
      this.load()
    },
    handleFileUploadSuccess(res) {
      this.form.file = res
    },
    handleImgUploadSuccess(res) {
      this.form.img = res
    },

    download(url) {
      window.open(url)
    },

    exp() {
      window.open("http://localhost:9090/conferenceDetail/export")
    },

    handleExcelImportSuccess() {
      this.$message.success("导入成功")
      this.load()
    },

  }
}
</script>


<style>

</style>

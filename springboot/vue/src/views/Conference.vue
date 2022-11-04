<template>
  <div>
    <div style="margin: 10px 0">

      <!--      搜索表单-->
      <el-form :inline="true" class="demo-form-inline">
        <el-form-item>
          <el-input v-model="conferenceQuery.id" placeholder="会议号"></el-input>
        </el-form-item>
        <el-form-item>
          <el-input v-model="conferenceQuery.conferenceName" placeholder="会议名称"></el-input>
        </el-form-item>
        <el-form-item>
          <el-input v-model="conferenceQuery.initiator" placeholder="发起人"></el-input>
        </el-form-item>
        <el-form-item>
          <!--        //下拉选择器-->
          <el-select v-model="conferenceQuery.room" clearable placeholder="会议会场">
            <el-option v-for="i in AllroomList" :label = "i.roomName" :value = "i.roomName"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="选择时间">
          <el-date-picker
              v-model="conferenceQuery.beginTime"
              type="datetime"
              placeholder="选择开始时间"
              value-format="yyyy-MM-dd HH:mm:ss"
              default-time="00:00:00">
          </el-date-picker>
        </el-form-item>

        <el-button class="ml-5" type="primary" @click="load">搜索</el-button>
        <el-button type="warning" @click="reset">重置</el-button>
        <el-button type="primary" @click="handleAdd">添加会议<i class="el-icon-circle-plus-outline"></i></el-button>
        <el-popconfirm
            class="ml-5"
            confirm-button-text='确定'
            cancel-button-text='我再想想'
            icon="el-icon-info"
            icon-color="red"
            title="您确定批量删除这些数据吗？"
            @confirm="delBatch"
        >
          <el-button type="danger" slot="reference">批量删除 <i class="el-icon-remove-outline"></i></el-button>
        </el-popconfirm>
        <el-upload action="http://localhost:9090/conference/import" :show-file-list="false" accept="xlsx"
                   :on-success="handleExcelImportSuccess" style="display: inline-block">
          <el-button type="primary" class="ml-5">导入 <i class="el-icon-bottom"></i></el-button>
        </el-upload>
        <el-button type="primary" @click="exp" class="ml-5">导出 <i class="el-icon-top"></i></el-button>
      </el-form>





    </div>

    <el-table :data="tableData" border stripe :header-cell-class-name="'headerBg'"
              @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55"></el-table-column>

      <el-table-column prop="id" label="ID" width="100" sortable></el-table-column>
      <el-table-column prop="conferenceName" label="会议名称"></el-table-column>

      <el-table-column prop="room" label="场所"></el-table-column>
      <el-table-column prop="beginTime" label="开始时间"></el-table-column>
      <el-table-column prop="endTime" label="结束时间"></el-table-column>
      <el-table-column prop="initiator" label="发起人"></el-table-column>
      <el-table-column label="加入人数" width="60">
        <template slot-scope="scope">
          {{ scope.row.joinNumber }}人
        </template>
      </el-table-column>
      <el-table-column label="签到人数" width="60">
        <template slot-scope="scope">
          {{ scope.row.signNumber }}人
        </template>
      </el-table-column>
      <el-table-column prop="introduce" label="简介"></el-table-column>

      <el-table-column label="操作" width="180" align="center">
        <template slot-scope="scope">
          <el-button type="success" @click="handleEdit(scope.row)">编辑 <i class="el-icon-edit"></i></el-button>
          <el-popconfirm
              class="ml-5"
              confirm-button-text='确定'
              cancel-button-text='我再想想'
              icon="el-icon-info"
              icon-color="red"
              title="您确定删除吗？"
              @confirm="del(scope.row.id)"
          >
            <el-button type="danger" slot="reference">删除 <i class="el-icon-remove-outline"></i></el-button>
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

    <el-dialog :title="title" :visible.sync="dialogFormVisible" width="40%" :close-on-click-modal="false">
      <el-form label-width="120px" size="small" style="width: 80%; margin: 0 auto">
        <el-form-item label="会议名称">
          <el-input v-model="form.conferenceName" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="发起人">
          <el-input v-model="form.initiator" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="开始时间">
          <el-date-picker v-model="form.beginTime" type="datetime" value-format="yyyy-MM-dd HH:mm:ss"
                          placeholder="选择日期时间"></el-date-picker>
        </el-form-item>

        <el-form-item label="结束时间">
          <el-date-picker v-model="form.endTime" type="datetime" value-format="yyyy-MM-dd HH:mm:ss"
                          placeholder="选择日期时间"></el-date-picker>
        </el-form-item>

        <el-form-item label="会议场所">
          <el-select v-model="form.room" placeholder="选择会议场所">
            <el-option v-for="teacher in roomList" :key="teacher.id" :label="teacher.roomName"
                       :value="teacher.roomName"/>
          </el-select>
        </el-form-item>

        <el-form-item label="简介">
          <el-input type="textarea" v-model="form.introduce" autocomplete="off"></el-input>
        </el-form-item>


      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import ConferenceAPI from '@/api/conference'
export default {
  name: "Conference",
  data() {
    return {
      title:"发起会议",
      conferenceQuery: {},
      tableData: [],
      total: 0,

      pageNum:1,
      pageSize: 10,
      roomName: '',
      conferenceName: "",
      form: {
        conferenceName: '',
        initiator: '',
        room: '',
        beginTime: '',
        introduce: '',
        endTime: '',
      },
      roomList: [],
      AllroomList:[],
      dialogFormVisible: false,
      multipleSelection: [],
      user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {}
    }
  },
  created() {
    this.load();
    this.getAllRoom();
    this.getRoom();

  },
  methods: {
    load(page) {
      page = 1;
      this.pageNum = page
      ConferenceAPI.getConference(this.pageNum,this.pageSize,this.conferenceQuery)
          .then((response) => {
            this.tableData = response.data.items
            this.total = response.data.total
          }).catch((err) => {
        console.log(err)
      })
    },

    getAllRoom() {
      this.request.get("/room/page", {
        params: {
          name:this.roomName,
          pageNum: this.pageNum,
          pageSize: this.pageSize,
        }
      }).then(res => {
        console.log("allroom====================",res)
        this.AllroomList = res.data.records
      })
    },

    getRoom() {
      this.request.get("/room/pagebystate", {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
        }
      }).then(res => {
        // console.log("================room", res)
        this.roomList = res.data.records
        // console.log("=====2222", this.roomList)
      })
    },
    save() {
      this.request.post("/conference", this.form).then(res => {
        if (res.code === '200') {
          this.$message.success("保存成功")
          this.dialogFormVisible = false
          this.load()
        } else {
          this.$message.error("保存失败")
        }
      })

    },
    handleAdd() {
      this.title="发起会议"
      this.dialogFormVisible = true
      this.form = {}
      this.$nextTick(() => {
        if (this.$refs.img) {
          this.$refs.img.clearFiles();
        }
        if (this.$refs.file) {
          this.$refs.file.clearFiles();
        }
      })
    },
    handleEdit(row) {
      this.title = "编辑会议"
      this.form = JSON.parse(JSON.stringify(row))
      this.dialogFormVisible = true
      this.$nextTick(() => {
        if (this.$refs.img) {
          this.$refs.img.clearFiles();
        }
        if (this.$refs.file) {
          this.$refs.file.clearFiles();
        }
      })
    },
    del(id) {
      this.request.delete("/conference/" + id).then(res => {
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
      this.request.post("/conference/del/batch", ids).then(res => {
        if (res.code === '200') {
          this.$message.success("批量删除成功")
          this.load()
        } else {
          this.$message.error("批量删除失败")
        }
      })
    },
    reset() {
      this.conferenceQuery = {}
      this.load()
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
    download(url) {
      window.open(url)
    },
    exp() {
      window.open("http://localhost:9090/conference/export")
    },
    handleExcelImportSuccess() {
      this.$message.success("导入成功")
      this.load()
    }
  }
}
</script>


<style>
.headerBg {
  background: #eee !important;
}
</style>

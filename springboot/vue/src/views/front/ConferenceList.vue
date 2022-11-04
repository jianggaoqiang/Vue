<template>

  <div style="margin-top: 40px">
    <h2>我发起的会议</h2>
    <el-tabs v-model="activeName"   >

      <el-tab-pane label="即将召开的会议" name="first"  >

        <div style="margin-top: auto">
          <el-card>
            <el-table :data="tableData" border stripe :header-cell-class-name="'headerBg'"  @selection-change="handleSelectionChange">

              <el-table-column prop="id" label="会议号"  ></el-table-column>
              <el-table-column prop="conferenceName" label="会议名" ></el-table-column>
              <el-table-column prop="beginTime" label="开始时间" sortable  ></el-table-column>
              <el-table-column prop="room" label="场所" ></el-table-column>
              <el-table-column label="操作"  align="center">
                <template slot-scope="scope">
                  <el-button style="padding-left: 20px" type="text" size="mini"  @click = "$router.push('/front/conferenceListDetail/'+scope.row.id)">进入</el-button>
                  <el-popconfirm
                      class="ml-5"
                      confirm-button-text='确定'
                      cancel-button-text='我再想想'
                      icon="el-icon-info"
                      icon-color="red"
                      title="您确定取消该会议吗？"
                      @confirm="del(scope.row.id)" >
                    <el-button type="text" slot="reference">取消</el-button>
                  </el-popconfirm>

                  <el-button type="text" @click="handleEdit(scope.row)" > 编辑 </el-button>

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
          </el-card>


        </div>
      </el-tab-pane>

      <el-dialog title="编辑会议" :visible.sync="dialogFormVisible" width="40%" :close-on-click-modal="false">
        <el-form label-width="120px" size="small" style="width: 80%; margin: 0 auto">
          <el-form-item label="会议名称">
            <el-input v-model="form.conferenceName" autocomplete="off"></el-input>
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
              <el-option v-for="teacher in rooms" :key="teacher.id" :label="teacher.roomName"
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



      <el-tab-pane label="已经结束的会议" name="second"  >
        <div style="margin-top: auto">
          <el-table :data="overtableData" border stripe :header-cell-class-name="'headerBg'"  @selection-change="handleSelectionChange">

            <el-table-column prop="id" label="会议号"  ></el-table-column>
            <el-table-column prop="conferenceName" label="会议名"></el-table-column>
            <el-table-column prop="endTime" label="结束时间" sortable  ></el-table-column>
            <el-table-column prop="room" label="场所" >
            </el-table-column>
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

<script>
import ConferenceAPI from "@/api/conference";

export default {
  name: "ConferenceList",
  data(){
    return{
      tabsStretch:true,
      conferenceQuery:{},
      total: 0,
      overtableData:[],
      tableData: [],
      pageNum: 1,
      pageSize: 10,
      initiator:'',
      number:[50,100,200,300],
      user:{},
      rooms:[],
      form:{
        beginTime:'',
        room:'',
        number:'',
        endTime:''
      },
      dialogFormVisible: false,
      activeName:"first"
    }

  },
  created() {
    this.load();
    this.getRoom();
  },
  methods:{
    getRoom() {
      this.request.get("/room/pagebystate", {
        params: {
          pageNum: 1,
          pageSize: 10,
        }
      }).then(res => {
        console.log("================room", res)
        this.rooms = res.data.records
        // console.log("=====2222", this.rooms)
      })
    },

    formateDate(date) {
      let year = date.getFullYear();
      let month = (date.getMonth() + 1).toString().padStart(2, '0');  // 月要+1
      let day = date.getDate().toString().padStart(2, '0');  // 获取天是getDate，而不是 getDay
      let hour = date.getHours().toString().padStart(2, '0');
      let minute = date.getMinutes().toString().padStart(2, '0');
      let second = date.getSeconds().toString().padStart(2, '0');
      let createTime = year + '-' + month + '-' + day + ' ' + hour + ':' + minute + ':' + second;
      return createTime;
    },



    load() {
      this.user=  localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {}
      let date = new Date()
      let afterDate = this.formateDate(date)
      // console.log(afterDate)
      this.conferenceQuery.initiator = this.user.username
      this.conferenceQuery.beginTime= afterDate
      ConferenceAPI.getConference(this.pageNum,this.pageSize,this.conferenceQuery)
          .then((response) => {
            console.log(response)
            this.tableData = response.data.items
            this.total = response.data.total
          }).catch((err) => {
        console.log(err)
      })


      this.request.get("/conference/page",{params:{
          pageNum:this.pageNum,
          pageSize :this.pageSize,
          endTime:this.conferenceQuery.beginTime,
          name:this.conferenceQuery.initiator
        }}).then(res=>{
          this.overtableData = res.data.records
        console.log(res,"over的会议============")
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

    save(){
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

    handleEdit(row){
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
    }



  },

}
</script>

<style scoped>

</style>
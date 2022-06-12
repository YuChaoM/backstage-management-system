<template>
  <div>
    <el-row :gutter="20">
      <el-col :span="16">
        <div style="margin: 10px 0;">
          <el-input class="ml-5" style="width: 200px" placeholder="请输入课程名称" suffix-icon="el-icon-search"
                    v-model="name"></el-input>
          <el-input class="ml-5" style="width: 200px" placeholder="请输入课程学分" suffix-icon="el-icon-message"
                    v-model="credit"></el-input>
          <el-input class="ml-5" style="width: 200px" placeholder="请输入课程属性" suffix-icon="el-icon-position"
                    v-model="attributes"></el-input>
          <el-button class="ml-5" type="primary" @click="load">搜索</el-button>
          <el-button type="warning" @click="reset">重置</el-button>
        </div>
      </el-col>
      <el-col :span="8">
        <div style="margin: 10px 0">
          <el-button class="ml-5" type="primary" @click="handleAdd" v-if="user.role === 'ROLE_ADMIN'">新增<i
              class="el-icon-circle-plus"/></el-button>
          <el-popconfirm
              class="ml-5"
              confirm-button-text='确定'
              cancel-button-text='我再想想'
              icon="el-icon-info"
              icon-color="red"
              title="您确定批量删除吗删除吗？"
              @confirm="delBatch()"
          >
            <el-button class="ml-5" type="danger" slot="reference" v-if="user.role === 'ROLE_ADMIN'">批量删除<i
                class="el-icon-remove"/></el-button>
          </el-popconfirm>
          <el-upload :action="'http://'+ srverIp +':9090/course/import'" :show-file-list="false" accept="xlsx"
                     :on-success="handelExcelImportSuccess" style="display: inline-block">
            <el-button class="ml-5" type="primary" v-if="user.role === 'ROLE_ADMIN'">导入<i class="el-icon-bottom"/>
            </el-button>
          </el-upload>
          <el-button class="ml-5" type="primary" @click="exp">导出<i class="el-icon-top"/></el-button>
        </div>
      </el-col>
    </el-row>

    <el-table :data="tableData" border stripe :header-cell-class-name="'headerBg'"
              @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="50"></el-table-column>
      <el-table-column prop="courseNum" label="课程编号" width="150"></el-table-column>
      <el-table-column prop="name" label="课程名称"></el-table-column>
      <el-table-column prop="credit" label="学分" width="80"></el-table-column>
      <el-table-column prop="times" label="课时"></el-table-column>
      <el-table-column prop="nature" label="课程性质" width="200"></el-table-column>
      <el-table-column prop="attributes" label="课程属性" width="100"></el-table-column>
      <!--      <el-table-column prop="score" label="总成绩"></el-table-column>-->
      <!--      <el-table-column prop="grade" label="成绩等级" v-if="user.role === 'ROLE_STUDENT'"></el-table-column>-->
      <el-table-column prop="teacher" label="授课老师" width="150"></el-table-column>
      <el-table-column label="是否开课" width="100">
        <template slot-scope="scope">
          <el-switch v-model="scope.row.status" active-color="#13ce66" inactive-color="#ccc"
                     :disabled="unavailable"
                     @change="changeEnable(scope.row)"></el-switch>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="220" align="center">
        <template slot-scope="scope">
          <el-button type="primary" @click="selectCourse(scope.row.id)"
                     v-if="user.role === 'ROLE_STUDENT'">选课
          </el-button>
          <el-button type="success" @click="handleEdit(scope.row)" v-if="user.role === 'ROLE_ADMIN'">编辑<i
              class="el-icon-edit"></i></el-button>
          <el-popconfirm
              class="ml-5"
              confirm-button-text='确定'
              cancel-button-text='我再想想'
              icon="el-icon-info"
              icon-color="red"
              title="您确定删除吗？"
              @confirm="del(scope.row.id)"
          >
            <el-button type="danger" slot="reference" v-if="user.role === 'ROLE_ADMIN'">删除<i
                class="el-icon-remove-outline"></i></el-button>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>

    <!-- 底部分页start-->
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
    <!-- 底部分页end-->

    <!-- 弹窗start-->
    <el-dialog title="课程信息" :visible.sync="dialogFormVisible" width="30%">
      <el-form label-width="80px" size="small">
        <el-form-item label="课程编号">
          <el-input v-model="form.courseNum" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="课程名称">
          <el-input v-model="form.name" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="学分">
          <el-input v-model="form.credit" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="学时">
          <el-input v-model="form.times" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="课程性质">
          <el-input v-model="form.nature" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="课程属性">
          <el-input v-model="form.attributes" autocomplete="off"></el-input>
        </el-form-item>
        <!--        <el-form-item label="总成绩">-->
        <!--          <el-input v-model="form.score" autocomplete="off"></el-input>-->
        <!--        </el-form-item>-->

        <!--        <el-form-item label="成绩等级">-->
        <!--          <el-select clearable v-model="form.grade" placeholder="请选择等级" style="width: 100%">-->
        <!--            <el-option v-for="item in grades" :key="item" :label="item" :value="item"></el-option>-->
        <!--          </el-select>-->
        <!--        </el-form-item>-->
        <el-form-item label="上课老师">
          <el-select clearable v-model="form.teacherId" placeholder="请选择" style="width: 100%">
            <el-option v-for="item in teachers" :key="item.id" :label="item.nickname" :value="item.id"></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="save()">确 定</el-button>
      </div>
    </el-dialog>
    <!-- 弹窗end-->

  </div>
</template>

<script>

import {serverIp} from "../../public/config";

export default {
  name: "Course",
  data() {
    return {
      serverIp: serverIp,
      form: {},
      tableData: [],
      name: '',
      credit: '',
      attributes: '',
      multipleSelection: [],
      pageNum: 1,
      pageSize: 10,
      total: 0,
      dialogFormVisible: false,
      teachers: [],
      grades: ['A', 'A-', 'B+', 'B', 'B-', 'C+', 'C', 'D'],
      user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {},
      unavailable: false
    }
  },
  created() {
    this.load()
    if (this.user.role === 'ROLE_STUDENT') {
      this.unavailable = true
    }
  },
  methods: {
    selectCourse(courseId) {
      //应该加个判断,如果已选,就不发请求了
      this.request.post('/course/studentCourse/' + courseId + "/" + this.user.id).then(res => {
        if (res.code === '200') {
          this.$message.success("选课成功")
        } else {
          this.$message.success(res.msg)
        }
      })
    },
    load() {
      this.request.get("/course/page", {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          name: this.name,
          credit: this.credit,
          attributes: this.attributes,
        }
      }).then(res => {
        console.log(res)
        this.tableData = res.data.records
        this.total = res.data.total

      })

      this.request.get("/user/role/ROLE_TEACHER").then(res => {
        this.teachers = res.data
      })
    },
    changeEnable(row) {
      this.request.post("/course/update", row).then(res => {
        if (res.code === '200') {
          this.$message.success("操作成功")
        }
      })
    },
    handleAdd() {
      this.dialogFormVisible = true
      this.form = {}
    },
    handleEdit(row) {
      this.form = JSON.parse(JSON.stringify(row))
      this.dialogFormVisible = true
    },
    del(id) {
      this.request.delete("/course/" + id).then(res => {
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
      let ids = this.multipleSelection.map(v => v.id)  // [{}, {}, {}] => [1,2,3]
      this.request.post("/course/del/batch", ids).then(res => {
        if (res.code === '200') {
          this.$message.success("批量删除成功")
          this.load()
        } else {
          this.$message.error("批量删除失败")
        }
      })
    },
    save() {
      // console.log(this.form)
      this.request.post("/course", this.form).then(res => {
        if (res.code === '200') {
          this.$message.success("保存成功")
          this.dialogFormVisible = false
          this.load()
        } else {
          this.$message.error("保存失败")
        }
      })
    },
    reset() {
      this.name = ""
      this.credit = ""
      this.attributes = ""
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
    exp() {
      window.open("http://" + this.serverIp + ":9090/course/export")
    },
    handelExcelImportSuccess() {
      this.$message.success("导入成功")
      this.load()
    }
  }
}
</script>

<style scoped>

</style>

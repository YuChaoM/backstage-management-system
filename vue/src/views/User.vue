<template>
  <div style="margin: 2px 0">

    <el-row :gutter="20">
      <el-col :span="16">
        <div style="margin: 10px 0;">
          <el-input class="ml-5" style="width: 200px" placeholder="请输入名称" suffix-icon="el-icon-search"
                    v-model="username"></el-input>
          <el-input class="ml-5" style="width: 200px" placeholder="请输入角色" suffix-icon="el-icon-message"
                    v-model="role"></el-input>
          <el-input class="ml-5" style="width: 200px" placeholder="请输入地址" suffix-icon="el-icon-position"
                    v-model="address"></el-input>
          <el-button class="ml-5" type="primary" @click="load">搜索</el-button>
          <el-button type="warning" @click="reset">重置</el-button>
        </div>
      </el-col>
      <el-col :span="8">
        <div style="margin: 10px 0">
          <el-button class="ml-5" type="primary" @click="handleadd">新增<i class="el-icon-circle-plus"/></el-button>
          <el-popconfirm
              class="ml-5"
              confirm-button-text='确定'
              cancel-button-text='我再想想'
              icon="el-icon-info"
              icon-color="red"
              title="您确定批量删除吗删除吗？"
              @confirm="delBatch()"
          >
            <el-button class="ml-5" type="danger" slot="reference">批量删除<i class="el-icon-remove"/></el-button>
          </el-popconfirm>
          <el-upload action="http://localhost:9090/user/import" :show-file-list="false" accept="xlsx"
                     :on-success="handelExcelImportSuccess" style="display: inline-block">
            <el-button class="ml-5" type="primary">导入<i class="el-icon-bottom"/></el-button>
          </el-upload>
          <el-button class="ml-5" type="primary" @click="exp">导出<i class="el-icon-top"/></el-button>
        </div>
      </el-col>
    </el-row>

    <!--表格start-->
    <el-table :data="tableData" border stripe :default-sort="{prop: 'date', order: 'descending'}"
              :header-cell-class-name="headerBg" @selection-change="handleSelectionChange">
      >
      <el-table-column type="selection" width="55"></el-table-column>
      <!--只要 prop值和字段一样，就会自动渲染 -->
      <el-table-column prop="id" label="ID" width="80" sortable>
      </el-table-column>
      <el-table-column prop="username" label="姓名" width="140" sortable></el-table-column>
      <el-table-column prop="nickname" label="昵称" width="100" sortable></el-table-column>
      <el-table-column prop="role" label="角色" width="120" sortable>
        <template slot-scope="scope">
          <el-tag type="success" v-if="scope.row.role === 'ROLE_ADMIN'">管理员</el-tag>
          <el-tag type="warning" v-if="scope.row.role === 'ROLE_TEACHER'">老师</el-tag>
          <el-tag type="primary" v-if="scope.row.role === 'ROLE_STUDENT'">学生</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="email" label="邮箱" sortable></el-table-column>
      <el-table-column prop="phone" label="电话" sortable></el-table-column>
      <el-table-column prop="address" label="地址" sortable></el-table-column>

      <el-table-column label="操作" width="300" align="center">
        <template slot-scope="scope">
          <el-button type="primary" @click="viewCourse(scope.row.courses)"
                     v-if="scope.row.role === 'ROLE_TEACHER'|| scope.row.role === 'ROLE_ADMIN'">查看教授课程<i
              class="el-icon-document"/></el-button>
          <el-button type="primary" @click="viewStuCourse(scope.row.stuCourses)"
                     v-if="scope.row.role === 'ROLE_STUDENT'">查看已选课程<i
              class="el-icon-document"/></el-button>
          <el-button type="success" @click="handleEdit(scope.row)">编辑<i class="el-icon-edit"/></el-button>
          <el-popconfirm
              class="ml-5"
              confirm-button-text='确定'
              cancel-button-text='我再想想'
              icon="el-icon-info"
              icon-color="red"
              title="您确定删除吗？"
              @confirm="del(scope.row.id)"
          >
            <el-button type="danger" slot="reference">删除<i class="el-icon-remove"></i></el-button>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>
    <!-- 表格结尾-->

    <!-- 底部分页start-->
    <div style="padding: 10px 0">
      <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="pageNum"
          :page-sizes="[10, 15, 20,50]"
          :page-size="pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total">
      </el-pagination>
    </div>
    <!--底部分页end-->


    <!--添加功能弹窗start-->
    <el-dialog title="用户信息" :visible.sync="dialogFormVisible" width="30%">
      <el-form label-width="80px" size="small">
        <el-form-item label="用户名">
          <el-input v-model="form.username" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="密码">
          <el-input v-model="form.password" autocomplete="off" show-password></el-input>
        </el-form-item>
        <el-form-item label="角色">
          <el-select clearable v-model="form.role" placeholder="请选择角色" style="width: 100%">
            <el-option v-for="item in roles" :key="item.name" :label="item.name" :value="item.flag"></el-option>
          </el-select>
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
          <el-input v-model="form.address" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="save()">确 定</el-button>
      </div>
    </el-dialog>
    <!--添加功能弹窗start-->

    <el-dialog title="课程信息" :visible.sync="visit" width="30%">
      <el-table :data="courses">
        <el-table-column prop="name" label="课程名称"></el-table-column>
        <el-table-column prop="credit" label="学分"></el-table-column>
      </el-table>
    </el-dialog>

    <el-dialog title="课程信息" :visible.sync="stuVisit" width="30%">
      <el-table :data="stuCourses">
        <el-table-column prop="name" label="课程名称"></el-table-column>
        <el-table-column prop="credit" label="学分"></el-table-column>
      </el-table>
    </el-dialog>

  </div>
</template>

<script>
export default {
  name: "User",
  data() {
    return {
      tableData: [],
      total: 0,//后台传过来的，然后和上面绑定
      pageNum: 1,//前端页面传过来的
      pageSize: 10,//默认每页两条
      username: "",
      email: "",
      address: "",
      form: {},
      isinsert: true,//默认是新增
      multipleSelection: [],
      dialogFormVisible: false,
      headerBg: 'headerBg', //表头背景颜色
      roles: [],
      role: "",
      flag: true,
      courses: [],
      stuCourses:[],
      visit: false,
      stuVisit:false,
    }
  },
  created() {
    //请求分页查询数据，用fetch api
    this.load()
  },
  methods: {
    viewCourse(courses) {
      this.courses = courses
      this.visit = true
    },
    viewStuCourse(stuCourses){
      this.stuCourses = stuCourses
      this.stuVisit = true
    },
    load() {
      // request.get("http://localhost:9090/user/page?pageNum=" + this.pageNum + "&pageSize=" + this.pageSize +
      //     "&username=" + this.username + "&email=" + this.email + "&address=" + this.address).then(res => {
      this.request.get("/user/page", {
            params: {//请求参数
              pageNum: this.pageNum,
              pageSize: this.pageSize,
              username: this.username,
              role: this.role,
              address: this.address
            }
          }
      ).then(res => {
        console.log(res)
        this.tableData = res.data.records
        this.total = res.data.total
      })

      this.request.get("/role").then(res => {
        this.roles = res.data
      })
    },
    save() {
      //.then()就是后台返回的结果 ，url在request.js添加了前缀
      this.request.post("/user", this.form).then(res => {
        console.log(res)

        if (res.code === '200') {
          this.$message.success("保存成功")
          this.dialogFormVisible = false//关闭弹窗
          if (this.isinsert) {
            const totalPage = Math.ceil((this.total + 1) / this.pageSize) // 总页数,向上取整
            //跳到最后一页
            console.log(totalPage)
            // this.pageNum = totalPage
            this.handleCurrentChange(totalPage)
            // this.load()
          } else {
            this.load()
            this.isinsert = true//改回默认值
            console.log(this.pageNum)
          }
        } else {
          this.$message.error(res.msg)
        }
      })
      // }

    },
    handleEdit(row) {
      this.form = row//把表格的数据赋予到弹窗里面
      this.dialogFormVisible = true//打开弹窗
      this.isinsert = false
    },
    del(id) {
      this.request.delete("/user/" + id).then(res => {
        if (res.code === '200') {
          this.$message.success("删除成功")
          // 为了在删除最后一页的最后一条数据时能成功跳转回最后一页的上一页
          const totalPage = Math.ceil((this.total - 1) / this.pageSize) // 总页数
          this.pageNum = this.pageNum > totalPage ? totalPage : this.pageNum
          this.pageNum = this.pageNum < 1 ? 1 : this.pageNum
          this.load()
        } else {
          this.$message.success("删除失败")
        }
      })
    },
    delBatch() {
      let ids = this.multipleSelection.map(v => v.id)//把对象的数组边为纯id的数组[{},{}]=>[1,2]
      this.request.post("/user/del/batch", ids).then(res => {
        // if (res.data) {后台返回的是boolean
        if (res.code === '200') {
          this.$message.success("批量删除成功")
          const totalPage = Math.ceil((this.total - this.multipleSelection.length) / this.pageSize)
          this.pageNum = this.pageNum > totalPage ? totalPage : this.pageNum
          this.pageNum = this.pageNum < 1 ? 1 : this.pageNum
          console.log(this.pageNum)
          this.load()
        } else {
          this.$message.success("删除失败")
        }
      })
    },
    handleSelectionChange(val) {//批量删除
      console.log(val)
      this.multipleSelection = val
    },
    handleSizeChange(pageSize) {
      console.log(`每页 ${pageSize} 条`);
      this.pageSize = pageSize
      this.pageNum = 1
      this.load()//重新请求分页数据
    },
    handleCurrentChange(pageNum) {
      console.log(`当前页: ${pageNum}`);
      this.pageNum = pageNum
      this.load()
    },
    reset() {
      this.username = ""
      this.email = ""
      this.address = ""
      this.load()
    },
    handleadd() {
      this.dialogFormVisible = true//弹窗打开
      this.form = {}
    },
    exp() {
      window.open("http://localhost:9090/user/export")
    },
    handelExcelImportSuccess() {
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

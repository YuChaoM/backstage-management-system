<template>
  <div style="margin: 2px 0">

    <el-row :gutter="20">
      <el-col :span="16">
        <div style="margin: 10px 0;">
          <el-input class="ml-5" style="width: 200px" placeholder="请输入名称" suffix-icon="el-icon-search"
                    v-model="name"></el-input>

          <el-button class="ml-5" type="primary" @click="load">搜索</el-button>
          <el-button type="warning" @click="reset">重置</el-button>
        </div>
      </el-col>
      <el-col :span="8">
        <div style="margin: 10px 0">
          <el-button class="ml-5" type="primary" @click="handleadd(null)">新增<i class="el-icon-circle-plus"/></el-button>
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
          <el-upload action="http://localhost:9090/menu/import" :show-file-list="false" accept="xlsx" :on-success="handelExcelImportSuccess" style="display: inline-block">
            <el-button class="ml-5" type="primary" >导入<i class="el-icon-bottom"/></el-button>
          </el-upload>
          <el-button class="ml-5" type="primary" @click="exp">导出<i class="el-icon-top"/></el-button>
        </div>
      </el-col>
    </el-row>

    <!--表格start-->
    <el-table :data="tableData" border stripe :default-sort="{prop: 'date', order: 'descending'}"
              :header-cell-class-name="headerBg" row-key="id"  default-expand-all
              @selection-change="handleSelectionChange">
      >
      <el-table-column type="selection" width="55"></el-table-column>
      <!--只要 prop值和字段一样，就会自动渲染 -->
      <el-table-column prop="id" label="ID" width="80" sortable></el-table-column>
      <el-table-column prop="name" label="名称" width="150" sortable></el-table-column>
      <el-table-column prop="path" label="路径" width="200" sortable></el-table-column>
      <el-table-column prop="pagePath" label="页面路径" width="200" sortable></el-table-column>
      <el-table-column label="图标" width="150" class-name="fontSize18" align="center" label-class-name="fontSize12">
        <template slot-scope="scope"><i :class="scope.row.icon" /> </template>
      </el-table-column>
      <el-table-column prop="description" label="描述" sortable></el-table-column>
      <el-table-column prop="sortNum" label="顺序"></el-table-column>
      <el-table-column label="操作" width="300" align="center">
        <template slot-scope="scope">
          <el-button type="primary" @click="handleadd(scope.row.id)" v-if="!scope.row.pid && !scope.row.path">新增子菜单<i class="el-icon-plus"/></el-button>
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



    <!--添加功能弹窗start-->
    <el-dialog title="菜单信息" :visible.sync="dialogFormVisible" width="30%">
      <el-form label-width="80px" size="small">
        <el-form-item label="名称">
          <el-input v-model="form.name" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="路径">
          <el-input v-model="form.path" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="页面路径">
          <el-input v-model="form.pagePath" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="图标">
<!--          <el-input v-model="form.icon" autocomplete="off"></el-input>-->
          <el-select clearable v-model="form.icon" placeholder="请选择" style="width: 100%">
            <el-option  v-for="item in options"
                        :key="item.name"
                        :label="item.name"
                        :value="item.value">
            <i :class="item.value"/>{{item.name}}
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="顺序">
          <el-input v-model="form.sortNum" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="form.description" autocomplete="off"></el-input>
        </el-form-item>

      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="save()">确 定</el-button>
      </div>
    </el-dialog>
    <!--添加功能弹窗start-->
  </div>
</template>

<script>
export default {
  name: "User",
  data() {
    return {
      tableData: [],
      name: "",
      form: {},
      multipleSelection: [],
      dialogFormVisible: false,
      headerBg: 'headerBg', //表头背景颜色
      options: [],
    }
  },
  created() {
    //请求数据，用fetch api
    this.load()
  },
  methods: {
    load() {
      // request.get("http://localhost:9090/user/page?pageNum=" + this.pageNum + "&pageSize=" + this.pageSize +
      //     "&username=" + this.username + "&email=" + this.email + "&address=" + this.address).then(res => {
      this.request.get("/menu", {
            params: {//请求参数
              name: this.name,
            }
          }
      ).then(res => {
        console.log(res)
        this.tableData = res.data
      })
      //请求图标数据
      this.request.get("/menu/icons").then(res => {
        this.options = res.data
      })

    },
    save() {
      //.then()就是后台返回的结果 ，url在request.js添加了前缀
      this.request.post("/menu", this.form).then(res => {
        console.log(res)
        if (res.code === '200') {
          this.$message.success("保存成功")
          this.dialogFormVisible = false//关闭弹窗
          this.load()
        } else {
          this.$message.error("失败")
        }
      })
    },
    handleEdit(row) {
      this.form = row//把表格的数据赋予到弹窗里面
      this.dialogFormVisible = true//打开弹窗

    },

    del(id) {
      this.request.delete("/menu/" + id).then(res => {
        if (res.code === '200') {
          this.$message.success("删除成功")
          this.load()
        } else {
          this.$message.success("删除失败")
        }
      })
    },
    delBatch() {
      let ids = this.multipleSelection.map(v => v.id)//把对象的数组边为纯id的数组[{},{}]=>[1,2]
      this.request.post("/menu/del/batch", ids).then(res => {
        // if (res.data) {后台返回的是boolean
        if (res.code === '200') {
          this.$message.success("批量删除成功")
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
    reset() {
      this.name = ""
      this.load()
    },
    handleadd(pid) {
      this.dialogFormVisible = true//弹窗打开
      this.form = {}
      if (pid){
        this.form.pid = pid
      }
    },
    exp() {
      window.open("http://localhost:9090/menu/export")
    },
    handelExcelImportSuccess(){
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
.fontSize18{
  font-size: 18px;
}
.fontSize12{
  font-size: 12px;
}

</style>

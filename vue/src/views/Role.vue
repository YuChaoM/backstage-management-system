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
        </div>
      </el-col>
    </el-row>

    <!--表格start-->
    <el-table :data="tableData" border stripe :default-sort="{prop: 'date', order: 'descending'}"
              :header-cell-class-name="headerBg" @selection-change="handleSelectionChange">
      >
      <el-table-column type="selection" width="55"></el-table-column>
      <!--只要 prop值和字段一样，就会自动渲染 -->
      <el-table-column prop="id" label="ID" width="80" sortable></el-table-column>
      <el-table-column prop="name" label="名称" sortable></el-table-column>
      <el-table-column prop="flag" label="唯一标识" sortable></el-table-column>
      <el-table-column prop="description" label="描述" sortable></el-table-column>

      <el-table-column label="操作" width="300" align="center">
        <template slot-scope="scope">
          <el-button type="info" @click="selectMenu(scope.row)">分配菜单<i class="el-icon-menu"/></el-button>
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
          :page-sizes="[5, 10, 15, 20]"
          :page-size="pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total">
      </el-pagination>
    </div>
    <!--底部分页end-->


    <!--添加功能弹窗start-->
    <el-dialog title="角色信息" :visible.sync="dialogFormVisible" width="30%">
      <el-form label-width="80px" size="small">
        <el-form-item label="名称">
          <el-input v-model="form.name" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="唯一标识">
          <el-input v-model="form.flag" autocomplete="off"></el-input>
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

    <!-- 分配菜单功能弹窗start-->
    <el-dialog title="菜单分配" :visible.sync="menuDialogVis" width="30%">
      <el-tree
          :props="props"
          :data="menuData"
          show-checkbox
          node-key="id"
          ref="tree"
          :default-expanded-keys="expends"
          :default-checked-keys="checks"
          @check-change="handleCheckChange">
          <span class="custom-tree-node" slot-scope="{ node, data }">
            <span><i :class="data.icon"/> {{ data.name }}</span>
          </span>
      </el-tree>
      <div slot="footer" class="dialog-footer">
        <el-button @click="menuDialogVis = false">取 消</el-button>
        <el-button type="primary" @click="saveRoleMenu">确 定</el-button>
      </div>
    </el-dialog>
    <!-- 分配菜单功能弹窗end-->
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
      pageSize: 5,//默认每页两条
      name: "",
      form: {},
      isinsert: true,//默认是新增
      multipleSelection: [],
      dialogFormVisible: false,
      menuDialogVis: false,
      headerBg: 'headerBg', //表头背景颜色
      menuData: [],
      props: {
        label: 'name',//怎么渲染的呢
      },
      expends: [],
      checks: [],
      roleId: 0,//给谁分配
      roleFlag: ''
    }
  },
  created() {
    //请求分页查询数据，用fetch api
    this.load()
  },
  methods: {
    load() {
      // request.get("http://localhost:9090/user/page?pageNum=" + this.pageNum + "&pageSize=" + this.pageSize +
      //     "&username=" + this.username + "&email=" + this.email + "&address=" + this.address).then(res => {
      this.request.get("/role/page", {
            params: {//请求参数
              pageNum: this.pageNum,
              pageSize: this.pageSize,
              name: this.name,
            }
          }
      ).then(res => {
        console.log(res)
        this.tableData = res.data.records
        this.total = res.data.total
      })
    },
    save() {
      //.then()就是后台返回的结果 ，url在request.js添加了前缀
      this.request.post("/role", this.form).then(res => {
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
          this.$message.error("失败")
        }
      })
    },
    saveRoleMenu() {
      console.log(this.$refs.tree.getCheckedKeys())
      this.request.post("/role/roleMenu/" + this.roleId, this.$refs.tree.getCheckedKeys()).then(res => {
        if (res.code === '200') {
          this.$message.success('绑定成功')
          this.menuDialogVis = false
          if (this.roleFlag == 'ROLE_ADMIN') {
            this.$store.commit("logout")//操作管理员角色时需要退出重新登录,调用logout
          }
        } else {
          this.$message.error(res.msg)
        }

      })
    },
    handleEdit(row) {
      this.form = row//把表格的数据赋予到弹窗里面
      this.dialogFormVisible = true//打开弹窗
      this.isinsert = false
    },
    del(id) {
      this.request.delete("/role/" + id).then(res => {
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
      this.request.post("/role/del/batch", ids).then(res => {
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
      this.name = ""
      this.load()
    },
    handleadd() {
      this.dialogFormVisible = true//弹窗打开
      this.form = {}
    },
    selectMenu(role) {
      this.menuDialogVis = true
      this.roleId = role.id
      this.roleFlag = role.flag
      //请求菜单数据，用于渲染选择框
      this.request.get("/menu").then(res => {
        console.log(res)
        this.menuData = res.data
        this.expends = this.menuData.map(v => v.id)//处理成id的数组

      })
      //查询已经分配的菜单
      this.request.get("/role/roleMenu/" + role.id).then(res => {
        console.log(res)
        this.checks = res.data//返回的数list,设置了父级菜单的勾选，所有都选上了，这个不生效，下面解决

        this.request.get("/menu/ids").then(r => {
          const ids = r.data
          ids.forEach(id => {
            if (!this.checks.includes(id)) {
              this.$refs.tree.setChecked(id, false)//checks没有的就不选中
            }
          })
        })
      })
      this.menuDialogVis = true
    },

    handleCheckChange(data, checked, indeterminate) {
      console.log(data, checked, indeterminate);
    },
  }
}
</script>


<style>
.headerBg {
  background: #eee !important;
}
</style>

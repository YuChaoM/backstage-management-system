<template>

  <el-container style="height: 100vh">

    <!--左边导航栏start-->
    <el-aside :width="sideWidth + 'px'"
              style="background-color: rgb(238, 241, 246); box-shadow: 2px 0 6px rgb(0 21 41 / 35%);">
      <el-menu :default-openeds="['1', '3']" style="height: 100%;overflow-x: hidden;"
               background-color="rgb(48,65,86)"
               text-color="#fff"
               active-text-color="#ffd04b"
               :collapse-transition="false"
               :collapse="isCollapse"
      >
        <div style="height: 60px;line-height: 60px;text-align: center">
          <img src="../assets/img.png" alt="" style="width: 25px;position: relative;top: 6px;margin-right: 5px">
          <b style="color: white" v-show="logoTextShow">后台管理系统</b>
        </div>
        <el-submenu index="1">
          <template slot="title"><i class="el-icon-message"></i><span slot="title">导航一</span></template>
          <el-menu-item-group>
            <template slot="title">分组一</template>
            <el-menu-item index="1-1">选项1</el-menu-item>
            <el-menu-item index="1-2">选项2</el-menu-item>
          </el-menu-item-group>
          <el-menu-item-group title="分组2">
            <el-menu-item index="1-3">选项3</el-menu-item>
          </el-menu-item-group>
          <el-submenu index="1-4">
            <template slot="title">选项4</template>
            <el-menu-item index="1-4-1">选项4-1</el-menu-item>
          </el-submenu>
        </el-submenu>
        <el-submenu index="2">
          <template slot="title"><i class="el-icon-menu"></i><span slot="title">导航二</span></template>
          <el-menu-item-group>
            <template slot="title">分组一</template>
            <el-menu-item index="2-1">选项1</el-menu-item>
            <el-menu-item index="2-2">选项2</el-menu-item>
          </el-menu-item-group>
          <el-menu-item-group title="分组2">
            <el-menu-item index="2-3">选项3</el-menu-item>
          </el-menu-item-group>
          <el-submenu index="2-4">
            <template slot="title">选项4</template>
            <el-menu-item index="2-4-1">选项4-1</el-menu-item>
          </el-submenu>
        </el-submenu>
        <el-submenu index="3">
          <template slot="title"><i class="el-icon-setting"></i><span slot="title">导航三</span></template>
          <el-menu-item-group>
            <template slot="title">分组一</template>
            <el-menu-item index="3-1">选项1</el-menu-item>
            <el-menu-item index="3-2">选项2</el-menu-item>
          </el-menu-item-group>
          <el-menu-item-group title="分组2">
            <el-menu-item index="3-3">选项3</el-menu-item>
          </el-menu-item-group>
          <el-submenu index="3-4">
            <template slot="title">选项4</template>
            <el-menu-item index="3-4-1">选项4-1</el-menu-item>
          </el-submenu>
        </el-submenu>
      </el-menu>
    </el-aside>
    <!--左边导航栏end-->

    <el-container>
      <el-header style="font-size: 12px;border-bottom: 1px solid #ccc;line-height: 60px;display: flex">
        <div style="flex: 1; font-size: 20px">
          <span :class="collapseBtnClass" style="cursor: pointer" @click="collapse"></span>
        </div>
        <div>
          <el-dropdown style="width: 70px;cursor: pointer">
            <span>王小虎</span><i class="el-icon-arrow-down" style="margin-left: 5px"></i>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item>个人信息</el-dropdown-item>
              <el-dropdown-item>退出</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </div>


      </el-header>

      <el-main>

        <el-row :gutter="20">
          <el-col :span="16">
            <div>
              <el-breadcrumb separator="/">
                <el-breadcrumb-item :to="{ path: '/' }" class="el-icon-s-home">首页</el-breadcrumb-item>
                <el-breadcrumb-item>用户管理</el-breadcrumb-item>
              </el-breadcrumb>
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
              <el-button class="ml-5" type="primary">导入<i class="el-icon-bottom"/></el-button>
              <el-button class="ml-5" type="primary">导出<i class="el-icon-top"/></el-button>
            </div>
          </el-col>

        </el-row>
        <el-row>
          <el-col :span="20">
            <div style="margin: 10px 0;">
              <el-input class="ml-5" style="width: 200px" placeholder="请输入名称" suffix-icon="el-icon-search"
                        v-model="username"></el-input>
              <el-input class="ml-5" style="width: 200px" placeholder="请输入邮箱" suffix-icon="el-icon-message"
                        v-model="email"></el-input>
              <el-input class="ml-5" style="width: 200px" placeholder="请输入地址" suffix-icon="el-icon-position"
                        v-model="address"></el-input>
              <el-button class="ml-5" type="primary" @click="load">搜索</el-button>
              <el-button type="warning" @click="reset">重置</el-button>
            </div>
          </el-col>
        </el-row>


        <!--表格start-->
        <el-table :data="tableData" border stripe :default-sort="{prop: 'date', order: 'descending'}"
                  :header-cell-class-name="headerBg"  @selection-change="handleSelectionChange">
        >
          <el-table-column type="selection" width="55"></el-table-column>
          <!--只要 prop值和字段一样，就会自动渲染 -->
          <el-table-column prop="id" label="ID" width="80" sortable>
          </el-table-column>
          <el-table-column prop="username" label="姓名" width="140" sortable>
          </el-table-column>
          <el-table-column prop="nickname" label="昵称" width="120" sortable>
          </el-table-column>
          <el-table-column prop="email" label="邮箱" sortable>
          </el-table-column>
          <el-table-column prop="phone" label="电话" sortable>
          </el-table-column>
          <el-table-column prop="address" label="地址" sortable>
          </el-table-column>

          <el-table-column label="操作" width="200" align="center">
            <template slot-scope="scope">
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
              :page-sizes="[2, 10, 15, 20]"
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

      </el-main>
    </el-container>

  </el-container>

</template>

<script>


import request from "@/utils/request";

export default {
  name: 'Home',
  data() {
    return {
      tableData: [],
      total: 0,//后台传过来的，然后和上面绑定
      pageNum: 1,//前端页面传过来的
      pageSize: 2,//默认每页两条
      username: "",
      email: "",
      address: "",
      form: {},
      isinsert: true,//默认是新增
      multipleSelection:[],
      dialogFormVisible: false,
      collapseBtnClass: 'el-icon-s-fold',
      isCollapse: false,
      sideWidth: 200,
      logoTextShow: true,
      headerBg: 'headerBg' //表头背景颜色
    }
  },
  created() {
    //请求分页查询数据，用fetch api
    this.load()
  },
  methods: {
    collapse() {  // 点击收缩按钮触发
      this.isCollapse = !this.isCollapse
      if (this.isCollapse) {  // 收缩
        this.sideWidth = 64
        this.collapseBtnClass = 'el-icon-s-unfold'//换为展开的图标
        this.logoTextShow = false
      } else {   // 展开
        this.sideWidth = 200
        this.collapseBtnClass = 'el-icon-s-fold'
        this.logoTextShow = true
      }
    },
    load() {
      // request.get("http://localhost:9090/user/page?pageNum=" + this.pageNum + "&pageSize=" + this.pageSize +
      //     "&username=" + this.username + "&email=" + this.email + "&address=" + this.address).then(res => {
      request.get("http://localhost:9090/user/page", {
            params: {//请求参数
              pageNum: this.pageNum,
              pageSize: this.pageSize,
              username: this.username,
              email: this.email,
              address: this.address
            }
          }
      ).then(res => {
        console.log(res)
        this.tableData = res.records
        this.total = res.total

      })

    },
    save() {
      //.then()就是后台返回的结果 ，url在request.js添加了前缀
      request.post("/user", this.form).then(res => {
        console.log(res)
        if (res) {
          this.$message.success("保存成功")
          this.dialogFormVisible = false//关闭弹窗
          if (this.isinsert) {
            const totalPage = Math.ceil((this.total + 1) / this.pageSize) // 总页数,向上取整
            //跳到最后一页
            console.log(totalPage)
            // this.handleCurrentChange(totalPage)
            this.pageNum = totalPage
            this.load()
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
    handleEdit(row) {
      this.form = row//把表格的数据赋予到弹窗里面
      this.dialogFormVisible = true//打开弹窗
      this.isinsert = false
    },
    del(id) {
      request.delete("/user/"+ id).then(res => {
        if (res) {
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
    delBatch(){
      let ids = this.multipleSelection.map(v => v.id)//把对象的数组边为纯id的数组[{},{}]=>[1,2]
      request.post("/user/del/batch", ids).then(res => {
        if (res) {
          this.$message.success("批量删除成功")
          const totalPage = Math.ceil((this.total - this.multipleSelection.length)/ this.pageSize)
          this.pageNum = this.pageNum > totalPage ? totalPage : this.pageNum
          this.pageNum = this.pageNum < 1 ? 1 : this.pageNum
          console.log(this.pageNum)
          this.load()
        } else {
          this.$message.success("删除失败")
        }
      })
    },
    handleSelectionChange(val){//批量删除
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
    }
  }

}
</script>
<style>
.headerBg {
  background: #eee !important;
}

</style>
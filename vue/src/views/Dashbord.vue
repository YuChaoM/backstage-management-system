<template>
  <div>
    <el-row :gutter="10" style="margin-bottom: 60px">
      <el-col :span="6">
        <el-card style="color: #409EFF;">
          <div><i class="el-icon-user-solid">用户总数</i></div>
          <div style="padding: 10px 0; text-align: center; font-weight: bold">
            <el-tag type="primary">1000</el-tag>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card style="color: #F56C6C">
          <div><i class="el-icon-money">销售总量</i></div>
          <div style="padding: 10px 0; text-align: center; font-weight: bold"> $11111111</div>

        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card style="color: #67C23A">
          <div><i class="el-icon-coin"> 收益总量</i></div>
          <div style="padding: 10px 0; text-align: center; font-weight: bold"> 6666666</div>

        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card style="color: #909399">
          <div><i class="el-icon-s-shop">用户总数</i></div>
          <div style="padding: 10px 0; text-align: center; font-weight: bold">000000</div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="24" style="margin-bottom: 60px">
      <el-col :span="12">
        <div id="main" style="width: 500px;height: 500px"></div>
      </el-col>
      <el-col :span="12">
        <div id="pie" style="width: 600px;height: 500px"></div>
      </el-col>
    </el-row>


  </div>
</template>

<script>
import * as echarts from "echarts"
import request from "../utils/request";
// import 'echarts-gl';
export default {
  name: "Home",
  data() {
    return {}
  },
  mounted() {//页面元素渲染之后再触发，created就不是
    var chartDom = document.getElementById('main');
    var myChart = echarts.init(chartDom);
    var option;

    option = {
      title: {
        text: '各季度用户数量统计',
        subtext: '趋势图',
        left: 'center'
      },
      tooltip: {
        trigger: 'item'
      },
      legend: {//显示name
        orient: 'vertical',
        left: 'left'
      },
      xAxis: {
        type: 'category',
        data: ["第一季度", "第二季度", "第三季度", "第四季度"]
      },
      yAxis: {
        type: 'value'
      },

      series: [
        {
          name: "星巴克",
          data: [],
          type: 'line'
        },
        {
          name: "星巴克",
          data: [],
          type: 'bar'
        },
        {
          name: "kk",
          data: [],
          type: 'line'
        },
        {
          name: "kk",
          data: [],
          type: 'bar'
        },
      ]
    };


    //饼图
    var pieDom = document.getElementById('pie');
    var pieChart = echarts.init(pieDom);
    var pieoption = {
      title: {
        text: '各季度用户数量统计',
        subtext: '饼图',
        left: 'center'
      },

      tooltip: {
        trigger: 'item'
      },
      legend: {//显示name
        orient: 'vertical',
        left: 'left'
      },

      series: [
        {
          name: '星巴克',
          type: 'pie',
          center:['30%','50%'],//左右和上下
          radius: '50%',//圆的大小
          label: {            //饼图图形上的文本标签
            normal: {
              show: true,
              position: 'inner', //标签的位置
              textStyle: {
                fontWeight: 300,
                fontSize: 14,    //文字的字体大小
                color: "#fff"
              },
              formatter: '{d}%'
            }
          },
          data: [],//补充数据
          emphasis: {
            itemStyle: {
              shadowBlur: 10,
              shadowOffsetX: 0,
              shadowColor: 'rgba(0, 0, 0, 0.5)'
            }
          }
        },
        {
          name: 'kkk',
          type: 'pie',
          center:['75%','50%'],
          radius: '40%',//圆的大小
          label: {            //饼图图形上的文本标签
            normal: {
              show: true,
              position: 'inner', //标签的位置
              textStyle: {
                fontWeight: 300,
                fontSize: 14,    //文字的字体大小
                color: "#fff"
              },
              formatter: '{d}%'
            }
          },
          data: [],//补充数据

        }
      ]
    };
    this.request.get("/echarts/members").then(res => {
      console.log(res)
      // option.xAxis.data = res.data.x
      option.series[0].data = res.data
      option.series[1].data = res.data
      option.series[2].data = [1, 2, 3, 4]
      option.series[3].data = [1, 2, 3, 4]
      //数组准备好之后再set
      myChart.setOption(option);

      pieoption.series[0].data = [
        {name: "第1季度", value: res.data[0]},
        {name: "第2季度", value: res.data[1]},
        {name: "第3季度", value: res.data[2]},
        {name: "第4季度", value: res.data[3]},
      ]
      pieoption.series[1].data = [
        {name: "第1季度", value: 5},
        {name: "第2季度", value: 6},
        {name: "第3季度", value: 7},
        {name: "第4季度", value: res.data[1]},
      ]
      pieChart.setOption(pieoption);
    })
  }
}
</script>

<style scoped>

</style>
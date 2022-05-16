<template>
  <div id="container" style="width: 100%; height:100%;resize:both;">

  </div>
</template>

<script>
export default {
  name: "Map",
  data(){
    return{}
  },
  created() {
  },
  mounted() {
    // 创建地图实例
    var map = new AMap.Map("container", {
      rotateEnable:true,
      pitchEnable:true,
      zoom: 17,
      pitch: 50,// 地图俯仰角度，有效范围 0 度- 83 度
      rotation: -15,
      center: [113.090459,23.410542],
      viewMode:'3D', //开启3D视图,默认为关闭
      zooms:[2,20],
      resizeEnable: true,
      // mapStyle: 'amap://styles/whitesmoke'
    })

    var clickHandler = function(e) {
      console.log('您在[ '+e.lnglat.getLng()+','+e.lnglat.getLat()+' ]的位置点击了地图！');
      // 打开信息窗体
      infoWindow.open(map, [113.090459,23.410542])
    };
    // 绑定地图点击事件
    // map.on('click', clickHandler);

    // 创建一个 Marker 实例：
    var marker = new AMap.Marker({
      position: new AMap.LngLat(113.090459,23.410542),   // 经纬度对象，也可以是经纬度构成的一维数组[116.39, 39.9]
      title: '广东培正学院',
      // icon: '//vdata.amap.com/icons/b18/1/2.png', // 添加 Icon 图标 URL
      // content: '<div style="font-size: 12px">a</div>',//可以代替icon
    });
    // 将创建的点标记添加到已有的地图实例：
    map.add(marker);
    // 绑定marker点击事件
    marker.on('click', clickHandler);

    // 移除已创建的 marker
    // map.remove(marker);

    // 信息窗体的内容
    var content = [
      "<div><img src='https://cdn.jsdelivr.net/gh/YuChaoM/images/Golang/11213917c59e902b48a0825090ac018.jpg' style='width: 80px;height: 80px'> ",
        "<div style='padding:0px 4px;'><b>广东培正学院</b>",
        "地址 : 广东省广州市花都区赤泥镇培正路53号</div></div>"
      // "<div style='font-size: 14px;color: #1E90FF;width: 200px; height: 50px'>信息窗体</div>"
    ];
    // 创建 infoWindow 实例
    var infoWindow = new AMap.InfoWindow({
      anchor: 'top-center',
      content: content.join("<br>")  //传入 dom 对象，或者 html 字符串
    });

  },
}
</script>

<style scoped>

</style>
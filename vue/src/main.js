import Vue from 'vue'
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
import App from './App.vue'
import router from './router'
import './assets/gloable.css'
//引用request对象
import request from "@/utils/request";
import store from './store'

Vue.config.productionTip = false
Vue.use(ElementUI, {size: "mini"});

Vue.prototype.request=request
new Vue({
    router,
    store,
    render: h => h(App)
}).$mount('#app')

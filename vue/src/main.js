import Vue from 'vue'
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
import App from './App.vue'
import router from './router'
import './assets/gloable.css'
//引用request对象
import request from "@/utils/request";
import store from './store'
import axios from "axios";
import mavonEditor from 'mavon-editor'
import 'mavon-editor/dist/css/index.css'
Vue.use(mavonEditor)

import Meta from "vue-meta";
Vue.use(Meta);

Vue.config.productionTip = false
Vue.use(ElementUI, {size: "mini"});

Vue.prototype.request=request
new Vue({
    router,
    store,
    render: h => h(App)
}).$mount('#app')


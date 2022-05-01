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

// router.beforeEach((to, from, next) => {
//     if (to.matched.some(record => record.meta.requiresAuth)) {
//         //这里判断用户是否登录，验证本地存储是否有token
//         if (!localStorage.token) { // 判断当前的token是否存在
//             next({
//                 path: '/login',
//                 query: { redirect: to.fullPath }
//             })
//         } else {
//             next()
//         }
//     } else {
//         next() // 确保一定要调用 next()
//     }
// })

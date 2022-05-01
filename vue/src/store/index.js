import Vue from 'vue'
import Vuex from 'vuex'
import router, {resetRoutes} from "../router";
Vue.use(Vuex)

const store = new Vuex.Store({
    state: {
        currentPathName: ''
    },
    mutations: {
        setPath (state) {
            state.currentPathName = localStorage.getItem("currentPathName")
        },
        logout(){//分配菜单后退出重新登录
            localStorage.removeItem("user")//退出后删除信息
            localStorage.removeItem("menus")//退出后删除信息
            router.push("/login")
            //重置路由,这样重新登录就会使用新的路由
            resetRoutes()
        }
    }
})

export default store
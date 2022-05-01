import Vue from 'vue'
import VueRouter from 'vue-router'
import store from "@/store";

Vue.use(VueRouter)

const routes = [

    {
        path: '/login',
        name: 'Login',
        component: () => import('../views/Login.vue'),

    },
    {
        path: '/register',
        name: 'Register',
        component: () => import('../views/Register.vue')
    },
    {
        path: '/404',
        name: '404',
        component: () => import('../views/404.vue')
    },
]

const router = new VueRouter({
    mode: 'history',
    base: process.env.BASE_URL,
    routes
})

// router.addRoute(    {
//     path: '/',
//     // name: 'Manage',
//     component: () => import( '../views/Manage.vue'),
//     redirect: "/home",//重定向到home
//     // meta: { requiresAuth: true }, // 添加表示需要验证
//
//     children: [//子路由
//         {path: 'home', name: '首页', component: () => import( '../views/Home.vue')},
//         {path: 'user', name: '用户管理', component: () => import( '../views/User.vue')},
//         {path: 'role', name: '角色管理', component: () => import( '../views/Role.vue')},
//         {path: 'menu', name: '菜单管理', component: () => import( '../views/Menu.vue')},
//         {path: 'person', name: '个人信息', component: () => import( '../views/Person.vue')},
//         {path: 'file', name: '文件管理', component: () => import('../views/File.vue')},
//
//     ]
// },)

//重置路由
export const resetRoutes = () => {
    router.matcher = new VueRouter({
        mode: 'history',
        base: process.env.BASE_URL,
        routes
    })
}

// 注意：刷新页面会导致页面路由重置，回不去了
export const setRoutes = () => {
    const storeMenus = localStorage.getItem("menus");
    if (storeMenus) {
        // 获取当前的路由对象名称数组
        const currentRouteNames = router.getRoutes().map(v => v.name)
        //不包含动态路由的时候在往里面添加
        if (!currentRouteNames.includes('Manage')) {

            // 拼装动态路由
            const manageRoute = { path: '/', name: 'Manage', component: () => import('../views/Manage.vue'), redirect: "/home", children: [
                    {path: 'person', name: '个人信息', component: () => import( '../views/Person.vue')},
                    {path: 'password', name: '修改密码', component: () => import( '../views/Password.vue')},
                ] }
            const menus = JSON.parse(storeMenus)
            menus.forEach(item => {
                if (item.path) {  // 当且仅当path不为空的时候才去设置路由
                    let itemMenu = { path: item.path.replace("/", ""), name: item.name, component: () => import('../views/' + item.pagePath + '.vue')}
                    manageRoute.children.push(itemMenu)
                } else if(item.children.length) {
                    //children,二级路由
                    item.children.forEach(item => {
                        if (item.path) {
                            let itemMenu = { path: item.path.replace("/", ""), name: item.name, component: () => import('../views/' + item.pagePath + '.vue')}
                            manageRoute.children.push(itemMenu)
                        }
                    })
                }
            })
            // 动态添加到现在的路由对象中去
            router.addRoute(manageRoute)
        }

    }
}

// 重置我就再set一次路由
setRoutes()

// 路由守卫 ,只要发生路由跳转都会经过这里
router.beforeEach((to, from, next) => {
    localStorage.setItem("currentPathName", to.name)  // 设置当前的路由名称，为了在Header组件中去使用
    store.commit("setPath")  // 触发store的数据更新

    //未找到路由
    if (!to.matched.length){
        const storeMenus = localStorage.getItem('menus')
        if (storeMenus) {//不为空说明登陆过
            next("/404");
        }else {
            //跳回登陆页面
            next("login")
        }
    }
    next();  // 放行路由
})

export default router

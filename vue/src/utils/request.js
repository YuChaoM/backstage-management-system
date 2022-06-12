import axios from 'axios'
import ElementUI from "element-ui";
import {serverIp} from "../../public/config";
const request = axios.create({
    // withCredentials:true,//配置这个请求才会带上cookie，session才能获取到值
    // baseURL: 'http://localhost:9090',
    // baseURL: 'http://120.25.172.243:9090',
    baseURL: `http://${serverIp}:9090`,
    timeout: 10000//请求超时的响应时间
})
// Vue.prototype.$axios=Axios

// request 拦截器
// 可以自请求发送前对请求做一些处理
// 比如统一加token，对请求参数统一加密
request.interceptors.request.use(config => {
    config.headers['Content-Type'] = 'application/json;charset=utf-8';
    let user = localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")):{}
    if (user) {//登录后user不为空，才能进来
        config.headers['token'] = user.token//设置请求头，前端发请求会把token带上
    }

    return config
}, error => {
    return Promise.reject(error)
});

// response 拦截器
// 可以在接口响应后统一处理结果
request.interceptors.response.use(
    response => {
        let res = response.data;
        // 如果是返回的文件
        if (response.config.responseType === 'blob') {
            return res
        }
        // 兼容服务端返回的字符串数据
        if (typeof res === 'string') {
            res = res ? JSON.parse(res) : res
        }
        // 当权限验证不通过的时候给出提示
        if (res.code === '401') {
            ElementUI.Message({
                message: res.msg,
                type: 'error'
            });
            // router.push("/login")
            this.$router.push({
                path: `/user/login`,
            })
            // next({path:'/login'})
        }
        return res;
    },
    error => {
        console.log('err' + error) // for debug
        return Promise.reject(error)
    }
)


export default request

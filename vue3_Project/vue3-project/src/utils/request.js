import axios from "axios";
//创建一个新的axios对象
const instance = axios.create({
    baseURL: import.meta.env.VITE_APP_BASE_URL, //后端的ip地址
    timeout: 30000 //响应时间
})

// request拦截器
// 可以在请求发送前对请求做一些处理
instance.interceptors.request.use(config => {
    if (config.data && typeof config.data === 'object') {
        // 如果请求数据是对象，设置 Content-Type
        config.headers['Content-Type'] = 'application/json;charset=utf-8';
    }
    let Authorization = localStorage.getItem("Authorization")
    if (Authorization) {
        config.headers['Authorization'] = Authorization
    }
    return config
}, error => {
    console.log('request error' + error)
    return Promise.reject(error)
})

//response拦截器
instance.interceptors.response.use(response => {
    return response.data;
}, error => {
    console.log('response error:' + error)
    return Promise.reject(error)
})

export default instance
import axios from "axios";

const myAxios = axios.create({
    baseURL: 'http://localhost:8080/api'
})

myAxios.defaults.withCredentials = true;

// 添加请求拦截器
myAxios.interceptors.request.use(function (config) {
    // 在发送请求之前做些什么
    console.log("我要发请求啦")
    return config;
}, function (error) {
    // 对请求错误做些什么
    return Promise.reject(error);
});

// 添加响应拦截器
myAxios.interceptors.response.use(function (response) {
    console.log("我收到了",response.data)
    // 对响应数据做点什么
    return response.data;
}, function (error) {
    // 对响应错误做点什么
    return Promise.reject(error);
});
export default myAxios;
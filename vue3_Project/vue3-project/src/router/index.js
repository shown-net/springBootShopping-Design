import {createRouter, createWebHistory} from 'vue-router'

import top from "@/views/homePage/navBar.vue";
import mall from "@/views/homePage/mall.vue";
import rl from "@/views/register_login/rl.vue";
import register from "@/views/register_login/register.vue";
import login from "@/views/register_login/login.vue";
import Space from "@/views/userSpace/Space.vue";
import Info from "@/views/userSpace/Info.vue";
import Order from "@/views/userSpace/order.vue";
import cart from "@/views/userSpace/consumer/cart.vue";
import statistics from "@/views/userSpace/salesman/statisticsSalesMan.vue";
import search from "@/views/shopping/search.vue";
import catalogue from "@/views/userSpace/salesman/catalogue.vue";
import statisticsManager from "@/views/userSpace/manager/statisticsManager.vue";
import salesManAccountController from "@/views/userSpace/manager/salesManAccountController.vue";
import showGood from "@/views/shopping/showGood.vue";
import userBrowseLog from "@/views/userSpace/salesman/userBrowseLog.vue";
import operationLog from "@/views/userSpace/operationLog.vue";
import recommendGoods from "@/views/shopping/recommendGoods.vue";


// const originalPush = VueRouter.prototype.push
// VueRouter.prototype.push = function push(location) {
//     return originalPush.call(this, location).catch(err => err)
// }
const routes = [
    {
        path: '/',
        name: 'top',
        component: top,
        redirect: '/mall',
        children: [
            {
                path: 'mall',  //商城主页
                name: 'mall',
                component: mall,
            },
            {
                path: 'rl',
                name: 'rl',
                component: rl,
                redirect: '/rl/login',
                children: [
                    {
                        path: 'register',
                        component: register
                    },
                    {
                        path: 'login',
                        component: login
                    }
                ]
            },
            {
                path: 'userSpace',
                name: 'userSpace',
                component: Space,
                redirect: '/userSpace/info',
                children: [
                    {
                        path: 'info',
                        component: Info
                    },
                    {
                        path: 'userBrowseLog',
                        component: userBrowseLog
                    },
                    {
                        path: 'order',
                        component: Order
                    },
                    // 消费者的购物车界面
                    {
                        path: 'cart',
                        component: cart
                    },
                    // 销售人员|管理者的操作日志界面
                    {
                        path: 'operatorLog',
                        component: operationLog
                    },
                    // 商家商品目录的管理
                    {
                        path: 'catalogue',
                        component: catalogue
                    },
                    // 商家销售数据的统计
                    {
                        path: 'statistics/salesMan',
                        component: statistics
                    },
                    // 管理者对所有销售数据的统计
                    {
                        path: 'statistics/manager',
                        component: statisticsManager
                    },
                    // 管理者对所有销售账号的管理
                    {
                        path: 'salesManAccountController',
                        component: salesManAccountController
                    },
                ]
            },
            {
                path: 'shopping',  //多个商品展示
                name: 'shopping',
                component: search
            },
            {
                path: 'item-show',  //单个商品展示
                name: 'item-show',
                component: showGood
            },
            {
                path: 'item-recommend',  //商品推荐
                name: 'item-recommend',
                component: recommendGoods
            },
        ]
    }
]

const router = createRouter({
    history: createWebHistory(),
    routes: routes
})

export default router

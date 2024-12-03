import Vue from 'vue';
import Router from 'vue-router';
import LoginPage from '../views/LoginPage.vue'; // 修改为 LoginPage
import AdminDashboard from '../views/AdminDashboard.vue';
import CustomerHome from '../views/CustomerHome.vue';
import RegisterPage from '../views/RegisterPage.vue';
import ShoppingCart from '../views/ShoppingCart.vue'; // 导入购物车组件
import OrderHistory from '../views/OrderHistory.vue';
import PaymentSuccess from '../views/PaymentSuccess.vue';
import ProductDetail from '@/components/ProductDetail.vue'; 
import adminProducts from '../views/adminProducts.vue';
import adminOrders from '../views/adminOrders.vue';
import SalesReport from '../views/SalesReport.vue';
import adminUsers from '../views/adminUsers.vue';
import UserLog from '../views/UserLog.vue';
Vue.use(Router);

export default new Router({
  // mode: 'history',
  routes: [{
      path: '/',
      redirect: '/login' // 默认重定向到登录页
    },
    {
      path: '/login',
      name: 'LoginPage', // 修改为 LoginPage
      component: LoginPage
    },
    {
      path: '/register',
      name: 'RegisterPage',
      component: RegisterPage
    },
    {
      path: '/admin/dashboard',
      name: 'AdminDashboard',
      component: AdminDashboard
    },
    {
      path: '/customer/home',
      name: 'CustomerHome',
      component: CustomerHome
    },
    {
      path: '/orders',
      name: 'OrderHistory',
      component: OrderHistory
    },
    {
      path: '/shoppingCart',
      name: 'ShoppingCart',
      component: ShoppingCart
    },
    {
      path: '/payment-success',
      name: 'PaymentSuccess',
      component: PaymentSuccess
    },
    {
      path: '/product/:id' , // 商品详情页面
      name: 'ProductDetail',
      component: ProductDetail,
      props: true // 将路由参数作为 props 传递给组件
    },
    {
      path: '/admin/products',
      name: 'adminProducts',
      component: adminProducts,
    },
    {
      path: '/admin/orders',
      name: 'adminOrders',
      component: adminOrders,
    },
    {
      path: '/admin/users',
      name: 'adminUsers',
      component: adminUsers,
    },
    {
      path: '/sales/report',
      name: 'SalesReport',
      component: SalesReport,
    },
    {
      path: '/user/log',
      name: 'UserLog',
      component: UserLog,
    }
  ]
});
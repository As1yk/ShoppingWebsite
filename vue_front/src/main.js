import Vue from 'vue';
import App from './App.vue';
import router from './router';
import ElementUI from 'element-ui';
import store from './store';
import 'element-ui/lib/theme-chalk/index.css';
import './assets/styles.css'; // 引入全局样式
import 'element-ui/lib/theme-chalk/index.css';  // 引入 Element UI 的样式

Vue.use(ElementUI);
Vue.config.productionTip = false;

new Vue({
  store,
  router,
  render: h => h(App),
}).$mount('#app');

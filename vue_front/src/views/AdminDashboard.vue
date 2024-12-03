<template>
  <el-container>
    <el-header
      style="font-size: 33px; font-weight: 800; display: flex; align-items: center; position: relative; color: black;">
      <span style="position: absolute; left: 50%; transform: translateX(-50%);">电子商务网站</span>
      <el-button type="danger" @click="logout" style="margin-left: auto;">注销</el-button>
    </el-header>

    <el-main>
      <el-row>
        <el-col :span="6">
          <el-menu class="admin-menu" :default-openeds="['1']">
            <el-submenu index="1">
              <template slot="title">
                <span style="font-size: 15px;font-weight: bold;">信息管理</span>
              </template>
              <el-menu-item-group title="商品信息">
              <el-menu-item index="1" @click="$router.push('/admin/products')" class="menu-item">商品管理</el-menu-item>
            </el-menu-item-group>
              <el-menu-item-group title="订单信息">
                <el-menu-item index="2" @click="$router.push('/admin/orders')" class="menu-item">订单管理</el-menu-item>
                <el-menu-item index="3" @click="$router.push('/sales/report')" class="menu-item">销售报表统计</el-menu-item>
              </el-menu-item-group>
              <el-menu-item-group title="客户信息">
              <el-menu-item index="4" @click="$router.push('/admin/users')" class="menu-item">客户管理</el-menu-item>
              <el-menu-item index="5" @click="$router.push('/user/log')" class="menu-item">客户日志记录</el-menu-item>
            </el-menu-item-group>
            </el-submenu>
          </el-menu>
        </el-col>
        <el-col :span="18">
          <h2>欢迎，销售员！</h2>
          <!-- 添加统计信息 -->
          <el-row :gutter="20" v-if="isDataLoaded">
            <el-col :span="8">
              <el-card :body-style="{ padding: '20px' }" class="orders-card"
                @click.native="$router.push('/admin/orders')">
                <h3>订单总数</h3>
                <p class="p">{{ ordersCount }}</p>
              </el-card>
            </el-col>
            <el-col :span="8">
              <el-card :body-style="{ padding: '20px' }" class="customers-card"
                @click.native="$router.push('/admin/users')">
                <h3>客户总数</h3>
                <p class="p">{{ usersCount }}</p>
              </el-card>
            </el-col>
            <el-col :span="8">
              <el-card :body-style="{ padding: '20px' }" class="products-card"
                @click.native="$router.push('/admin/products')">
                <h3>商品总数</h3>
                <p class="p">{{ productsCount }}</p>
              </el-card>
            </el-col>
          </el-row>
          <!-- 加载中提示 -->
          <el-row v-else>
            <el-col :span="24" style="text-align: center; font-size: 18px;">
              数据加载中...
            </el-col>
          </el-row>


        </el-col>
      </el-row>
    </el-main>
  </el-container>
</template>

<script>
import axios from 'axios';
import { Message } from 'element-ui';
export default {
  data() {
    return {
      // 统计信息数据
      ordersCount: 1,
      usersCount: 1,
      productsCount: 1,
      isDataLoaded: false, // 默认数据未加载
    };
  },
  mounted() {
    // 在组件挂载后调用 fetchStats 方法
    console.log('Component mounted.');
    this.fetchStats();
  },
  methods: {
    logout() {
            Message.success('注销成功');
            this.$router.push('/login');
        },
    // 获取统计信息
    fetchStats() {
      // 这里可以调用后端接口获取统计信息
      // 例如，使用 Axios 发送 GET 请求
      console.log('Fetching stats...');
      axios.get('http://106.52.82.181:8081/api/users/init').then(response => {
        // 更新统计信息数据
        this.ordersCount = response.data.data.ordersCount;
        this.usersCount = response.data.data.usersCount;
        this.productsCount = response.data.data.productsCount;
        this.isDataLoaded = true; // 数据加载完成
      }).catch(error => {
        console.error('获取统计信息失败:', error);
        this.isDataLoaded = true; // 即使失败，显示加载完成状态
      });
    }
  }
}
</script>
<style scoped>
.el-header {
  background-color: #35495e;
  padding: 15px;
  color: black;
}

/* 管理员菜单 */
.admin-menu {
  width: 200px;
  background: linear-gradient(135deg, #8ef0e2, #21d6a9);
  font-size: 15px;
  color: black;
  /* 渐变的浅蓝色背景 */
}

/* 顶部头部 */
.el-header {
  background: linear-gradient(135deg, #f8cbb0, #a1ed8a);
  /* 渐变的浅蓝色背景 */
  padding: 15px;
}

/* 给菜单项添加渐变底色 */
.el-menu-item.menu-item {
  transition: all 0.3s ease;
  /* 平滑过渡效果 */
  /* border-radius: 5px; */
  /* 圆角效果 */
  background: linear-gradient(135deg, #e0f7fa, #80deea);
  /* 渐变背景色 */
  color: #333;
  font-weight: bold;
  /* 默认字体颜色 */
}

/* 鼠标悬停时添加渐变背景和动画 */
.el-menu-item.menu-item:hover {
  background: linear-gradient(135deg, #80deea, #4dd0e1);
  /* 悬停时的渐变背景 */
  transform: scale(1.05);
  /* 放大效果 */
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
  /* 添加阴影效果 */
  color: #ffffff;
  /* 悬停时字体颜色为白色 */
}

/* 激活项的样式 */
.el-menu-item.menu-item.is-active {
  background: linear-gradient(135deg, #4dd0e1, #00bcd4);
  /* 选中项的渐变背景 */
  color: #ffffff;
  /* 选中项字体颜色 */
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.2);
  /* 激活时添加阴影效果 */
}

/* 基本卡片样式 */
.stats-card {
  border-radius: 10px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  padding: 20px;
  color: #333;
  transition: all 0.3s ease;
  /* 平滑过渡效果 */
  height: 100%;
}

/* 订单总数卡片的渐变背景 */
.orders-card {
  background: linear-gradient(135deg, #ff9a8b, #ff6a88);
  /* 红色到粉色渐变 */
}

.orders-card:hover {
  background: linear-gradient(135deg, #ff6a88, #ff9a8b);
  /* 悬停时的渐变色调变化 */
  transform: scale(1.05);
  box-shadow: 0 6px 12px rgba(0, 0, 0, 0.2);
  color: #ffffff;
}

/* 客户总数卡片的渐变背景 */
.customers-card {
  background: linear-gradient(135deg, #86fde8, #a0eaff);
  /* 绿色到蓝色渐变 */
}

.customers-card:hover {
  background: linear-gradient(135deg, #a0eaff, #86fde8);
  /* 悬停时渐变色调变化 */
  transform: scale(1.05);
  box-shadow: 0 6px 12px rgba(0, 0, 0, 0.2);
  color: #ffffff;
}

/* 商品总数卡片的渐变背景 */
.products-card {
  background: linear-gradient(135deg, #f6d365, #fda085);
  /* 黄色到橙色渐变 */
}

.products-card:hover {
  background: linear-gradient(135deg, #fda085, #f6d365);
  /* 悬停时渐变色调变化 */
  transform: scale(1.05);
  box-shadow: 0 6px 12px rgba(0, 0, 0, 0.2);
  color: #ffffff;
}

/* 标题的样式 */
.stats-card h3 {
  font-size: 18px;
  margin-bottom: 10px;
  font-weight: bold;
  text-align: center;
}

/* 内容部分的样式 */
.p {
  font-size: 22px;
  font-weight: bold;
  text-align: center;
  color: #e74c3c;
  /* 强调数字 */
}

.el-main {
  margin: 20px;
}

.el-row {
  margin-top: 20px;
}

.el-menu-item-group {
  background-color: #34495e; /* 设置组的背景色 */
  font-size: 15px;
  font-weight: bold;
  text-align: center; /* 使文字居中 */
}

.el-menu-item-group__title {
  font-weight: bold;
  font-size: 16px;
  color: #ecf0f1;
  padding: 10px 15px;
  background-color: #2c3e50; /* 设置标题的背景色 */
  /* border-radius: 6px; */
}
</style>
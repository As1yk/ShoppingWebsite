<template>
  <div class="orders-page">
    <h2>订单管理</h2>
    <!-- 搜索商品 -->

    <el-input v-model="search" placeholder="输入订单号" class="search-bar"
      style="width: 300px; border-radius: 20px; box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);">
      <template #suffix>
        <!-- 图标包裹容器 -->
        <div class="icon-wrapper" @click="fetchOrders">
          <i class="el-icon-search"></i> <!-- 图标 -->
        </div>
      </template>
    </el-input>

    <el-table :data="paginatedOrders" style="width: 90%; margin: 0 auto;">
      <el-table-column prop="orderId" label="订单号" width="200" />
      <el-table-column prop="userId" label="用户 ID" width="200" />
      <el-table-column prop="totalPrice" label="总价" width="200" />
      <el-table-column prop="createdAt" label="创建时间" width="250" />
      <el-table-column prop="status" label="状态" width="200" />

      <el-table-column label="操作" width="200">
        <template #default="scope">
          <el-button v-if="scope.row.status == '已支付，待发货'" type="primary" @click="shipOrder(scope.row.orderId)">
            立即发货
          </el-button>
          <el-button v-if="scope.row.status == '交易完成'" type="danger" @click="deleteOrder(scope.row.orderId)">
            删除订单
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination :current-page="currentPage" :page-size="itemsPerPage" :total="filteredOrders.length"
      @current-change="handlePageChange" layout="total, prev, pager, next"
      style="margin-top: 20px; text-align: center; font-size: 14px; color: #666; border-radius: 5px; ">
    </el-pagination>
  </div>
</template>

<script>
import axios from 'axios';
import { Message } from 'element-ui';
export default {
  data() {
    return {
      search: '',
      orders: [], // 示例数据
      paginatedOrders: [],
      itemsPerPage: 12, // 每页显示的商品数量
      currentPage: 1    // 当前页数
    };
  },
  computed: {
    filteredOrders() {
      // 当搜索字段为空时，返回所有订单；否则返回匹配订单号的订单
      return this.orders.filter(order => {
        // 将 orderId 转换为字符串后进行匹配
        return order.orderId.toString().includes(this.search);
      });
    }
  },
  watch: {
    currentPage() {
      this.updatePaginatedOrders();
    },
    filteredOrders() {
      this.updatePaginatedOrders();
    },
    itemsPerPage() {
      this.updatePaginatedOrders();
    }
  },
  mounted() {
    // 初始加载商品
    this.updatePaginatedOrders();

  },
  created() {
    this.fetchOrders(); // 在组件加载时获取商品信息

  },
  methods: {
    handlePageChange(page) {
      this.currentPage = page;
      this.updatePaginatedOrders();
    },
    updatePaginatedOrders() {
      const start = (this.currentPage - 1) * this.itemsPerPage;
      const end = start + this.itemsPerPage;
      this.paginatedOrders = this.filteredOrders.slice(start, end);
    },
    async fetchOrders() {
      try {
        const response = await axios.get('http://106.52.82.181:8081/api/orders/page');
        this.orders = response.data.data;
        console.log(this.orders);

        // 批量转换每个订单的 createdAt 字段
        this.orders.forEach(order => {
          const date = new Date(order.createdAt);
          order.createdAt = `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')} ${String(date.getHours()).padStart(2, '0')}:${String(date.getMinutes()).padStart(2, '0')}:${String(date.getSeconds()).padStart(2, '0')}`;
        });
        this.updatePaginatedOrders();
        console.log("123")
        console.log(this.paginatedOrders)
      } catch (error) {
        Message.error('Failed to fetch orders');
        console.error(error);
      }
    },

    shipOrder(orderId) {
      axios.post('http://106.52.82.181:8081/api/orders/ship', null, {
        params: {
          orderId: orderId
        }
      }).then(response => {
        if (response.data.code === 1) {
          Message.success('订单发货成功');
          // 更新前端显示的订单状态
          const order = this.orders.find(order => order.orderId === orderId);
          if (order) {
            order.status = '已发货，待确认收货';
          }

        }
        else {
          Message.error('库存不足，发货失败');
        }
      }).catch(error => {
        Message.error('Failed to fetch orders:', error);
      });
    },
    deleteOrder(orderId) {
      axios.post('http://106.52.82.181:8081/api/orders/delete', null, {
        params: {
          orderId: orderId
        }
      }).then(response => {
        if (response.data.code === 1) {
          Message.success('订单删除成功');
          // 从 orders 数组中移除该订单
          this.orders = this.orders.filter(order => order.orderId !== orderId);
        }
      })
    },
    cancelOrder(order) {
      console.log('Cancelling order:', order);
      // 取消订单逻辑
    },
  },
};
</script>

<style scoped>
.orders-page {
  padding: 20px;
}

.search-bar {
  width: 300px;
  margin-bottom: 20px;
}

.pagination {
  margin-top: 20px;
  text-align: center;
}

.icon-wrapper {
  display: flex;
  /* 使用 flex 布局使图标居中 */
  align-items: center;
  /* 垂直居中 */
  justify-content: center;
  /* 水平居中 */
  width: 36px;
  /* 定义容器的宽度 */
  height: 36px;
  /* 定义容器的高度 */
  cursor: pointer;
  /* 鼠标变为点击手势 */
}

.icon-wrapper i {
  font-size: 24px;
  /* 设置图标的大小 */
  color: #409eff;
  /* 设置图标颜色（可选） */
}

.el-table {
  font-size: 16px;
  /* 字体大小 */
  /* 字体颜色 */
  font-weight: bold;
}

.el-button--danger {
  background-color: #f56c6c;
  border-color: #f56c6c;
  color: #333;
  font-size: 16px;
  /* 字体大小 */
  /* 字体颜色 */
  font-weight: bold;
  /* 字体加粗 */
  padding: 10px 20px;
  /* 内边距，增加按钮的点击区域 */
  border-radius: 25px;
  /* 圆角 */
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  /* 阴影效果 */
  transition: all 0.3s ease;
  /* 过渡效果 */
}

.el-button--primary {
  background-color: #40ffa3;
  /* 默认背景色 */
  border-color: #40ffa3;
  /* 默认边框色 */
  color: #333;
  /* 字体颜色 */
  font-size: 16px;
  /* 字体大小 */
  font-weight: bold;
  /* 字体加粗 */
  padding: 10px 20px;
  /* 内边距 */
  border-radius: 25px;
  /* 圆角 */
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  /* 阴影 */
  transition: all 0.3s ease;
  /* 过渡效果 */
}

.el-button--primary:hover {
  background-color: #36e594;
  /* 悬停时的背景色 */
  border-color: #36e594;
  /* 悬停时的边框色 */
}

.el-button--primary:active {
  background-color: #36e594;
  /* 点击时背景色 */
  border-color: #36e594;
  /* 点击时的边框色 */
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
  /* 点击时的阴影效果 */
}

.el-button--primary:focus {
  outline: none;
  /* 去除焦点时的轮廓 */
}
</style>
<template>
  <el-container>
    <el-header style="font-size: 23px; font-weight: 800; display: flex; align-items: center; position: relative;">
      <h1 style="color: #fff; position: absolute; left: 50%; transform: translateX(-50%);">我的订单</h1>
    </el-header>
    <el-main>
      <el-table :data="orders" style="width: 80%; margin: 0 auto;">
        <el-table-column prop="orderId" label="订单号" width="220"></el-table-column>
        <el-table-column prop="createdAt" label="时间" width="240"></el-table-column>
        <el-table-column prop="status" label="状态" width="220"></el-table-column>
        <el-table-column prop="totalPrice" label="总价"></el-table-column>
        <el-table-column label="操作">
          <template slot-scope="scope">
            <el-button @click="viewOrder(scope.row)" type="text">查看</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 订单详情弹窗 -->
      <el-dialog :visible.sync="showOrderDetails" width="60%" class="order-details-dialog">
        <h3 class="dialog-title">订单详情</h3>
        <el-table :data="orderDetails" style="width: 100%" class="order-table">
          <el-table-column prop="productName" label="商品名" align="center"></el-table-column>
          <el-table-column prop="price" label="单价" align="center"></el-table-column>
          <el-table-column prop="quantity" label="数量" align="center"></el-table-column>
        </el-table>

        <div v-if="selectedOrder.status === '已发货，待确认收货'" class="confirm-receipt">
          <el-button @click="confirmReceipt(selectedOrder.orderId)" type="primary" class="confirm-btn">
            确认收货
          </el-button>
        </div>

        <span slot="footer" class="dialog-footer">
          <el-button @click="close" class="close-btn">关闭</el-button>
        </span>
      </el-dialog>
    </el-main>
  </el-container>
</template>


<script>
import axios from 'axios';
import { Message } from 'element-ui';
export default {
  data() {
    return {
      orders: [],
      selectedOrder: {}, // 当前查看的订单
      orderDetails: {},
      showOrderDetails: false // 控制订单详情弹窗的显示
    };
  },
  // 从后端获取订单数据
  mounted() {
    this.fetchOrders();
  },
  methods: {
    async fetchOrders() {
      try {
        const response = await axios.get('http://106.52.82.181:8081/api/orders/get', {
          params: {
            username: this.$store.getters.username
          }
        });
        if (response.data.code === 1) {
          this.orders = response.data.data;

          this.orders.forEach(order => {
            order.createdAt = this.formatDate(order.createdAt);
          });

        } else {
          Message.error('获取订单失败');
        }
      } catch (error) {
        console.error('请求订单数据时出错:', error);
        Message.error('无法连接到服务器');
      }
    },
    formatDate(dateString) {
      const date = new Date(dateString);
      return date.toLocaleString(); // 格式化为本地日期时间字符串
    },
    viewOrder(order) {

      this.selectedOrder = order;  // 确保 selectedOrder 被赋值

      this.showOrderDetails = true;
      axios.get('http://106.52.82.181:8081/api/orders/detail', {
        params: {
          orderId: this.selectedOrder.orderId
        }
      }).then(response => {
        if (response.data.code == 1) {
          this.orderDetails = response.data.data;
        } else {
          Message.error('获取订单详情失败');
        }
      }).catch(error => {
        Message.error('请求失败', error);
      });
    },

    // 确认收货
    confirmReceipt(orderId) {
      console.log("Confirming receipt for order:", orderId);
      axios.post('http://106.52.82.181:8081/api/orders/confirmReceipt', null, {
        params: {
          orderId: orderId
        }
      })
        .then(response => {
          if (response.data.code === 1) {
            this.selectedOrder.status = "交易成功";
            this.$message.success("订单已确认收货");
          } else {
            this.$message.error("确认收货失败");
          }
        })
        .catch(error => {
          console.error("确认收货请求失败", error);
          this.$message.error("网络错误，请稍后重试");
        });
    },
    close() {
      this.showOrderDetails = false;  // 关闭对话框
      // this.selectedOrder = null;  // 重置订单详情
    }

  },

};
</script>

<style scoped>
.el-header {
  background-color: #35495e;
  padding: 15px;
  color: white;
}

/* 对话框标题样式 */
.dialog-title {
  font-size: 24px;
  font-weight: bold;
  color: #333;
  text-align: center;
  margin-bottom: 20px;
}

/* 表格样式 */
.order-table {
  margin-bottom: 20px;
}

/* 每一行的单元格边框 */
.el-table th,
.el-table td {
  padding: 10px;
  text-align: center;
  border: 1px solid #ddd;
}

/* 按钮样式 */
.confirm-btn {
  background-color: #409EFF;
  color: white;
  padding: 10px 20px;
  border-radius: 5px;
  transition: background-color 0.3s;
}

.confirm-btn:hover {
  background-color: #66b1ff;
}

/* 关闭按钮样式 */
.close-btn {
  background-color: #f5f5f5;
  color: #409EFF;
  border-radius: 5px;
  padding: 5px 10px;
  transition: background-color 0.3s;
}

.close-btn:hover {
  background-color: #e6f7ff;
}

/* 对话框样式 */
.order-details-dialog .el-dialog {
  background-color: #fff;
  border-radius: 10px;
  box-shadow: 0px 4px 8px rgba(0, 0, 0, 0.1);
  padding: 20px;
}

/* 按钮和表格的间距 */
.confirm-receipt {
  display: flex;
  justify-content: center;
  margin-top: 20px;
}

.dialog-footer {
  display: flex;
  justify-content: center;
}
</style>

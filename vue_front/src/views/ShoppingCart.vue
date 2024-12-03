<template>
  <el-container>
    <el-header style="font-size: 33px; font-weight: 800; display: flex; align-items: center;position: relative;">
      <span style="color: #fff;position: absolute; left: 50%; transform: translateX(-50%);">购物车</span>
      <el-button type="danger" @click="clearCart" style="margin-left: auto;">清空购物车</el-button>
    </el-header>
    <el-main>
      <el-table :data="cartItems" style="width: 80%; margin: 0 auto;">
        <el-table-column prop="name" label="商品名称" />
        <el-table-column prop="price" label="价格" />
        <el-table-column prop="quantity" label="数量" />
        <el-table-column label="操作">
          <template #default="scope">
            <el-button type="danger" @click="removeItem(scope.row.productId)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div class="total" style="width: 80%; margin: 0 auto;">
        <span>总金额: {{ computedTotalPrice }} 元</span>
        <el-button type="primary" @click="checkout" style="margin-top: 10px;">支付</el-button>
      </div>
    </el-main>
  </el-container>
</template>

<script>
import axios from 'axios';
import { Message } from 'element-ui';
export default {
  data() {
    return {
      cartItems: [
      ],
      totalPrice: 0,
    };
  },
  computed: {
    computedTotalPrice() {
      return this.cartItems.reduce((total, item) => total + item.price * item.quantity, 0);
    }
  },
  created() {
    this.cartItems = this.fetchCartItems();
  },
  mounted() {
    console.log(this.cartItems); // 确认是否有数据
  },
  methods: {
    updateTotalPrice() {
      this.totalPrice = this.computedTotalPrice; // 在方法中更新 totalPrice
    },
    async fetchCartItems() {
      try {
        const response = await axios.post('http://106.52.82.181:8081/api/products/fetchCartItems', null, {
          params: {
            username: this.$store.getters.username
          }
        });
        console.log(this.$store.getters.username);
        console.log(response);
        if (response.data.code === 1) {
          this.cartItems = response.data.data;
        } else {
          Message.error('未能获取购物车数据');
        }
      }
      catch (error) {
        console.error('获取购物车数据失败:', error);
      }
    },
    removeItem(id) {
      console.log("Remove button clicked with id:", id); // 检查事件是否被触发
      this.cartItems = this.cartItems.filter(item => item.productId !== id);

      axios.post('http://106.52.82.181:8081/api/products/deleteOneCartItem', null, {
        params: {
          username: this.$store.getters.username,
          productId: this.cartItems[id].productId
        }
      })
        .then(response => {
          if (response.data.code === 1) {
            Message.success('删除成功');
          } else {
            Message.error('删除失败');
          }
        })
    },
    clearCart() {
      this.cartItems = [];
      axios.post('http://106.52.82.181:8081/api/products/deleteAllCartItems', null, {
        params: {
          username: this.$store.getters.username

        }
      })
        .then(response => {
          if (response.data.code === 1) {
            Message.success('清空购物车成功');
          } else {
            Message.error('清空购物车失败');
          }
        })
    },
    checkout() {
      this.updateTotalPrice();
      const orderData = {
        totalPrice: this.totalPrice,  // 购物车的总价格
        username: this.$store.getters.username,  // 获取当前用户的用户名
        products: this.cartItems.map(item => ({
          productName: item.name,
          quantity: item.quantity,
          price: item.price
        }))
      };
      axios.post('http://106.52.82.181:8081/api/orders/add', orderData)
        .then(response => {
          if (response.data.code === 1) {
            this.$router.push('/payment-success');
          } else {
            Message.error('支付失败');
          }

        })
        .catch(error => {
          console.error("Checkout error:", error);
        });
      const Data = {
        username: this.$store.getters.username,
        productIds: this.cartItems.map(item => item.productId)  // 收集商品ID数组
      };
      axios.post('http://106.52.82.181:8081/api/logs/purchase', Data,{
        headers: {
          'Content-Type': 'application/json'  // 设置请求头为 JSON 格式
        }
      })
        .then(response => {
          if (response.data.code === 1) {
            console.log('购买日志记录成功');
          } else {
            Message.error('记录购买日志失败');
          }
        })
    }
  }
};
</script>

<style scoped>
.el-header {
  background-color: #35495e;
  padding: 15px;
  color: white;
}

.total {
  margin-top: 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
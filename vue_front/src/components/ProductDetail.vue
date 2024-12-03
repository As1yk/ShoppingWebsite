<template>
  <div>
    <el-card class="product-detail">
      <h2>{{ product.name }}</h2>
      <p>价格：￥{{ product.price }} 元</p>
      <p>发货地址：{{ product.location }}</p>
      <el-input-number v-model="num" @change="handleChange" :min="1" :max="10" style="margin-right: 10px;"
        size="small"></el-input-number>
      <el-button type="primary" @click="addToCart()">添加到购物车</el-button>
      <br>
      <br>
      <el-button @click="goBack">返回</el-button>
    </el-card>
  </div>
</template>

<script>
import axios from 'axios';
import { Message } from 'element-ui';
export default {
  data() {
    return {
      num: 1,
      product: {} // 用于存储选中的商品详情
    };
  },
  created() {
    this.fetchProductDetail();
  },
  methods: {
    async addToCart() {
      // const username = this.$store.getters.username; // 从 Vuex 或其他存储中获取 user_id
      try {
        const response = await axios.post('http://106.52.82.181:8081/api/products/cart', null, {
          params: {
            username: this.$store.getters.username, // 从 Vuex 获取用户名
            productId: this.product.productId,
            num: this.num
          }
        });
        console.log(this.$store.getters.username,this.product.productId,this.num)
        console.log(response);
        if (response.data.code == 1) {
          Message.success('添加成功');
        } else {
          Message.error('添加失败');
        }
      } catch (error) {
        console.error('获取商品数据失败:', error);
      }
    },
    goBack() {
      this.$router.push('/customer/home'); // 返回商品列表页
    },
    async fetchProductDetail() {
      // 从路由参数中获取商品 ID
      console.log("123");
      const productId = this.$route.query.productId;
      console.log(productId);
      try {
        const response = await axios.get('http://106.52.82.181:8081/api/products/id', {
          params: { productId: productId }
        });
        console.log(response);
        if (response.data) {
          this.product = response.data.data;  // 将获取到的商品数据赋值给 products 
        } else {
          Message.error('未能获取商品数据');
        }
      } catch (error) {
        console.error('获取商品数据失败:', error);
      }

    }
  }
};
</script>

<style scoped>
.product-detail {
  padding: 20px;
}
</style>

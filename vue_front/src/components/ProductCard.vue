<template>
  <el-card class="product-card" :body-style="{  width: '80%' }">
    <div style="text-align: center;">
      <h3>{{ product.name }}</h3>
      <p>价格: ¥{{ product.price }}</p>
      <el-button type="primary" @click="addToCart">添加到购物车</el-button>
    </div>
  </el-card>
</template>

<script>
export default {
  name: 'ProductCard',
  props: {
    product: {
      type: Object,
      required: true
    }
  },
  methods: {
    addToCart() {
      this.$emit('add-to-cart', this.product);
    }
  }
};
</script>

<style scoped>
.product-card {
  width: 200px;
  margin: 10px;
}
</style>

<template>
    <div class="payment-success">
      <h2>支付成功</h2>
      <p>感谢您的购买！您的订单已经生成。</p>
      <el-button @click="$router.push('/customer/home')">返回首页</el-button>
    </div>
  </template>
  
  <script>
  export default {
    name: 'PaymentSuccess'
  };
  </script>
  
  <style scoped>
  .payment-success {
    text-align: center;
    margin-top: 50px;
  }
  </style>
  
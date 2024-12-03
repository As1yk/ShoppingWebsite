<template>
  <div id="app">
    <router-view></router-view>
  </div>
</template>

<script>
export default {
  name: 'App'
};
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
}
/* src/assets/styles/global.css 或 App.vue 中的 <style> */
body {
  background: linear-gradient(135deg, #65d2f6, hsl(282, 49%, 53%));
  min-height: 100vh; /* 确保背景覆盖整个页面 */
  margin: 0;
  font-family: 'Roboto', 'Arial', sans-serif; /* 设置主字体和备选字体 */
  font-size: 16px; /* 设置基本字体大小 */
  line-height: 1.6; /* 设置适当的行高，提升可读性 */
  color: #333; /* 适当的文字颜色 */
}

h1, h2, h3, h4, h5, h6 {
  font-family: 'Roboto', sans-serif; /* 为标题设置相同的字体 */
  font-weight: 700; /* 增强标题的粗体效果 */
}

a {
  color: #3498db; /* 设置链接颜色 */
  text-decoration: none; /* 移除默认的下划线 */
}

a:hover {
  text-decoration: underline; /* 鼠标悬停时增加下划线 */
}

p {
  font-size: 1rem; /* 段落文字大小 */
  margin-bottom: 16px; /* 段落间距 */
}

/* 美化按钮样式 */
.el-button {
  font-family: 'Roboto', 'Arial', sans-serif; /* 按钮字体 */
  font-size: 14px; /* 按钮文字大小 */
  border-radius: 5px; /* 按钮圆角 */
}

/* 统一输入框的字体 */
.el-input input {
  font-family: 'Roboto', 'Arial', sans-serif;
  font-size: 14px; /* 输入框字体大小 */
}
</style>

<template>
  <el-container style="height: 100vh; justify-content: center; align-items: center; ">
    <el-card class="login-card" shadow="always">
      <h2 style="text-align: center; margin-bottom: 20px;">欢迎来到电子商务网站</h2>
      <el-form @submit.prevent="login" :model="form" ref="loginForm" label-width="80px">
        <el-form-item label="角色" required>
          <el-select v-model="form.role" placeholder="选择角色" style="width: 100%;">
            <el-option label="客户" value="CUSTOMER"></el-option>
            <el-option label="销售员" value="ADMIN"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="用户名" required>
          <el-input v-model="form.username" placeholder="请输入用户名" prefix-icon="el-icon-user"></el-input>
        </el-form-item>
        <el-form-item label="密码" required>
          <el-input v-model="form.password" type="password" placeholder="请输入密码" prefix-icon="el-icon-lock"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="login" style="width: 100%;">登录</el-button>
        </el-form-item>
      </el-form>
      <div style="text-align: center; margin-top: 20px;">
        <router-link to="/register">没有账号？点击注册</router-link>
      </div>
    </el-card>
  </el-container>
</template>

<script>
import { Message } from 'element-ui';
import axios from 'axios';
export default {
  data() {
    return {
      form: {
        role: '',
        username: '',
        password: ''
      }
    };
  },
  mounted() {
    this.resetForm(); // 组件挂载时清空表单
  },
  methods: {
    resetForm() {
      this.form.password = '';
      this.form.role = '';
    },
    async login() {
      try {
        // 发送登录请求
        const response = await axios.post('http://106.52.82.181:8081/api/users/login', {
          username: this.form.username,
          password: this.form.password,
          role: this.form.role
        }, {
          headers: {
            'Content-Type': 'application/json'
          }
        });

        // 根据用户角色进行路由跳转
        if (response.data.code === 1) {
          // 登录成功后，存储用户名到 Vuex
          this.$store.dispatch('setUsername', this.form.username);
          // 提示登录成功
          Message.success('登录成功');
          // 路由跳转
          if (this.form.role === "ADMIN") {
            this.$router.push('/admin/dashboard');
          } else {
            this.$router.push('/customer/home');
          }
        } else {
          // 提示登录失败
          Message.error(response.data.msg);
        }

      } catch (error) {
        console.log(error);
      }
    }
  }

};
</script>

<style scoped>
.login-card {
  width: 400px;
  padding: 20px;
  border-radius: 10px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}
</style>

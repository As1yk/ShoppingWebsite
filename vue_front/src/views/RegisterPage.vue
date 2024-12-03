<template>
    <el-container style="height: 100vh; justify-content: center; align-items: center;">
        <el-card class="register-card" shadow="always">
            <h2 style="text-align: center; margin-bottom: 20px;">注册新账户</h2>
            <el-form :model="form" ref="registerForm" label-width="80px" @submit.prevent="register">
                <el-form-item label="用户名" required>
                    <el-input v-model="form.username" placeholder="请输入用户名" prefix-icon="el-icon-user"></el-input>
                </el-form-item>
                <el-form-item label="邮箱" required>
                    <el-input v-model="form.email" type="email" placeholder="请输入邮箱"
                        prefix-icon="el-icon-email"></el-input>
                </el-form-item>
                <el-form-item label="密码" required>
                    <el-input v-model="form.password" type="password" placeholder="请输入密码"
                        prefix-icon="el-icon-lock"></el-input>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" @click="register" style="width: 100%;">注册</el-button>
                </el-form-item>
            </el-form>
            <div style="text-align: center; margin-top: 20px;">
                <router-link to="/login">已有账号？点击登录</router-link>
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
                username: '',
                email: '',
                password: ''
            }
        };
    },
    methods: {
        async register() {
            try {
                const response = await axios.post('http://106.52.82.181:8081/api/users/register', this.form);

                if (response.data.code == 1) {
                    // 注册成功提示
                    Message.success(response.data.msg);
                }
                else {
                    // 注册失败提示
                    Message.error(response.data.msg);
                }

                // 清空表单
                console.log(this.$refs.registerForm);
                this.$refs.registerForm.resetFields();

            } catch (error) {
                if (error.response) {
                    if (error.response.status === 400) {
                        // 注册失败提示
                        Message.error(error.response.data.msg);
                    } else {
                        // 其他错误提示
                        Message.error('注册失败，请重试');
                    }
                } else {
                    // 网络错误或其他未处理的错误
                    Message.error('注册失败，请重试');
                }
            }
        }
    }
};
</script>

<style scoped>
.register-card {
    width: 400px;
    padding: 20px;
    border-radius: 10px;
    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}
</style>

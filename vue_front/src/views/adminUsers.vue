<template>
  <div class="customers-page">
    <h2>客户管理</h2>
    <el-input v-model="search" placeholder="输入客户名称" class="search-bar"
      style="width: 300px; border-radius: 20px; box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);">
      <template #suffix>

        <div class="icon-wrapper" @click="fetchUsers">
          <i class="el-icon-search" style="font-size: 24px;"></i>
        </div>
      </template>
    </el-input>

    <el-table :data="paginatedUsers" style="width: 70%;margin: 0 auto;">
      <el-table-column prop="userId" label="客户id" width="200" />
      <el-table-column prop="username" label="客户名称" width="200" />
      <el-table-column prop="email" label="邮箱" width="250" />
      <el-table-column prop="createdAt" label="账号创建时间" width="250" />
    </el-table>

    <!-- 分页组件 -->
    <el-pagination :current-page="currentPage" :page-size="itemsPerPage" :total="filteredUsers.length"
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
      users: [], // 示例数据
      paginatedUsers: [],
      itemsPerPage: 10, // 每页显示的商品数量
      currentPage: 1    // 当前页数
    };
  },

  computed: {
    filteredUsers() {
      return this.users.filter(user => user.username.includes(this.search));
    }
  },
  watch: {
    filteredUsers() {
      // 当过滤的商品变化时更新分页商品
      this.updatePaginatedUsers();
    }
  },

  created() {
    this.fetchUsers(); // 在组件加载时获取商品信息
  },
  methods: {
    handlePageChange(page) {
      this.currentPage = page;
      this.updatePaginatedUsers();
    },
    updatePaginatedUsers() {
      const start = (this.currentPage - 1) * this.itemsPerPage;
      const end = start + this.itemsPerPage;
      this.paginatedUsers = this.filteredUsers.slice(start, end);
    },
    async fetchUsers() {
      try {
        const response = await axios.get('http://106.52.82.181:8081/api/users/page');
        this.users = response.data.data;
        console.log(this.users);

        // 批量转换每个客户的 createdAt 字段
        this.users.forEach(user => {
          const date = new Date(user.createdAt);
          user.createdAt = `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')} ${String(date.getHours()).padStart(2, '0')}:${String(date.getMinutes()).padStart(2, '0')}:${String(date.getSeconds()).padStart(2, '0')}`;
        });

        this.updatePaginatedUsers();
      } catch (error) {
        Message.error('Failed to fetch users:', error);
      }
    }

  },
};
</script>

<style scoped>
.customers-page {
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
</style>
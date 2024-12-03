<template>
  <div class="log-page">
    <h2>客户浏览/购买日志记录</h2>

    <!-- 日期筛选器 -->
    <el-date-picker v-model="dateRange" type="daterange" range-separator="至" start-placeholder="开始日期"
      end-placeholder="结束日期" style="margin-bottom: 20px; width: 300px;" />

    <!-- 日志记录表格 -->
    <el-table :data="paginatedLogs" style="width: 70%; margin: 0 auto;">
      <el-table-column prop="userName" label="用户名" width="150" />
      <el-table-column prop="productName" label="商品名称" width="450" />
      <el-table-column prop="actionType" label="操作类型" width="150" />
      <el-table-column prop="activityDate" label="时间" width="250" />
    </el-table>

    <!-- 分页组件 -->
    <el-pagination :current-page="currentPage" :page-size="itemsPerPage" :total="totalLogs"
      @current-change="handlePageChange" layout="total, prev, pager, next"
      style="margin-top: 20px; text-align: center; font-size: 14px; color: #666; border-radius: 5px; " />
  </div>
</template>

<script>
import axios from 'axios';
import { Message } from 'element-ui';

export default {
  data() {
    return {
      logs: [], // 日志记录数据
      dateRange: [], // 日期筛选
      itemsPerPage: 12, // 每页显示的商品数量
      currentPage: 1,   // 当前页数
      totalLogs: 0,     // 总日志数
    };
  },
  watch: {
    dateRange() {
      this.fetchLogs(); // 日期范围变化时重新获取日志
    }
  },
  computed: {
    filteredLogs() {
      // 根据 dateRange 值过滤日志列表
      if (!this.dateRange || this.dateRange.length !== 2) {
        return this.logs; // 如果没有选择日期范围，则返回所有日志
      }

      const [startDate, endDate] = this.dateRange; // 获取开始日期和结束日期

      return this.logs.filter(log => {
        const logDate = new Date(log.activityDate); // 将日志日期转换为 Date 对象
        return logDate >= new Date(startDate) && logDate <= new Date(endDate); // 过滤在范围内的日志
      });
    },
    paginatedLogs() {
      // 计算分页后的日志
      const start = (this.currentPage - 1) * this.itemsPerPage;
      const end = start + this.itemsPerPage;
      return this.filteredLogs.slice(start, end);
    }
  },
  mounted() {
    this.fetchLogs(); // 初始加载数据
  },
  methods: {
    // 格式化日期
    formatDate(date) {
      const d = new Date(date);
      return `${d.getFullYear()}-${String(d.getMonth() + 1).padStart(2, '0')}-${String(d.getDate()).padStart(2, '0')} ${String(d.getHours()).padStart(2, '0')}:${String(d.getMinutes()).padStart(2, '0')}:${String(d.getSeconds()).padStart(2, '0')}`;
    },
    fetchLogs(page = this.currentPage) {
      // 如果 dateRange 为空，设置默认日期范围
      let startDate = this.dateRange && this.dateRange.length > 0 ? this.dateRange[0].toISOString().split('T')[0] : '2000-01-01'; // 默认开始日期
      let endDate = this.dateRange && this.dateRange.length > 1 ? this.dateRange[1].toISOString().split('T')[0] : '2100-12-31'; // 默认结束日期

      // 发起请求获取数据
      axios.get('http://106.52.82.181:8081/api/logs', {
        params: {
          pageNumber: page,
          pageSize: this.itemsPerPage,
          startDate: startDate,
          endDate: endDate
        }
      }).then(response => {
        const data = response.data.data;
        this.logs = data.logs; // 更新表格数据
        this.totalLogs = data.totalLogs; // 更新总记录数

        // 如果没有日志数据，显示提示
        if (this.totalLogs === 0) {
          Message.warning('没有找到符合条件的日志记录');
        } else {
          this.logs.forEach(log => {
            log.activityDate = this.formatDate(log.activityDate);
          });
        }
      }).catch(error => {
        console.error('Failed to fetch logs:', error);
      });
    },
    handlePageChange(page) {
      this.currentPage = page;
      this.fetchLogs(page); // 分页时重新获取数据
    }
  },
};
</script>

<style scoped>
.log-page {
  text-align: center;
  padding: 20px;
}

.el-table th,
.el-table td {
  text-align: center;
}
</style>

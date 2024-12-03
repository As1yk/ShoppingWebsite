<template>
    <div>
        <h2>销售统计报表</h2>
        <!-- <el-date-picker v-model="dateRange" type="daterange" range-separator="至" start-placeholder="开始日期"
            end-placeholder="结束日期" @change="fetchSalesReport" style="margin-bottom: 20px;"/> -->

        <el-table :data="salesData" style="width: 70%; margin-top: 20px;margin: 0 auto;" show-summary
            :summary-method="summaryMethod">
            <el-table-column prop="productName" label="商品名称" width="250" />
            <el-table-column prop="productQuantity" label="销售数量" width="250" />
            <el-table-column prop="productPrice" label="单价" width="250" />
            <el-table-column prop="productTotalPrice" label="总销售额" width="250" />
        </el-table>

    </div>
</template>

<script>
import axios from 'axios';
import { Message } from 'element-ui';
export default {
    data() {
        return {
            salesData: [],
        };
    },
    mounted() {
        this.fetchSalesReport();
    },
    methods: {
        fetchSalesReport() {
            axios.get('http://106.52.82.181:8081/api/orders/sales').then(response => {
                this.salesData = response.data.data;
            }).catch(error => {
                console.error('获取销售统计数据失败:', error);
                Message.error('获取销售统计数据失败');
            });
        },
        summaryMethod({ columns, data }) {
            const sums = [];
            columns.forEach((column, index) => {
                if (index === 3) { // 总销售额列
                    // 计算总销售额
                    const total = data.reduce((sum, row) => {
                        return sum + (row.productTotalPrice || 0); // 累加总销售额
                    }, 0);
                    sums[index] = total; // 设置合计值
                } else if(index === 0){
                    sums[index] = '合计';
                }else {
                    sums[index] = '-'; // 其他列不显示合计
                }
            });
            return sums;
        }
    }
};
</script>
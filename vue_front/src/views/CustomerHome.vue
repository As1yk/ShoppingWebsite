<template>
    <el-container>
        <el-header style="font-size: 33px; font-weight: 800; display: flex; align-items: center; position: relative;">
            <span style="position: absolute; left: 50%; transform: translateX(-50%);">电子商务网站</span>
            <el-button type="danger" @click="logout" style="margin-left: auto;">注销</el-button>
        </el-header>
        <el-container>
            <el-aside width="200px"
                style="background: linear-gradient(135deg, #f0f8ff, #f7d1d1); color: #555; padding: 20px; box-shadow: 2px 0px 8px rgba(0, 0, 0, 0.15); border-right: 1px solid #ddd; border-radius: 10px;">

                <el-menu :default-openeds="['1', '3']" background-color="#ffffff" text-color="#555"
                    active-text-color="#ff6f61" style="border-radius: 10px;">

                    <el-menu-item-group class="custom-menu-group">
                        <!-- 购物车项 -->
                        <el-menu-item index="1" @click="$router.push('/shoppingCart')"
                            style="transition: all 0.3s; background-color: #f7faff; border-radius: 5px; margin-bottom: 10px; box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1); font-weight: bold;">
                            <i class="el-icon-shopping-cart" style="margin-right: 8px;"></i> 购物车
                        </el-menu-item>

                        <!-- 我的订单项 -->
                        <el-menu-item index="2" @click="$router.push('/orders')"
                            style="transition: all 0.3s; background-color: #f9f7ff; border-radius: 5px; box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1); font-weight: bold;">
                            <i class="el-icon-document" style="margin-right: 8px;"></i> 我的订单
                        </el-menu-item>
                    </el-menu-item-group>

                </el-menu>
            </el-aside>
            <el-main>
                <el-input v-model="search" placeholder="搜索商品"
                    style="margin-bottom: 20px; width: 400px; border-radius: 10px; box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1); background: #fff; padding: 8px 20px; font-size: 16px; transition: all 0.3s ease;" />

                <el-row :gutter="20">
                    <el-col v-for="product in paginatedProducts" :key="product.productId" :span="6" :xs="12" :sm="8"
                        :md="6">
                        <el-card :body-style="{ padding: '20px' }" class="product-card"
                            @click.native="viewProductDetail(product)"
                            style="border-radius: 15px; box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1); transition: transform 0.3s ease;"
                            @mouseenter="handleHoverIn" @mouseleave="handleHoverOut">
                            <!-- 使用 object-fit: contain 来确保图片完整显示 -->
                            <img :src="product.imageUrl" alt="Product Image"
                                style="width: 100%; height: 200px; object-fit: contain; border-radius: 10px;">
                            <h3 style="font-size: 18px; margin-top: 10px; text-align: center; color: #333; 
                      white-space: nowrap; overflow: hidden; text-overflow: ellipsis; 
                      width: 100%; display: block; line-height: 1.2em;">
                                {{ product.name }}
                            </h3>
                            <p
                                style="font-size: 18px; font-weight: bold; color: #E74C3C; text-align: center; margin-top: 5px;">
                                ￥{{ product.price }} 元
                            </p>
                        </el-card>
                    </el-col>
                </el-row>


                <el-pagination :current-page="currentPage" :page-size="itemsPerPage" :total="filteredProducts.length"
                    @current-change="handlePageChange" layout="total, prev, pager, next"
                    style="margin-top: 20px; text-align: center; font-size: 14px; color: #666; background: #f7f7f7; border-radius: 5px; box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1); padding: 10px 20px;">
                </el-pagination>

            </el-main>
        </el-container>
    </el-container>
</template>

<script>
import { Message } from 'element-ui';
import axios from 'axios';
export default {
    data() {
        return {
            search: '',
            products: [],
            paginatedProducts: [],
            itemsPerPage: 12, // 每页显示的商品数量
            currentPage: 1    // 当前页数
        };
    },
    computed: {
        filteredProducts() {
            // 根据 search 值过滤商品列表
            return this.products.filter(product => product.name.includes(this.search));
        }
    },
    methods: {

        // 商品 Hover 动效
        handleHoverIn(event) {
            event.currentTarget.style.transform = 'scale(1.05)';
            event.currentTarget.style.boxShadow = '0 6px 12px rgba(0, 0, 0, 0.2)';
        },
        handleHoverOut(event) {
            event.currentTarget.style.transform = 'scale(1)';
            event.currentTarget.style.boxShadow = '0 4px 8px rgba(0, 0, 0, 0.1)';
        },

        logout() {
            Message.success('注销成功');
            this.$router.push('/login');
        },
        viewProductDetail(product) {
            this.$router.push({ path: `/product/${product.id}`, query: { productId: product.productId } }); // 导航到商品详情页
            axios.post('http://106.52.82.181:8081/api/logs/view', null, {
                params: {
                    username: this.$store.getters.username,
                    productId: product.productId,
                }
            }).then(response => {
                if (response.data.code === 1) {
                    console.log('已记录浏览日志');
                } else {
                    Message.error('记录日志失败');
                }

            }).catch(error => {
                Message.error('记录浏览日志失败:', error);
            })


        },
        handlePageChange(page) {
            this.currentPage = page;
            this.updatePaginatedProducts();
        },
        updatePaginatedProducts() {
            // 根据当前页码和每页数量计算分页商品
            const start = (this.currentPage - 1) * this.itemsPerPage;
            const end = start + this.itemsPerPage;
            this.paginatedProducts = this.filteredProducts.slice(start, end);
        },
        // 获取商品信息
        async fetchProducts() {
            try {
                const response = await axios.get('http://106.52.82.181:8081/api/products'); // 后端接口路径

                if (response.data) {
                    this.products = response.data.data;  // 将获取到的商品数据赋值给 products 列表
                } else {
                    Message.error('未能获取商品数据');
                }
            } catch (error) {
                console.error('获取商品数据失败:', error);
            }
        }
    },
    watch: {
        filteredProducts() {
            // 当过滤的商品变化时更新分页商品
            this.updatePaginatedProducts();
        }
    },
    mounted() {
        // 初始加载商品
        this.updatePaginatedProducts();
    },
    created() {
        this.fetchProducts(); // 在组件加载时获取商品信息
    },
};
</script>

<style scoped>
.el-header {
    background-color: #35495e;
    padding: 15px;
    color: white;
}

.product-card {
    transition: transform 0.2s, box-shadow 0.3s;
}

.product-card:hover {
    transform: translateY(-5px);
    box-shadow: 0 4px 15px rgba(0, 0, 0, 0.3);
}

.side-menu .el-menu-item:hover {
    background-color: #fff3f3;
    color: #ff6f61;
}

.side-menu .el-menu-item:active {
    background-color: #ffd1d1;
    color: #ff2a6e;
}

/* 鼠标悬停时变更背景色和字体颜色 */
.el-menu-item:hover {
    background-color: #ffefef;
    color: #ff6f61;
}

/* 点击时背景色变为深色，保持样式 */
.el-menu-item:active {
    background-color: #ffd1d1;
    color: #ff2a6e;
}

/* 保持点击后的背景色 */
.el-menu-item.is-active {
    background-color: #ffefd5;
    color: #ff2a6e;
}

/* 给菜单项添加圆角效果 */
.el-menu-item {
    border-radius: 5px;
}

/* 给整个菜单添加过渡效果 */
.el-menu {
    transition: all 0.3s ease;
    border-radius: 10px;
}

/* 鼠标悬停时更改菜单项的文字颜色 */
.el-menu-item:hover {
    background-color: #ffebeb;
    color: #ff6f61;
}

/* 设置菜单项的间距 */
.el-menu-item-group {
    margin-bottom: 10px;
}

/* 增加菜单项的阴影效果 */
.el-menu-item {
    box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}

/* 鼠标点击时菜单项阴影变化 */
.el-menu-item.is-active {
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
}

/* 过渡效果 */
.el-menu-item {
    transition: background-color 0.3s ease, color 0.3s ease, box-shadow 0.3s ease;
}

/* 为整个侧边栏添加背景色、阴影和圆角 */
.el-aside {
    background: linear-gradient(135deg, #f0f8ff, #f7d1d1);
    color: #555;
    padding: 20px;
    box-shadow: 2px 0px 8px rgba(0, 0, 0, 0.15);
    border-right: 1px solid #ddd;
    border-radius: 10px;
}

/* 菜单栏选中的字体颜色 */
.el-menu-item.is-active {
    background-color: #ffefd5;
    color: #ff6f61;
}

/* 美化菜单项 */
.custom-menu-group {
    background: linear-gradient(to bottom, #e0f7fa, #80deea);
    /* 蓝绿色渐变背景色 */

    /* 更亮的渐变背景色 */
    border-radius: 5px;
    /* 圆角 */
    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
    /* 阴影效果 */
    padding: 10px;
    margin-bottom: 0;
    /* 去掉下方的外边距 */
}

.product-card {
    background-color: #fff;
    /* 背景白色 */
    border: 1px solid #eee;
    /* 浅灰色边框 */
    transition: transform 0.3s ease, box-shadow 0.3s ease;
    /* 动画效果 */
}

/* 商品卡片 hover 放大效果 */
.product-card:hover {
    transform: scale(1.05);
    box-shadow: 0 6px 12px rgba(0, 0, 0, 0.2);
}

/* 输入框和按钮样式 */
.el-input {
    border-radius: 20px;
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.el-button {
    background-color: #E74C3C;
    color: #fff;
    border-radius: 5px;
}

.el-pagination {
    font-size: 14px;
    color: #666;
    margin-top: 20px;
}

/* 商品展示时的布局 */
.el-card img {
    width: 100%;
    height: auto;
    border-radius: 10px;
}

.el-card h3 {
    font-size: 18px;
    margin-top: 10px;
    text-align: center;
    color: #333;
}

.el-card p {
    font-size: 16px;
    color: #E74C3C;
    text-align: center;
    margin-top: 5px;
}

.el-pagination {
    background: linear-gradient(135deg, #ff9a9e, #fad0c4);
    /* 背景渐变色（粉色渐变） */
    border-radius: 10px;
    /* 边框圆角 */
    box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
    /* 阴影效果 */
    padding: 15px 25px;
    /* 内边距 */
    display: inline-block;
    /* 居中显示 */
    transition: background-color 0.3s ease, box-shadow 0.3s ease;
    /* 增加过渡效果 */
}

/* 分页项样式 */
.el-pagination .el-pager li {
    background: #fff;
    /* 背景色 */
    color: #333;
    /* 文字颜色 */
    padding: 8px 12px;
    /* 内边距 */
    margin: 0 5px;
    /* 分页项间距 */
    border-radius: 4px;
    /* 圆角效果 */
    cursor: pointer;
    transition: background-color 0.3s ease, color 0.3s ease;
    /* 增加悬停时的过渡效果 */
}

/* 分页项悬停效果 */
.el-pagination .el-pager li:hover {
    background-color: #ff6f61;
    /* 悬停背景色 */
    color: #fff;
    /* 悬停文字颜色 */
}

/* 激活页样式 */
.el-pagination .el-pager li.is-current {
    background-color: #ff6f61;
    /* 当前页背景色 */
    color: white;
    /* 当前页文字颜色 */
    font-weight: bold;
    /* 当前页加粗 */
}

/* 分页按钮样式 */
.el-pagination .el-button {
    background-color: #ff9a9e;
    /* 按钮背景色 */
    color: white;
    /* 按钮文字颜色 */
    border-radius: 4px;
    /* 圆角效果 */
    padding: 8px 15px;
    /* 按钮内边距 */
    border: none;
    /* 移除按钮边框 */
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
    /* 阴影效果 */
    cursor: pointer;
    transition: background-color 0.3s ease, box-shadow 0.3s ease;
    /* 按钮过渡效果 */
}

/* 按钮悬停效果 */
.el-pagination .el-button:hover {
    background-color: #ff6f61;
    /* 悬停背景色 */
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.2);
    /* 悬停时按钮的阴影效果 */
}

.el-input {
    width: 300px;
    /* 设置输入框宽度 */
    box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
    /* 添加阴影 */
    background: linear-gradient(135deg, #f6d365, #fda085);
    /* 渐变背景 */
    padding: 4px 10px;
    /* 设置内边距 */
    font-size: 16px;
    /* 设置字体大小 */
    transition: all 0.3s ease;
    /* 过渡效果 */
}

/* 焦点时的样式 */
.el-input:focus {
    border-color: #ff6f61;
    /* 聚焦时边框颜色 */
    box-shadow: 0 4px 12px rgba(255, 111, 97, 0.3);
    /* 聚焦时的阴影 */
}

/* 提示文字样式 */
.el-input::placeholder {
    color: #aaa;
    /* 设置灰色的提示文字 */
    font-size: 14px;
    /* 提示文字的字体大小 */
}

/* 输入框悬停时的效果 */
.el-input:hover {
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
    /* 悬停时加大阴影 */
}

/* 禁用状态下的样式 */
.el-input.is-disabled {
    background-color: #f5f5f5;
    /* 禁用状态下的背景色 */
    cursor: not-allowed;
    /* 禁用时光标变为不可点击 */
}
</style>

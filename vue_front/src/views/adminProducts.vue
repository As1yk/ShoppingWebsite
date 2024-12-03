<template>
    <div class="products-page">
        <h2>商品管理</h2>
        <el-header style="display: flex; align-items: center; position: relative;">

            <!-- 搜索商品 -->
            <el-input v-model="search" placeholder="输入商品名称" class="search-bar"
                style="width: 300px; border-radius: 20px; box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);position: absolute; left: 50%; transform: translateX(-50%);">
                <template #suffix>
                    <!-- 图标包裹容器 -->
                    <div class="icon-wrapper" @click="fetchProducts">
                        <i class="el-icon-search"></i> <!-- 图标 -->
                    </div>
                </template>
            </el-input>

            <!-- 右边的新增商品按钮 -->
            <el-button type="primary" @click="showAddProductDialog"
                style="border-radius: 200px;margin-left: auto; margin-right:70px; box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);">
                新增商品
            </el-button>
        </el-header>


        <el-table :data="paginatedProducts" style="width: 90%; margin: 0 auto;" v-bind="tableStyle"
            @row-hover="handleRowHover">
            <el-table-column prop="name" label="商品名称" width="600" />
            <el-table-column prop="location" label="发货地址" width="150" />
            <el-table-column prop="price" label="价格" width="150" />
            <el-table-column prop="stock" label="库存" width="150" />
            <el-table-column label="操作" width="200">
                <template #default="scope">
                    <el-button size="mini" @click="editProduct(scope.row)">编辑</el-button>
                    <el-button size="mini" type="danger" @click="deleteProduct(scope.row)">删除</el-button>
                </template>
            </el-table-column>
        </el-table>

        <!-- 底部操作按钮 -->
        <el-pagination :current-page="currentPage" :page-size="itemsPerPage" :total="filteredProducts.length"
            @current-change="handlePageChange" layout="total, prev, pager, next"
            style="margin-top: 20px; text-align: center; font-size: 14px; color: #666;  border-radius: 5px; ">
        </el-pagination>


        <template>
            <el-dialog title="新增商品" :visible.sync="addProductDialogVisible" width="65%">
                <!-- 商品输入表单 -->
                <el-form :model="newProduct" ref="addProductForm" label-width="120px">
                    <el-form-item label="商品名称" :rules="[{ required: true, message: '请输入商品名称', trigger: 'blur' }]">
                        <el-input v-model="newProduct.name" placeholder="请输入商品名称" />
                    </el-form-item>

                    <el-form-item label="发货地址" :rules="[{ required: true, message: '请输入发货地址', trigger: 'blur' }]">
                        <el-input v-model="newProduct.location" placeholder="请输入发货地址" />
                    </el-form-item>

                    <el-form-item label="价格" :rules="[{ required: true, message: '请输入商品价格', trigger: 'blur' }]">
                        <el-input v-model="newProduct.price" type="number" placeholder="请输入商品价格" />
                    </el-form-item>

                    <el-form-item label="库存" :rules="[{ required: true, message: '请输入商品库存', trigger: 'blur' }]">
                        <el-input v-model="newProduct.stock" type="number" placeholder="请输入商品库存" />
                    </el-form-item>

                    <el-form-item label="商品图片链接" :rules="[{ required: true, message: '请输入商品图片链接', trigger: 'blur' }]">
                        <el-input v-model="newProduct.imageUrl" placeholder="请输入商品图片链接" />
                    </el-form-item>
                </el-form>
                <span slot="footer" class="dialog-footer">
                <el-button @click="addProductDialogVisible = false">取消</el-button>
                <el-button type="primary" @click="submitAddProduct">提交</el-button>
            </span>
            </el-dialog>
        </template>

        <el-dialog title="编辑商品" :visible.sync="editProductDialogVisible" width="65%">
            <el-form :model="editProductForm" ref="editProductForm" label-width="120px">
                <el-form-item label="商品名称" prop="name">
                    <el-input v-model="editProductForm.name" placeholder="请输入商品名称"></el-input>
                </el-form-item>
                <el-form-item label="发货地址" prop="location">
                    <el-input v-model="editProductForm.location" placeholder="请输入发货地址"></el-input>
                </el-form-item>
                <el-form-item label="价格" prop="price">
                    <el-input v-model="editProductForm.price" type="number" placeholder="请输入商品价格"></el-input>
                </el-form-item>
                <el-form-item label="库存" prop="stock">
                    <el-input v-model="editProductForm.stock" type="number" placeholder="请输入商品库存"></el-input>
                </el-form-item>
                <el-form-item label="图片链接" :rules="[{ required: true, message: '请输入商品图片链接', trigger: 'blur' }]" prop="imageUrl">
                    <el-input v-model="editProductForm.imageUrl" placeholder="请输入商品图片链接"></el-input>
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
                <el-button @click="editProductDialogVisible = false">取消</el-button>
                <el-button type="primary" @click="submitEditProduct">提交</el-button>
            </span>
        </el-dialog>
    </div>
</template>

<script>
import axios from 'axios';
import { Message } from 'element-ui';
export default {
    data() {
        return {
            search: '',
            products: [],
            paginatedProducts: [],
            itemsPerPage: 10, // 每页显示的商品数量
            currentPage: 1,    // 当前页数
            addProductDialogVisible: false, // 控制新增商品对话框显示
            newProduct: { // 用于绑定商品数据
                name: '',
                location: '',
                price: '',
                stock: '',
                image_url: ''
            },
            editProductDialogVisible: false, // 控制对话框显示
            editProductForm: {
                name: '',
                description: '',
                price: null,
                stock: null,
                image_url: ''
            }
        };
    },
    computed: {
        filteredProducts() {
            return this.products.filter(product => product.name.includes(this.search));
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
    methods: {
        
        handlePageChange(page) {
            this.currentPage = page;
            this.updatePaginatedProducts();
        },
        
        updatePaginatedProducts() {
            const start = (this.currentPage - 1) * this.itemsPerPage;
            const end = start + this.itemsPerPage;
            this.paginatedProducts = this.filteredProducts.slice(start, end);
        },
        // 显示新增商品对话框
        showAddProductDialog() {
            this.addProductDialogVisible = true;
        },
        // 获取商品信息
        async fetchProducts() {
            try {
                const response = await axios.get('http://106.52.82.181:8081/api/products'); // 后端接口路径

                if (response.data) {
                    this.products = response.data.data;  // 将获取到的商品数据赋值给 products 列表
                    console.log(this.products);
                } else {
                    Message.error('未能获取商品数据');
                }
            } catch (error) {
                console.error('获取商品数据失败:', error);
            }
        },
        // 提交新增商品数据
        async submitAddProduct() {
            // 校验表单
            const isValid = await this.$refs.addProductForm.validate();
            if (!isValid) return;
            // 提交数据到后端
            try {
                const response = await axios.post('http://106.52.82.181:8081/api/products/addProduct', this.newProduct);
                Message.success('新增商品成功');
                this.addProductDialogVisible = false;

                // 清空表单
                this.newProduct = {
                    name: '',
                    description: '',
                    price: '',
                    stock: '',
                    image_url: ''
                };

                // 更新商品列表
                this.filteredProducts.push(response.data.data);
            } catch (error) {
                this.$message.error('新增商品失败');
            }
        }, // 点击编辑按钮时
        editProduct(product) {
            console.log('编辑商品:', product);
            // 将商品信息填充到编辑表单
            this.editProductForm = { ...product };
            this.editProductDialogVisible = true;
        },

        // 提交编辑后的商品信息
        async submitEditProduct() {
            // 校验表单
            const isValid = await this.$refs.editProductForm.validate();
            if (!isValid) return;

            try {
                const response = await axios.put(`http://106.52.82.181:8081/api/products/editProduct/${this.editProductForm.productId}`, this.editProductForm);
                if (response.status === 200) {
                    this.$message.success('商品信息更新成功');
                    this.editProductDialogVisible = false;

                    // 找到列表中对应的商品并更新信息
                    const updatedProduct = this.products.find(product => product.productId === this.editProductForm.productId);
                    if (updatedProduct) {
                        Object.assign(updatedProduct, this.editProductForm);
                    }
                }
            } catch (error) {
                console.error('更新商品失败:', error);
                this.$message.error('更新商品失败');
            }
        },
        // 删除商品
        deleteProduct(product) {
            axios.post('http://106.52.82.181:8081/api/products/delete', null, {
                params: {
                    productId: product.productId
                }
            }).then(response => {
                if (response.data.code === 1) {
                    Message.success('已删除商品: ' + product.name);
                    // 从 products 数组中移除该商品
                    this.products = this.products.filter(p => p.productId !== product.productId);
                    // 从 filteredProducts 数组中移除该商品
                    this.filteredProducts = this.filteredProducts.filter(p => p.productId !== product.productId);

                }
            }).catch(error => {
                Message.error('删除商品失败:', error);
            });
        },

    },
};
</script>

<style scoped>
.products-page {
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

.el-table {
    width: 90%;
    /* 表格宽度 */
    margin: 20px auto;
    /* 表格居中 */
    border-radius: 10px;
    /* 圆角 */
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
    /* 阴影 */
    background-color: #fff;
    /* 背景色 */
    overflow: hidden;
    /* 防止内容溢出 */
}

/* 表头样式 */
.el-table th {
    background-color: #f4f6f9;
    /* 浅灰色背景 */
    color: #333;
    /* 字体颜色 */
    font-size: 14px;
    /* 字体大小 */
    font-weight: bold;
    /* 加粗 */
    text-align: center;
    /* 水平居中 */
    padding: 10px 0;
    /* 内边距 */
}

/* 表格单元格样式 */
.el-table td {
    text-align: center;
    /* 水平居中 */
    padding: 12px 8px;
    /* 内边距 */
    color: #666;
    /* 字体颜色 */
}

.el-table .el-table__row:hover {
    background-color: #0065a4;
    /* 鼠标悬停时改变行背景色 */
}

.el-table__cell {
    padding: 12px 15px;
}

.el-table__header-wrapper {
    border-radius: 8px 8px 0 0;
}

.el-button {
    background-color: #40d2ff;
    /* 背景色 */
    border-color: #40d2ff;
    margin-right: 10px;
    /* 编辑和删除按钮之间的间距 */
    color: #333;
    /* 字体颜色 */
    font-weight: bold;
    /* 字体加粗 */
    padding: 10px 20px;
    /* 内边距，增加按钮的点击区域 */
    border-radius: 25px;
    /* 圆角 */
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
    /* 阴影效果 */
    transition: all 0.3s ease;
    /* 过渡效果 */
}

.el-button-mini {
    background-color: #40d2ff;
    /* 背景色 */
    border-color: #40d2ff;
    font-size: 12px;
    color: #333;
    /* 字体颜色 */
    font-weight: bold;
    /* 字体加粗 */
    padding: 10px 20px;
    /* 内边距，增加按钮的点击区域 */
    border-radius: 25px;
    /* 圆角 */
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
    /* 阴影效果 */
    transition: all 0.3s ease;
    /* 过渡效果 */
}

.el-button--danger {
    background-color: #f56c6c;
    border-color: #f56c6c;
    color: #333;
    /* 字体颜色 */
    font-weight: bold;
    /* 字体加粗 */
    padding: 10px 20px;
    /* 内边距，增加按钮的点击区域 */
    border-radius: 25px;
    /* 圆角 */
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
    /* 阴影效果 */
    transition: all 0.3s ease;
    /* 过渡效果 */
}

.el-button--primary {
    background-color: #40ffa3;
    /* 默认背景色 */
    border-color: #40ffa3;
    /* 默认边框色 */
    color: #333;
    /* 字体颜色 */
    font-size: 16px;
    /* 字体大小 */
    font-weight: bold;
    /* 字体加粗 */
    padding: 10px 20px;
    /* 内边距 */
    border-radius: 25px;
    /* 圆角 */
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
    /* 阴影 */
    transition: all 0.3s ease;
    /* 过渡效果 */
}

.el-button--primary:hover {
    background-color: #36e594;
    /* 悬停时的背景色 */
    border-color: #36e594;
    /* 悬停时的边框色 */
}

.el-button--primary:active {
    background-color: #36e594;
    /* 点击时背景色 */
    border-color: #36e594;
    /* 点击时的边框色 */
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
    /* 点击时的阴影效果 */
}

.el-button--primary:focus {
    outline: none;
    /* 去除焦点时的轮廓 */
}

/* 添加分页样式 */
.pagination {
    margin-top: 20px;
    text-align: center;
}

/* 分页器样式 */
.el-pagination {
    display: flex;
    justify-content: center;
    align-items: center;
    padding: 20px 0;
}

.el-pagination .el-button--primary {
    background-color: #40ffcf;
    border-color: #40ffdc;
}

.el-pagination .el-button--default {
    color: #409EFF;
    border-color: #409EFF;
}

.el-pagination .el-icon {
    font-size: 14px;
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

@media (max-width: 768px) {
    .el-table {
        width: 100%;
        font-size: 12px;
    }

    .el-table-column {
        width: auto;
    }

    .el-button {
        font-size: 10px;
    }
}
</style>
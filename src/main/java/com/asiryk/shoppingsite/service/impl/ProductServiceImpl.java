package com.asiryk.shoppingsite.service.impl;

import com.asiryk.shoppingsite.mapper.ProductMapper;
import com.asiryk.shoppingsite.pojo.CartItem;
import com.asiryk.shoppingsite.pojo.Product;
import com.asiryk.shoppingsite.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductMapper productMapper;

//    @Override
//    public Product addProduct(Product product) {
//        productMapper.insertProduct(product);
//        return product;
//    }

    @Override
    public List<Product> getAllProducts() {
        return productMapper.findAll();
    }
    @Override
    public Product getProductById(int productId) {
        return productMapper.findByid(productId);
    }
    @Override
    public Product getProductByName(String name) {
        return productMapper.findByName(name);
    }
    @Override
    public void pushToCart(String username, int productId, int num){
        productMapper.pushToCart(username,productId,num);
    }
    @Override
    public List<CartItem> fetchCartItems(String username) {
        return productMapper.fetchCartItems(username);
    }
    @Override
    public void deleteOneCartItem(String username, int productId) {
        productMapper.deleteOneCartItem(username,productId);
    }
    @Override
    public void deleteAllCartItems(String username) {
        productMapper.deleteAllCartItems(username);
    }
    @Override
    public Integer Count(){
        return productMapper.Count();
    }
    @Override
    public void deleteProductById(Integer productId){
        productMapper.deleteProductById(productId);
    }
    @Override
    public Product addProduct(Product product) {
        // 插入商品到数据库
        productMapper.insertProduct(product);
        // 假设插入后数据库会自动生成 ID，可以通过查询返回完整的商品数据
        return product;  // 返回插入后的商品数据，包含自动生成的 ID 和其他字段
    }
    @Override
    public Product updateProduct(Integer id, Product product) {
        // 先查找商品是否存在
        Product existingProduct = productMapper.findByid(id);
        if (existingProduct == null) {
            throw new RuntimeException("商品不存在");
        }

        // 更新商品信息
        existingProduct.setName(product.getName());
        existingProduct.setDescription(product.getDescription());
        existingProduct.setPrice(product.getPrice());
        existingProduct.setStock(product.getStock());
        existingProduct.setImageUrl(product.getImageUrl());

        productMapper.updateProduct(existingProduct);
        return existingProduct;
    }
    @Override
    public List<Product> searchProducts(String search){
        // 如果没有搜索条件，返回所有客户
        if (search == null || search.trim().isEmpty()) {
            return productMapper.findAll();
        } else {
            // 根据客户名称进行模糊查询
            return productMapper.searchProducts("%" + search + "%");
        }
    }
//    @Override
//    public Product updateProduct(Integer id, Product product) {
//        product.setProductId(id); // 确保更新时使用正确的 ID
//        productMapper.updateProduct(product);
//        return product;
//    }
//
//    @Override
//    public void deleteProduct(Integer id) {
//        productMapper.deleteProduct(id);
//    }
}


package com.asiryk.shoppingsite.controller;
import com.asiryk.shoppingsite.mapper.ProductMapper;
import com.asiryk.shoppingsite.pojo.*;
import com.asiryk.shoppingsite.service.ProductService;
import com.asiryk.shoppingsite.service.UserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Array;
import java.util.Collections;
import java.util.List;
import java.util.Random;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "*") // 允许所有来源的请求跨域
public class ProductController {

    @Autowired
    private ProductService productService;
    private UserService userService;
    @Autowired
    private ProductMapper productMapper;

    // 获取所有商品
    @GetMapping()
    public Result getProductsByPage() {
        List<Product> products = productMapper.findAll();
        // 使用一个固定的随机种子
        long seed = 12345;
        Collections.shuffle(products, new Random(seed));

        return Result.success(products);
    }

    // 根据productId查询商品
    @GetMapping("/id")
    public Result getProductById(@RequestParam int productId) {
        Product products = productService.getProductById(productId);
        return Result.success(products);
    }

    @PostMapping("/cart")
    public Result pushToCart(@RequestParam String username, @RequestParam int productId,
                             @RequestParam int num) {
        // 打印接收到的参数
        System.out.println("Received username: " + username);
        System.out.println("Received productId: " + productId);
        System.out.println("Received num: " + num);
        productService.pushToCart(username, productId, num);
        return Result.success("成功加入购物车");
    }

    @PostMapping("/fetchCartItems")
    public Result fetchCartItems(@RequestParam String username) {
        List<CartItem> products = productService.fetchCartItems(username);
        return Result.success(products);
    }
    @PostMapping("/deleteOneCartItem")
    public Result deleteCartItem(@RequestParam String username,@RequestParam int productId) {
        productService.deleteOneCartItem(username,productId);
        return Result.success("删除成功");
    }

    @PostMapping("/deleteAllCartItems")
    public Result deleteAllCartItems(@RequestParam String username) {
        productService.deleteAllCartItems(username);
        return Result.success("清空购物车成功");
    }

    @PostMapping("/delete")
    public Result deleteProductById(@RequestParam Integer productId) {
        productService.deleteProductById(productId);
        return Result.success();
    }
    @PostMapping("/addProduct")
    public Result addProduct(@RequestBody Product product) {
        Product createdProduct = productService.addProduct(product);
        return Result.success(createdProduct);
    }
    @PutMapping("/editProduct/{id}")
    public Result editProduct(@PathVariable Integer id, @RequestBody Product product) {
        try {
            // 执行商品更新逻辑
            Product updatedProduct = productService.updateProduct(id, product);
            return Result.success(updatedProduct); // 返回更新后的商品
        } catch (Exception e) {
            return Result.error("修改失败");
        }
    }

}

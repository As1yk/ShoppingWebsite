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
        // 从数据库获取所有商品
        List<Product> products = productMapper.findAll();
        // 使用一个固定的随机种子打乱商品顺序
        long seed = 12345;
        Collections.shuffle(products, new Random(seed));

        // 返回成功的结果，并将商品列表传递给前端
        return Result.success(products);
    }

    // 根据 productId 查询单个商品
    @GetMapping("/id")
    public Result getProductById(@RequestParam int productId) {
        // 调用 service 获取指定商品
        Product products = productService.getProductById(productId);
        // 返回查询结果
        return Result.success(products);
    }

    // 将商品加入购物车
    @PostMapping("/cart")
    public Result pushToCart(@RequestParam String username, @RequestParam int productId,
                             @RequestParam int num) {
        // 打印接收到的参数，用于调试
        System.out.println("Received username: " + username);
        System.out.println("Received productId: " + productId);
        System.out.println("Received num: " + num);

        // 调用 service 将商品添加到购物车
        productService.pushToCart(username, productId, num);

        // 返回成功信息
        return Result.success("成功加入购物车");
    }

    // 获取指定用户的购物车商品
    @PostMapping("/fetchCartItems")
    public Result fetchCartItems(@RequestParam String username) {
        // 获取用户购物车中的商品
        List<CartItem> products = productService.fetchCartItems(username);
        // 返回购物车商品列表
        return Result.success(products);
    }

    // 删除购物车中的单个商品
    @PostMapping("/deleteOneCartItem")
    public Result deleteCartItem(@RequestParam String username, @RequestParam int productId) {
        // 调用 service 删除指定商品
        productService.deleteOneCartItem(username, productId);

        // 返回删除成功的消息
        return Result.success("删除成功");
    }

    // 清空购物车中的所有商品
    @PostMapping("/deleteAllCartItems")
    public Result deleteAllCartItems(@RequestParam String username) {
        // 调用 service 清空购物车
        productService.deleteAllCartItems(username);

        // 返回清空购物车成功的消息
        return Result.success("清空购物车成功");
    }

    // 根据商品 ID 删除商品
    @PostMapping("/delete")
    public Result deleteProductById(@RequestParam Integer productId) {
        // 调用 service 删除指定商品
        productService.deleteProductById(productId);

        // 返回删除操作的成功消息
        return Result.success();
    }

    // 添加新商品
    @PostMapping("/addProduct")
    public Result addProduct(@RequestBody Product product) {
        // 调用 service 添加新商品
        Product createdProduct = productService.addProduct(product);

        // 返回创建的商品信息
        return Result.success(createdProduct);
    }

    // 修改商品信息
    @PutMapping("/editProduct/{id}")
    public Result editProduct(@PathVariable Integer id, @RequestBody Product product) {
        try {
            // 执行商品更新操作
            Product updatedProduct = productService.updateProduct(id, product);
            // 返回更新后的商品
            return Result.success(updatedProduct);
        } catch (Exception e) {
            // 捕获异常并返回错误信息
            return Result.error("修改失败");
        }
    }


}

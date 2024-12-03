package com.asiryk.shoppingsite.controller;
import com.asiryk.shoppingsite.mapper.UserMapper;
import com.asiryk.shoppingsite.pojo.Order;
import com.asiryk.shoppingsite.pojo.Result;
import com.asiryk.shoppingsite.pojo.User;
import com.asiryk.shoppingsite.service.OrderService;
import com.asiryk.shoppingsite.service.ProductService;
import com.asiryk.shoppingsite.service.UserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*") // 允许所有来源的请求跨域
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private ProductService productService;
    @Autowired
    private UserMapper userMapper;

    // 用户注册
    @PostMapping("/register")
    public Result register(@RequestBody User user) {
        try {
            // 服务层返回一个布尔值，表示是否注册成功
            boolean isRegistered = userService.register(user);
            if (isRegistered) {
                return Result.success();  // 注册成功时返回成功响应
            } else {
                return Result.error("注册失败，已有相同用户名或邮箱"); // 注册失败时返回错误消息
            }
        } catch (Exception e) {
            return Result.error("服务器内部错误");  // 捕获异常时返回统一错误响应
        }
    }

    // 用户登录
    @PostMapping("/login")
    public Result login(@RequestBody User user) {
        String username = user.getUsername();  // 获取用户名
        String password = user.getPassword();  // 获取密码
        String role = user.getRole();  // 获取角色
        try {
            // 服务层返回一个布尔值，表示是否认证成功
            boolean isAuthenticated = userService.login(username, password, role);
            if (isAuthenticated) {
                return Result.success("登录成功");  // 登录成功时返回成功响应
            } else {
                return Result.error("登录失败，账号或密码错误"); // 登录失败时返回错误消息
            }
        } catch (Exception e) {
            return Result.error("服务器内部错误");  // 捕获异常时返回统一错误响应
        }
    }

    // 销售员查询信息（统计订单数、用户数、商品数）
    @GetMapping("/init")
    public Result init() {
        class AdminCount {
            public Integer ordersCount;  // 订单数量
            public Integer usersCount;   // 用户数量
            public Integer productsCount; // 商品数量
        }
        AdminCount adminCount = new AdminCount();
        adminCount.ordersCount = orderService.Count();  // 获取订单数量
        adminCount.usersCount = userService.Count() - 1; // 获取用户数量（减去管理员）
        adminCount.productsCount = productService.Count(); // 获取商品数量
        return Result.success(adminCount);  // 返回统计结果
    }

    // 销售端获取客户详情
    @GetMapping("/page")
    public Result getUsersByPage() {
        List<User> users = userMapper.findAllCustomers();  // 查询所有客户信息
        return Result.success(users);  // 返回客户信息
    }


}


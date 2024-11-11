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
        String username = user.getUsername();
        String password = user.getPassword();
        String role = user.getRole();
        try {
            // 服务层返回一个布尔值，表示是否注册成功
            boolean isAuthenticated = userService.login(username,password, role);
            if (isAuthenticated) {
                return Result.success("登录成功");  // 注册成功时返回成功响应
            } else {
                return Result.error("登录失败，账号或密码错误"); // 注册失败时返回错误消息
            }
        } catch (Exception e) {
            return Result.error("服务器内部错误");  // 捕获异常时返回统一错误响应
        }
    }
    //销售员查询信息
    @GetMapping("/init")
    public Result init() {
        class AdminCount {
            public Integer ordersCount;
            public Integer usersCount;
            public Integer productsCount;
        }
        AdminCount adminCount = new AdminCount();
        adminCount.ordersCount = orderService.Count();
        adminCount.usersCount = userService.Count() - 1;
        adminCount.productsCount = productService.Count();
        return Result.success(adminCount);
    }
    //销售端获取客户详情
    @GetMapping("/page")
    public Result getUsersByPage() {
//        // 设置分页参数
//        PageHelper.startPage(pageNumber, pageSize);

        // 根据搜索条件查询客户
        List<User> users = userMapper.findAllCustomers();

//        // 获取分页后的客户数据
//        PageInfo<User> pageInfo = new PageInfo<>(users);
//        List<Order> orders = orderService.getOrdersByPage(pageNumber, pageSize);
        return Result.success(users);
    }
//
//    // 用户注销
//    @PostMapping("/logout")
//    public ResponseEntity<Void> logout() {
//        userService.logout();
//        return ResponseEntity.ok().build();
//    }
//
//    // 获取所有用户（仅管理员可用）
//    @GetMapping
//    public ResponseEntity<List<User>> getAllUsers() {
//        return ResponseEntity.ok(userService.getAllUsers());
//    }
}


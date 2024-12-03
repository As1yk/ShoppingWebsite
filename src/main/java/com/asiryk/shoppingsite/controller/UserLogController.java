package com.asiryk.shoppingsite.controller;

import com.asiryk.shoppingsite.mapper.ProductMapper;
import com.asiryk.shoppingsite.mapper.UserLogMapper;
import com.asiryk.shoppingsite.mapper.UserMapper;
import com.asiryk.shoppingsite.pojo.Product;
import com.asiryk.shoppingsite.pojo.PurchaseLogRequest;
import com.asiryk.shoppingsite.pojo.Result;
import com.asiryk.shoppingsite.pojo.UserLog;
import com.asiryk.shoppingsite.service.ProductService;
import com.asiryk.shoppingsite.service.UserLogService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/logs")
@CrossOrigin(origins = "*") // 允许所有来源的请求跨域
public class UserLogController {
    @Autowired
    private UserLogService userLogService;
    @Autowired
    private UserLogMapper userLogMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ProductMapper productMapper;

    // 获取日志记录
    @GetMapping
    public Result getLogs() {

        List<UserLog> pageInfo = userLogMapper.findAll(); // 获取日志记录

        // 填充用户名和商品名
        for (UserLog log : pageInfo) {  // 遍历所有日志记录
            log.setUserName(userMapper.getNameById(log.getUserId()));  // 根据 userId 填充用户名
            log.setProductName(productMapper.getNameById(log.getProductId()));  // 根据 productId 填充商品名
        }

        // 返回日志列表和总日志数
        Map<String, Object> result = new HashMap<>();
        result.put("logs", pageInfo);  // 放入日志记录列表
        result.put("totalLogs", pageInfo.size());  // 放入总日志数
        return Result.success(result);  // 返回查询结果
    }

    // 新增浏览记录
    @PostMapping("/view")
    public Result addView(@RequestParam String username, @RequestParam Integer productId ) {
        userLogService.addView(username, productId);  // 调用 service 层保存浏览记录
        return Result.success("成功新增浏览记录");  // 返回成功响应
    }

    // 新增购买记录
    @PostMapping("/purchase")
    public Result addPurchase(@RequestBody PurchaseLogRequest request) {
        String username = request.getUsername();  // 获取用户名
        List<Integer> productIds = request.getProductIds();  // 获取购买的商品 ID 列表
        System.out.println(productIds);  // 打印商品 ID 列表，调试用

        // 处理日志记录逻辑
        userLogService.addPurchase(username, productIds);  // 调用 service 层保存购买记录
        return Result.success("成功新增购买记录");  // 返回成功响应
    }

}

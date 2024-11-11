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

    @GetMapping
    public Result getLogs(
            @RequestParam(defaultValue = "1") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "2020-01-01 20:38:05") String startDate,
            @RequestParam(defaultValue = "2100-01-01 20:38:05") String endDate) {

        // 设置分页
        PageHelper.startPage(pageNumber, pageSize);
        PageInfo<UserLog> pageInfo = userLogMapper.findLogsWithPagination(pageNumber, pageSize, startDate, endDate);

        // 如果没有查询到数据，返回一个提示信息
        if (pageInfo.getList().isEmpty()) {
            System.out.println("error");
            Map<String, Object> response = new HashMap<>();
            response.put("logs", pageInfo.getList());
            response.put("totalLogs", 0);
            return Result.success("没有找到符合条件的日志记录");
        }

        // 填充用户名和商品名
        for (UserLog log : pageInfo.getList()) {
            log.setUserName(userMapper.getNameById(log.getUserId()));  // 填充用户名
            log.setProductName(productMapper.getNameById(log.getProductId()));  // 填充商品名
        }

        Map<String, Object> result = new HashMap<>();
        result.put("logs", pageInfo.getList());
        result.put("totalLogs", pageInfo.getTotal());
        return Result.success(result);
    }

    @PostMapping("/view")
    public Result addView(@RequestParam String username, @RequestParam Integer productId ) {
        userLogService.addView(username, productId);
        return Result.success("成功新增浏览记录");
    }
    @PostMapping("/purchase")
    public Result addPurchase(@RequestBody PurchaseLogRequest request) {
        String username = request.getUsername();
        List<Integer> productIds = request.getProductIds();
        System.out.println(productIds);
        // 处理日志记录逻辑
        userLogService.addPurchase(username,productIds);
        return Result.success("成功新增购买记录");
    }
}

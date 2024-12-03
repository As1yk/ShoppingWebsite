package com.asiryk.shoppingsite.controller;

import com.asiryk.shoppingsite.mapper.OrderMapper;
import com.asiryk.shoppingsite.mapper.ProductMapper;
import com.asiryk.shoppingsite.pojo.*;
import com.asiryk.shoppingsite.service.OrderItemService;
import com.asiryk.shoppingsite.service.OrderService;
import com.asiryk.shoppingsite.service.ProductService;
import com.asiryk.shoppingsite.service.UserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "*") // 允许所有来源的请求跨域
public class OrderController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private UserService userService;
    @Autowired
    private ProductService productService;
    @Autowired
    private OrderItemService orderItemService;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private OrderMapper orderMapper;

    // 创建新订单
    @PostMapping("/add")
    public Result createOrder(@RequestBody CheckoutRequest request) {
        // 查询用户的 user_id
        User user = userService.findByUsername(request.getUsername());
        if (user == null) {
            return Result.error("没有该用户");
        }
        int userId = user.getUserId();
        // 创建订单并插入 orders 表
        Order order = new Order();
        order.setUserId(userId);
        order.setTotalPrice(request.getTotalPrice());
        order.setStatus("已支付，待发货");
        orderService.addOrder(order);
        System.out.println(request.getProducts());

        // 插入每个订单项到 order_items 表
        for (CheckoutRequest.ProductInfo item : request.getProducts()) {
            Product product = productService.getProductByName(item.getProductName());
            OrderItem orderItem = new OrderItem();
            orderItem.setOrderId(order.getOrderId());
            orderItem.setProductId(product.getProductId());
            orderItem.setQuantity(item.getQuantity());
            orderItem.setPrice(item.getPrice());
            orderItemService.addOrderItem(orderItem);
        }

        // 在支付成功后从购物车中移除该商品
        productService.deleteAllCartItems(user.getUsername());
        return Result.success("订单创建成功");
    }

    // 获取用户订单历史
    @GetMapping("/get")
    public Result getUserOrders(@RequestParam String username) {
        List<Order> orders = orderService.getUserOrders(username);
        return Result.success(orders);
    }

    //获取单个订单详情
    @GetMapping("/detail")
    public Result getOrderDetail(@RequestParam Integer orderId) {
        List<OrderItem> orderItems = orderItemService.getOrderDetail(orderId);
        // 查询商品名，假设你有一个 ProductService 或者直接通过数据库查询
        for (OrderItem orderItem : orderItems) {
            Product product = productService.getProductById(orderItem.getProductId());
            if (product != null) {
                orderItem.setProductName(product.getName()); // 设置商品名
            }
        }
        return Result.success(orderItems);
    }

    //客户确认收货
    @PostMapping("/confirmReceipt")
    public Result confirmReceipt(@RequestParam Integer orderId) {
        orderService.confirmReceipt(orderId);
        return Result.success();
    }

    //销售端获取订单详情
    @GetMapping("/page")
    public Result getOrdersByPage() {
        List<Order> orders = orderMapper.findAll();
        return Result.success(orders);
    }

    //销售端发货
    @PostMapping("/ship")
    public Result shipOrder(@RequestParam int orderId) {
        try {
            boolean success = orderService.shipOrder(orderId);
            if (success) {
                return Result.success("订单发货成功");
            } else {
                return Result.error("库存不足，发货失败");
            }
        } catch (Exception e) {
            return Result.error("发货失败");
        }
    }
    //销售删除订单
    @PostMapping("/delete")
    public Result deleteOrder(@RequestParam Integer orderId) {
        orderService.deleteOrderByid(orderId);
        return Result.success("已成功删除订单："+orderId);
    }
    //销售统计报表
    @GetMapping("/sales")
    public Result getSales() {
        // 获取所有交易完成的订单
        List<Order> orders = orderService.getOrderByStatus("交易完成");
        // 使用商品ID作为键，存储销售数据
        Map<Integer, Sale> salesMap = new HashMap<>();
        List<Sale> salesList = new ArrayList<>();
        // 获取订单项
        for (Order order : orders) {
            List<OrderItem> orderItems = orderItemService.getOrderItemsByOrderId(order.getOrderId());
            for (OrderItem orderItem : orderItems) {
                int productId = orderItem.getProductId();
                String productName = productMapper.getNameById(productId);
                BigDecimal productPrice = orderItem.getPrice();
                int quantity = orderItem.getQuantity();
                BigDecimal totalPrice = productPrice.multiply(BigDecimal.valueOf(quantity));

                // 如果 salesMap 中已经有该商品，则更新销售数量和销售额
                if (salesMap.containsKey(productId)) {
                    Sale existingSale = salesMap.get(productId);
                    existingSale.setProductQuantity(existingSale.getProductQuantity() + quantity);
                    existingSale.setProductTotalPrice(existingSale
                                                        .getProductTotalPrice()
                                                        .add(totalPrice));
                } else {
                    // 如果没有该商品，则创建新的 Sale 对象并加入 salesMap
                    Sale newSale = new Sale();
                    newSale.setProductName(productName);
                    newSale.setProductPrice(productPrice);
                    newSale.setProductQuantity(quantity);
                    newSale.setProductTotalPrice(totalPrice);
                    salesMap.put(productId, newSale);
                }
            }
        }
        // 将所有销售记录从 salesMap 转换为列表
        salesList.addAll(salesMap.values());
        return Result.success(salesList);
    }

}


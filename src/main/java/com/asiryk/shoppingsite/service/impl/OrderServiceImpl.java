package com.asiryk.shoppingsite.service.impl;

import com.asiryk.shoppingsite.mapper.OrderItemMapper;
import com.asiryk.shoppingsite.mapper.OrderMapper;
import com.asiryk.shoppingsite.mapper.ProductMapper;
import com.asiryk.shoppingsite.pojo.Order;
import com.asiryk.shoppingsite.pojo.OrderItem;
import com.asiryk.shoppingsite.pojo.Product;
import com.asiryk.shoppingsite.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderItemMapper orderItemMapper;
    @Autowired
    private ProductMapper productMapper;
    @Override
    public void addOrder(Order order) {
        orderMapper.insertOrder(order);
    }
    @Override
    public List<Order> getUserOrders(String username){
        return orderMapper.findByUserName(username);
    }
    @Override
    public void confirmReceipt(Integer orderId){
        orderMapper.updateStatus(orderId,"交易完成");
    }
    @Override
    public Integer Count(){
        return orderMapper.Count();
    };
    @Override
    public List<Order> getOrdersByPage(int pageNumber, int pageSize) {
        int offset = (pageNumber - 1) * pageSize;
        return orderMapper.selectOrdersByPage(pageSize, offset);
    }

    @Override
    public boolean shipOrder(int orderId) {
        // 获取订单项信息
        List<OrderItem> orderItems = orderItemMapper.findByOrderId(orderId);

        // 检查库存是否足够
        for (OrderItem item : orderItems) {
            Product product = productMapper.findByid(item.getProductId());
            if (product.getStock() < item.getQuantity()) {
                return false; // 库存不足，发货失败
            }
        }

        // 更新库存
        for (OrderItem item : orderItems) {
            productMapper.decreaseStock(item.getProductId(), item.getQuantity());
        }
        // 更新订单状态为 'shipped'
        orderMapper.updateStatus(orderId, "已发货，待确认收货");
        return true;
    }
    @Override
    public void deleteOrderByid(Integer orderId){
        orderMapper.deleteByid(orderId);
    }
    @Override
    public List<Order> getOrderByStatus(String status) {
        return orderMapper.selectOrdersByStatus(status);

    }
    @Override
    public List<Order> searchOrders(String search) {
        // 如果没有搜索条件，返回所有订单
        if (search == null || search.trim().isEmpty()) {
            return orderMapper.findAll();
        } else {
            // 根据订单ID和状态进行模糊查询
            return orderMapper.searchOrders("%" + search + "%");
        }
    }
//
//    @Override
//    public List<Order> getUserOrders(Integer userId) {
//        return orderMapper.findByUserId(userId);
//    }
//
//    @Override
//    public Order updateOrderStatus(Integer orderId, OrderStatusRequest statusRequest) {
//        orderMapper.updateOrderStatus(orderId, statusRequest.getStatus());
//        return orderMapper.findById(orderId); // 返回更新后的订单
//    }
}


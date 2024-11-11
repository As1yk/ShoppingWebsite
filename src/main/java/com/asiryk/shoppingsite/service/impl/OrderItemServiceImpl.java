package com.asiryk.shoppingsite.service.impl;

import com.asiryk.shoppingsite.mapper.OrderItemMapper;
import com.asiryk.shoppingsite.pojo.OrderItem;
import com.asiryk.shoppingsite.service.OrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderItemServiceImpl implements OrderItemService {

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Override
    public List<OrderItem> getOrderItemsByOrderId(Integer orderId) {
        return orderItemMapper.findByOrderId(orderId);
    }

    @Override
    public void addOrderItem(OrderItem orderItem) {
        orderItemMapper.insertOrderItem(orderItem);
    }

    @Override
    public void deleteOrderItem(Integer orderItemId) {
        orderItemMapper.deleteOrderItem(orderItemId);
    }

    @Override
    public List<OrderItem> getOrderDetail(int orderId){
        return orderItemMapper.findByOrderId(orderId);
    }
}


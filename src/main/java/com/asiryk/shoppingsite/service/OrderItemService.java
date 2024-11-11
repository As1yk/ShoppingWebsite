package com.asiryk.shoppingsite.service;

import com.asiryk.shoppingsite.pojo.OrderItem;
import com.asiryk.shoppingsite.pojo.Product;

import java.util.List;

public interface OrderItemService {

    List<OrderItem> getOrderItemsByOrderId(Integer orderId);

    void addOrderItem(OrderItem orderItem);

    void deleteOrderItem(Integer orderItemId);
    List<OrderItem> getOrderDetail(int orderId);

}


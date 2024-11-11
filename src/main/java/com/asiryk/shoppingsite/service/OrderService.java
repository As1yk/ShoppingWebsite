package com.asiryk.shoppingsite.service;

import com.asiryk.shoppingsite.pojo.Order;
import com.asiryk.shoppingsite.pojo.OrderItem;

import java.util.List;

public interface OrderService {

    void addOrder(Order order);
    List<Order> getUserOrders(String username);
    void confirmReceipt(Integer orderId);
    Integer Count();
    List<Order> getOrdersByPage(int pageNumber, int pageSize);
    boolean shipOrder(int orderId);
    void deleteOrderByid(Integer orderId);
    List<Order> getOrderByStatus(String status);
    List<Order> searchOrders(String search);
//
//    List<Order> getUserOrders(Integer userId);
//
//    Order updateOrderStatus(Integer orderId, OrderStatusRequest statusRequest);
}


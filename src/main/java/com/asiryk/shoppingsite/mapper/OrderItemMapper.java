package com.asiryk.shoppingsite.mapper;

import com.asiryk.shoppingsite.pojo.OrderItem;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface OrderItemMapper {

    @Insert("INSERT INTO order_items (order_id, product_id, quantity,price) VALUES (#{orderId}, #{productId}, #{quantity},#{price})")
    void insertOrderItem(OrderItem orderItem);

    @Select("SELECT * FROM order_items WHERE order_id = #{orderId}")
    List<OrderItem> findByOrderId(Integer orderId);

    @Delete("DELETE FROM order_items WHERE order_item_id = #{orderItemId}")
    void deleteOrderItem(Integer orderItemId);

}


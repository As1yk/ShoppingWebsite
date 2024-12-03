package com.asiryk.shoppingsite.mapper;

import com.asiryk.shoppingsite.pojo.Order;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface OrderMapper {

    @Insert("INSERT INTO orders (user_id, status, total_price, created_at) VALUES (#{userId}, #{status}, #{totalPrice}, NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "orderId")  // 自动填充生成的主键到 orderId
    void insertOrder(Order order);

    @Select("SELECT * FROM orders WHERE order_id = #{orderId}")
    Order findById(Integer orderId);

    @Select("SELECT * FROM orders WHERE user_id = #{userId}")
    List<Order> findByUserId(Integer userId);

    @Select("SELECT * FROM orders WHERE user_id = (SELECT user_id FROM users WHERE username = #{username})")
    List<Order> findByUserName(String username);

    @Select("select count(*) from orders")
    Integer Count();

    @Select("  SELECT * FROM orders\n" +
            "  LIMIT #{pageSize} OFFSET #{offset}")
    List<Order> selectOrdersByPage(int pageSize, int offset);

    @Update("UPDATE orders SET status = #{status} WHERE order_id = #{orderId}")
    void updateStatus(int orderId, String status);

    @Delete("DELETE from orders where order_id = #{orderId}")
    void deleteByid(Integer orderId);

    @Select("SELECT * from orders where status = #{status}")
    List<Order> selectOrdersByStatus(String status);

    @Select("select * from orders")
    List<Order> findAll();

    @Select("SELECT * FROM orders\n" +
            "WHERE order_id LIKE #{search}")
    List<Order> searchOrders(String search);
}


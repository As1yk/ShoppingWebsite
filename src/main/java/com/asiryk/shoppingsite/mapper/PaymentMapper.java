package com.asiryk.shoppingsite.mapper;

import com.asiryk.shoppingsite.pojo.Payment;
import org.apache.ibatis.annotations.*;

@Mapper
public interface PaymentMapper {

    @Insert("INSERT INTO payments (order_id, amount, status) VALUES (#{order.orderId}, #{amount}, #{status})")
    void insertPayment(Payment payment);

    @Select("SELECT * FROM payments WHERE order_id = #{orderId}")
    Payment findByOrderId(Integer orderId);

    @Delete("DELETE FROM payments WHERE payment_id = #{paymentId}")
    void deletePayment(Integer paymentId);
}


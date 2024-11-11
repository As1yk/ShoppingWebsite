package com.asiryk.shoppingsite.pojo;
import lombok.*;

import java.math.BigDecimal;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {

    private Integer orderItemId;

    private Integer orderId;

    private Integer productId;
    private String productName; // 商品名

    private Integer quantity;

    private BigDecimal price;
}


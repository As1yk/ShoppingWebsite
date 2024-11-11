package com.asiryk.shoppingsite.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CheckoutRequest {
    private String username;
    private BigDecimal totalPrice;
    private List<ProductInfo> products;

    // 内部类或独立类定义每个商品的信息
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductInfo {
        private String productName;
        private int quantity;
        private BigDecimal price;
    }

}


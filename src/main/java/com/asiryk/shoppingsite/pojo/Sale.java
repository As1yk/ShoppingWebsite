package com.asiryk.shoppingsite.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Sale {
    private String productName;
    private BigDecimal productPrice;
    private int productQuantity;
    private BigDecimal productTotalPrice;
}

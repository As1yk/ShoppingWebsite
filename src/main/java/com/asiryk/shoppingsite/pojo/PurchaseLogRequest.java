package com.asiryk.shoppingsite.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseLogRequest {
    private String username;
    private List<Integer> productIds;
}

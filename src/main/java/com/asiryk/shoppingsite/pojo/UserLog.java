package com.asiryk.shoppingsite.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserLog {
    private Integer logId;
    private Integer userId;
    private String userName;
    private Integer productId;
    private String productName;
    private String actionType;
    private Date activityDate;
}

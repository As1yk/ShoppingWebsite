package com.asiryk.shoppingsite.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class User {
    private Integer userId;
    private String username;
    private String email;
    private String password;
    private String role;
    private Date createdAt;
}

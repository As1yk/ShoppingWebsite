package com.asiryk.shoppingsite.service;

import com.asiryk.shoppingsite.pojo.Order;
import com.asiryk.shoppingsite.pojo.User;

import java.util.List;

public interface UserService {

    boolean register(User user);
    boolean login(String username, String password, String role);
    User findByUsername(String username);
    Integer Count();
    List<User> searchUsers(String search);
}

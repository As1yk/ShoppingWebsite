package com.asiryk.shoppingsite.service.impl;

import com.asiryk.shoppingsite.mapper.UserMapper;
import com.asiryk.shoppingsite.pojo.User;
import com.asiryk.shoppingsite.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public boolean register(User user) {
        int count = userMapper.checkUserExists(user.getUsername(), user.getEmail());
        if (count > 0) {
            return false;
        }
        userMapper.insertUser(user);
        return true;
    }
    @Override
    public boolean login(String username, String password, String role) {

        int count = userMapper.checkLogin(username, password, role);
        if (count > 0) {
            return true;
        }
        return false;
    }

    @Override
    public User findByUsername(String username){
        return userMapper.findByUsername(username);
    }

    @Override
    public Integer Count(){
        return userMapper.Count();
    }
    @Override
    public List<User> searchUsers(String search){
        // 如果没有搜索条件，返回所有客户
        if (search == null || search.trim().isEmpty()) {
            return userMapper.findAllCustomers();
        } else {
            // 根据客户名称进行模糊查询
            return userMapper.searchCustomers("%" + search + "%");
        }
    }
}




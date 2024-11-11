package com.asiryk.shoppingsite.service.impl;

import com.asiryk.shoppingsite.mapper.ProductMapper;
import com.asiryk.shoppingsite.mapper.UserLogMapper;
import com.asiryk.shoppingsite.mapper.UserMapper;
import com.asiryk.shoppingsite.pojo.Product;
import com.asiryk.shoppingsite.pojo.User;
import com.asiryk.shoppingsite.pojo.UserLog;
import com.asiryk.shoppingsite.service.UserLogService;
import com.github.pagehelper.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserLogServiceImpl implements UserLogService {
    @Autowired
    private UserLogMapper userLogMapper;

    @Autowired
    private UserMapper userMapper;
    @Override
    public void addView(String username, Integer productId) {

        User user = userMapper.findByUsername(username);
        userLogMapper.insertView(user.getUserId(),productId);
    }

    @Override
    public void addPurchase(String username, List<Integer> productIds){
        // 获取用户ID
        User user = userMapper.findByUsername(username);
        int userId = user.getUserId();

        // 遍历每个商品ID，插入购买日志
        for (Integer productId : productIds) {
            userLogMapper.insertPurchase(userId, productId);
        }
    }
//    @Override
//    public Page<UserLog> getLogs(int pageNumber, int pageSize, String startDate, String endDate) {
//        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize); // pageNumber 从 0 开始
//        return userLogMapper.findLogs(startDate, endDate, pageable);
//    }
}

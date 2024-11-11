package com.asiryk.shoppingsite.service;


import com.asiryk.shoppingsite.pojo.Product;
import com.asiryk.shoppingsite.pojo.UserLog;
import com.github.pagehelper.Page;

import java.util.List;

public interface UserLogService {
    void addView(String username, Integer productId);
    void addPurchase(String username, List<Integer> productIds);
//    Page<UserLog> getLogs(int pageNumber, int pageSize, String startDate, String endDate);
}

package com.asiryk.shoppingsite;

import com.asiryk.shoppingsite.controller.UserController;
import com.asiryk.shoppingsite.mapper.UserMapper;
import com.asiryk.shoppingsite.pojo.User;
import com.asiryk.shoppingsite.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import java.util.List;

@SpringBootTest
class ShoppingSiteApplicationTests {

    @Autowired
    private UserController userController;
    private User user = new User();

    @Test
    public void testListUser() {

    }

}

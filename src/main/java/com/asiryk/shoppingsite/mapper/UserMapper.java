package com.asiryk.shoppingsite.mapper;

import com.asiryk.shoppingsite.pojo.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper {

    @Select("SELECT COUNT(*) FROM users WHERE username = #{username} OR email = #{email}")
    int checkUserExists(String username, String email);

    @Insert("INSERT INTO users (username, email, password, role, created_at) VALUES (#{username}, #{email}, #{password},'CUSTOMER', NOW())")
    void insertUser(User user);

    @Select("SELECT COUNT(*) FROM users WHERE username = #{username} AND password = #{password} AND role = #{role}" )
    int checkLogin(String username, String password, String role);

    @Select("SELECT * FROM users")
    List<User> findAll();

    @Select("SELECT * FROM users WHERE username = #{username}")
    User findByUsername(String username);

    @Update("UPDATE users SET password = #{password} WHERE user_id = #{userId}")
    void updatePassword(@Param("userId") Integer userId, @Param("password") String password);

    @Delete("DELETE FROM users WHERE user_id = #{userId}")
    void deleteUser(Integer userId);

    @Select("select count(*) from users")
    Integer Count();

    @Select("select * from users where role != 'admin'")
    List<User> findAllCustomers();

    @Select("SELECT * FROM users\n" +
            "WHERE users.username LIKE #{search} OR users.user_id = #{search} AND role != 'admin'")
    List<User> searchCustomers(String search);

    @Select("SELECT username from users where user_id = #{user_id}")
    String getNameById(Integer userId);
}


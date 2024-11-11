package com.asiryk.shoppingsite.mapper;

import com.asiryk.shoppingsite.pojo.CartItem;
import com.asiryk.shoppingsite.pojo.Product;
import com.asiryk.shoppingsite.pojo.User;
import org.apache.ibatis.annotations.*;

import java.sql.Array;
import java.util.Arrays;
import java.util.List;

@Mapper
public interface ProductMapper {

//    @Insert("INSERT INTO products (name, price, stock) VALUES (#{name}, #{price}, #{stock})")
//    void insertProduct(Product product);
//
//    @Select("SELECT * FROM products WHERE product_id = #{productId}")
//    Product findById(Integer productId);

    @Select("SELECT * FROM products")
    List<Product> findAll();

    @Select("SELECT * FROM products where product_id = #{productId}")
    Product findByid(int productId);

    @Select("SELECT * FROM products where name = #{name}")
    Product findByName(String name);

    @Insert("INSERT INTO cart (user_id, product_id, quantity)\n" +
            "VALUES (\n" +
            "    (SELECT user_id FROM users WHERE username = #{username}),  -- 获取user_id\n" +
            "    #{productId},                                              -- 商品ID\n" +
            "    #{num}                                                    -- 商品数量\n" +
            ")\n" +
            "ON DUPLICATE KEY UPDATE\n" +
            "    quantity = quantity + VALUES(quantity)               -- 如果商品已存在，增加数量\n")
    void pushToCart(String username, int productId, int num);

    @Select("SELECT \n" +
            "    p.product_id AS productId,\n"+
            "    p.name AS name,\n" +
            "    p.price AS price,\n" +
            "    c.quantity AS quantity\n" +
            "FROM \n" +
            "    cart c\n" +
            "JOIN \n" +
            "    users u ON c.user_id = u.user_id\n" +
            "JOIN \n" +
            "    products p ON c.product_id = p.product_id\n" +
            "WHERE \n" +
            "    u.username = #{username}; \n")
    List<CartItem> fetchCartItems(String username);

    @Delete("DELETE FROM cart WHERE user_id = (SELECT user_id FROM users WHERE username = #{username}) AND product_id = #{productId}")
    void deleteOneCartItem(String username, int productId);

    @Delete("DELETE from cart where user_id = (select user_id from users where username = #{username})")
    void deleteAllCartItems(String username);

    @Select("select count(*) from products")
    Integer Count();

    @Update("UPDATE products SET stock = stock - #{quantity} WHERE product_id = #{productId}")
    void decreaseStock(int productId, int quantity);

    @Delete("DELETE FROM products where product_id = #{productId}")
    void deleteProductById(Integer productId);

    @Insert(" INSERT INTO products (name, price, stock, image_url, location)\n" +
            "    VALUES (#{name}, #{price}, #{stock}, #{imageUrl}, #{location})")
    @Options(useGeneratedKeys = true, keyProperty = "productId")  // 自动填充生成的主键到 orderId
    void insertProduct(Product product);

    @Update("UPDATE products SET name = #{name}, price = #{price}, stock = #{stock}, image_url = #{imageUrl}, location = #{location}, WHERE product_id = #{productId}")
    void updateProduct(Product product);

    @Select("SELECT name from products where product_id = #{productId}")
    String getNameById(int productId);

    @Select("SELECT * FROM products\n" +
            "WHERE name LIKE #{search}")
    List<Product> searchProducts(String search);
//    @Update("UPDATE products SET name = #{name}, price = #{price}, stock = #{stock} WHERE product_id = #{productId}")
//    void updateProduct(Product product);
//
//    @Delete("DELETE FROM products WHERE product_id = #{productId}")
//    void deleteProduct(Integer productId);
}


package com.asiryk.shoppingsite.service;

import com.asiryk.shoppingsite.pojo.CartItem;
import com.asiryk.shoppingsite.pojo.Product;
import com.asiryk.shoppingsite.pojo.User;

import java.lang.reflect.Array;
import java.util.List;

public interface ProductService {

//    Product addProduct(Product product);
        List<Product> getAllProducts();
        Product getProductById(int productId);
        Product getProductByName(String name);
        void pushToCart(String username, int productId, int num);
        List<CartItem> fetchCartItems(String username);
        void deleteOneCartItem(String username, int productId);
        void deleteAllCartItems(String username);
        Integer Count();
        void deleteProductById(Integer productId);
        Product addProduct(Product product) ;
        Product updateProduct(Integer id, Product product);
        List<Product> searchProducts(String search);
//    Product updateProduct(Integer id, Product product);
//
//    void deleteProduct(Integer id);
}


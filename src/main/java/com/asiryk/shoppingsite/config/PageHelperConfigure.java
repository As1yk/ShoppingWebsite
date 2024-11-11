//package com.asiryk.shoppingsite.config;
//
//import com.github.pagehelper.PageInterceptor;
//import org.apache.ibatis.session.SqlSessionFactory;
//import org.apache.ibatis.plugin.Interceptor;
//import org.apache.ibatis.session.SqlSessionFactoryBuilder;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.annotation.Order;
//
//import javax.sql.DataSource;
//
//@Configuration
//public class PageHelperConfigure {
//
//    @Bean
//    public Interceptor[] plugins() {
//        return new Interceptor[]{new PageInterceptor()};
//    }
//}
//package com.asiryk.shoppingsite.config;
//
//import com.github.pagehelper.PageInterceptor;
//import org.apache.ibatis.session.SqlSessionFactory;
//import org.apache.ibatis.session.SqlSessionFactoryBuilder;
//import org.apache.ibatis.plugin.Interceptor;
//import org.mybatis.spring.SqlSessionFactoryBean;
//import org.mybatis.spring.annotation.MapperScan;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.annotation.Order;
//
//import javax.sql.DataSource;
//
//@Configuration
//@MapperScan(basePackages = "com.asiryk.shoppingsite.mapper")  // 扫描 Mapper
//public class MyBatisConfig {
//
//    @Bean
//    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
//        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
//        factoryBean.setDataSource(dataSource);
//        factoryBean.setPlugins(new Interceptor[]{new PageInterceptor()});  // 配置分页插件
//        return factoryBean.getObject();
//    }
//}

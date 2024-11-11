package com.asiryk.shoppingsite.mapper;

import com.asiryk.shoppingsite.pojo.Product;
import com.asiryk.shoppingsite.pojo.UserLog;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface UserLogMapper {
    @Insert("INSERT INTO user_activity_logs (user_id, product_id, action_type, activity_date) " +
            "VALUES (#{userId}, #{productId}, '浏览', NOW())")
    void insertView(int userId, Integer productId);

    @Insert("INSERT INTO user_activity_logs (user_id, product_id, action_type, activity_date) " +
            "VALUES (#{userId}, #{productId}, '购买', NOW())")
    void insertPurchase(int userId, Integer productId);

    @Select("SELECT * FROM user_activity_logs\n" +
            "WHERE activity_date BETWEEN #{startDate} AND #{endDate}\n" +
            "ORDER BY activity_date DESC\n" )
    Page<UserLog> findByDateRange(@Param("startDate") LocalDateTime startDate,
                                  @Param("endDate") LocalDateTime endDate,
                                  Pageable pageable);

    @Select("SELECT * FROM user_activity_logs ORDER BY activity_date DESC")
    Page<UserLog> findAll(Pageable pageable);

    // 分页查询日志，支持过滤日期范围
// Mapper接口
    @Select("SELECT * FROM user_activity_logs WHERE activity_date BETWEEN #{startDate} AND #{endDate} ORDER BY activity_date DESC")
    List<UserLog> findLogs(@Param("startDate") String startDate,
                           @Param("endDate") String endDate);

    default PageInfo<UserLog> findLogsWithPagination(int pageNumber, int pageSize, String startDate, String endDate) {
        System.out.println("Page Number: " + pageNumber + ", Page Size: " + pageSize);  // 调试输出
        // 启动分页，pageNumber 从1开始，pageSize是每页显示的记录数
        PageHelper.startPage(pageNumber, pageSize);

        // 调用原始查询方法，获取数据
        List<UserLog> logs = findLogs(startDate, endDate);

        // 将查询结果封装为PageInfo对象，提供分页信息
        return new PageInfo<>(logs);
    }
}

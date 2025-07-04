package com.example.backend.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Slf4j
@Component
public class DatabaseTestUtil {

    @Autowired
    private DataSource dataSource;

    /**
     * 测试数据库连接
     */
    public boolean testConnection() {
        try (Connection connection = dataSource.getConnection()) {
            log.info("数据库连接成功！");
            log.info("数据库URL: {}", connection.getMetaData().getURL());
            log.info("数据库产品名称: {}", connection.getMetaData().getDatabaseProductName());
            log.info("数据库版本: {}", connection.getMetaData().getDatabaseProductVersion());
            return true;
        } catch (SQLException e) {
            log.error("数据库连接失败: {}", e.getMessage());
            return false;
        }
    }

    /**
     * 检查数据库表是否存在
     */
    public boolean checkTables() {
        try (Connection connection = dataSource.getConnection()) {
            String[] tables = {"user", "fridge", "food", "food_consumption"};
            for (String table : tables) {
                try {
                    connection.createStatement().executeQuery("SELECT 1 FROM " + table + " LIMIT 1");
                    log.info("表 {} 存在", table);
                } catch (SQLException e) {
                    log.error("表 {} 不存在或无法访问: {}", table, e.getMessage());
                    return false;
                }
            }
            return true;
        } catch (SQLException e) {
            log.error("检查数据库表失败: {}", e.getMessage());
            return false;
        }
    }
} 
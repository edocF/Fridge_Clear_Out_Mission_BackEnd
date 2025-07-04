package com.example.backend.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Slf4j
@Component
public class DatabaseDiagnosticUtil {

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    @Value("${spring.datasource.driver-class-name}")
    private String driverClassName;

    /**
     * 诊断数据库连接问题
     */
    public void diagnoseConnection() {
        log.info("=== 数据库连接诊断开始 ===");
        log.info("数据库URL: {}", url);
        log.info("用户名: {}", username);
        log.info("驱动类: {}", driverClassName);
        log.info("密码: {}", password != null ? "***已设置***" : "***未设置***");

        // 1. 检查驱动
        checkDriver();

        // 2. 测试连接
        testDirectConnection();

        // 3. 检查数据库是否存在
        checkDatabaseExists();

        log.info("=== 数据库连接诊断完成 ===");
    }

    /**
     * 检查JDBC驱动
     */
    private void checkDriver() {
        try {
            Class.forName(driverClassName);
            log.info("✅ JDBC驱动加载成功: {}", driverClassName);
        } catch (ClassNotFoundException e) {
            log.error("❌ JDBC驱动加载失败: {}", e.getMessage());
        }
    }

    /**
     * 测试直接连接
     */
    private void testDirectConnection() {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            log.info("✅ 数据库连接成功！");
            log.info("数据库产品: {}", connection.getMetaData().getDatabaseProductName());
            log.info("数据库版本: {}", connection.getMetaData().getDatabaseProductVersion());
            log.info("连接URL: {}", connection.getMetaData().getURL());
        } catch (SQLException e) {
            log.error("❌ 数据库连接失败: {}", e.getMessage());
            log.error("错误代码: {}", e.getErrorCode());
            log.error("SQL状态: {}", e.getSQLState());
            
            // 提供具体的解决建议
            provideSolution(e);
        }
    }

    /**
     * 检查数据库是否存在
     */
    private void checkDatabaseExists() {
        // 从URL中提取数据库名
        String dbName = extractDatabaseName(url);
        if (dbName != null) {
            log.info("尝试检查数据库: {}", dbName);
            
            // 尝试连接到MySQL服务器（不指定数据库）
            String serverUrl = url.replace("/" + dbName, "");
            try (Connection connection = DriverManager.getConnection(serverUrl, username, password)) {
                log.info("✅ MySQL服务器连接成功");
                
                // 检查数据库是否存在
                try (var stmt = connection.createStatement();
                     var rs = stmt.executeQuery("SHOW DATABASES LIKE '" + dbName + "'")) {
                    if (rs.next()) {
                        log.info("✅ 数据库 {} 存在", dbName);
                    } else {
                        log.error("❌ 数据库 {} 不存在", dbName);
                        log.info("💡 解决方案: 创建数据库 {}", dbName);
                        log.info("SQL: CREATE DATABASE {} CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;", dbName);
                    }
                }
            } catch (SQLException e) {
                log.error("❌ 无法连接到MySQL服务器: {}", e.getMessage());
            }
        }
    }

    /**
     * 从URL中提取数据库名
     */
    private String extractDatabaseName(String url) {
        try {
            // 格式: jdbc:mysql://localhost:3306/database_name
            String[] parts = url.split("/");
            if (parts.length > 0) {
                String lastPart = parts[parts.length - 1];
                // 移除查询参数
                return lastPart.split("\\?")[0];
            }
        } catch (Exception e) {
            log.warn("无法从URL中提取数据库名: {}", url);
        }
        return null;
    }

    /**
     * 根据错误提供解决建议
     */
    private void provideSolution(SQLException e) {
        log.info("=== 解决建议 ===");
        
        if (e.getErrorCode() == 1045) {
            log.info("🔧 错误1045: 用户名或密码错误");
            log.info("解决方案:");
            log.info("1. 检查application.yml中的用户名和密码");
            log.info("2. 确认MySQL root用户密码");
            log.info("3. 尝试重置MySQL密码");
        } else if (e.getErrorCode() == 1049) {
            log.info("🔧 错误1049: 数据库不存在");
            log.info("解决方案:");
            log.info("1. 创建数据库: CREATE DATABASE fridge_missiondb;");
            log.info("2. 执行初始化脚本: database_init.sql");
        } else if (e.getErrorCode() == 2003) {
            log.info("🔧 错误2003: 无法连接到MySQL服务器");
            log.info("解决方案:");
            log.info("1. 检查MySQL服务是否启动");
            log.info("2. 检查端口3306是否被占用");
            log.info("3. 检查防火墙设置");
        } else {
            log.info("🔧 其他错误，请检查:");
            log.info("1. MySQL服务状态");
            log.info("2. 网络连接");
            log.info("3. 数据库配置");
        }
    }
} 
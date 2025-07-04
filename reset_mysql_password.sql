-- MySQL密码重置脚本
-- 注意：此脚本需要在MySQL命令行中执行，且需要管理员权限

-- 方法1：重置root用户密码（推荐）
-- 1. 停止MySQL服务
-- 2. 以安全模式启动MySQL
-- 3. 执行以下SQL：

-- 重置root密码为 'fht041029'
ALTER USER 'root'@'localhost' IDENTIFIED BY 'fht041029';

-- 刷新权限
FLUSH PRIVILEGES;

-- 验证密码
SELECT User, Host FROM mysql.user WHERE User = 'root';

-- 方法2：如果方法1不行，可以尝试以下方法：

-- 删除root用户并重新创建
-- DROP USER 'root'@'localhost';
-- CREATE USER 'root'@'localhost' IDENTIFIED BY 'fht041029';
-- GRANT ALL PRIVILEGES ON *.* TO 'root'@'localhost' WITH GRANT OPTION;
-- FLUSH PRIVILEGES;

-- 方法3：创建新的管理员用户
-- CREATE USER 'admin'@'localhost' IDENTIFIED BY 'fht041029';
-- GRANT ALL PRIVILEGES ON *.* TO 'admin'@'localhost' WITH GRANT OPTION;
-- FLUSH PRIVILEGES;

-- 验证连接
-- mysql -u root -p
-- 输入密码: fht041029 
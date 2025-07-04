-- 测试用户名更新功能
USE fridge_missiondb;

-- 查看当前用户表结构
DESCRIBE user;

-- 查看当前用户数据
SELECT id, username, email FROM user;

-- 测试更新用户名（假设用户ID为1）
UPDATE user SET username = 'newtestuser' WHERE id = 1;

-- 验证更新结果
SELECT id, username, email FROM user WHERE id = 1;

-- 测试用户名唯一性约束
-- 尝试插入重复用户名（应该失败）
INSERT INTO user (username, email, password) VALUES ('newtestuser', 'test2@example.com', 'password');

-- 恢复原始用户名
UPDATE user SET username = 'testuser' WHERE id = 1; 
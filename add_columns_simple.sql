-- 简单的添加字段脚本
USE fridge_missiondb;

-- 添加字段（如果字段已存在会报错，但不会影响程序运行）
ALTER TABLE `user` ADD COLUMN `dietary_taboos` json DEFAULT NULL;
ALTER TABLE `user` ADD COLUMN `dietary_preferences` varchar(255) DEFAULT NULL;
ALTER TABLE `user` ADD COLUMN `seasoning_preferences` varchar(255) DEFAULT NULL;
ALTER TABLE `user` ADD COLUMN `image` varchar(255) DEFAULT NULL;

-- 显示表结构
DESCRIBE `user`; 
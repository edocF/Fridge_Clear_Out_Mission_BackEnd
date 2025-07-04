-- 添加缺失的字段到user表
USE fridge_missiondb;

-- 添加dietary_taboos字段（如果不存在）
SET @sql = (SELECT IF(
    (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS 
     WHERE TABLE_SCHEMA = 'fridge_missiondb' 
     AND TABLE_NAME = 'user' 
     AND COLUMN_NAME = 'dietary_taboos') = 0,
    'ALTER TABLE `user` ADD COLUMN `dietary_taboos` json DEFAULT NULL',
    'SELECT "dietary_taboos column already exists"'
));
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- 添加dietary_preferences字段（如果不存在）
SET @sql = (SELECT IF(
    (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS 
     WHERE TABLE_SCHEMA = 'fridge_missiondb' 
     AND TABLE_NAME = 'user' 
     AND COLUMN_NAME = 'dietary_preferences') = 0,
    'ALTER TABLE `user` ADD COLUMN `dietary_preferences` varchar(255) DEFAULT NULL',
    'SELECT "dietary_preferences column already exists"'
));
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- 添加seasoning_preferences字段（如果不存在）
SET @sql = (SELECT IF(
    (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS 
     WHERE TABLE_SCHEMA = 'fridge_missiondb' 
     AND TABLE_NAME = 'user' 
     AND COLUMN_NAME = 'seasoning_preferences') = 0,
    'ALTER TABLE `user` ADD COLUMN `seasoning_preferences` varchar(255) DEFAULT NULL',
    'SELECT "seasoning_preferences column already exists"'
));
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- 添加image字段（如果不存在）
SET @sql = (SELECT IF(
    (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS 
     WHERE TABLE_SCHEMA = 'fridge_missiondb' 
     AND TABLE_NAME = 'user' 
     AND COLUMN_NAME = 'image') = 0,
    'ALTER TABLE `user` ADD COLUMN `image` varchar(255) DEFAULT NULL',
    'SELECT "image column already exists"'
));
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- 显示表结构
DESCRIBE `user`; 
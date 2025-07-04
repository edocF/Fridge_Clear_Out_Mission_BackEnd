-- 创建数据库
CREATE DATABASE IF NOT EXISTS fridge_missiondb CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE fridge_missiondb;

-- 创建用户表
CREATE TABLE IF NOT EXISTS `user` (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL UNIQUE,
  `email` varchar(100) NOT NULL UNIQUE,
  `password` varchar(255) NOT NULL,
  `image` varchar(255) DEFAULT NULL,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `family_id` int DEFAULT NULL,
  `dietary_taboos` json DEFAULT NULL,
  `dietary_preferences` varchar(255) DEFAULT NULL,
  `seasoning_preferences` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 创建冰箱表
CREATE TABLE IF NOT EXISTS `fridge` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  FOREIGN KEY (`user_id`) REFERENCES `user`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 创建食材表
CREATE TABLE IF NOT EXISTS `food` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `emoji` varchar(10) DEFAULT NULL,
  `kcal` double DEFAULT 0,
  `carb` double DEFAULT 0,
  `protein` double DEFAULT 0,
  `fat` double DEFAULT 0,
  `price` decimal(10,2) DEFAULT 0.00,
  `expired` boolean DEFAULT FALSE,
  `exp_soon` boolean DEFAULT FALSE,
  `expiry_date` datetime DEFAULT NULL,
  `expiry_duration` int DEFAULT 7,
  `manufacture_date` datetime DEFAULT NULL,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `fridge_id` int NOT NULL,
  `number` int DEFAULT 1,
  `image` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  FOREIGN KEY (`fridge_id`) REFERENCES `fridge`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 创建食材消耗记录表
CREATE TABLE IF NOT EXISTS `food_consumption` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `food_id` int NOT NULL,
  `food_name` varchar(100) NOT NULL,
  `food_emoji` varchar(10) DEFAULT NULL,
  `consumed_amount` double NOT NULL,
  `kcal` double DEFAULT 0,
  `carb` double DEFAULT 0,
  `protein` double DEFAULT 0,
  `fat` double DEFAULT 0,
  `consumed_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  FOREIGN KEY (`user_id`) REFERENCES `user`(`id`) ON DELETE CASCADE,
  FOREIGN KEY (`food_id`) REFERENCES `food`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 插入测试用户数据（密码：123456）
INSERT INTO `user` (`username`, `email`, `password`) VALUES 
('testuser', 'test@example.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa');

-- 为测试用户创建冰箱
INSERT INTO `fridge` (`user_id`) VALUES (1);

-- 插入一些测试食材
INSERT INTO `food` (`name`, `emoji`, `kcal`, `carb`, `protein`, `fat`, `price`, `fridge_id`, `number`) VALUES 
('鸡蛋', '🥚', 155, 1.1, 12.6, 10.6, 0.50, 1, 10),
('西红柿', '🍅', 18, 3.9, 0.9, 0.2, 2.00, 1, 5),
('青菜', '🥬', 15, 2.9, 1.5, 0.2, 1.50, 1, 3),
('胡萝卜', '🥕', 41, 9.6, 0.9, 0.2, 1.80, 1, 4),
('土豆', '🥔', 77, 17, 2, 0.1, 2.50, 1, 6);

-- 创建索引
CREATE INDEX idx_user_username ON `user`(`username`);
CREATE INDEX idx_user_email ON `user`(`email`);
CREATE INDEX idx_food_fridge_id ON `food`(`fridge_id`);
CREATE INDEX idx_food_expired ON `food`(`expired`, `exp_soon`);
CREATE INDEX idx_consumption_user_id ON `food_consumption`(`user_id`);
CREATE INDEX idx_consumption_consumed_time ON `food_consumption`(`consumed_time`); 
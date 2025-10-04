-- =================================================================
-- 百货公司电商平台 - 数据库初始化脚本
-- 功能: 创建所有必要的表并插入足量的测试数据
-- =================================================================

-- 禁用外键约束，以便安全地删除和创建表
SET FOREIGN_KEY_CHECKS = 0;

-- 删除已存在的旧表 (按逆向依赖顺序)
DROP TABLE IF EXISTS `order_items`;
DROP TABLE IF EXISTS `orders`;
DROP TABLE IF EXISTS `reviews`;
DROP TABLE IF EXISTS `cart_items`;
DROP TABLE IF EXISTS `shopping_carts`;
DROP TABLE IF EXISTS `products`;
DROP TABLE IF EXISTS `users`;


-- =================================================================
-- 1. 创建数据表 (CREATE TABLES)
-- =================================================================

-- 用户表 (Users Table)
CREATE TABLE `users` (
                         `id` BIGINT NOT NULL AUTO_INCREMENT,
                         `username` VARCHAR(255) NOT NULL UNIQUE,
                         `password` VARCHAR(255) NOT NULL,
                         `email` VARCHAR(255) NOT NULL UNIQUE,
                         `role` VARCHAR(50) NOT NULL, -- 'CUSTOMER' 或 'ADMIN'
                         `personal_details` TEXT,
                         `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                         PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 商品表 (Products Table)
CREATE TABLE `products` (
                            `id` BIGINT NOT NULL AUTO_INCREMENT,
                            `name` VARCHAR(255) NOT NULL,
                            `category` VARCHAR(255),
                            `description` TEXT,
                            `price` DECIMAL(10, 2) NOT NULL,
                            `stock_quantity` INT NOT NULL,
                            `is_visible` BOOLEAN DEFAULT TRUE,
                            `image_url` VARCHAR(255),
                            PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 购物车表 (Shopping Carts Table) - 与用户一一对应
CREATE TABLE `shopping_carts` (
                                  `id` BIGINT NOT NULL AUTO_INCREMENT,
                                  `user_id` BIGINT NOT NULL UNIQUE,
                                  PRIMARY KEY (`id`),
                                  FOREIGN KEY (`user_id`) REFERENCES `users`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 购物车项目表 (Cart Items Table)
CREATE TABLE `cart_items` (
                              `id` BIGINT NOT NULL AUTO_INCREMENT,
                              `shopping_cart_id` BIGINT NOT NULL,
                              `product_id` BIGINT NOT NULL,
                              `quantity` INT NOT NULL,
                              PRIMARY KEY (`id`),
                              FOREIGN KEY (`shopping_cart_id`) REFERENCES `shopping_carts`(`id`) ON DELETE CASCADE,
                              FOREIGN KEY (`product_id`) REFERENCES `products`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 订单表 (Orders Table)
CREATE TABLE `orders` (
                          `id` BIGINT NOT NULL AUTO_INCREMENT,
                          `order_date` DATETIME NOT NULL,
                          `total_amount` DECIMAL(10, 2) NOT NULL,
                          `status` VARCHAR(50) NOT NULL,
                          `user_id` BIGINT NOT NULL,
                          PRIMARY KEY (`id`),
                          FOREIGN KEY (`user_id`) REFERENCES `users`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 订单项目表 (Order Items Table)
CREATE TABLE `order_items` (
                               `id` BIGINT NOT NULL AUTO_INCREMENT,
                               `order_id` BIGINT NOT NULL,
                               `product_id` BIGINT NOT NULL,
                               `quantity` INT NOT NULL,
                               `unit_price` DECIMAL(10, 2) NOT NULL,
                               PRIMARY KEY (`id`),
                               FOREIGN KEY (`order_id`) REFERENCES `orders`(`id`) ON DELETE CASCADE,
                               FOREIGN KEY (`product_id`) REFERENCES `products`(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 评价表 (Reviews Table)
CREATE TABLE `reviews` (
                           `id` BIGINT NOT NULL AUTO_INCREMENT,
                           `rating` INT NOT NULL,
                           `comment` TEXT,
                           `review_date` DATETIME NOT NULL,
                           `user_id` BIGINT NOT NULL,
                           `product_id` BIGINT NOT NULL,
                           PRIMARY KEY (`id`),
                           FOREIGN KEY (`user_id`) REFERENCES `users`(`id`) ON DELETE CASCADE,
                           FOREIGN KEY (`product_id`) REFERENCES `products`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- =================================================================
-- 2. 插入测试数据 (INSERT SAMPLE DATA)
-- =================================================================

-- 插入用户 (2个顾客, 1个管理员)
INSERT INTO `users` (`username`, `password`, `email`, `role`, `personal_details`) VALUES
                                                                                      ('alice', 'password123', 'alice@example.com', 'CUSTOMER', '{"address": "123 Orchard Road"}'),
                                                                                      ('bob', 'password123', 'bob@example.com', 'CUSTOMER', '{"address": "456 Marina Bay"}'),
                                                                                      ('admin_user', 'adminpass', 'admin@example.com', 'ADMIN', '{"department": "IT"}');

-- 插入商品
INSERT INTO `products` (`name`, `category`, `description`, `price`, `stock_quantity`, `is_visible`, `image_url`) VALUES
                                                                                                                     ('高性能笔记本电脑', '电子产品', '最新款处理器，16GB内存，超快固态硬盘', 1399.99, 50, TRUE, 'https://placehold.co/600x400/E5E4E2/000000?text=Laptop'),
                                                                                                                     ('无线蓝牙耳机', '电子产品', '降噪功能，24小时续航', 199.50, 150, TRUE, 'https://placehold.co/600x400/E5E4E2/000000?text=Headphones'),
                                                                                                                     ('智能手机', '电子产品', '高清摄像头，全面屏设计', 899.00, 80, FALSE, 'https://placehold.co/600x400/E5E4E2/000000?text=Phone'),
                                                                                                                     ('Java编程思想', '书籍', '经典的Java入门与进阶读物', 108.00, 200, TRUE, 'https://placehold.co/600x400/F3EFE0/000000?text=Java+Book'),
                                                                                                                     ('科幻小说：三体', '书籍', '刘慈欣著，中国科幻的里程碑之作', 99.00, 300, TRUE, 'https://placehold.co/600x400/F3EFE0/000000?text=Sci-Fi+Book'),
                                                                                                                     ('舒适办公椅', '家居用品', '人体工学设计，缓解久坐疲劳', 258.80, 60, TRUE, 'https://placehold.co/600x400/D4E2D4/000000?text=Office+Chair'),
                                                                                                                     ('香薰加湿器', '家居用品', '静音运行，七彩夜灯', 89.90, 120, TRUE, 'https://placehold.co/600x400/D4E2D4/000000?text=Humidifier'),
                                                                                                                     ('自动咖啡机', '家居用品', '一键制作意式浓缩、拿铁', 650.00, 30, TRUE, 'https://placehold.co/600x400/D4E2D4/000000?text=Coffee+Machine'),
                                                                                                                     ('潮流运动鞋', '时尚服饰', '透气网面，舒适缓震', 450.00, 180, TRUE, 'https://placehold.co/600x400/FDE2F3/000000?text=Sneakers'),
                                                                                                                     ('纯棉T恤', '时尚服饰', '100%纯棉，柔软亲肤', 79.00, 500, TRUE, 'https://placehold.co/600x400/FDE2F3/000000?text=T-Shirt'),
                                                                                                                     ('设计师手提包', '时尚服饰', '简约设计，百搭风格', 888.00, 40, TRUE, 'https://placehold.co/600x400/FDE2F3/000000?text=Handbag'),
                                                                                                                     ('羊毛围巾', '时尚服饰', '保暖舒适，秋冬必备', 120.00, 0, TRUE, 'https://placehold.co/600x400/FDE2F3/000000?text=Scarf');

-- 为每个用户创建空的购物车
INSERT INTO `shopping_carts` (`user_id`) VALUES
                                             (1), -- Alice's cart
                                             (2), -- Bob's cart
                                             (3); -- Admin's cart

-- 在Alice的购物车中添加一些商品 (用于测试购物车功能)
INSERT INTO `cart_items` (`shopping_cart_id`, `product_id`, `quantity`) VALUES
                                                                            (1, 2, 1),  -- Alice的购物车 (ID=1) 添加了 1 个 无线蓝牙耳机 (ID=2)
                                                                            (1, 10, 2); -- Alice的购物车 (ID=1) 添加了 2 件 纯棉T恤 (ID=10)

-- 为Bob创建一个历史订单 (用于测试购买历史功能)
-- 1. 创建订单
INSERT INTO `orders` (`order_date`, `total_amount`, `status`, `user_id`) VALUES
    ('2025-09-15 10:30:00', 366.80, 'COMPLETED', 2); -- Bob (ID=2) 的订单

-- 2. 创建订单内的商品项
INSERT INTO `order_items` (`order_id`, `product_id`, `quantity`, `unit_price`) VALUES
                                                                                   (1, 4, 1, 108.00), -- 订单1 包含 1本 Java编程思想
                                                                                   (1, 6, 1, 258.80); -- 订单1 包含 1把 舒适办公椅

-- 添加一些商品评价
INSERT INTO `reviews` (`rating`, `comment`, `review_date`, `user_id`, `product_id`) VALUES
                                                                                        (5, '音质非常好，降噪效果明显，物超所值！', '2025-09-20 14:00:00', 1, 2), -- Alice 评价 无线蓝牙耳机
                                                                                        (4, '椅子坐着很舒服，安装也简单。如果靠背能再高一点就完美了。', '2025-09-18 11:00:00', 2, 6), -- Bob 评价 舒适办公椅
                                                                                        (5, '经典中的经典，每个Java开发者都应该读一遍。', '2025-09-19 20:00:00', 2, 4); -- Bob 评价 Java编程思想

-- 启用外键约束
SET FOREIGN_KEY_CHECKS = 1;

-- 脚本结束
SELECT '数据库初始化成功！已创建7张表并插入了测试数据。' AS Status;
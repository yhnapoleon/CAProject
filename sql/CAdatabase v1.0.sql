/*
 Navicat Premium Data Transfer

 Source Server         : myDataBase
 Source Server Type    : MySQL
 Source Server Version : 80023 (8.0.23)
 Source Host           : localhost:3306
 Source Schema         : ca

 Target Server Type    : MySQL
 Target Server Version : 80023 (8.0.23)
 File Encoding         : 65001

 Date: 09/10/2025 12:19:52
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for coupon
-- ----------------------------
DROP TABLE IF EXISTS `coupon`;
CREATE TABLE `coupon`  (
  `coupon_id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NULL DEFAULT NULL,
  `coupon_number` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `coupon_value` int NOT NULL,
  `coupon_start_time` datetime NOT NULL,
  `coupon_end_time` datetime NOT NULL,
  `coupon_name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `coupon_status` int NOT NULL,
  PRIMARY KEY (`coupon_id`) USING BTREE,
  INDEX `FK_Reference_39`(`user_id` ASC) USING BTREE,
  CONSTRAINT `FK_Reference_39` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 41 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of coupon
-- ----------------------------

-- ----------------------------
-- Table structure for discount
-- ----------------------------
DROP TABLE IF EXISTS `discount`;
CREATE TABLE `discount`  (
  `discount_id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NULL DEFAULT NULL,
  `discount_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `discount_description` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `discount_start_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `discount_end_time` timestamp NULL DEFAULT NULL,
  `discount_discount` decimal(10, 2) NOT NULL,
  `discount_status` int NOT NULL,
  PRIMARY KEY (`discount_id`) USING BTREE,
  INDEX `FK_Reference_34`(`user_id` ASC) USING BTREE,
  CONSTRAINT `FK_Reference_34` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 6014 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of discount
-- ----------------------------
INSERT INTO `discount` VALUES (6004, 100000, 'Mid-Autumn Sale', 'Celebrate Mid-Autumn Festival with 20% off sitewide.', '2025-09-01 00:00:00', '2025-09-15 23:59:59', 0.80, 0);
INSERT INTO `discount` VALUES (6005, 100000, 'Halloween Special', 'Trick or treat yourself to spooky savings!', '2025-10-25 00:00:00', '2025-10-31 23:59:59', 0.85, 0);
INSERT INTO `discount` VALUES (6006, 100000, 'Singles Day Deal', 'Biggest sale of the year on 11.11!', '2025-11-10 00:00:00', '2025-11-12 23:59:59', 0.70, 0);
INSERT INTO `discount` VALUES (6007, 100000, 'Black Friday', 'Massive discounts on all electronics.', '2025-11-28 00:00:00', '2025-11-29 23:59:59', 0.60, 0);
INSERT INTO `discount` VALUES (6008, 100000, 'Christmas Promo', 'Get ready for Christmas shopping with great deals.', '2025-12-15 00:00:00', '2025-12-25 23:59:59', 0.75, 0);
INSERT INTO `discount` VALUES (6009, 100000, 'New Year Celebration', 'Ring in the new year with 25% off everything.', '2025-12-30 00:00:00', '2026-01-02 23:59:59', 0.75, 0);
INSERT INTO `discount` VALUES (6010, 100000, 'Valentine’s Offer', 'Buy 1 get 1 half off for Valentine’s Day.', '2026-02-10 00:00:00', '2026-02-15 23:59:59', 0.90, 0);
INSERT INTO `discount` VALUES (6011, 100000, 'Easter Sale', 'Hop into savings this Easter season.', '2026-03-25 00:00:00', '2026-03-31 23:59:59', 0.85, 0);
INSERT INTO `discount` VALUES (6012, 100000, 'Summer Festival', 'Enjoy your summer with 30% off selected items.', '2026-06-01 00:00:00', '2026-06-15 23:59:59', 0.70, 0);
INSERT INTO `discount` VALUES (6013, 100000, 'Anniversary Celebration', 'Thank you for being with us — 20% off sitewide.', '2026-08-01 00:00:00', '2026-08-07 23:59:59', 0.80, 0);

-- ----------------------------
-- Table structure for location
-- ----------------------------
DROP TABLE IF EXISTS `location`;
CREATE TABLE `location`  (
  `location_id` int NOT NULL AUTO_INCREMENT,
  `location_text` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `user_id` int NOT NULL,
  PRIMARY KEY (`location_id`) USING BTREE,
  INDEX `user_id`(`user_id` ASC) USING BTREE,
  CONSTRAINT `user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of location
-- ----------------------------

-- ----------------------------
-- Table structure for order
-- ----------------------------
DROP TABLE IF EXISTS `order`;
CREATE TABLE `order`  (
  `order_id` int NOT NULL AUTO_INCREMENT,
  `order_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `discount_id` int NULL DEFAULT NULL,
  `coupon_id` int NULL DEFAULT NULL,
  `user_id` int NOT NULL,
  `order_status` int NOT NULL,
  `delivery_type` int NOT NULL,
  `delivery_location` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`order_id`) USING BTREE,
  INDEX `FK_Reference_35`(`discount_id` ASC) USING BTREE,
  INDEX `FK_Reference_42`(`coupon_id` ASC) USING BTREE,
  INDEX `FK_Reference_43`(`user_id` ASC) USING BTREE,
  CONSTRAINT `FK_Reference_35` FOREIGN KEY (`discount_id`) REFERENCES `discount` (`discount_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `FK_Reference_42` FOREIGN KEY (`coupon_id`) REFERENCES `coupon` (`coupon_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `FK_Reference_43` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1062700010 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of order
-- ----------------------------

-- ----------------------------
-- Table structure for order_item
-- ----------------------------
DROP TABLE IF EXISTS `order_item`;
CREATE TABLE `order_item`  (
  `order_item_id` int NOT NULL AUTO_INCREMENT,
  `product_id` int NOT NULL,
  `order_id` int NOT NULL,
  `quantity` int NOT NULL,
  `unit_price` int NOT NULL,
  PRIMARY KEY (`order_item_id`) USING BTREE,
  INDEX `FK_Reference_40`(`product_id` ASC) USING BTREE,
  INDEX `FKt4dc2r9nbvbujrljv3e23iibt`(`order_id` ASC) USING BTREE,
  CONSTRAINT `FK_Reference_40` FOREIGN KEY (`product_id`) REFERENCES `product` (`product_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `FK_Reference_41` FOREIGN KEY (`order_id`) REFERENCES `order` (`order_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 14 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of order_item
-- ----------------------------

-- ----------------------------
-- Table structure for product
-- ----------------------------
DROP TABLE IF EXISTS `product`;
CREATE TABLE `product`  (
  `product_id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NULL DEFAULT NULL,
  `product_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `product_description` text CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `product_price` decimal(10, 2) NOT NULL,
  `product_stock_quantity` int NOT NULL COMMENT '库存',
  `product_category` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `is_visible` int NOT NULL,
  `image_url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`product_id`) USING BTREE,
  INDEX `FK_Reference_33`(`user_id` ASC) USING BTREE,
  CONSTRAINT `FK_Reference_33` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 200002 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of product
-- ----------------------------
INSERT INTO `product` VALUES (200001, NULL, 'aaa', 'sss', 12.00, 345, '1', 1, NULL);

-- ----------------------------
-- Table structure for review
-- ----------------------------
DROP TABLE IF EXISTS `review`;
CREATE TABLE `review`  (
  `review_id` int NOT NULL AUTO_INCREMENT,
  `product_id` int NOT NULL,
  `user_id` int NOT NULL,
  `comment` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `review_create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `review_rank` int NOT NULL,
  PRIMARY KEY (`review_id`) USING BTREE,
  INDEX `FK_Reference_30`(`product_id` ASC) USING BTREE,
  INDEX `FK_Reference_36`(`user_id` ASC) USING BTREE,
  CONSTRAINT `FK_Reference_30` FOREIGN KEY (`product_id`) REFERENCES `product` (`product_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `FK_Reference_36` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 38 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of review
-- ----------------------------

-- ----------------------------
-- Table structure for shopping_cart
-- ----------------------------
DROP TABLE IF EXISTS `shopping_cart`;
CREATE TABLE `shopping_cart`  (
  `shopping_cart_id` int NOT NULL AUTO_INCREMENT,
  `product_id` int NOT NULL,
  `user_id` int NOT NULL,
  `quantity` int NOT NULL,
  PRIMARY KEY (`shopping_cart_id`) USING BTREE,
  INDEX `FK_Reference_31`(`product_id` ASC) USING BTREE,
  INDEX `FK_Reference_32`(`user_id` ASC) USING BTREE,
  CONSTRAINT `FK_Reference_31` FOREIGN KEY (`product_id`) REFERENCES `product` (`product_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `FK_Reference_32` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of shopping_cart
-- ----------------------------

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `user_id` int NOT NULL AUTO_INCREMENT,
  `user_phone` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `user_email` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `user_password` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `user_type` int NOT NULL COMMENT '类型：0管理员、1用户',
  `user_register_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `user_lastlogin_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `user_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `user_gender` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `user_birthday` date NOT NULL,
  `user_introduce` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `user_profile_url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `wallet` float NULL DEFAULT NULL,
  PRIMARY KEY (`user_id`) USING BTREE,
  UNIQUE INDEX `user_phone`(`user_phone` ASC) USING BTREE,
  UNIQUE INDEX `user_email`(`user_email` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 100059 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (100000, '12345678', '123456@email.com', '3456', 0, '2025-10-05 12:47:18', '2025-10-05 12:47:18', 'admin', 'male', '2005-06-17', 'wjkjhgfdsasj', NULL, NULL);
INSERT INTO `user` VALUES (100031, '76543212', '234567@email.com', '76543', 1, '2025-10-05 12:49:03', '2025-10-05 12:49:03', 'Mary', 'female', '2005-06-26', 'lkjhgfdsa', NULL, 300);
INSERT INTO `user` VALUES (100032, '83456789', 'user0@example.com', 'pass789', 1, '2025-10-05 12:51:54', '2025-10-05 12:51:54', 'Charlie', 'male', '1995-12-20', 'Hey, I am Charlie.', NULL, 450);
INSERT INTO `user` VALUES (100033, '84567890', 'user4@example.com', 'pwd321', 1, '2025-10-06 09:30:00', '2025-10-08 11:10:00', 'Diana', 'female', '1992-03-10', 'Love art and travel.', NULL, 600);
INSERT INTO `user` VALUES (100034, '85678901', 'user5@example.com', 'hello555', 1, '2025-10-06 09:40:00', '2025-10-08 12:00:00', 'Edward', 'male', '1994-11-22', 'Tech geek and gamer.', NULL, 280);
INSERT INTO `user` VALUES (100035, '86789012', 'user6@example.com', 'pwd666', 1, '2025-10-06 09:45:00', '2025-10-08 12:30:00', 'Fiona', 'female', '1998-02-18', 'Enjoys coffee and movies.', NULL, 720);
INSERT INTO `user` VALUES (100036, '87890123', 'user7@example.com', 'qwerty7', 1, '2025-10-06 10:00:00', '2025-10-09 08:00:00', 'George', 'male', '1985-07-30', 'System administrator', NULL, 1000);
INSERT INTO `user` VALUES (100037, '88901234', 'user8@example.com', 'mypwd8', 1, '2025-10-07 10:15:00', '2025-10-08 16:30:00', 'Helen', 'female', '1999-09-12', 'Love photography.', NULL, 340);
INSERT INTO `user` VALUES (100038, '89012345', 'user9@example.com', 'strong999', 1, '2025-10-07 10:30:00', '2025-10-09 09:00:00', 'Ian', 'male', '2001-04-05', 'Student and music lover.', NULL, 420);
INSERT INTO `user` VALUES (100039, '80123456', 'user10@example.com', 'pass1010', 1, '2025-10-07 11:00:00', '2025-10-09 09:30:00', 'Jack', 'male', '1992-10-21', 'Love basketball and coding.', NULL, 380);
INSERT INTO `user` VALUES (100040, '81239876', 'user11@example.com', 'pass1111', 1, '2025-10-07 11:05:00', '2025-10-09 09:35:00', 'Karen', 'female', '1997-04-12', 'Enjoys hiking and travel.', NULL, 520);
INSERT INTO `user` VALUES (100041, '82340987', 'user12@example.com', 'pass1212', 1, '2025-10-07 11:10:00', '2025-10-09 09:40:00', 'Leo', 'male', '1995-02-03', 'Tech enthusiast.', NULL, 240);
INSERT INTO `user` VALUES (100042, '83452098', 'user13@example.com', 'pass1313', 1, '2025-10-07 11:15:00', '2025-10-09 09:45:00', 'Mia', 'female', '1993-09-15', 'Reading and writing.', NULL, 670);
INSERT INTO `user` VALUES (100043, '84563109', 'user14@example.com', 'pass1414', 1, '2025-10-07 11:20:00', '2025-10-09 09:50:00', 'Nathan', 'male', '1990-01-28', 'Coffee addict.', NULL, 310);
INSERT INTO `user` VALUES (100044, '85674210', 'user15@example.com', 'pass1515', 1, '2025-10-07 11:25:00', '2025-10-09 09:55:00', 'Olivia', 'female', '2000-07-11', 'Music and fashion.', NULL, 460);
INSERT INTO `user` VALUES (100045, '86785321', 'user16@example.com', 'pass1616', 1, '2025-10-07 11:30:00', '2025-10-09 10:00:00', 'Peter', 'male', '1994-03-05', 'Likes photography.', NULL, 250);
INSERT INTO `user` VALUES (100046, '87896432', 'user17@example.com', 'pass1717', 1, '2025-10-07 11:35:00', '2025-10-09 10:05:00', 'Queenie', 'female', '1998-06-19', 'Nature lover.', NULL, 560);
INSERT INTO `user` VALUES (100047, '88907543', 'user18@example.com', 'pass1818', 1, '2025-10-07 11:40:00', '2025-10-09 10:10:00', 'Ryan', 'male', '1991-11-23', 'Movie and travel fan.', NULL, 430);
INSERT INTO `user` VALUES (100048, '89018654', 'user19@example.com', 'pass1919', 1, '2025-10-07 11:45:00', '2025-10-09 10:15:00', 'Sophia', 'female', '1999-05-14', 'Loves drawing.', NULL, 520);
INSERT INTO `user` VALUES (100049, '80129765', 'user20@example.com', 'pass2020', 1, '2025-10-07 11:50:00', '2025-10-09 10:20:00', 'Thomas', 'male', '1987-12-06', 'Enjoys tech and coffee.', NULL, 340);
INSERT INTO `user` VALUES (100050, '81230876', 'user21@example.com', 'pass2121', 1, '2025-10-07 11:55:00', '2025-10-09 10:25:00', 'Uma', 'female', '1996-08-02', 'Reading and travel.', NULL, 310);
INSERT INTO `user` VALUES (100051, '82341987', 'user22@example.com', 'pass2222', 1, '2025-10-07 12:00:00', '2025-10-09 10:30:00', 'Victor', 'male', '1993-04-09', 'Sports and fitness.', NULL, 480);
INSERT INTO `user` VALUES (100052, '83452099', 'user23@example.com', 'pass2323', 1, '2025-10-07 12:05:00', '2025-10-09 10:35:00', 'Wendy', 'female', '1997-01-15', 'Coffee and travel.', NULL, 250);
INSERT INTO `user` VALUES (100053, '84563119', 'user24@example.com', 'pass2424', 1, '2025-10-07 12:10:00', '2025-10-09 10:40:00', 'Xavier', 'male', '1989-03-27', 'Foodie and traveler.', NULL, 680);
INSERT INTO `user` VALUES (100054, '85674229', 'user25@example.com', 'pass2525', 1, '2025-10-07 12:15:00', '2025-10-09 10:45:00', 'Yuki', 'female', '1998-09-10', 'Anime and books.', NULL, 370);
INSERT INTO `user` VALUES (100055, '86785339', 'user26@example.com', 'pass2626', 1, '2025-10-07 12:20:00', '2025-10-09 10:50:00', 'Zack', 'male', '1994-10-04', 'Music and sports.', NULL, 590);
INSERT INTO `user` VALUES (100056, '87896449', 'user27@example.com', 'pass2727', 1, '2025-10-07 12:25:00', '2025-10-09 10:55:00', 'Amy', 'female', '1995-05-23', 'Loves coding.', NULL, 430);
INSERT INTO `user` VALUES (100057, '88907559', 'user28@example.com', 'pass2828', 1, '2025-10-07 12:30:00', '2025-10-09 11:00:00', 'Ben', 'male', '1992-02-19', 'Enjoys hiking.', NULL, 310);
INSERT INTO `user` VALUES (100058, '89018669', 'user29@example.com', 'pass2929', 1, '2025-10-07 12:35:00', '2025-10-09 11:05:00', 'Cathy', 'female', '1999-07-28', 'Love cats and design.', NULL, 480);

-- ----------------------------
-- Table structure for user_coupon
-- ----------------------------
DROP TABLE IF EXISTS `user_coupon`;
CREATE TABLE `user_coupon`  (
  `user_coupon_id` int NOT NULL AUTO_INCREMENT,
  `coupon_id` int NOT NULL,
  `user_id` int NOT NULL,
  `coupon_status` int NOT NULL,
  PRIMARY KEY (`user_coupon_id`) USING BTREE,
  INDEX `FK_Reference_38`(`user_id` ASC) USING BTREE,
  INDEX `FKig76c2u2gd1hlmf516082w3p7`(`coupon_id` ASC) USING BTREE,
  CONSTRAINT `FK_Reference_37` FOREIGN KEY (`coupon_id`) REFERENCES `coupon` (`coupon_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `FK_Reference_38` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 22 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_coupon
-- ----------------------------


--
INSERT INTO `product` (`product_id`, `user_id`, `product_name`, `product_description`, `product_price`, `product_stock_quantity`, `product_category`, `isVisible`, `is_visible`, `image_url`) VALUES
(200002, 100000, '高性能笔记本电脑', '最新款16英寸笔记本电脑，配备M3芯片，18GB内存和512GB SSD。适用于专业人士和游戏玩家。', 9999.00, 50, '电子产品', 1, 1, 'https://example.com/images/laptop.jpg'),
(200003, 100000, '无线蓝牙耳机', '主动降噪功能，提供沉浸式音效体验。长达24小时电池续航。', 1299.00, 150, '电子产品', 1, 1, 'https://example.com/images/headphones.jpg'),
(200004, 100000, '智能手机旗舰版', '6.7英寸OLED显示屏，三摄系统，A17处理器，全天候电池续航。', 7999.00, 80, '电子产品', 1, 1, 'https://example.com/images/smartphone.jpg'),
(200005, 100031, '男士纯棉T恤', '100%纯棉，舒适透气，经典圆领设计，多种颜色可选。', 188.00, 300, '服装', 1, 1, 'https://example.com/images/tshirt.jpg'),
(200006, 100031, '女士牛仔裤', '修身设计，弹力面料，打造完美腿型。适合日常穿着。', 399.00, 200, '服装', 1, 1, 'https://example.com/images/jeans.jpg'),
(200007, 100032, '《数据库系统概念》', '数据库领域的经典教材，全面介绍了数据库设计、理论和实现。', 98.00, 500, '书籍', 1, 1, 'https://example.com/images/db_book.jpg'),
(200008, 100032, '《人工智能：一种现代方法》', '人工智能领域的权威著作，涵盖了从基础理论到前沿应用的所有内容。', 158.00, 250, '书籍', 1, 1, 'https://example.com/images/ai_book.jpg'),
(200009, 100033, '香薰加湿器', '超声波技术，静音运行，七彩夜灯，帮助放松身心，改善空气质量。', 259.00, 120, '家居用品', 1, 1, 'https://example.com/images/humidifier.jpg'),
(200010, 100033, '记忆棉枕头', '人体工程学设计，有效支撑颈部，缓解压力，提升睡眠质量。', 199.00, 180, '家居用品', 1, 1, 'https://example.com/images/pillow.jpg'),
(200011, 100000, '4K智能电视', '55英寸超高清显示屏，支持HDR，内置智能操作系统，畅享海量内容。', 3599.00, 60, '电子产品', 1, 1, 'https://example.com/images/tv.jpg');

--
-- 表：`location` 的样例数据
-- 描述：为多个用户添加收货地址
--
INSERT INTO `location` (`location_id`, `location_text`, `user_id`) VALUES
(1, '上海市浦东新区世纪大道88号金茂大厦', 100031),
(2, '北京市海淀区中关村大街1号北京大学', 100032),
(3, '广东省深圳市南山区科技园高新南一道', 100033),
(4, '浙江省杭州市余杭区文一西路969号', 100034),
(5, '江苏省南京市玄武区中山路1号', 100035),
(6, '四川省成都市武侯区天府大道中段1号', 100036),
(7, '上海市徐汇区漕溪北路1111号', 100031);

--
-- 表：`coupon` 的样例数据
-- 描述：创建不同类型的优惠券，包括通用券和特定用户券
-- coupon_status: 0-可用, 1-已用, 2-已过期
--
INSERT INTO `coupon` (`coupon_id`, `user_id`, `coupon_number`, `coupon_value`, `coupon_start_time`, `coupon_end_time`, `coupon_name`, `coupon_status`) VALUES
(41, NULL, 'NEWUSER10', 10, '2025-10-01 00:00:00', '2025-12-31 23:59:59', '新人专享10元券', 0),
(42, 100031, 'VIP20OFF', 20, '2025-10-01 00:00:00', '2025-10-31 23:59:59', '会员20元券', 0),
(43, 100032, 'SAVE15OCT', 15, '2025-10-01 00:00:00', '2025-10-31 23:59:59', '十月惊喜15元券', 0),
(44, 100033, 'FASHION50', 50, '2025-09-15 00:00:00', '2025-10-15 23:59:59', '时尚购物50元券', 0),
(45, 100034, 'TECHGEEK30', 30, '2025-10-05 00:00:00', '2025-11-05 23:59:59', '数码发烧友30元券', 0),
(46, NULL, 'EXPIRED5', 5, '2025-09-01 00:00:00', '2025-09-30 23:59:59', '已过期5元券', 2);

--
-- 表：`user_coupon` 的样例数据
-- 描述：将优惠券分配给用户
-- coupon_status: 0-未使用, 1-已使用
--
INSERT INTO `user_coupon` (`user_coupon_id`, `coupon_id`, `user_id`, `coupon_status`) VALUES
(22, 41, 100035, 0),
(23, 41, 100036, 0),
(24, 42, 100031, 0),
(25, 43, 100032, 0),
(26, 44, 100033, 0),
(27, 45, 100034, 0);

--
-- 表：`shopping_cart` 的样例数据
-- 描述：为不同用户的购物车添加商品
--
INSERT INTO `shopping_cart` (`shopping_cart_id`, `product_id`, `user_id`, `quantity`) VALUES
(1, 200003, 100031, 1),
(2, 200005, 100031, 2),
(3, 200007, 100032, 1),
(4, 200011, 100034, 1),
(5, 200009, 100035, 1);

--
-- 表：`order` 和 `order_item` 的样例数据
-- 描述：创建一系列订单和对应的订单项
-- order_status: 0-待付款, 1-处理中, 2-已发货, 3-已完成, 4-已取消
-- delivery_type: 0-标准快递, 1-特快配送
--
-- 订单 1: 用户 100032 购买书籍，未使用优惠
INSERT INTO `order` (`order_id`, `order_time`, `discount_id`, `coupon_id`, `user_id`, `order_status`, `delivery_type`, `delivery_location`) VALUES
(1001, '2025-10-01 10:30:00', NULL, NULL, 100032, 3, 0, '北京市海淀区中关村大街1号北京大学');
INSERT INTO `order_item` (`order_item_id`, `product_id`, `order_id`, `quantity`, `unit_price`) VALUES
(1, 200007, 1001, 1, 98),
(2, 200008, 1001, 1, 158);

-- 订单 2: 用户 100033 购买家居用品，并使用了优惠券
INSERT INTO `order` (`order_id`, `order_time`, `discount_id`, `coupon_id`, `user_id`, `order_status`, `delivery_type`, `delivery_location`) VALUES
(1002, '2025-10-02 14:00:00', NULL, 44, 100033, 2, 0, '广东省深圳市南山区科技园高新南一道');
INSERT INTO `order_item` (`order_item_id`, `product_id`, `order_id`, `quantity`, `unit_price`) VALUES
(3, 200009, 1002, 2, 259),
(4, 200010, 1002, 1, 199);

-- 订单 3: 用户 100036 购买电子产品，并参与了中秋节促销
INSERT INTO `order` (`order_id`, `order_time`, `discount_id`, `coupon_id`, `user_id`, `order_status`, `delivery_type`, `delivery_location`) VALUES
(1003, '2025-09-10 20:15:00', 6004, NULL, 100036, 3, 1, '四川省成都市武侯区天府大道中段1号');
INSERT INTO `order_item` (`order_item_id`, `product_id`, `order_id`, `quantity`, `unit_price`) VALUES
(5, 200002, 1003, 1, 9999);

-- 订单 4: 用户 100031 购买服装
INSERT INTO `order` (`order_id`, `order_time`, `discount_id`, `coupon_id`, `user_id`, `order_status`, `delivery_type`, `delivery_location`) VALUES
(1004, '2025-10-05 11:45:00', NULL, NULL, 100031, 1, 0, '上海市浦东新区世纪大道88号金茂大厦');
INSERT INTO `order_item` (`order_item_id`, `product_id`, `order_id`, `quantity`, `unit_price`) VALUES
(6, 200006, 1004, 1, 399);

-- 订单 5: 用户 100034 购买电视，并使用优惠券
INSERT INTO `order` (`order_id`, `order_time`, `discount_id`, `coupon_id`, `user_id`, `order_status`, `delivery_type`, `delivery_location`) VALUES
(1005, '2025-10-06 09:05:00', NULL, 45, 100034, 0, 1, '浙江省杭州市余杭区文一西路969号');
INSERT INTO `order_item` (`order_item_id`, `product_id`, `order_id`, `quantity`, `unit_price`) VALUES
(7, 200011, 1005, 1, 3599);

-- 订单 6: 用户 100032 取消的订单
INSERT INTO `order` (`order_id`, `order_time`, `discount_id`, `coupon_id`, `user_id`, `order_status`, `delivery_type`, `delivery_location`) VALUES
(1006, '2025-10-07 18:20:00', NULL, NULL, 100032, 4, 0, '北京市海淀区中关村大街1号北京大学');
INSERT INTO `order_item` (`order_item_id`, `product_id`, `order_id`, `quantity`, `unit_price`) VALUES
(8, 200005, 1006, 5, 188);

--
-- 表：`review` 的样例数据
-- 描述：用户对已完成订单中的商品进行评论
-- review_rank: 1-5 星
--
INSERT INTO `review` (`review_id`, `product_id`, `user_id`, `comment`, `review_create_time`, `review_rank`) VALUES
(38, 200007, 100032, '这本书太棒了，内容详实，是学习数据库的必备好书！物流也很快。', '2025-10-05 12:00:00', 5),
(39, 200008, 100032, '经典之作，印刷质量很好，值得收藏。', '2025-10-05 12:01:00', 5),
(40, 200002, 100036, '电脑性能非常强劲，运行流畅，屏幕显示效果惊艳。价格虽然贵，但物有所值。', '2025-09-15 10:00:00', 5),
(41, 200009, 100033, '加湿器外观很漂亮，运行起来几乎没有声音，夜灯功能很温馨。', '2025-10-08 22:30:00', 4),
(42, 200010, 100033, '枕头很舒服，对颈椎有好处，但是感觉有点小。', '2025-10-08 22:31:00', 3);

--
-- 重新启用外键检查
--
SET FOREIGN_KEY_CHECKS = 1;

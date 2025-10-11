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
  `isVisible` int NOT NULL,
  `imageUrl` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `is_visible` int NOT NULL,
  `image_url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`product_id`) USING BTREE,
  INDEX `FK_Reference_33`(`user_id` ASC) USING BTREE,
  CONSTRAINT `FK_Reference_33` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 200002 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of product
-- ----------------------------
INSERT INTO `product` VALUES (200001, NULL, 'aaa', 'sss', 12.00, 345, '1', 1, NULL, 1, NULL);

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

SET FOREIGN_KEY_CHECKS = 1;

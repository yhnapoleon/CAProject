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

 Date: 05/10/2025 12:53:08
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
  `coupon_type` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `discount` decimal(10,2) NOT NULL,
  `manjian_value` int NOT NULL,
  `coupon_name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `coupon_quantity` int NOT NULL,
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
  `discount_discount` decimal(10,2) NOT NULL,
  PRIMARY KEY (`discount_id`) USING BTREE,
  INDEX `FK_Reference_34`(`user_id` ASC) USING BTREE,
  CONSTRAINT `FK_Reference_34` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of discount
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
  `unitPrice` int NOT NULL,
  PRIMARY KEY (`order_item_id`) USING BTREE,
  INDEX `FK_Reference_40`(`product_id` ASC) USING BTREE,
  INDEX `FK_Reference_41`(`order_id` ASC) USING BTREE,
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
  `product_price` decimal(10,2) NOT NULL,
  `product_stock_quantity` int NOT NULL COMMENT '库存',
  `product_category` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `isVisible` int NOT NULL,
  `imageUrl` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`product_id`) USING BTREE,
  INDEX `FK_Reference_33`(`user_id` ASC) USING BTREE,
  CONSTRAINT `FK_Reference_33` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 80 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of product
-- ----------------------------

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
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

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
  PRIMARY KEY (`user_id`) USING BTREE,
  UNIQUE INDEX `user_phone`(`user_phone` ASC) USING BTREE,
  UNIQUE INDEX `user_email`(`user_email` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 100005 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (100000, '12345678', '123456@email.com', '3456', 0, '2025-10-05 12:47:18', '2025-10-05 12:47:18', 'asd', 'male', '2025-06-17', 'wjkjhgfdsasj', NULL);
INSERT INTO `user` VALUES (100001, '76543212', '234567@email.com', '76543', 1, '2025-10-05 12:49:03', '2025-10-05 12:49:03', 'fegreg', 'female', '2025-06-26', 'lkjhgfdsa', NULL);
INSERT INTO `user` VALUES (100002, '1234567890', 'user1@example.com', 'pass123', 1, '2025-10-05 12:51:54', '2025-10-05 12:51:54', 'Alice', 'Female', '1990-05-01', 'Hi, I am Alice.', NULL);
INSERT INTO `user` VALUES (100003, '2345678901', 'user2@example.com', 'pass456', 1, '2025-10-05 12:51:54', '2025-10-05 12:51:54', 'Bob', 'Male', '1988-08-15', 'Hello, I am Bob.', NULL);
INSERT INTO `user` VALUES (100004, '3456789012', 'user3@example.com', 'pass789', 1, '2025-10-05 12:51:54', '2025-10-05 12:51:54', 'Charlie', 'Male', '1995-12-20', 'Hey, I am Charlie.', NULL);

-- ----------------------------
-- Table structure for user_coupon
-- ----------------------------
DROP TABLE IF EXISTS `user_coupon`;
CREATE TABLE `user_coupon`  (
  `user_coupon_id` int NOT NULL AUTO_INCREMENT,
  `coupon_id` int NOT NULL,
  `user_id` int NOT NULL,
  PRIMARY KEY (`user_coupon_id`) USING BTREE,
  INDEX `FK_Reference_37`(`coupon_id` ASC) USING BTREE,
  INDEX `FK_Reference_38`(`user_id` ASC) USING BTREE,
  CONSTRAINT `FK_Reference_37` FOREIGN KEY (`coupon_id`) REFERENCES `coupon` (`coupon_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `FK_Reference_38` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 22 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_coupon
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;

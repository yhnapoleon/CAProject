-- ===============================================================
--  H2 In-Memory Database - Test Data Initialization Script
--  (此文件仅在测试运行时由 Spring Boot 自动执行)
-- ===============================================================

-- 插入测试用户 (1个顾客, 1个管理员)
INSERT INTO user (user_id, user_phone, user_email, user_password, user_type, user_register_time, user_lastlogin_time, user_name, user_gender, user_birthday, user_introduce, user_profile_url) VALUES
(1, '13800138000', 'customer@test.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', 1, '2025-10-01 12:00:00', '2025-10-01 12:00:00', 'Test Customer', 'Male', '1990-01-01', 'Test customer account', NULL),
(2, '13800138001', 'admin@test.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', 0, '2025-10-01 12:00:00', '2025-10-01 12:00:00', 'Test Admin', 'Male', '1985-01-01', 'Test admin account', NULL);

-- 插入测试商品
INSERT INTO product (product_id, user_id, product_name, product_description, product_price, product_stock_quantity, product_category, isVisible, imageUrl) VALUES
(101, 2, '测试笔记本', '用于测试的笔记本电脑', 100.00, 10, 'ELECTRONICS', 1, NULL),
(102, 2, '测试书籍', '用于测试的书籍', 20.00, 50, 'BOOKS', 1, NULL),
(103, 2, '测试T恤', '用于测试的T恤', 5.00, 100, 'CLOTHING', 1, NULL);

-- 为测试用户创建购物车
INSERT INTO shopping_cart (shopping_cart_id, product_id, user_id, quantity) VALUES
(1, 101, 1, 2), -- test_customer 的购物车中有 2 件 测试笔记本(101)
(2, 102, 1, 1); -- test_customer 的购物车中有 1 本 测试书籍(102)

-- 为 test_customer 创建一个历史订单
INSERT INTO `order` (order_id, order_time, discount_id, coupon_id, user_id, order_status) VALUES
(1, '2025-10-01 12:00:00', NULL, NULL, 1, 1);

-- 历史订单的商品项
INSERT INTO order_item (order_item_id, product_id, order_id, quantity, unitPrice) VALUES
(1, 102, 1, 1, 20), -- 订单1 中有 1 本 测试书籍(102)
(2, 103, 1, 1, 5);  -- 订单1 中有 1 件 测试T恤(103)

-- 为商品添加一条评价
INSERT INTO review (review_id, product_id, user_id, comment, review_create_time, review_rank) VALUES
(1, 102, 1, '这本书作为测试数据非常棒!', '2025-10-02 18:00:00', 5);
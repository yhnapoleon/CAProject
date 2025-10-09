-- ===============================================================
--  H2 In-Memory Database - Test Data Initialization Script
--  (此文件仅在测试运行时由 Spring Boot 自动执行)
-- ===============================================================

-- 插入测试用户 (1个顾客, 1个管理员)
INSERT INTO users (user_id, user_name, user_password, user_email, user_phone, user_type, user_register_time, user_lastlogin_time, user_gender) VALUES
                                                                              (1, 'test_customer', 'password', 'customer@test.com', '1234567890', 1, '2025-01-01 10:00:00', '2025-01-01 10:00:00', 'Male'),
                                                                              (2, 'test_admin', 'adminpass', 'admin@test.com', '0987654321', 0, '2025-01-01 10:00:00', '2025-01-01 10:00:00', 'Female');

-- 插入测试商品
INSERT INTO product (product_id, product_name, product_category, product_description, product_price, product_stock_quantity, is_visible) VALUES
                                                                                              (101, '测试笔记本', '电子产品', '用于测试的笔记本电脑', 100.00, 10, 1),
                                                                                              (102, '测试书籍', '书籍', '用于测试的书籍', 20.00, 50, 1),
                                                                                              (103, '测试T恤', '时尚服饰', '用于测试的T恤', 5.00, 100, 1);

-- 为测试用户创建购物车
INSERT INTO shopping_carts (id, user_id) VALUES
                                             (1, 1), -- test_customer 的购物车
                                             (2, 2); -- test_admin 的购物车

-- 在 test_customer 的购物车中添加一件商品
INSERT INTO cart_items (id, shopping_cart_id, product_id, quantity) VALUES
    (1, 1, 101, 2); -- 购物车1 中有 2 件 测试笔记本(101)

-- 为 test_customer 创建一个历史订单
INSERT INTO orders (order_id, order_date, total_amount, status, user_id) VALUES
    (1, '2025-10-01 12:00:00', 25.00, 'COMPLETED', 1);

-- 历史订单的商品项
INSERT INTO order_items (id, order_id, product_id, quantity, unit_price) VALUES
                                                                             (1, 1, 102, 1, 20.00), -- 订单1 中有 1 本 测试书籍(102)
                                                                             (2, 1, 103, 1, 5.00);  -- 订单1 中有 1 件 测试T恤(103)

-- 为商品添加一条评价
INSERT INTO reviews (id, rating, comment, review_date, user_id, product_id) VALUES
    (1, 5, '这本书作为测试数据非常棒!', '2025-10-02 18:00:00', 1, 102);
package sg.nusiss.t6.caproject.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sg.nusiss.t6.caproject.controller.dto.OrderRequestDTO;
import sg.nusiss.t6.caproject.model.*;
import sg.nusiss.t6.caproject.repository.*;
import sg.nusiss.t6.caproject.service.OrderService;
import sg.nusiss.t6.caproject.util.Code;
import sg.nusiss.t6.caproject.util.DataResult;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DiscountRepository discountRepository;

    @Autowired
    private CouponRepository couponRepository;

    @Override
    public List<Order> getOrdersByUserId(Integer userId){
        return orderRepository.findOrdersByUserId(userId);
    }

    @Override
    public DataResult createOrder(OrderRequestDTO orderRequest) {
        // 1️⃣ 查询用户
        User user = userRepository.findById(orderRequest.getUserId())
                .orElseThrow(() -> new RuntimeException("用户不存在"));

        // 2️⃣ 创建订单
        Order order = new Order();
        order.setUser(user);

        // 设置订单状态（如果前端传入了状态，使用前端的；否则默认为0=待支付）
        if (orderRequest.getOrderStatus() != null) {
            order.setOrderStatus(orderRequest.getOrderStatus());
        } else {
            order.setOrderStatus(0); // 0=待支付
        }

        // 设置订单时间（如果前端传入了时间，使用前端的；否则使用当前时间）
        if (orderRequest.getOrderTime() != null) {
            order.setOrderTime(orderRequest.getOrderTime());
        } else {
            order.setOrderTime(LocalDateTime.now());
        }

        // 设置配送信息
        if (orderRequest.getDeliveryType() != null) {
            order.setDeliveryType(orderRequest.getDeliveryType());
        }
        if (orderRequest.getDeliveryLocation() != null) {
            order.setDeliveryLocation(orderRequest.getDeliveryLocation());
        }

        // 3️⃣ 处理折扣（如果前端传入了discountId）
        if (orderRequest.getDiscountId() != null) {
            Discount discount = discountRepository.findById(orderRequest.getDiscountId())
                    .orElseThrow(() -> new RuntimeException("折扣不存在: " + orderRequest.getDiscountId()));
            order.setDiscount(discount);
        }

        // 4️⃣ 处理优惠券（如果前端传入了couponId）
        if (orderRequest.getCouponId() != null) {
            Coupon coupon = couponRepository.findById(orderRequest.getCouponId())
                    .orElseThrow(() -> new RuntimeException("优惠券不存在: " + orderRequest.getCouponId()));
            order.setCoupon(coupon);
        }

        // 5️⃣ 遍历前端传来的商品列表
        List<OrderItem> orderItems = new ArrayList<>();
        for (OrderRequestDTO.OrderItemDTO itemDTO : orderRequest.getItems()) {
            Product product = productRepository.findById(itemDTO.getProductId())
                    .orElseThrow(() -> new RuntimeException("商品不存在: " + itemDTO.getProductId()));

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(itemDTO.getQuantity());
            orderItem.setUnitPrice(itemDTO.getUnitPrice());

            orderItems.add(orderItem);
        }

        // 6️⃣ 建立关系
        order.setOrderItems(orderItems);

        // 7️⃣ 保存订单（JPA 会自动级联保存订单项）
        orderRepository.save(order);

        // 8️⃣ 返回结果
        return new DataResult(200, order.getOrderId(), "订单创建成功");
    }

    @Override
    public DataResult updateOrderStatus(Integer orderId, Integer orderStatus) {
        try {
            // 1️⃣ 查找订单
            Order order = orderRepository.findById(orderId)
                    .orElseThrow(() -> new RuntimeException("订单不存在: " + orderId));

            // 2️⃣ 更新订单状态
            order.setOrderStatus(orderStatus);

            // 3️⃣ 保存订单
            orderRepository.save(order);

            // 4️⃣ 返回结果
            return new DataResult(Code.SUCCESS, order.getOrderId(), "订单状态更新成功");
        } catch (Exception e) {
            return new DataResult(Code.FAILED, null, "订单状态更新失败: " + e.getMessage());
        }
    }

    @Override
    public DataResult processPayment(Integer userId, Float totalPrice) {
        try {
            // 1️⃣ 查找用户
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("用户不存在: " + userId));

            // 2️⃣ 检查钱包余额
            Float currentBalance = user.getWallet() != null ? user.getWallet() : 0.0f;

            if (currentBalance < totalPrice) {
                return new DataResult(Code.FAILED, null,
                        String.format("余额不足，当前余额: %.2f，需要支付: %.2f", currentBalance, totalPrice));
            }

            // 3️⃣ 执行扣款
            Float newBalance = currentBalance - totalPrice;
            user.setWallet(newBalance);

            // 4️⃣ 保存用户信息
            userRepository.save(user);

            // 5️⃣ 返回结果
            return new DataResult(Code.SUCCESS, newBalance,
                    String.format("付款成功，扣款: %.2f，剩余余额: %.2f", totalPrice, newBalance));
        } catch (Exception e) {
            return new DataResult(Code.FAILED, null, "付款失败: " + e.getMessage());
        }
    }
}

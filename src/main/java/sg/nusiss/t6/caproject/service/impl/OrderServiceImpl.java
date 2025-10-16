package sg.nusiss.t6.caproject.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sg.nusiss.t6.caproject.controller.dto.OrderRequestDTO;
import sg.nusiss.t6.caproject.model.*;
import sg.nusiss.t6.caproject.repository.*;
import sg.nusiss.t6.caproject.service.OrderService;
import sg.nusiss.t6.caproject.util.Code;
import sg.nusiss.t6.caproject.util.DataResult;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import sg.nusiss.t6.caproject.util.Code;
import sg.nusiss.t6.caproject.util.DataResult;

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
    public List<Order> getOrdersByUserId(Integer userId) {
        return orderRepository.findOrdersByUserId(userId);
    }

    @Override
    public DataResult createOrder(OrderRequestDTO orderRequest) {
        // 1) Query user
        User user = userRepository.findById(orderRequest.getUserId())
                .orElseThrow(() -> new RuntimeException("用户不存在"));

        // 2) Create order
        Order order = new Order();
        order.setUser(user);

        // Set order status (use frontend value if provided; otherwise default to
        // 0=pending payment)
        if (orderRequest.getOrderStatus() != null) {
            order.setOrderStatus(orderRequest.getOrderStatus());
        } else {
            order.setOrderStatus(0); // 0=pending payment
        }

        // Set order time (use frontend value if provided; otherwise use current time)
        if (orderRequest.getOrderTime() != null) {
            order.setOrderTime(orderRequest.getOrderTime());
        } else {
            order.setOrderTime(LocalDateTime.now());
        }

        // Set delivery info
        if (orderRequest.getDeliveryType() != null) {
            order.setDeliveryType(orderRequest.getDeliveryType());
        }
        if (orderRequest.getDeliveryLocation() != null) {
            order.setDeliveryLocation(orderRequest.getDeliveryLocation());
        }

        // 3) Handle discount (if discountId provided)
        if (orderRequest.getDiscountId() != null) {
            Discount discount = discountRepository.findById(orderRequest.getDiscountId())
                    .orElseThrow(() -> new RuntimeException("Discount not found: " + orderRequest.getDiscountId()));
            order.setDiscount(discount);
        }

        // 4) Handle coupon (if couponId provided)
        if (orderRequest.getCouponId() != null) {
            Coupon coupon = couponRepository.findById(orderRequest.getCouponId())
                    .orElseThrow(() -> new RuntimeException("Coupon not found: " + orderRequest.getCouponId()));
            order.setCoupon(coupon);
        }

        // 5) Iterate through items from frontend
        List<OrderItem> orderItems = new ArrayList<>();
        for (OrderRequestDTO.OrderItemDTO itemDTO : orderRequest.getItems()) {
            Product product = productRepository.findById(itemDTO.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found: " + itemDTO.getProductId()));

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(itemDTO.getQuantity());
            orderItem.setUnitPrice(itemDTO.getUnitPrice());

            orderItems.add(orderItem);
        }

        // 6) Establish relations
        order.setOrderItems(orderItems);

        // 7) Save order (JPA will cascade to save order items)
        orderRepository.save(order);

        // 8) Return result
        return new DataResult(200, order.getOrderId(), "Order created successfully");
    }

    @Override
    public DataResult updateOrderStatus(Integer orderId, Integer orderStatus) {
        try {
            // 1) Find order
            Order order = orderRepository.findById(orderId)
                    .orElseThrow(() -> new RuntimeException("Order not found: " + orderId));

            // 2) Update order status
            order.setOrderStatus(orderStatus);

            // 3) Save order
            orderRepository.save(order);

            // 4) Return result
            return new DataResult(Code.SUCCESS, order.getOrderId(), "Order status updated successfully");
        } catch (Exception e) {
            return new DataResult(Code.FAILED, null, "Failed to update order status: " + e.getMessage());
        }
    }

    @Override
    public DataResult processPayment(Integer userId, Float totalPrice) {
        try {
            // 1) Find user
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found: " + userId));

            // 2) Check wallet balance (use BigDecimal directly)
            BigDecimal currentBalance = user.getWallet() != null ? user.getWallet().setScale(2, RoundingMode.HALF_UP)
                    : BigDecimal.ZERO;
            BigDecimal totalPriceBD = BigDecimal.valueOf(totalPrice).setScale(2, RoundingMode.HALF_UP);

            if (currentBalance.compareTo(totalPriceBD) < 0) {
                return new DataResult(Code.FAILED, null,
                        String.format("Insufficient balance. Current: %.2f, Required: %.2f", currentBalance,
                                totalPriceBD));
            }

            // 3) Deduct amount (precise calculation)
            BigDecimal newBalance = currentBalance.subtract(totalPriceBD);

            // 4) Save BigDecimal directly to DB to ensure precision
            user.setWallet(newBalance);

            // 5) Save user
            userRepository.save(user);

            // 6) Return result (use BigDecimal for consistency)
            return new DataResult(Code.SUCCESS, newBalance.doubleValue(),
                    String.format("Payment successful. Deducted: %.2f, Remaining: %.2f", totalPriceBD, newBalance));
        } catch (Exception e) {
            return new DataResult(Code.FAILED, null, "Payment failed: " + e.getMessage());
        }
    }
}

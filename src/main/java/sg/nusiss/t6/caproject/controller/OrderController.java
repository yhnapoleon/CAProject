package sg.nusiss.t6.caproject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sg.nusiss.t6.caproject.controller.dto.OrderRequestDTO;
import sg.nusiss.t6.caproject.controller.dto.OrderResponseDTO;
import sg.nusiss.t6.caproject.model.Order;
import sg.nusiss.t6.caproject.model.User;
import sg.nusiss.t6.caproject.service.OrderService;
import sg.nusiss.t6.caproject.util.DataResult;
import sg.nusiss.t6.caproject.util.Code;
import sg.nusiss.t6.caproject.repository.UserRepository;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/getOrders/{userId}")
    public DataResult getOrders(@PathVariable Integer userId) {
        try {
            List<Order> orders = orderService.getOrdersByUserId(userId);

            // Convert to DTO format
            List<OrderResponseDTO> responseDTOs = orders.stream()
                    .map(order -> {
                        OrderResponseDTO dto = new OrderResponseDTO();
                        dto.setOrderId(order.getOrderId());
                        dto.setOrderTime(order.getOrderTime());
                        dto.setOrderStatus(order.getOrderStatus());

                        // Set discount info
                        if (order.getDiscount() != null) {
                            OrderResponseDTO.DiscountInfo discountInfo = new OrderResponseDTO.DiscountInfo();
                            discountInfo.setDiscountId(order.getDiscount().getDiscountId());
                            discountInfo.setDiscountName(order.getDiscount().getDiscountName());
                            discountInfo.setDiscountDescription(order.getDiscount().getDiscountDescription());
                            discountInfo.setDiscountStartTime(order.getDiscount().getDiscountStartTime());
                            discountInfo.setDiscountEndTime(order.getDiscount().getDiscountEndTime());
                            discountInfo.setDiscountDiscount(order.getDiscount().getDiscountDiscount().doubleValue());
                            dto.setDiscount(discountInfo);
                        }

                        // Set coupon info
                        if (order.getCoupon() != null) {
                            OrderResponseDTO.CouponInfo couponInfo = new OrderResponseDTO.CouponInfo();
                            couponInfo.setCouponId(order.getCoupon().getCouponId());
                            couponInfo.setCouponNumber(order.getCoupon().getCouponNumber());
                            couponInfo.setCouponValue(order.getCoupon().getCouponValue());
                            couponInfo.setCouponStartTime(order.getCoupon().getCouponStartTime());
                            couponInfo.setCouponEndTime(order.getCoupon().getCouponEndTime());
                            couponInfo.setCouponName(order.getCoupon().getCouponName());

                            // Set coupon's associated user info
                            if (order.getCoupon().getUser() != null) {
                                OrderResponseDTO.CouponInfo.UserInfo userInfo = new OrderResponseDTO.CouponInfo.UserInfo();
                                userInfo.setUserId(order.getCoupon().getUser().getUserId());
                                userInfo.setUserName(order.getCoupon().getUser().getUserName());
                                couponInfo.setUser(userInfo);
                            }

                            dto.setCoupon(couponInfo);
                        }

                        // Set order item info
                        if (order.getOrderItems() != null) {
                            List<OrderResponseDTO.OrderItemInfo> orderItemInfos = order.getOrderItems().stream()
                                    .map(orderItem -> {
                                        OrderResponseDTO.OrderItemInfo itemInfo = new OrderResponseDTO.OrderItemInfo();
                                        itemInfo.setOrderItemId(orderItem.getOrderItemId());
                                        itemInfo.setQuantity(orderItem.getQuantity());
                                        itemInfo.setUnitPrice(orderItem.getUnitPrice());

                                        // Set product info
                                        OrderResponseDTO.OrderItemInfo.ProductInfo productInfo = new OrderResponseDTO.OrderItemInfo.ProductInfo();
                                        productInfo.setProductId(orderItem.getProduct().getProductId());
                                        productInfo.setProductName(orderItem.getProduct().getProductName());
                                        productInfo
                                                .setProductDescription(orderItem.getProduct().getProductDescription());
                                        productInfo.setProductPrice(
                                                orderItem.getProduct().getProductPrice().doubleValue());
                                        productInfo.setProductStockQuantity(
                                                orderItem.getProduct().getProductStockQuantity());
                                        productInfo.setProductCategory(orderItem.getProduct().getProductCategory());
                                        productInfo.setIsVisible(orderItem.getProduct().getIsVisible());
                                        productInfo.setImageUrl(orderItem.getProduct().getImageUrl());

                                        itemInfo.setProduct(productInfo);
                                        return itemInfo;
                                    })
                                    .toList();
                            dto.setOrderItems(orderItemInfos);
                        }

                        return dto;
                    })
                    .toList();

            return new DataResult(Code.SUCCESS, responseDTOs, "Fetched order list successfully");
        } catch (Exception e) {
            return new DataResult(Code.FAILED, null, "Failed to fetch order list: " + e.getMessage());
        }
    }

    @PostMapping("/createOrder")
    public DataResult createOrder(@RequestBody OrderRequestDTO orderRequest) {
        return orderService.createOrder(orderRequest);
    }

    @PutMapping("/updateOrderStatus")
    public DataResult updateOrderStatus(@RequestBody Map<String, Integer> request) {
        try {
            Integer orderId = request.get("orderId");
            Integer orderStatus = request.get("orderStatus");

            if (orderId == null || orderStatus == null) {
                return new DataResult(Code.FAILED, null, "Order ID and order status must not be null");
            }

            return orderService.updateOrderStatus(orderId, orderStatus);
        } catch (Exception e) {
            return new DataResult(Code.FAILED, null, "Failed to update order status: " + e.getMessage());
        }
    }

    @PostMapping("/payOrder")
    public DataResult processPayment(@RequestBody Map<String, Object> request) {
        try {
            Integer userId = (Integer) request.get("userId");
            Float totalPrice = ((Number) request.get("totalPrice")).floatValue();

            if (userId == null || totalPrice == null) {
                return new DataResult(Code.FAILED, null, "User ID and total price must not be null");
            }

            if (totalPrice <= 0) {
                return new DataResult(Code.FAILED, null, "Total price must be greater than 0");
            }

            return orderService.processPayment(userId, totalPrice);
        } catch (Exception e) {
            return new DataResult(Code.FAILED, null, "Payment processing failed: " + e.getMessage());
        }
    }

    @GetMapping("/getWallet")
    public DataResult getWallet(@RequestParam Integer userId) {
        try {
            if (userId == null) {
                return new DataResult(Code.FAILED, null, "User ID must not be null");
            }

            User user = userRepository.findById(userId)
                    .orElse(null);
            if (user == null) {
                return new DataResult(Code.FAILED, null, "User does not exist");
            }

            java.math.BigDecimal wallet = user.getWallet();
            // Return value directly from DB to remain consistent with payOrder method
            if (wallet == null) {
                return new DataResult(Code.SUCCESS, 0.0, "Fetched wallet balance successfully");
            }

            // Return value directly from DB for consistency
            return new DataResult(Code.SUCCESS, wallet.doubleValue(), "Fetched wallet balance successfully");
        } catch (Exception e) {
            return new DataResult(Code.FAILED, null, "Failed to fetch wallet balance: " + e.getMessage());
        }
    }
}

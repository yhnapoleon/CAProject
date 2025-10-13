package sg.nusiss.t6.caproject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sg.nusiss.t6.caproject.controller.dto.OrderRequestDTO;
import sg.nusiss.t6.caproject.controller.dto.OrderResponseDTO;
import sg.nusiss.t6.caproject.model.Order;
import sg.nusiss.t6.caproject.service.OrderService;
import sg.nusiss.t6.caproject.util.DataResult;
import sg.nusiss.t6.caproject.util.Code;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @GetMapping("/getOrders/{userId}")
    public DataResult getOrders(@PathVariable Integer userId){
        try {
            List<Order> orders = orderService.getOrdersByUserId(userId);

            // 转换为DTO格式
            List<OrderResponseDTO> responseDTOs = orders.stream()
                    .map(order -> {
                        OrderResponseDTO dto = new OrderResponseDTO();
                        dto.setOrderId(order.getOrderId());
                        dto.setOrderTime(order.getOrderTime());
                        dto.setOrderStatus(order.getOrderStatus());

                        // 设置折扣信息
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

                        // 设置优惠券信息
                        if (order.getCoupon() != null) {
                            OrderResponseDTO.CouponInfo couponInfo = new OrderResponseDTO.CouponInfo();
                            couponInfo.setCouponId(order.getCoupon().getCouponId());
                            couponInfo.setCouponNumber(order.getCoupon().getCouponNumber());
                            couponInfo.setCouponValue(order.getCoupon().getCouponValue());
                            couponInfo.setCouponStartTime(order.getCoupon().getCouponStartTime());
                            couponInfo.setCouponEndTime(order.getCoupon().getCouponEndTime());
                            couponInfo.setCouponName(order.getCoupon().getCouponName());

                            // 设置优惠券关联的用户信息
                            if (order.getCoupon().getUser() != null) {
                                OrderResponseDTO.CouponInfo.UserInfo userInfo = new OrderResponseDTO.CouponInfo.UserInfo();
                                userInfo.setUserId(order.getCoupon().getUser().getUserId());
                                userInfo.setUserName(order.getCoupon().getUser().getUserName());
                                couponInfo.setUser(userInfo);
                            }

                            dto.setCoupon(couponInfo);
                        }

                        // 设置订单项信息
                        if (order.getOrderItems() != null) {
                            List<OrderResponseDTO.OrderItemInfo> orderItemInfos = order.getOrderItems().stream()
                                    .map(orderItem -> {
                                        OrderResponseDTO.OrderItemInfo itemInfo = new OrderResponseDTO.OrderItemInfo();
                                        itemInfo.setOrderItemId(orderItem.getOrderItemId());
                                        itemInfo.setQuantity(orderItem.getQuantity());
                                        itemInfo.setUnitPrice(orderItem.getUnitPrice());

                                        // 设置商品信息
                                        OrderResponseDTO.OrderItemInfo.ProductInfo productInfo = new OrderResponseDTO.OrderItemInfo.ProductInfo();
                                        productInfo.setProductId(orderItem.getProduct().getProductId());
                                        productInfo.setProductName(orderItem.getProduct().getProductName());
                                        productInfo.setProductDescription(orderItem.getProduct().getProductDescription());
                                        productInfo.setProductPrice(orderItem.getProduct().getProductPrice().doubleValue());
                                        productInfo.setProductStockQuantity(orderItem.getProduct().getProductStockQuantity());
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

            return new DataResult(Code.SUCCESS, responseDTOs, "获取订单列表成功");
        } catch (Exception e) {
            return new DataResult(Code.FAILED, null, "获取订单列表失败: " + e.getMessage());
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
                return new DataResult(Code.FAILED, null, "订单ID和订单状态不能为空");
            }

            return orderService.updateOrderStatus(orderId, orderStatus);
        } catch (Exception e) {
            return new DataResult(Code.FAILED, null, "更新订单状态失败: " + e.getMessage());
        }
    }

    @PostMapping("/payOrder")
    public DataResult processPayment(@RequestBody Map<String, Object> request) {
        try {
            Integer userId = (Integer) request.get("userId");
            Float totalPrice = ((Number) request.get("totalPrice")).floatValue();

            if (userId == null || totalPrice == null) {
                return new DataResult(Code.FAILED, null, "用户ID和支付金额不能为空");
            }

            if (totalPrice <= 0) {
                return new DataResult(Code.FAILED, null, "支付金额必须大于0");
            }

            return orderService.processPayment(userId, totalPrice);
        } catch (Exception e) {
            return new DataResult(Code.FAILED, null, "付款处理失败: " + e.getMessage());
        }
    }
}

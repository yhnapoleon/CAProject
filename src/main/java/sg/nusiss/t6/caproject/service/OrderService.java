package sg.nusiss.t6.caproject.service;

import sg.nusiss.t6.caproject.controller.dto.OrderRequestDTO;
import sg.nusiss.t6.caproject.model.Order;
import sg.nusiss.t6.caproject.util.DataResult;

import java.util.List;

public interface OrderService {
    List<Order> getOrdersByUserId(Integer userId);

    DataResult createOrder(OrderRequestDTO orderRequest);

    DataResult updateOrderStatus(Integer orderId, Integer orderStatus);

    DataResult processPayment(Integer userId, Float totalPrice);
}

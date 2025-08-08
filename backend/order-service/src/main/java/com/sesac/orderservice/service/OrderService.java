package com.sesac.orderservice.service;

import com.sesac.orderservice.client.ProductServiceClient;
import com.sesac.orderservice.client.UserServiceClient;
import com.sesac.orderservice.client.dto.ProductDto;
import com.sesac.orderservice.client.dto.UserDto;
import com.sesac.orderservice.dto.OrderRequestDto;
import com.sesac.orderservice.entity.Order;
import com.sesac.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {
  public final OrderRepository orderRepository;
  private final UserServiceClient userServiceClient;
  private final ProductServiceClient productServiceClient;

  public Order findById(Long id) {
      return orderRepository.findById(id).orElseThrow(
              () -> new RuntimeException(("User not found with id: "+id))
      );
  }

    @Transactional
    public Order createOrder(OrderRequestDto request) {

        UserDto user = userServiceClient.getUserById(request.getUserId());
        if (user == null) throw new RuntimeException("User not found");

        ProductDto product = productServiceClient.getProductById(request.getProductId());
        if (product == null) throw new RuntimeException("Product not found");

        if (product.getStockQuantity() < request.getQuantity()) {
            throw new RuntimeException("Out of stock");
        }

        Order order = new Order();
        order.setUserId(request.getUserId());
        order.setTotalAmount(product.getPrice().multiply(BigDecimal.valueOf(request.getQuantity())));
        order.setStatus("COMPLETED");

        return orderRepository.save(order);
    }


}

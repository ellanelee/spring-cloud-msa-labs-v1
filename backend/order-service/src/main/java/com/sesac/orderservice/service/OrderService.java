package com.sesac.orderservice.service;

import com.sesac.orderservice.entity.Order;
import com.sesac.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {
  public final OrderRepository orderRepository;
  public Order findById(Long id) {
      return orderRepository.findById(id).orElseThrow(
              () -> new RuntimeException(("User not found with id: "+id))
      );
  }
}

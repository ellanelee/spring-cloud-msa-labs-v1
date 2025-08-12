package com.sesac.orderservice.service;

import com.sesac.orderservice.client.ProductServiceClient;
import com.sesac.orderservice.client.dto.ProductDto;
import com.sesac.orderservice.client.dto.UserDto;
import com.sesac.orderservice.dto.OrderRequestDto;
import com.sesac.orderservice.entity.Order;
import com.sesac.orderservice.facade.UserServiceFacade;
import com.sesac.orderservice.repository.OrderRepository;

import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {
  private final OrderRepository orderRepository;
  private final ProductServiceClient productServiceClient;
  private final UserServiceFacade userServiceFacade;
  private final Tracer tracer;


  public Order findById(Long id) {
      return orderRepository.findById(id).orElseThrow(
              () -> new RuntimeException(("User not found with id: "+id))
      );
  }

    @Transactional
    public Order createOrder(OrderRequestDto request) {

       Span span = tracer.nextSpan()
               .name("createOrder")
               .tag("order.userId", request.getUserId())
               .tag("order.productId", request.getProductId())
               .start();

       try(Tracer.SpanInScope ss = tracer.withSpan(span)) {

           UserDto user = userServiceFacade.getUserById(request.getUserId());
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
       }catch(Exception e){
          span.tag("error", e.getMessage());
          throw e;
       }finally {
          span.end();
       }
    }

    public List<Order> getOrdersByUserId(Long userId) {
      return orderRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

}

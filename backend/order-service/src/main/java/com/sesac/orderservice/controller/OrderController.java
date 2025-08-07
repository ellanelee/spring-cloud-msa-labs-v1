package com.sesac.orderservice.controller;

import com.sesac.orderservice.entity.Order;
import com.sesac.orderservice.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/{id}")
    @Operation(summary="사용자 조회", description="ID로 사용자 정보조회합니다.")
    public ResponseEntity<Order> getUser(@PathVariable Long id) {
        try{
            Order order = orderService.findById(id);
            return ResponseEntity.ok(order);
        }catch(RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}

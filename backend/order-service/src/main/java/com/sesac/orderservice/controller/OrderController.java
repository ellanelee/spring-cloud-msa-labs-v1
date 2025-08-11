package com.sesac.orderservice.controller;

import com.sesac.orderservice.dto.OrderRequestDto;
import com.sesac.orderservice.entity.Order;
import com.sesac.orderservice.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody OrderRequestDto request) {
        try {
            Order order = orderService.createOrder(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(order);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/my")
    @Operation(summary="내 주문목록", description="로그인한 사용자의 주문목록조회")
    public ResponseEntity<List<Order>> getMyOrder(HttpServletRequest request) {
     // API GATEWAY에서 전달한 X-User-Id 헤더에서 사용자 ID추출
        String userIdHeader = request.getHeader("X-User-Id");
        if(userIdHeader == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        List<Order> order= orderService.getOrdersByUserId(Long.parseLong(userIdHeader));
        return ResponseEntity.ok(order);
    } //HttpServletRequest로 들어오는 내용은 HTTP 요청 원본에 해당, Header를 읽어들임
}

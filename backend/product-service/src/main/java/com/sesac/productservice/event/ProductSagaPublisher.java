package com.sesac.productservice.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductSagaPublisher {

    private final RabbitTemplate rabbitTemplate;

    @Value("${order.event.exchange}")
    private String exchange;

    @Value("${order.event.routing-key.payment-request}")
    private String paymentRequestRoutingKey;

    @Value("${order.event.routing-key.inventory-failed}")
    private String inventoryFailedRoutingKey;

    @Value("${order.event.routing-key.inventory-restore}")
    private String inventoryRestoreRoutingKey;



    // 재고 차감 성공 => 결재요청 이벤트 발행
    public void publishPaymentRequest(PaymentRequestEvent event) {
        log.info("결재요청 이벤트 발행 : ", event);
        rabbitTemplate.convertAndSend(exchange, paymentRequestRoutingKey, event);
    }

    // 재고 부족  => 재고실패 이벤트 발행
    public void publishInventoryFailed(InventoryFailedEvent event) {
       //rabbitMQ로 보내는 것(order서비스에 결재실패 이벤트 - 주문 취소, product서비스에 결재실패 이벤트-재고 복구)
        rabbitTemplate.convertAndSend(exchange, inventoryFailedRoutingKey, event);
        rabbitTemplate.convertAndSend(exchange, inventoryRestoreRoutingKey, event);
    }
}

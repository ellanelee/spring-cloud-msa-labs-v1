package com.sesac.orderservice.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderSagaHandler {
    //수신 이벤트 목록
    //InventoryFiledEvent
    //PaymentCompletedEvent
    //PaymentFailedEvent
    @RabbitListener( queues = "${order.event.queue.inventory-failed}")
    public void handleInventoryFailed(InventoryFailedEvent event){
        log.info("재고 실패 이벤트 수신: {}, reason: {} ", event.getOrderId(), event.getReason());

    }
    @RabbitListener( queues = "${order.event.queue.payment-completed}")
    public void handlePaymentCompleted(PaymentCompletedEvent event){
        log.info("결재 완료 이벤트 수신: {}, amount: {} ", event.getOrderId(), event.getAmount());
    }

    @RabbitListener( queues = "${order.event.queue.payment-failed}")
    public void handlePaymentFailed(PaymentFailedEvent event){
        log.info("결재 실패 이벤트 수신: {}, reason: {} ", event.getOrderId(), event.getReason());

    }

}

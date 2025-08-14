package com.sesac.paymentservice.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentSagaPublisher {

    private final RabbitTemplate rabbitTemplate;

    @Value("${order.event.exchange}")
    private String exchange;

    @Value("${order.event.routing-key.payment-completed}")
    private String paymentCompletedRoutingKey;

    @Value("${order.event.routing-key.payment-failed}")
    private String paymentFailedRoutingKey;

    // 재고 차감 성공 => 결재요청 이벤트 발행
    public void publishPaymentCompleted(PaymentCompletedEvent event) {
        rabbitTemplate.convertAndSend(exchange, paymentCompletedRoutingKey, event);
        log.info("결재성공 이벤트 발행완료");
    }

    public void publishPaymentFailed(PaymentFailedEvent event) {
        rabbitTemplate.convertAndSend(exchange, paymentFailedRoutingKey , event);
    }

}

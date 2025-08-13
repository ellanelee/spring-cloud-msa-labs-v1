package com.sesac.notificationservice.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderEventListener {

    //Queue를 바라보는 Listener설정
    @RabbitListener(queues ="${order.event.queue.notification}")
    public void handleOrderEvent(OrderCreatedEvent event) {
     log.info("주문 생성 Event수신 - orderId {}", event.getOrderId());

     try {
         Thread.sleep(3000);  //문자 등 알림보내는 시간(실제 서비스 생략)
         log.info("이메일 발송완료 - orderId {}", event.getOrderId());
     }catch(InterruptedException e) {
         e.printStackTrace();
         log.info("이메일 발송실패");
     }
    }
}

package com.sesac.paymentservice.service;

import com.sesac.paymentservice.entity.Payment;
import com.sesac.paymentservice.entity.PaymentStatus;
import com.sesac.paymentservice.event.PaymentRequestEvent;
import com.sesac.paymentservice.repository.PaymentRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Data
@Slf4j
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public Payment processPayment(PaymentRequestEvent event) {
        // 결재 성공과 실패 (random하게 줄수 있고, 조건을 통해 실패를 유발)
        Payment payment = new Payment();
        payment.setOrderId(event.getOrderId());
        payment.setUserId(event.getUserId());
        payment.setAmount(event.getTotalAmount());
        payment.setStatus(PaymentStatus.PENDING);
        payment.setPaymentMethod("CARD");

        Payment savedPayment = paymentRepository.save(payment);

        try {
            Thread.sleep(3000);
            if(Math.random() <0.3) {
                throw new RuntimeException("잔액 부족");
            }
            payment.setStatus(PaymentStatus.COMPLETED);
            paymentRepository.save(savedPayment);
        } catch (Exception e) {
            savedPayment.setStatus(PaymentStatus.FAILED);
            savedPayment.setFailureReason(e.getMessage());
            paymentRepository.save(savedPayment);
            throw new RuntimeException("결제실패");
        }
     return savedPayment;
    }
}

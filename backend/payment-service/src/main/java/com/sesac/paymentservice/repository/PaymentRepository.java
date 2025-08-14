package com.sesac.paymentservice.repository;

import com.sesac.paymentservice.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PaymentRepository extends JpaRepository<Payment, Long> {
}

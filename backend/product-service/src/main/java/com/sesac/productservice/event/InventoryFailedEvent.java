package com.sesac.productservice.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventoryFailedEvent {
    private static final long serialVersionUID = 1L;  //직렬화에 사용할 버전 정보 (스트림 처리)

    private Long orderId;
    private Long productId;
    private Integer quantity;
    private String reason;
}

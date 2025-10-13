package com.ecommerce.order.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class OrderConfirmedDto {
    private Long orderId;
    private String paymentId;
    private BigDecimal amountPaid;
}

package com.ecommerce.order.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BuyNowDto {

    @NotNull
    @Min(value=1)
    private Integer quantity;

    @NotBlank(message = "payment Mode required")
    private String paymentMode;
}

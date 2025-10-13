package com.ecommerce.product.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Map;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddProductDto {

    @NotBlank(message = "name is required")
    @Size(min=4)
    private String name;

    @NotBlank(message = "description is required")
    @Size(min=12)
    private String description;

    @NotNull
    @Min(value = 1)
    private BigDecimal price;

    @NotNull
    @Min(value = 1)
    private Integer stock;

    @NotBlank
    @Size(max=30)
    private String category;


    private Map<String,Object> attributes;
}

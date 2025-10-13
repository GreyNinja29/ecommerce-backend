package com.ecommerce.product.dto;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
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
public class UpdateProdDto {

    @NotBlank
    private String id;

    @Size(min=4)
    private String name;


    @Size(min=12)
    private String description;


    @Min(value = 1)
    private BigDecimal price;


    @Min(value = 1)
    private Integer stock;


    @Size(max=30)
    private String category;


    private Map<String,Object> attributes;


}

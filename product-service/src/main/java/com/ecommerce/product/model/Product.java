package com.ecommerce.product.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.Map;

@Document(collection = "product")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Product {
    @Id
    private String id;

    @TextIndexed
    private String name;
    @TextIndexed
    private String description;

    private BigDecimal price;

    private Integer stock;

    @TextIndexed
    private String category;

    private Map<String,Object> attributes;

}

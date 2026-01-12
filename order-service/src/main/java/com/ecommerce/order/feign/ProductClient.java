package com.ecommerce.order.feign;

import com.ecommerce.order.dto.ProductDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

//@FeignClient("PRODUCT-SERVICE")
@FeignClient(name = "product-service",url = "http://localhost:8082/")
public interface ProductClient {

    @GetMapping("products/{prodId}")
    public ProductDto getProdById(@PathVariable String prodId) ;


}

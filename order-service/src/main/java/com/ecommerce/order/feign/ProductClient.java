package com.ecommerce.order.feign;

import com.ecommerce.order.dto.ProductDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

//@FeignClient("PRODUCT-SERVICE")
@FeignClient(name = "product-service",url = "http://localhost:8082/")
public interface ProductClient {

    @GetMapping("products/{prodId}")
    public ProductDto getProdById(@PathVariable String prodId) ;

    @PutMapping("products/reduce-quantity/{prodId}")
    public ResponseEntity<?> reduceStock(@PathVariable String prodId, @RequestParam int quant);


}

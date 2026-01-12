package com.ecommerce.order.controller;

import com.ecommerce.order.dto.BuyNowDto;
import com.ecommerce.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    OrderService orderService;

    @GetMapping("/")
    public String greet() {
        return "Hola!";
    }

    @PostMapping("/checkout/{productId}")
    public ResponseEntity<?> checkOut(@PathVariable String productId,
                                      @Validated @RequestBody BuyNowDto buyNowDto, Principal principal) {
        String userId=principal.getName();
            return orderService.checkOut(productId,buyNowDto, Long.valueOf(userId));
    }
}


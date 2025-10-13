package com.ecommerce.order.service;


import com.ecommerce.order.dto.BuyNowDto;
import com.ecommerce.order.dto.OrderConfirmedDto;
import com.ecommerce.order.dto.ProductDto;
import com.ecommerce.order.feign.ProductClient;
import com.ecommerce.order.model.Order;
import com.ecommerce.order.model.OrderItem;
import com.ecommerce.order.model.OrderStatus;
import com.ecommerce.order.repository.OrderItemRepo;
import com.ecommerce.order.repository.OrdersRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    OrdersRepo ordersRepo;
    @Autowired
    OrderItemRepo orderItemRepo;

    @Autowired
    ProductClient productClient;


    @Transactional
    public ResponseEntity<?> checkOut(String productId, BuyNowDto buyNowDto,Long userId) {

        try {

            ProductDto product= productClient.getProdById(productId);

            if(buyNowDto.getQuantity()>product.getStock()) {
                return new ResponseEntity<>("Not Enough Stock",HttpStatus.BAD_REQUEST);
            }

            BigDecimal totalAmount=product.getPrice().multiply(
                    BigDecimal.valueOf(buyNowDto.getQuantity())
            );



            Order order=new Order();
            order.setUserId(userId);
            order.setTotalAmount(totalAmount);
            order.setStatus(OrderStatus.PENDING);
            order.setCreatedAt(LocalDateTime.now());



            OrderItem orderItem= OrderItem.builder()
                    .productId(productId)
                    .productName(product.getName())
                    .productPrice(product.getPrice())
                    .quantity(buyNowDto.getQuantity())
                    .order(order)
                    .build();

            order.setItems(List.of(orderItem));

            ordersRepo.save(order);

            // do some payment logic and get the payment id

            order.setPaymentId("234938490f9df");

            order.setStatus(OrderStatus.CONFIRMED);

            OrderConfirmedDto confirmendOrder=OrderConfirmedDto.builder()
                    .orderId(order.getOrderId())
                    .paymentId(order.getPaymentId())
                    .amountPaid(order.getTotalAmount())
                    .build();




            return new ResponseEntity<>(confirmendOrder,HttpStatus.OK);

        }catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}

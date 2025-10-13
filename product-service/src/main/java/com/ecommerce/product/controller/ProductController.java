package com.ecommerce.product.controller;

import com.ecommerce.product.dto.AddProductDto;
import com.ecommerce.product.dto.UpdateProdDto;
import com.ecommerce.product.model.Product;
import com.ecommerce.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {



    @Autowired
    ProductService productService;

    @GetMapping("/{prodId}")
    public ResponseEntity<?> getProdById(@PathVariable String prodId) {
        return productService.getProdById(prodId);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Product>> getAllProducts() {
        return productService.getAllProds();
    }

    @GetMapping("/search")
    public ResponseEntity<List<Product>> searchProducts(@RequestParam String keyword) {
        return productService.searchProducts(keyword);
    }

    @PostMapping("/add")
    public ResponseEntity<?> addProd(@Validated @RequestBody AddProductDto addProductDto) {
        return productService.addProd(addProductDto);

    }

    @PutMapping("/update")
    public ResponseEntity<?> updateProd(@Validated @RequestBody UpdateProdDto updateProdDto) {

        return productService.updateProd(updateProdDto);

    }

    @DeleteMapping("/{prodId}")
    public ResponseEntity<?> deleteProd(@PathVariable String prodId) {
        return productService.deleteProd(prodId);
    }








}

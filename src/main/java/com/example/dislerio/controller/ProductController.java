package com.example.dislerio.controller;

import com.example.dislerio.exception.ResourceNotFoundException;
import com.example.dislerio.model.Product;
import com.example.dislerio.repository.ProductsRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class ProductController {

    private static final Logger logger = LogManager.getLogger(ProductController.class);

    @Autowired
    ProductsRepository productsRepository;

    @GetMapping("/products")
    public List<Product> getTailings() {
        logger.info(getUserNameWithDate() + "getting Tailings");
        return productsRepository.findAll().stream().filter(p -> p.getQuantity() < 5).collect(Collectors.toList());
    }

    @PostMapping("/products")
    public Product createProduct(@Valid @RequestBody Product product) {

        logger.info(getUserNameWithDate() + "creating a new Product");
        return productsRepository.save(product);
    }

    @GetMapping("/products/{id}")
    public Product getProductById(@PathVariable(value = "id") Long productId) {
        logger.info(getUserNameWithDate() + "getting info about Product with ID: " + productId);
        return productsRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", productId));
    }

    @PutMapping("/products/{id}")
    public Product updateProduct(@PathVariable(value = "id") Long productId,
                              @Valid @RequestBody Product productDetails) {

        Product product = productsRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", productId));

        product.setTitle(productDetails.getTitle());
        product.setBrand(productDetails.getBrand());
        logger.warn(getUserNameWithDate() + "updating info about Product with ID: " + productId);
        return productsRepository.save(product);
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable(value = "id") Long productId) {
        Product product = productsRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", productId));
        productsRepository.delete(product);
        logger.debug(getUserNameWithDate() + "deleting Product with ID: " + productId);
        return ResponseEntity.ok().build();
    }
    private String getUserNameWithDate(){
        return SecurityContextHolder.getContext().getAuthentication().getName() + ": " + new Date() + " - ";
    }
}

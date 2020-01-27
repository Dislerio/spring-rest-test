package com.example.dislerio.controller;

import com.example.dislerio.exception.ResourceNotFoundException;
import com.example.dislerio.model.Product;
import com.example.dislerio.repository.ProductsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ProductController {

    @Autowired
    ProductsRepository productsRepository;

    @GetMapping("/products")
    public List<Product> getTailings() {
        return productsRepository.findAll();
    }

    @PostMapping("/products")
    public Product createProduct(@Valid @RequestBody Product product) {
        return productsRepository.save(product);
    }

    @GetMapping("/products/{id}")
    public Product getProductById(@PathVariable(value = "id") Long productId) {
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

        Product updatedProduct = productsRepository.save(product);
        return updatedProduct;
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable(value = "id") Long productId) {
        Product product = productsRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", productId));

        productsRepository.delete(product);

        return ResponseEntity.ok().build();
    }
}

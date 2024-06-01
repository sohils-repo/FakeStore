package com.scaler.demoproject.controller;

import com.scaler.demoproject.dto.CategoryDto;
import com.scaler.demoproject.dto.ProductDto;
import com.scaler.demoproject.exceptions.ProductNotFoundException;
import com.scaler.demoproject.model.Product;
import com.scaler.demoproject.service.ProductService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ProductController {

    private final ProductService productService;

    public ProductController(@Qualifier("selfproductservice") ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/products")
    public Product createProduct(@RequestBody Product product) {
       // Whenever someone is doing a post request on /product
       // Plz execute this method
        return productService.createProduct(product);
    }

    @GetMapping("/products/{id}")
    public Product getProduct(@PathVariable("id") Long productId) throws ProductNotFoundException {
        // Whenever someone is doing a get request on /product/{id}
        // Plz execute this method
        Product currentProduct = productService.getSingleProduct(productId);
        return currentProduct;
    }

    @GetMapping("/products")
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @DeleteMapping("/products/{id}")
    public String deleteProduct(@PathVariable("id") Long productId) throws ProductNotFoundException {
        return productService.deleteProduct(productId);
    }

    @PutMapping("/products")
    public Product updateProduct(@RequestBody Product product) throws ProductNotFoundException {
        return productService.updateProduct(product);
    }

    @GetMapping("/products/category/{category}")
    public List<Product> getProductsByCategory(@PathVariable("category") String category) {
        return productService.getProductsByCategory(category);
    }

    @GetMapping("/products/categories")
    public List<CategoryDto> getCategories() {
        return productService.getCategories();
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<String> handleProductNotFoundException(ProductNotFoundException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

}

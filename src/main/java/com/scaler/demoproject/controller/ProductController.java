package com.scaler.demoproject.controller;

import com.scaler.demoproject.model.Category;
import com.scaler.demoproject.model.Product;
import com.scaler.demoproject.service.FakeStoreProductService;
import com.scaler.demoproject.service.ProductService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@RestController
public class ProductController {

// POST /product
//    Request Body
//    {
//        "title": "Apple airpods",
//            "price": 25000,
//            "description": "Best airpods ever",
//            "image": "https://i.pravatar.cc",
//            "category": "electronic"
//    }

    private ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/products")
    public Product createProduct(@RequestBody Product product) {
       // Whenever someone is doing a post request on /product
       // Plz execute this method
        Product postRequestResponse = productService.createProduct(product);
        return postRequestResponse;
    }

    @GetMapping("/products/{id}")
    public Product getProduct(@PathVariable("id") Long productId) {
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
    public String deleteProduct(@PathVariable("id") Long productId) {
        return productService.deleteProduct(productId);
    }

    @PutMapping("/products")
    public String updateProduct(@RequestBody Product product) {
        return productService.updateProduct(product);
    }

    @GetMapping("/products/category/{category}")
    public List<Product> getProductsByCategory(@PathVariable("category") String category) {
        return productService.getProductsByCategory(category);
    }

    @GetMapping("/products/categories")
    public List<Category> getCategories() {
        return productService.getCategories();
    }
}

package com.scaler.demoproject.service;

import com.scaler.demoproject.model.Category;
import com.scaler.demoproject.model.Product;

import java.util.List;

public interface ProductService {
    Product getSingleProduct(Long productId);
    List<Product> getAllProducts();
    Product createProduct(Product product);

    String deleteProduct(Long productId);

    String updateProduct(Product product);

    List<Product> getProductsByCategory(String category);

    List<Category> getCategories();
}

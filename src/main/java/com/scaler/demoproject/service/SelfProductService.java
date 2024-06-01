package com.scaler.demoproject.service;

import com.scaler.demoproject.dto.CategoryDto;
import com.scaler.demoproject.exceptions.ProductNotFoundException;
import com.scaler.demoproject.model.Category;
import com.scaler.demoproject.model.Product;
import com.scaler.demoproject.repositories.CategoryRepository;
import com.scaler.demoproject.repositories.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service("selfproductservice")
public class SelfProductService implements ProductService {

    ProductRepository productRepository;
    CategoryRepository categoryRepository;

    public SelfProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Product getSingleProduct(Long productId) throws ProductNotFoundException {
        Optional<Product> product = productRepository.findById(productId);
        if (product.isPresent()) {
            return product.get();
        }
        throw new ProductNotFoundException("Product with Id " + productId + " not found");
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product createProduct(Product product) {
        Category cat = categoryRepository.findByTitle(product.getCategory().getTitle());
        if(cat == null) {
            // No category with our title in the database
            Category newCat = new Category();
            newCat.setTitle(product.getCategory().getTitle());
            Category newRow = categoryRepository.save(newCat);
            // newRow will have now catId
            product.setCategory(newRow);
        } else {
            product.setCategory(cat);
        }
        productRepository.save(product);
        return product;
    }

    @Override
    public String deleteProduct(Long productId) throws ProductNotFoundException {
        Product product = getSingleProduct(productId);
        product.setDeleted(true);
        productRepository.save(product);
        return null;
    }

    @Override
    @Transactional
    public Product updateProduct(Product product) throws ProductNotFoundException{
        Product existingProduct = getSingleProduct(product.getId());
        existingProduct.setTitle(product.getTitle());
        existingProduct.setDescription(product.getDescription());
        existingProduct.setPrice(product.getPrice());
        existingProduct.setCategory(product.getCategory());
        Date currentDate = new Date();
        existingProduct.setUpdatedAt(currentDate);
        return productRepository.save(existingProduct);
    }

    @Override
    public List<Product> getProductsByCategory(String title) {
        Category category = categoryRepository.findByTitle(title);
        if(category == null) {
            return List.of();
        }
        return category.getProducts();
    }

    @Override
    public List<CategoryDto> getCategories() {
        List<Category> categoryList = categoryRepository.findAll();
        List<CategoryDto> categoryDtoList = new ArrayList<>();
        for (Category category : categoryList) {
            CategoryDto categoryDto = new CategoryDto();
            categoryDto.setTitle(category.getTitle());
            categoryDtoList.add(categoryDto);
        }
        return categoryDtoList;
    }
}

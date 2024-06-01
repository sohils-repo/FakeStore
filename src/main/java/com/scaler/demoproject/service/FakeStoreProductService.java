package com.scaler.demoproject.service;

import com.scaler.demoproject.dto.CategoryDto;
import com.scaler.demoproject.dto.ProductDto;
import com.scaler.demoproject.model.Product;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service("fakestoreproductservice")
public class FakeStoreProductService implements ProductService{

    private final RestTemplate restTemplate;

    public FakeStoreProductService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public Product getSingleProduct(Long productId) {
        ProductDto fakeStoreProductDto = restTemplate.getForObject(
                "https://fakestoreapi.com/products/" + productId,
                ProductDto.class
        );

        //System.out.printf(fakeStoreProductDto.toString());

        return fakeStoreProductDto.toProduct();
    }

    @Override
    public List<Product> getAllProducts() {
        ProductDto[] fakeStoreProductDtoList = restTemplate.getForObject(
                "https://fakestoreapi.com/products",
                ProductDto[].class
        );
        return Arrays.stream(fakeStoreProductDtoList)
                .map(ProductDto::toProduct)
                .toList();
    }

    @Override
    public String deleteProduct(Long productId) {
        restTemplate.delete("https://fakestoreapi.com/products/" + productId);
        return "Product with id " + productId + " deleted successfully.";
    }


    @Override
    public Product updateProduct(Product product) {
        ProductDto fakeStoreProductDto = toFakeStoreProductDto(product);
        HttpEntity<ProductDto> request = new HttpEntity<>(fakeStoreProductDto);
        HttpEntity<ProductDto> updatedProduct = restTemplate.exchange("https://fakestoreapi.com/products/" + product.getId(),
                HttpMethod.PUT,
                request,
                ProductDto.class);
//        restTemplate.put(
//                "https://fakestoreapi.com/products/" + product.getId(),
//                fakeStoreProductDto
//        );
        return Objects.requireNonNull(updatedProduct.getBody()).toProduct();
//        return "Product with id " + product.getId() + " updated successfully.";
    }

    @Override
    public List<Product> getProductsByCategory(String category) {
        ProductDto[] fakeStoreProductDtos = restTemplate.getForObject(
                "https://fakestoreapi.com/products/category/" + category,
                ProductDto[].class
        );
        if (fakeStoreProductDtos != null) {
            return Arrays.stream(fakeStoreProductDtos)
                    .map(ProductDto::toProduct)
                    .toList();
        }
        return List.of();
    }

    @Override
    public List<CategoryDto> getCategories() {
        String[] fakeStoreCategory = restTemplate.getForObject(
                "https://fakestoreapi.com/products/categories",
                String[].class
        );

        if (fakeStoreCategory != null) {
            long id = 1L;
            List<CategoryDto> categories = new ArrayList<>();
            for (String category : fakeStoreCategory) {
                CategoryDto categoryDto = new CategoryDto();
                categoryDto.setTitle(category);
                categories.add(categoryDto);
            }
            return categories;
        }
        return List.of();
    }

    @Override
    public Product createProduct(Product product) {
        ProductDto fs = toFakeStoreProductDto(product);

        ProductDto response = restTemplate.postForObject(
              "https://fakestoreapi.com/products",
              fs,
              ProductDto.class
        );

//        Product p = new Product();
//        p.setId(response.getId());
//        // set all the variables and return p;
        return response.toProduct();
    }

    public ProductDto toFakeStoreProductDto(Product product) {
        ProductDto fs = new ProductDto();
        fs.setId(product.getId());
        fs.setTitle(product.getTitle());
        fs.setPrice(product.getPrice());
        fs.setDescription(product.getDescription());
        fs.setImage(product.getImageUrl());
        fs.setCategory(product.getCategory().getTitle());
        return fs;
    }
}

package com.scaler.demoproject.service;

import com.scaler.demoproject.dto.FakeStoreProductDto;
import com.scaler.demoproject.model.Category;
import com.scaler.demoproject.model.Product;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class FakeStoreProductService implements ProductService{

    private RestTemplate restTemplate;

    public FakeStoreProductService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public Product getSingleProduct(Long productId) {
        FakeStoreProductDto fakeStoreProductDto = restTemplate.getForObject(
                "https://fakestoreapi.com/products/" + productId,
                FakeStoreProductDto.class
        );

        //System.out.printf(fakeStoreProductDto.toString());

        return fakeStoreProductDto.toProduct();
    }

    @Override
    public List<Product> getAllProducts() {
        FakeStoreProductDto[] fakeStoreProductDtoList = restTemplate.getForObject(
                "https://fakestoreapi.com/products",
                FakeStoreProductDto[].class
        );
        return Arrays.stream(fakeStoreProductDtoList)
                .map(FakeStoreProductDto::toProduct)
                .toList();
    }

    @Override
    public String deleteProduct(Long productId) {
        restTemplate.delete("https://fakestoreapi.com/products/" + productId);
        return "Product with id " + productId + " deleted successfully.";
    }


    @Override
    public String updateProduct(Product product) {
        FakeStoreProductDto fakeStoreProductDto = toFakeStoreProductDto(product);
        restTemplate.put(
                "https://fakestoreapi.com/products/" + product.getId(),
                fakeStoreProductDto
        );
        return "Product with id " + product.getId() + " updated successfully.";
    }

    @Override
    public List<Product> getProductsByCategory(String category) {
        FakeStoreProductDto[] fakeStoreProductDtos = restTemplate.getForObject(
                "https://fakestoreapi.com/products/category/" + category,
                FakeStoreProductDto[].class
        );
        if (fakeStoreProductDtos != null) {
            return Arrays.stream(fakeStoreProductDtos)
                    .map(FakeStoreProductDto::toProduct)
                    .toList();
        }
        return List.of();
    }

    @Override
    public List<Category> getCategories() {
        String[] fakeStoreCategoryDtos = restTemplate.getForObject(
                "https://fakestoreapi.com/products/categories",
                String[].class
        );

        if (fakeStoreCategoryDtos != null) {
            long id = 1L;
            List<Category> categories = new ArrayList<>();
            for (String fakeStoreCategoryDto : fakeStoreCategoryDtos) {
                categories.add(new Category(id++, fakeStoreCategoryDto));
            }

            return categories;
        }
        return List.of();
    }

    @Override
    public Product createProduct(Product product) {
        FakeStoreProductDto fs = toFakeStoreProductDto(product);

        FakeStoreProductDto response = restTemplate.postForObject(
              "https://fakestoreapi.com/products",
              fs,
              FakeStoreProductDto.class
        );

//        Product p = new Product();
//        p.setId(response.getId());
//        // set all the variables and return p;
        return response.toProduct();
    }

    public FakeStoreProductDto toFakeStoreProductDto(Product product) {
        FakeStoreProductDto fs = new FakeStoreProductDto();
        fs.setId(product.getId());
        fs.setTitle(product.getTitle());
        fs.setPrice(product.getPrice());
        fs.setDescription(product.getDescription());
        fs.setImage(product.getImageUrl());
        fs.setCategory(product.getCategory().getTitle());
        return fs;
    }
}

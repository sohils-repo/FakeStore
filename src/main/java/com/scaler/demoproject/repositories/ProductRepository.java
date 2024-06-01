package com.scaler.demoproject.repositories;

import com.scaler.demoproject.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>{
//    Product save(Product product);
    Product findById(long id);
//    List<Product> findAllByDeleted(boolean deleted);
//    Product updateProductById(long id, Product product);
}

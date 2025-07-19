package com.sfc.project.product_service.repository;

import com.sfc.project.product_service.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository // Marks this interface as a Spring Data JPA repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    // You can define custom query methods here, Spring Data JPA will implement them automatically
    Optional<Product> findByName(String name);
    List<Product> findByCategory(String category);
    List<Product> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);
}
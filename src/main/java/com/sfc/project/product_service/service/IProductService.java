package com.sfc.project.product_service.service;

import com.sfc.project.product_service.model.Product;

import java.util.List;
import java.util.Optional;

public interface IProductService {
    Product createProduct(Product product);
    List<Product> getAllProducts();
    Optional<Product> getProductById(Long id);
    Product updateProduct(Long id, Product productDetails);
    void deleteProduct(Long id);
    Optional<Product> getProductByName(String name);
    List<Product> getProductsByCategory(String category);
    String getWelcomeMessage();
}

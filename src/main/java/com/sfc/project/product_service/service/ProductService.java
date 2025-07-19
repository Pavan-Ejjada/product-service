package com.sfc.project.product_service.service;

import com.sfc.project.product_service.model.Product;
import com.sfc.project.product_service.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service // Marks this class as a Spring Service component
@RequiredArgsConstructor // Lombok: Generates a constructor with required arguments (final fields)
public class ProductService implements IProductService{

    @Autowired
    ProductRepository productRepository;
    // Injects a value from configuration (potentially from Config Server)
    @Value("${product-catalog.welcome-message:Default welcome message}")
    private String welcomeMessage;


    @Override
    @CachePut(value = "products", key = "#product.id", condition = "#product.id != null", unless = "#result == null")
    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    @Cacheable(value = "products", unless = "#result == null")
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    @Cacheable(value = "products", key = "#id", unless = "#result == null") // Cache by product ID
    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    @Override
    @Transactional
    @CachePut(value = "products", key = "#id", unless = "#result == null")
    public Product updateProduct(Long id, Product productDetails) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));

        existingProduct.setName(productDetails.getName());
        existingProduct.setDescription(productDetails.getDescription());
        existingProduct.setPrice(productDetails.getPrice());
        existingProduct.setQuantity(productDetails.getQuantity());
        existingProduct.setCategory(productDetails.getCategory());

        return productRepository.save(existingProduct);
    }

    @Override
    @Transactional
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new RuntimeException("Product not found with id: " + id);
        }
        productRepository.deleteById(id);
    }

    @Override
    @Cacheable(value = "products", key = "#name", unless = "#result == null")
    public Optional<Product> getProductByName(String name) {
        return productRepository.findByName(name);
    }

    @Override
    @Cacheable(value = "products", key = "#category", unless = "#result == null")
    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategory(category);
    }

    @Override
    public String getWelcomeMessage() {
        return welcomeMessage;
    }
}
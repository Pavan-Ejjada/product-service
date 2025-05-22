package com.sfc.project.product_service.controller;

import com.sfc.project.product_service.model.Product;
import com.sfc.project.product_service.service.IProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // Marks this class as a REST controller
@RequestMapping("/api/products") // Base path for all endpoints in this controller
@Tag(name = "Product API", description = "Operations related to product catalog management") // Add a tag for the controller
public class ProductController {

    @Autowired
    IProductService productService;

    // Endpoint to test config server integration
    @Operation(summary = "Get welcome message from Config Server") // Custom operation summary
    @GetMapping("/welcome")
    public String getWelcomeMessage() {
        return productService.getWelcomeMessage();
    }

    @Operation(summary = "Create a new product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Product created successfully", content = @Content(schema = @Schema(implementation = Product.class))),
            @ApiResponse(responseCode = "400", description = "Invalid product details supplied")
    })
    @PostMapping(produces = {MediaType.APPLICATION_JSON_VALUE}) // Handles POST requests to /api/products (Create Product)
    @ResponseStatus(HttpStatus.CREATED) // Sets HTTP status to 201 Created on success
    public Product createProduct(@RequestBody Product product) {
        return productService.createProduct(product);
    }

    @Operation(summary = "Get all products")
    @GetMapping // Handles GET requests to /api/products (Get All Products)
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @Operation(summary = "Get a product by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product found", content = @Content(schema = @Schema(implementation = Product.class))),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @GetMapping(value = "/{id}",produces = {MediaType.APPLICATION_JSON_VALUE}) // Handles GET requests to /api/products/{id} (Get Product by ID)
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        return productService.getProductById(id)
                .map(ResponseEntity::ok) // If product found, return 200 OK with product body
                .orElse(ResponseEntity.notFound().build()); // If not found, return 404 Not Found
    }

    @Operation(summary = "Update an existing product by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product updated successfully"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @PutMapping("/{id}") // Handles PUT requests to /api/products/{id} (Update Product)
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product productDetails) {
        try {
            Product updatedProduct = productService.updateProduct(id, productDetails);
            return ResponseEntity.ok(updatedProduct); // Return 200 OK with updated product
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build(); // Return 404 if product not found
        }
    }

    @Operation(summary = "Delete a product by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Product deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @DeleteMapping("/{id}") // Handles DELETE requests to /api/products/{id} (Delete Product)
    @ResponseStatus(HttpStatus.NO_CONTENT) // Sets HTTP status to 204 No Content on success
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        try {
            productService.deleteProduct(id);
            return ResponseEntity.noContent().build(); // Return 204 No Content
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build(); // Return 404 if product not found
        }
    }

    // --- Custom Search Endpoints ---
    @Operation(summary = "Search for a product by name")
    @GetMapping("/search/name") // Example: /api/products/search/name?name=Laptop Pro 15
    public ResponseEntity<Product> getProductByName(@RequestParam String name) {
        return productService.getProductByName(name)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Search for products by category")
    @GetMapping("/search/category") // Example: /api/products/search/category?category=Electronics
    public List<Product> getProductsByCategory(@RequestParam String category) {
        return productService.getProductsByCategory(category);
    }
}
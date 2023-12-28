package com.example.demo.service;


import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Product;
import com.example.demo.exception.ProductServiceException;
import com.example.demo.repository.ProductRepository;

@Service
public class ProductService {

 private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

 @Autowired
 private ProductRepository productRepository;

 public Product addProduct(Product product) {
     try {
         if (product.getName() == null || product.getPrice() <= 0) {
             throw new ProductServiceException("Invalid product data");
         }
         return productRepository.save(product);
     } catch (Exception e) {
         logger.error("Error adding product: {}", e.getMessage(), e);
         throw new ProductServiceException("Error adding product");
     }
 }

 public List<Product> getAllProducts() {
     try {
         return productRepository.findAll();
     } catch (Exception e) {
         logger.error("Error fetching all products: {}", e.getMessage(), e);
         throw new ProductServiceException("Error fetching all products");
     }
 }

 public Product getProductById(Long id) {
     try {
         Optional<Product> optionalProduct = productRepository.findById(id);
         return optionalProduct.orElseThrow(() -> new ProductServiceException("Product not found"));
     } catch (Exception e) {
         logger.error("Error fetching product by ID {}: {}", id, e.getMessage(), e);
         throw new ProductServiceException("Error fetching product by ID");
     }
 }
}

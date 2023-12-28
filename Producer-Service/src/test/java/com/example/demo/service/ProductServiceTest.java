package com.example.demo.service;

import com.example.demo.entity.Product;
import com.example.demo.exception.ProductServiceException;
import com.example.demo.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    void testAddProduct() {
        Product product = new Product();
        product.setName("Test Product");
        product.setPrice(20.0);

        when(productRepository.save(any(Product.class))).thenReturn(product);

        Product result = productService.addProduct(product);

        assertNotNull(result);
        assertEquals("Test Product", result.getName());
        assertEquals(20.0, result.getPrice());
    }

    @Test
    void testAddProduct_InvalidData() {
        Product invalidProduct = new Product(); // Invalid product with null name and zero price

        try {
            productService.addProduct(invalidProduct);
            fail("Expected ProductServiceException, but no exception was thrown");
        } catch (ProductServiceException e) {
            assertEquals("Error adding product", e.getMessage());
        }
    }

    @Test
    void testGetAllProducts() {
        Product product1 = new Product();
        product1.setId(1L);
        product1.setName("Product 1");
        product1.setPrice(30.0);

        Product product2 = new Product();
        product2.setId(2L);
        product2.setName("Product 2");
        product2.setPrice(40.0);

        List<Product> productList = Arrays.asList(product1, product2);

        when(productRepository.findAll()).thenReturn(productList);

        List<Product> result = productService.getAllProducts();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Product 1", result.get(0).getName());
        assertEquals(30.0, result.get(0).getPrice());
        assertEquals("Product 2", result.get(1).getName());
        assertEquals(40.0, result.get(1).getPrice());
    }

    @Test
    void testGetProductById() {
        Long productId = 1L;
        Product product = new Product();
        product.setId(productId);
        product.setName("Test Product");
        product.setPrice(20.0);

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        Product result = productService.getProductById(productId);

        assertNotNull(result);
        assertEquals("Test Product", result.getName());
        assertEquals(20.0, result.getPrice());
    }

    @Test
    void testGetProductById_ProductNotFound() {
        Long productId = 1L;

        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        try {
            productService.getProductById(productId);
            fail("Expected ProductServiceException, but no exception was thrown");
        } catch (ProductServiceException e) {
            assertEquals("Error fetching product by ID", e.getMessage());
        }
    }
}


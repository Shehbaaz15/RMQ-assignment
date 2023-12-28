package com.example.demo.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.Product;
import com.example.demo.service.ProductService;

@RestController
@RequestMapping("/api/products")
public class ProductController {

	private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

	@Autowired
	private ProductService productService;
	
	@Autowired
    private RabbitTemplate rabbitTemplate;

	@PostMapping
	public Product addProduct(@RequestBody Product product) {
		try {
			return productService.addProduct(product);
		} catch (Exception e) {
			logger.error("Error adding product: {}", e.getMessage(), e);
			throw e;
		}
	}

	@GetMapping
	public List<Product> getAllProducts() {
		try {
			return productService.getAllProducts();
		} catch (Exception e) {
			logger.error("Error fetching all products: {}", e.getMessage(), e);
			throw e;
		}
	}

	@GetMapping("/{id}")
	public Product getProductById(@PathVariable Long id) {
		try {
			Product product= productService.getProductById(id);
			rabbitTemplate.convertAndSend("test-queue", product.toString());
	       return product;
		} catch (Exception e) {
			logger.error("Error fetching product by ID {}: {}", id, e.getMessage(), e);
			throw e;
		}
	}
}

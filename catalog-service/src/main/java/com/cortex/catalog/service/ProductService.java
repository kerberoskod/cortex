package com.cortex.catalog.service;

import com.cortex.common.dto.ProductDTO;
import com.cortex.catalog.model.Product;
import com.cortex.catalog.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll().stream()
                .map(this::toDTO)
                .toList();
    }

    public ProductDTO getProductById(Long id) {
        var product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));
        return toDTO(product);
    }

    public ProductDTO createProduct(ProductDTO dto) {
        var product = new Product(dto.getName(), dto.getDescription(), dto.getPrice(), dto.getStock());
        product = productRepository.save(product);
        return toDTO(product);
    }

    public ProductDTO updateProduct(Long id, ProductDTO dto) {
        var product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setStock(dto.getStock());
        product = productRepository.save(product);
        return toDTO(product);
    }

    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new IllegalArgumentException("Product not found");
        }
        productRepository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return productRepository.existsById(id);
    }

    private ProductDTO toDTO(Product product) {
        return new ProductDTO(product.getId(), product.getName(),
                product.getDescription(), product.getPrice(), product.getStock());
    }
}

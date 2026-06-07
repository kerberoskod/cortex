package com.cortex.catalog.config;

import com.cortex.catalog.model.Product;
import com.cortex.catalog.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class DataSeeder implements CommandLineRunner {
    private final ProductRepository productRepository;

    public DataSeeder(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public void run(String... args) {
        if (productRepository.count() > 0) return;

        productRepository.save(new Product("Wireless Headphones", "Noise-cancelling Bluetooth headphones with 30h battery", new BigDecimal("149.99"), 50));
        productRepository.save(new Product("Mechanical Keyboard", "RGB mechanical keyboard with Cherry MX switches", new BigDecimal("89.99"), 30));
        productRepository.save(new Product("USB-C Hub", "7-in-1 USB-C hub with HDMI, USB 3.0, SD card reader", new BigDecimal("34.99"), 100));
        productRepository.save(new Product("27\" Monitor", "4K IPS 27-inch monitor with USB-C connectivity", new BigDecimal("499.99"), 15));
        productRepository.save(new Product("Laptop Stand", "Adjustable aluminum laptop stand with ventilation", new BigDecimal("39.99"), 75));
    }
}

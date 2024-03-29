package org.techie.produceservice.service;

import org.techie.produceservice.dto.ProductRequest;
import org.techie.produceservice.dto.ProductResponse;
import org.techie.produceservice.model.Product;
import org.techie.produceservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;

    public void createProduct(ProductRequest productRequest){
        Product product = Product.builder()
            .name(productRequest.getName())
            .description(productRequest.getDescription())
            .price(productRequest.getPrice())
            .build();
        productRepository.save(product);
        log.info("Product {} is created", product.getId());
    }

    public List<ProductResponse> getProducts(){
        List<Product> products = productRepository.findAll();
        return products.stream().map(p -> mapToProductResponse(p)).collect(Collectors.toList());
    }

    private ProductResponse mapToProductResponse(Product product){
        return ProductResponse.builder()
            .name(product.getName())
            .description(product.getDescription())
            .price(product.getPrice())
            .build();
    }

}

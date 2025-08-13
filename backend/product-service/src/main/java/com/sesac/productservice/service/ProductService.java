package com.sesac.productservice.service;

import com.sesac.productservice.entity.Product;
import com.sesac.productservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public Product findById(Long id) {
        return productRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Product not found with id: " + id)
        );
    }

    @Transactional
    public void decreaseStock(Long productId, Integer quantity) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new RuntimeException("상품을 찾을수 없습니다: " + productId));

        if(product.getStockQuantity() < quantity){
            throw new RuntimeException("재고가 부족합니다.");
        }

        product.setStockQuantity(product.getStockQuantity() - quantity);
        productRepository.save(product); //Jpa는 save를 안해도 저절로 반영되기는 함

        log.info("재고 차감완료- 상품: {}, 남은재고: {}", product.getName(), product.getStockQuantity());
    }
}

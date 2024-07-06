package com.example.ShopAcc.repository;

import com.example.ShopAcc.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    Page<Product> findProductTypeByNameContainingAndPriceBetween(String productname, int minPrice, int maxPrice, Pageable pageable);
    Page<Product> findProductTypeByNameContainingAndPriceGreaterThanEqual(String productname, int minPrice, Pageable pageable);
    Page<Product> findProductTypeByNameContainingAndPriceLessThanEqual(String productname, int maxPrice, Pageable pageable);

    List<Product> findByStatus(boolean status);
}

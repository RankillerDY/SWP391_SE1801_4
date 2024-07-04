package com.example.ShopAcc.service;

import com.example.ShopAcc.model.Product;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductService {
    List<Product> getAll();
    Page<Product> getProducts(String search, int page, int size, String sortDirection, String priceRange);
}

package com.example.ShopAcc.service.Impl;

import com.example.ShopAcc.model.Product;
import com.example.ShopAcc.repository.ProductRepository;
import com.example.ShopAcc.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    ProductRepository productRepository;

    @Override
    public List<Product> getAll() {
        return productRepository.findAll();
    }

    @Override
    public Page<Product> getProducts(String search, int page, int size, String sortDirection, String priceRange) {
        Sort sort = Sort.by("price");
        sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? sort.ascending() : sort.descending();
        PageRequest pageRequest = PageRequest.of(page, size, sort);

        int minPrice = 0;
        int maxPrice = Integer.MAX_VALUE;

        if (priceRange != null && !priceRange.isEmpty()) {
            String[] priceBounds = priceRange.split("-");
            if (!priceBounds[0].isEmpty()) {
                minPrice = Integer.parseInt(priceBounds[0]);
            }
            if (priceBounds.length > 1 && !priceBounds[1].isEmpty()) {
                maxPrice = Integer.parseInt(priceBounds[1]);
            }
        }

        if (maxPrice == Integer.MAX_VALUE) {
            return productRepository.findProductTypeByNameContainingAndPriceGreaterThanEqual(search, minPrice, pageRequest);
        } else if (minPrice == 0) {
            return productRepository.findProductTypeByNameContainingAndPriceLessThanEqual(search, maxPrice, pageRequest);
        } else {
            return productRepository.findProductTypeByNameContainingAndPriceBetween(search, minPrice, maxPrice, pageRequest);
        }
    }
}

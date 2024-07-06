package com.example.ShopAcc.service;

import com.example.ShopAcc.dto.ProductDto;
import com.example.ShopAcc.dto.ResponseObject;
import com.example.ShopAcc.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ProductService {
    ResponseEntity<ResponseObject> getAll();
    Page<Product> getProducts(String search, int page, int size, String sortDirection, String priceRange);
    ResponseEntity<ResponseObject> createProduct(ProductDto productObj);
    ResponseEntity<ResponseObject> editProduct(int id, ProductDto productDto);
    ResponseEntity<ResponseObject> softDelete(int id);
    ResponseEntity<ResponseObject> filterByStatus(boolean status);
}

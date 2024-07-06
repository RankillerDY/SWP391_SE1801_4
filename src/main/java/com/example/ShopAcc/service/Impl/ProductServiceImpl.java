package com.example.ShopAcc.service.Impl;

import com.example.ShopAcc.dto.ProductDto;
import com.example.ShopAcc.dto.ResponseObject;
import com.example.ShopAcc.model.Product;
import com.example.ShopAcc.repository.ProductRepository;
import com.example.ShopAcc.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    ProductRepository productRepository;

    @Override
    public ResponseEntity<ResponseObject> getAll() {
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("Successfully", "Get all products", productRepository.findAll()));
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

    @Override
    public ResponseEntity<ResponseObject> createProduct(ProductDto productDto) {
        // Validate the productDto
        String validationError = validateProductDto(productDto);
        if (validationError != null) {
            ResponseObject errorResponse = new ResponseObject();
            errorResponse.setStatus("error");
            errorResponse.setMessage(validationError);
            errorResponse.setData(null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }

        Product product = Product.builder()
                .name(productDto.getName())
                .price(productDto.getPrice())
                .Sold(productDto.getSold())
                .image(productDto.getImage())
                .Describes(productDto.getDescribes())
                .status(productDto.isStatus())
                .build();

        Product savedProduct = productRepository.save(product);

        ResponseObject responseObject = new ResponseObject();
        responseObject.setStatus("success");
        responseObject.setMessage("Product created successfully");
        responseObject.setData(savedProduct);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseObject);
    }

    @Override
    public ResponseEntity<ResponseObject> editProduct(int id, ProductDto productDto) {
        try {
            // Validate the productDto
            String validationError = validateProductDto(productDto);
            if (validationError != null) {
                ResponseObject errorResponse = new ResponseObject();
                errorResponse.setStatus("error");
                errorResponse.setMessage(validationError);
                errorResponse.setData(null);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
            }

            // Find the existing product by ID
            Optional<Product> existingProductOpt = productRepository.findById(id);
            if (!existingProductOpt.isPresent()) {
                ResponseObject errorResponse = new ResponseObject();
                errorResponse.setStatus("error");
                errorResponse.setMessage("Product not found");
                errorResponse.setData(null);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
            }

            Product existingProduct = existingProductOpt.get();

            // Update the existing product with new values
            existingProduct.setName(productDto.getName());
            existingProduct.setPrice(productDto.getPrice());
            existingProduct.setSold(productDto.getSold());
            existingProduct.setImage(productDto.getImage());
            existingProduct.setDescribes(productDto.getDescribes());
            existingProduct.setStatus(productDto.isStatus());

            // Save the updated product to the repository
            Product updatedProduct = productRepository.save(existingProduct);

            // Create the response object
            ResponseObject responseObject = new ResponseObject();
            responseObject.setStatus("success");
            responseObject.setMessage("Product updated successfully");
            responseObject.setData(updatedProduct);

            return ResponseEntity.status(HttpStatus.OK).body(responseObject);
        } catch (Exception e) {
            ResponseObject errorResponse = new ResponseObject();
            errorResponse.setStatus("error");
            errorResponse.setMessage("Internal server error");
            errorResponse.setData(null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @Override
    public ResponseEntity<ResponseObject> softDelete(int id) {
        try {
            // Find the existing product by ID
            Optional<Product> existingProductOpt = productRepository.findById(id);
            if (!existingProductOpt.isPresent()) {
                ResponseObject errorResponse = new ResponseObject();
                errorResponse.setStatus("error");
                errorResponse.setMessage("Product not found");
                errorResponse.setData(null);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
            }

            Product existingProduct = existingProductOpt.get();

            // Toggle the product status
            existingProduct.setStatus(!existingProduct.isStatus());

            // Save the updated product to the repository
            Product updatedProduct = productRepository.save(existingProduct);

            // Create the response object
            ResponseObject responseObject = new ResponseObject();
            responseObject.setStatus("success");
            responseObject.setMessage("Product status updated successfully");
            responseObject.setData(updatedProduct);

            return ResponseEntity.status(HttpStatus.OK).body(responseObject);
        } catch (Exception e) {
            ResponseObject errorResponse = new ResponseObject();
            errorResponse.setStatus("error");
            errorResponse.setMessage("Internal server error");
            errorResponse.setData(null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    private String validateProductDto(ProductDto productDto) {
        if (productDto == null) {
            return "Product data is required";
        }
        if (!StringUtils.hasText(productDto.getName())) {
            return "Product name is required";
        }
        if (productDto.getPrice() <= 0) {
            return "Product price must be greater than zero";
        }
        if (productDto.getSold() < 0) {
            return "Product sold count cannot be negative";
        }
        if (!StringUtils.hasText(productDto.getImage())) {
            return "Product image URL is required";
        }
        if (!StringUtils.hasText(productDto.getDescribes())) {
            return "Product description is required";
        }
        return null;
    }
}

package com.example.ShopAcc.controller;

import com.example.ShopAcc.dto.ProductDto;
import com.example.ShopAcc.dto.ResponseObject;
import com.example.ShopAcc.dto.UserDto;
import com.example.ShopAcc.model.Product;
import com.example.ShopAcc.repository.ProductRepository;
import com.example.ShopAcc.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/product")
//@SessionAttributes("userdto")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @ModelAttribute("userdto")
    public UserDto userDto() {
        return new UserDto();
    }

    @GetMapping("/Product")
    public String Productshow(@ModelAttribute("userdto") UserDto userDto,
                              @RequestParam(defaultValue = "") String search,
                              @RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "4") int size,
                              @RequestParam(defaultValue = "asc") String sort,
                              @RequestParam(defaultValue = "") String priceRange,
                              Model model) {
        Page<Product> products = productService.getProducts(search, page, size, sort, priceRange);
        System.out.println("Hello: " + products.getContent());
        model.addAttribute("products", products.getContent());
        model.addAttribute("totalPages", products.getTotalPages());
        model.addAttribute("currentPage", page);
        model.addAttribute("search", search);
        model.addAttribute("sort", sort);
        model.addAttribute("priceRange", priceRange);
        return "product";
    }

    @GetMapping("/")
    ResponseEntity<ResponseObject> getProducts() {
        return productService.getAll();
    }

    @GetMapping("/filter")
    ResponseEntity<ResponseObject> getProductsFiltered(@RequestParam(value = "status") Boolean status) {
        return productService.filterByStatus(status);
    }

    @PostMapping("/create")
    ResponseEntity<ResponseObject> createProduct(@RequestBody ProductDto productObj) {
        return productService.createProduct(productObj);
    }

    @PutMapping("/edit/{id}")
    ResponseEntity<ResponseObject> editProduct(@RequestBody ProductDto productObj, @PathVariable(name = "id") Integer id) {
        return productService.editProduct(id, productObj);
    }

    @DeleteMapping("/delete/{id}")
    ResponseEntity<ResponseObject> createProduct(@PathVariable(name = "id") Integer id) {
        return productService.softDelete(id);
    }
}

package com.example.ShopAcc.controller;

import com.example.ShopAcc.dto.UserDto;
import com.example.ShopAcc.model.Product;
import com.example.ShopAcc.repository.ProductRepository;
import com.example.ShopAcc.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("")
@SessionAttributes("userdto")
public class ProductControll {
    @Autowired
    ProductRepository productRepository;
    @Autowired
    ProductService productService;
    @ModelAttribute("userdto")
    public UserDto userDto(){
        return new UserDto();
    }

    @GetMapping("/Product")
    public String Productshow(@ModelAttribute("userdto") UserDto userDto,
                              @RequestParam(defaultValue = "") String search,
                              @RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "4") int size,
                              @RequestParam(defaultValue = "asc") String sort,
                              @RequestParam(defaultValue = "") String priceRange,
                              Model model)  {
        Page<Product> products = productService.getProducts(search, page, size, sort, priceRange);
        System.out.println("Hello: "+products.getContent());
        model.addAttribute("products", products.getContent());
        model.addAttribute("totalPages", products.getTotalPages());
        model.addAttribute("currentPage", page);
        model.addAttribute("search", search);
        model.addAttribute("sort", sort);
        model.addAttribute("priceRange", priceRange);
        return "product";
    }
}

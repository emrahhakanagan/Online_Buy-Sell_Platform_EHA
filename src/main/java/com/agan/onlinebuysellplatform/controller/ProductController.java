package com.agan.onlinebuysellplatform.controller;

import com.agan.onlinebuysellplatform.model.Product;
import com.agan.onlinebuysellplatform.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("/")
    public String products(Model model) {
        model.addAttribute("products", productService.listProducts());
        return "products";
    }


    @GetMapping("/product/{id}")
    public String productInfo(@PathVariable Long id, Model model) {
        Optional<Product> optionalProduct = productService.getProductById(id);

        if(optionalProduct.isPresent()) {
            model.addAttribute("product", optionalProduct.get());

            return "product-info";
        } else {
            return "redirect:/";
        }

    }


    @PostMapping("/product/create")
    public String createProduct(Product product) {
        productService.saveProduct(product);

        return "redirect:/";
    }

    @PostMapping("/product/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);

        return "redirect:/";
    }

}

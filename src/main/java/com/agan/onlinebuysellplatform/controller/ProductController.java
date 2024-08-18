package com.agan.onlinebuysellplatform.controller;

import com.agan.onlinebuysellplatform.model.Product;
import com.agan.onlinebuysellplatform.model.User;
import com.agan.onlinebuysellplatform.service.GermanCityService;
import com.agan.onlinebuysellplatform.service.ProductService;
import com.agan.onlinebuysellplatform.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    private final GermanCityService germanCityService;
    private final UserService userService;

    @GetMapping("/")
    public String products(@RequestParam(name = "searchWord", required = false) String keyword,
                           @RequestParam(name = "cityId", required = false) Long cityId,
                           Principal principal, Model model) {

        List<Product> products = productService.searchProduct(cityId, keyword);
        String messageSearchProduct = productService.showMessageSearchProduct(cityId, keyword, products);

        model.addAttribute("products", products);
        model.addAttribute("user", productService.getUserByPrincipal(principal));
        model.addAttribute("searchWord", keyword);
        model.addAttribute("allCities", germanCityService.getAllCities());
        model.addAttribute("messageSearchProduct", messageSearchProduct);

        return "products";
    }

    @GetMapping("/product/{id}")
    public String productInfo(@PathVariable Long id, Model model, Principal principal) {
        Product product = productService.getProductById(id);
        model.addAttribute("user", productService.getUserByPrincipal(principal));
        model.addAttribute("product", product);
        model.addAttribute("images", product.getImages());
        model.addAttribute("authorProduct", product.getUser());
        return "product-info";
    }

    @PostMapping("/product/create")
    public String createProduct(@Valid @ModelAttribute("product") Product product, BindingResult bindingResult,
                                Principal principal, Model model,
                                @RequestParam(value = "file1", required = false) MultipartFile file1,
                                @RequestParam(value = "file2", required = false) MultipartFile file2,
                                @RequestParam(value = "file3", required = false) MultipartFile file3,
                                @RequestParam List<Long> cityIds) {
        if (bindingResult.hasErrors()) {
                model.addAttribute("cities", germanCityService.getAllCities());
                return "add-product";
        }

        productService.saveProduct(principal, product, cityIds, file1, file2, file3);
        return "redirect:/my/products";
    }

    @GetMapping("/product/edit/{id}")
    public String editProductForm(@PathVariable Long id, Model model, Principal principal) {
        Product product = productService.getProductById(id);
        if (product == null || !product.getUser().equals(userService.getUserByPrincipal(principal))) {
            return "redirect:/my/products";
        }
        model.addAttribute("product", product);
        model.addAttribute("cities", germanCityService.getAllCities());
        model.addAttribute("user", userService.getUserByPrincipal(principal));
        model.addAttribute("images", product.getImages());
        return "edit-product";
    }

    @PostMapping("/product/edit/{id}")
    public String editProduct(@PathVariable Long id, @Valid @ModelAttribute("product") Product product,
                              BindingResult bindingResult, Principal principal, Model model,
                              @RequestParam("cityIds") List<Long> cityIds,
                              @RequestParam("file1") MultipartFile file1,
                              @RequestParam("file2") MultipartFile file2,
                              @RequestParam("file3") MultipartFile file3) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("bindingResult", bindingResult);
            model.addAttribute("cities", germanCityService.getAllCities());
            return "edit-product";
        }
        productService.updateProduct(id, principal, product, cityIds, file1, file2, file3);
        return "redirect:/my/products";
    }

    @PostMapping("/product/delete/{id}")
    public String deleteProduct(@PathVariable Long id, Principal principal) {
        productService.deleteProduct(id);

        return "redirect:/my/products";
    }

    @GetMapping("/my/products")
    public String userProducts(Principal principal, Model model) {
        User user = productService.getUserByPrincipal(principal);

        model.addAttribute("user", user);
        model.addAttribute("products", user.getProducts());
        model.addAttribute("germanCities", germanCityService.getAllCities());
        return "my-products";
    }
}
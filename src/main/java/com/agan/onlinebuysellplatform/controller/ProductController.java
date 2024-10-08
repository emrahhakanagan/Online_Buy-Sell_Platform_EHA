package com.agan.onlinebuysellplatform.controller;

import com.agan.onlinebuysellplatform.model.Product;
import com.agan.onlinebuysellplatform.model.User;
import com.agan.onlinebuysellplatform.model.enums.ConfigurationLimits;
import com.agan.onlinebuysellplatform.service.GermanCityService;
import com.agan.onlinebuysellplatform.service.ProductService;
import com.agan.onlinebuysellplatform.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

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
    public ResponseEntity<?> createProduct(@Valid @ModelAttribute("product") Product product,
                                           BindingResult bindingResult,
                                           Principal principal,
                                           @RequestParam List<Long> cityIds,
                                           @RequestParam(value = "files", required = false) MultipartFile... files) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = bindingResult.getFieldErrors().stream()
                    .collect(Collectors.toMap(
                            FieldError::getField,
                            fieldError -> Objects.requireNonNullElse(fieldError.getDefaultMessage(), "Unknown error")
                    ));
            return ResponseEntity.badRequest().body(Map.of("errors", errors));
        }

        productService.saveProduct(principal, product, cityIds, files);
        return ResponseEntity.ok().build();
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
        model.addAttribute("imagesLimit", ConfigurationLimits.IMAGE_NUMBER_OF_PRODUCT.getValue());
        return "edit-product";
    }

    @PostMapping("/product/edit/{id}")
    public String editProduct(@PathVariable Long id, @Valid @ModelAttribute("product") Product product,
                              BindingResult bindingResult, Principal principal, Model model,
                              @RequestParam("cityIds") List<Long> cityIds,
                              @RequestParam("files") MultipartFile... files) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("bindingResult", bindingResult);
            model.addAttribute("cities", germanCityService.getAllCities());
            model.addAttribute("imagesLimit", ConfigurationLimits.IMAGE_NUMBER_OF_PRODUCT.getValue());
            return "edit-product";
        }
        productService.updateProduct(id, principal, product, cityIds, files);
        return "redirect:/my/products";
    }

    @DeleteMapping("/product/delete/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id, Principal principal) {
        productService.deleteProduct(id, principal);
        return ResponseEntity.ok().build();
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
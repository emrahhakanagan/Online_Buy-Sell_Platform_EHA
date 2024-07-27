package com.agan.onlinebuysellplatform.controller;

import com.agan.onlinebuysellplatform.model.Product;
import com.agan.onlinebuysellplatform.model.User;
import com.agan.onlinebuysellplatform.service.GermanCityService;
import com.agan.onlinebuysellplatform.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    private final GermanCityService germanCityService;

    @GetMapping("/")
    public String products(@RequestParam(name = "searchWord", required = false) String keyword, Principal principal, Model model) {
        List<Product> products;
        String messageSearchProduct;

        if (keyword != null && !keyword.isBlank()) {
            products = productService.searchProductByKeywordTitle(keyword);
            messageSearchProduct = products.size() + " Product(s) found based on the request";
        } else {
            products = productService.listProducts(null);
            messageSearchProduct = "No products found based on the request! You can see other products on our platform;";
        }
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
    public String createProduct(@RequestParam("file1") MultipartFile file1,
                                @RequestParam("file2") MultipartFile file2,
                                @RequestParam("file3") MultipartFile file3,
                                Product product,
                                Principal principal,
                                @RequestParam List<Long> cityIds) {
        productService.saveProduct(principal, product, cityIds, file1, file2, file3);
        return "redirect:/my/products";
    }

    @PostMapping("/product/delete/{id}")
    public String deleteProduct(@PathVariable Long id, Principal principal) {
        productService.deleteProduct(productService.getUserByPrincipal(principal), id);
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

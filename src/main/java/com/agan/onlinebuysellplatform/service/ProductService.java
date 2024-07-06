package com.agan.onlinebuysellplatform.service;

import com.agan.onlinebuysellplatform.model.Product;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    /*
    * for now, I am using a simple service model. Later, I will add a repository
    * */

    private List<Product> products = new ArrayList<>();
    private long ID = 0;

    {
        products.add(new Product(++ID, "PlayStation 5", "Simple description", 550, "Berlin",
                "emrah"));
        products.add(new Product(++ID, "IPhone 15 Pro", "Simple description", 1070, "Wiesbaden",
                "hakan"));
    }

    public List<Product> listProducts() {
        return products;
    }

    public void saveProduct(Product product) {
        product.setId(++ID);
        products.add(product);
    }

    public void deleteProduct(Long id) {
        products.removeIf(product -> product.getId().equals(id));
    }

    public Optional<Product> getProductById(Long id) {
        return products.stream()
                .filter(product -> product.getId().equals(id))
                .findFirst();
    }
}

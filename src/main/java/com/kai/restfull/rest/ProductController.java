package com.kai.restfull.rest;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kai.restfull.rest.model.Product;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/v1/products")
public class ProductController {
    private static List<Product> products;

    static {
        byte[] jsonData;
        try {
            jsonData = Files.readAllBytes(Paths.get("F:\\Giang day lap trinh\\assignment\\rest\\data\\product.json"));
            ObjectMapper objectMapper = new ObjectMapper();

            TypeReference<List<Product>> mapType = new TypeReference<List<Product>>() {
            };
            products = objectMapper.readValue(jsonData, mapType);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping
    public List<Product> getAllProduct(@RequestParam(required = false) Integer limit) {
        if (limit == null)
            return products;
        return products.stream().limit(limit).collect(Collectors.toList());
    }

    @PostMapping
    public void createProduct(@RequestBody Product req) {
        products.add(req);
    }

    @PutMapping(value = "/{id}")
    public void updateProductById(@RequestBody Product req, @PathVariable int id) {
        for (Product product : products) {
            if (product.getId() == id) {
                product.setCategory(req.getCategory());
                product.setDescription(req.getDescription());
                product.setImage(req.getImage());
                product.setPrice(req.getPrice());
                product.setTitle(req.getTitle());
                product.setRating(req.getRating());
            }
        }
    }

    @GetMapping(value = "/{id}")
    public Product getProductById(@PathVariable int id) {
        return products.stream().filter(product -> product.getId() == id).findFirst().orElse(null);
    }

    @DeleteMapping(value = "/{id}")
    public void deleteProductById(@PathVariable int id) {
        products.removeIf(product -> product.getId() == id);
    }
}

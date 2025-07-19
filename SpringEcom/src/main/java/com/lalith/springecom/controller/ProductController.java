package com.lalith.springecom.controller;

import com.lalith.springecom.model.Product;
import com.lalith.springecom.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class ProductController {
    @Autowired
    private ProductService productservice;

    @GetMapping("/products")
    public ResponseEntity<List<Product>> getProducts() {
        return new ResponseEntity<>(productservice.getAllProducts(), HttpStatus.OK);
    }

    @GetMapping("/product/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable int id) {
        Product product = productservice.getProduct(id);
        if(product != null)
        return new ResponseEntity<>(product,HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/product")
    public ResponseEntity<?> addProduct(@RequestPart Product product,@RequestPart(required = false) MultipartFile imageFile) {
        Product savedproduct = null;
        try {
            savedproduct = productservice.addProduct(product,imageFile);
            return new ResponseEntity<>(savedproduct,HttpStatus.CREATED);
        } catch (IOException e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("/product/{id}/image")
    public ResponseEntity<byte[]> getProductImage(@PathVariable int id) {
        Product product = productservice.getProduct(id);
        return new ResponseEntity<>(product.getImageData(),HttpStatus.OK);
    }

    @PutMapping("/product/{id}")
    public ResponseEntity<String> updateProduct(@PathVariable int id, @RequestPart Product product,@RequestPart MultipartFile imageFile ) {
        Product updatedproduct = null;
        try{
            updatedproduct =productservice.updateProduct(product,imageFile);
            return new ResponseEntity<>("Updated",HttpStatus.OK);
        }catch(IOException e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);

        }
    }
    @DeleteMapping("product/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable int id) {
            Product product = productservice.getProduct(id);
            if(product != null) {
                productservice.deleteProduct(id);
                return new ResponseEntity<>("Deleted", HttpStatus.OK);
            }
        else
           return new ResponseEntity<>(HttpStatus.BAD_REQUEST);


    }

    @GetMapping("/products/search")
    public ResponseEntity<List<Product>> getProductsSearch(@RequestParam String keyword) {
        System.out.println("searching with " + keyword);
       List<Product> products = productservice.searchProducts(keyword);
       return new ResponseEntity<>(products,HttpStatus.OK);
    }



}

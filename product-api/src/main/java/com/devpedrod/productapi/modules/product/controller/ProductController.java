package com.devpedrod.productapi.modules.product.controller;

import com.devpedrod.productapi.modules.product.DTO.ProductResponse;
import com.devpedrod.productapi.modules.product.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/product")
public class ProductController {

    private final IProductService productService;

    @Autowired
    public ProductController(IProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> findAll() {
        return ResponseEntity.ok(productService.findAll());
    }

    @GetMapping("{id}")
    public ProductResponse findById(@PathVariable Long id) {
        return productService.findByIdResponse(id);
    }

    @GetMapping("name/{name}")
    public ResponseEntity<List<ProductResponse>> findByName(@PathVariable String name) {
        return ResponseEntity.ok(productService.findByName(name));
    }

    @GetMapping("category/{categoryId}")
    public ResponseEntity<List<ProductResponse>> findByCategoryId(@PathVariable Long categoryId) {
        return ResponseEntity.ok(productService.findByCategoryId(categoryId));
    }

    @GetMapping("supplier/{supplierId}")
    public ResponseEntity<List<ProductResponse>> findBySupplierId(@PathVariable Long supplierId) {
        return ResponseEntity.ok(productService.findBySupplierId(supplierId));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

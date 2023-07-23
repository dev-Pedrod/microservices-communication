package com.devpedrod.productapi.modules.product.controller;

import com.devpedrod.productapi.modules.product.DTO.ProductCheckStockRequest;
import com.devpedrod.productapi.modules.product.DTO.ProductRequest;
import com.devpedrod.productapi.modules.product.DTO.ProductResponse;
import com.devpedrod.productapi.modules.product.DTO.ProductSalesResponse;
import com.devpedrod.productapi.modules.product.service.IProductService;
import com.devpedrod.productapi.modules.shared.DTO.SuccessResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("api/product")
public class ProductController {

    private final IProductService productService;

    @Autowired
    public ProductController(IProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<ProductResponse> create(@Valid @RequestBody ProductRequest request) {
        var response = productService.create(request);
        return ResponseEntity.created(ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.getId())
                .toUri()).build();
    }

    @PutMapping()
    public ResponseEntity<ProductResponse> update(@Valid @RequestBody ProductRequest request) {
        return ResponseEntity.ok(productService.update(request));
    }

    @PatchMapping("reactivate/{id}")
    public ResponseEntity<Void> reactive(@PathVariable("id") Long id) {
        productService.reactivateEntity(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
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

    @PostMapping("check-stock")
    public ResponseEntity<SuccessResponse> checkProductsStock(@RequestBody ProductCheckStockRequest request) {
        return ResponseEntity.ok(productService.checkProductsStock(request));
    }


    @GetMapping("{id}/sales")
    public ResponseEntity<ProductSalesResponse> findProductSales(@PathVariable Long id) {
        return ResponseEntity.ok(productService.findProductSales(id));
    }
}

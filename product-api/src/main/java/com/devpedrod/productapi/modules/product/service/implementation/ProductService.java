package com.devpedrod.productapi.modules.product.service.implementation;

import com.devpedrod.productapi.modules.category.service.ICategoryService;
import com.devpedrod.productapi.modules.product.DTO.*;
import com.devpedrod.productapi.modules.product.model.Product;
import com.devpedrod.productapi.modules.product.repository.IProductRepository;
import com.devpedrod.productapi.modules.product.service.IProductService;
import com.devpedrod.productapi.modules.sales.DTO.SalesConfirmationDTO;
import com.devpedrod.productapi.modules.sales.enums.SalesStatus;
import com.devpedrod.productapi.modules.shared.exceptions.ValidationException;
import com.devpedrod.productapi.modules.shared.service.implementation.GenericService;
import com.devpedrod.productapi.modules.shared.strategy.product.ProductStrategy;
import com.devpedrod.productapi.modules.supplier.service.ISupplierService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.util.ObjectUtils.isEmpty;

@Slf4j
@Service
public class ProductService extends GenericService<Product, ProductResponse, Long, IProductRepository> implements IProductService {

    private ProductStrategy productStrategy;
    private ICategoryService categoryService;
    private ISupplierService supplierService;

    @Autowired
    protected ProductService(WebApplicationContext applicationContext, ModelMapper mapper,
                             ProductStrategy productStrategy, ISupplierService supplierService,
                             ICategoryService categoryService) {
        super(applicationContext, mapper);
        this.productStrategy = productStrategy;
        this.supplierService = supplierService;
        this.categoryService = categoryService;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> findByName(String name) {
        if (isEmpty(name)) throw new ValidationException("The product name must be informed.");
        return repository.findByNameIgnoreCaseContaining(name)
                .stream()
                .map(ProductResponse::of)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ProductResponse create(ProductRequest request) {
        Product product = super.save(() -> (Product) productStrategy.applyBusinessRule(request));
        return ProductResponse.of(product);
    }

    @Override
    @Transactional
    public ProductResponse update(ProductRequest request) {
        Product product = super.update(() -> (Product) productStrategy.applyBusinessRule(request));
        return ProductResponse.of(product);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> findBySupplierId(Long supplierId) {
        if (isEmpty(supplierId)) throw new ValidationException("The product' supplier id must be informed.");
        return repository.findBySupplierId(supplierId)
                .stream()
                .map(ProductResponse::of)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> findByCategoryId(Long categoryId) {
        if (isEmpty(categoryId)) throw new ValidationException("The product' category id must be informed.");
        return repository.findByCategoryId(categoryId)
                .stream()
                .map(ProductResponse::of)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ProductSalesResponse findProductSales(Long id) {
        if (isEmpty(id)) throw new ValidationException("The product id must be informed.");
        var product = super.findById(id);
        var sales = new ArrayList<String>();
        return ProductSalesResponse.of(product, sales);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponse findByIdResponse(Long id) {
        if (isEmpty(id)) throw new ValidationException("The product id must be informed.");
        return ProductResponse.of(super.findById(id));
    }

    @Override
    @Transactional
    public void updateProductStock(ProductStockDTO product) {
        try {
            validateStockUpdateData(product);
            updateStock(product);
        } catch (Exception ex) {
            log.error("Error while trying to update stock for message with error: {}", ex.getMessage(), ex);
            var rejectedMessage = new SalesConfirmationDTO(product.getSalesId(), SalesStatus.REJECTED, product.getTransactionid());
            // TODO: SEND CONFIRMATION MESSAGE TO SALES API
            // salesConfirmationSender.sendSalesConfirmationMessage(rejectedMessage);
        }
    }

    @Override
    public Boolean existsByCategoryId(Long categoryId) {
        return repository.existsByCategoryId(categoryId);
    }

    @Override
    public Boolean existsBySupplierId(Long supplierId) {
        return repository.existsBySupplierId(supplierId);
    }

    private void validateStockUpdateData(ProductStockDTO product) {
        if (isEmpty(product) || isEmpty(product.getSalesId())) {
            throw new ValidationException("The product data and the sales ID must be informed.");
        }
        if (isEmpty(product.getProducts())) {
            throw new ValidationException("The sales' products must be informed.");
        }
        product.getProducts()
                .forEach(salesProduct -> {
                    if (isEmpty(salesProduct.getQuantity()) || isEmpty(salesProduct.getProductId())) {
                        throw new ValidationException("The productID and the quantity must be informed.");
                    }
                });
    }

    private void validateStock(ProductQuantityDTO productQuantity) {
        if (isEmpty(productQuantity.getProductId()) || isEmpty(productQuantity.getQuantity())) {
            throw new ValidationException("Product ID and quantity must be informed.");
        }
        Product product = findById(productQuantity.getProductId());
        if (productQuantity.getQuantity() > product.getQuantityAvailable()) {
            throw new ValidationException(String.format("The product %s is out of stock.", product.getId()));
        }
    }

    @Transactional
    void updateStock(ProductStockDTO product) {
        List<Product> productsForUpdate = new ArrayList<>();
        product.getProducts()
                .forEach(salesProduct -> {
                    Product existingProduct = findById(salesProduct.getProductId());
                    validateQuantityInStock(salesProduct, existingProduct);
                    existingProduct.updateStock(salesProduct.getQuantity());
                    productsForUpdate.add(existingProduct);
                });

        if (!isEmpty(productsForUpdate)) {
            repository.saveAll(productsForUpdate);
            var approvedMessage = new SalesConfirmationDTO(product.getSalesId(), SalesStatus.APPROVED, product.getTransactionid());
            // TODO: SEND CONFIRMATION MESSAGE TO SALES API
            // salesConfirmationSender.sendSalesConfirmationMessage(approvedMessage);
        }
    }

    private void validateQuantityInStock(ProductQuantityDTO salesProduct, Product existingProduct) {
        if (salesProduct.getQuantity() > existingProduct.getQuantityAvailable()) {
            throw new ValidationException(String.format("The product %s is out of stock.", existingProduct.getId()));
        }
    }
}

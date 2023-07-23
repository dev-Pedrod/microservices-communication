package com.devpedrod.productapi.modules.product.service.implementation;

import com.devpedrod.productapi.config.HTTP.RequestUtil;
import com.devpedrod.productapi.modules.product.DTO.*;
import com.devpedrod.productapi.modules.product.model.Product;
import com.devpedrod.productapi.modules.product.repository.IProductRepository;
import com.devpedrod.productapi.modules.product.service.IProductService;
import com.devpedrod.productapi.modules.sales.DTO.SalesConfirmationDTO;
import com.devpedrod.productapi.modules.sales.DTO.SalesProductResponse;
import com.devpedrod.productapi.modules.sales.client.SalesClient;
import com.devpedrod.productapi.modules.sales.enums.SalesStatus;
import com.devpedrod.productapi.modules.sales.rabbit.SalesConfirmationSender;
import com.devpedrod.productapi.modules.shared.DTO.SuccessResponse;
import com.devpedrod.productapi.modules.shared.exceptions.ValidationException;
import com.devpedrod.productapi.modules.shared.service.implementation.GenericService;
import com.devpedrod.productapi.modules.shared.strategy.product.ProductStrategy;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
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

    private static final Integer ZERO = 0;
    private static final String AUTHORIZATION = "Authorization";
    private static final String TRANSACTION_ID = "transactionid";
    private static final String SERVICE_ID = "serviceid";


    private final ProductStrategy productStrategy;
    private final SalesConfirmationSender salesConfirmationSender;
    private final SalesClient salesClient;
    private final ObjectMapper objectMapper;

    @Autowired
    protected ProductService(WebApplicationContext applicationContext, ModelMapper mapper,
                             ProductStrategy productStrategy, SalesConfirmationSender salesConfirmationSender,
                             SalesClient salesClient, ObjectMapper objectMapper) {
        super(applicationContext, mapper);
        this.productStrategy = productStrategy;
        this.salesConfirmationSender = salesConfirmationSender;
        this.salesClient = salesClient;
        this.objectMapper = objectMapper;
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
    @Transactional
    public void delete(Long id) {
        super.delete(id, () -> {
            SalesProductResponse sales = getSalesByProductId(id);
            if (!isEmpty(sales.getSalesIds())) {
                throw new ValidationException("The product cannot be deleted. There are sales for it.");
            }
        });
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
        Product product = super.findById(id);
        var sales = getSalesByProductId(product.getId());
        return ProductSalesResponse.of(product, sales.getSalesIds());
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
            salesConfirmationSender.sendSalesConfirmationMessage(rejectedMessage);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Boolean existsByCategoryId(Long categoryId) {
        return repository.existsByCategoryId(categoryId);
    }

    @Override
    @Transactional(readOnly = true)
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

    @Override
    @Transactional(readOnly = true)
    public SuccessResponse checkProductsStock(ProductCheckStockRequest request) {
        try {
            HttpServletRequest currentRequest = RequestUtil.getCurrentRequest();
            String transactionid = currentRequest.getHeader(TRANSACTION_ID);
            var serviceid = currentRequest.getAttribute(SERVICE_ID);
            log.info("Request to POST product stock with data {} | [transactionID: {} | serviceID: {}]",
                    objectMapper.writeValueAsString(request), transactionid, serviceid);

            if (isEmpty(request) || isEmpty(request.getProducts())) {
                throw new ValidationException("The request data and products must be informed.");
            }

            request.getProducts().forEach(this::validateStock);
            SuccessResponse response = SuccessResponse.create("The stock is ok!");
            log.info("Response to POST product stock with data {} | [transactionID: {} | serviceID: {}]",
                    objectMapper.writeValueAsString(response), transactionid, serviceid);
            return response;
        } catch (Exception ex) {
            throw new ValidationException(ex.getMessage());
        }
    }

    @Transactional(readOnly = true)
    void validateStock(ProductQuantityDTO productQuantity) {
        if (isEmpty(productQuantity.getProductId()) || isEmpty(productQuantity.getQuantity())) {
            throw new ValidationException("Product ID and quantity must be informed.");
        }
        Product product = findById(productQuantity.getProductId());
        if (productQuantity.getQuantity() > product.getQuantityAvailable()) {
            throw new ValidationException(String.format("The product %s is out of stock.", product.getId()));
        }
    }

    @Transactional
    public void updateStock(ProductStockDTO product) {
        var productsForUpdate = new ArrayList<Product>();
        product.getProducts()
                .forEach(salesProduct -> {
                    var existingProduct = findById(salesProduct.getProductId());
                    validateQuantityInStock(salesProduct, existingProduct);
                    existingProduct.updateStock(salesProduct.getQuantity());
                    productsForUpdate.add(existingProduct);
                });
        if (!isEmpty(productsForUpdate)) {
            repository.saveAll(productsForUpdate);
            var approvedMessage = new SalesConfirmationDTO(product.getSalesId(), SalesStatus.APPROVED, product.getTransactionid());
            salesConfirmationSender.sendSalesConfirmationMessage(approvedMessage);
        }
    }

    private void validateQuantityInStock(ProductQuantityDTO salesProduct, Product existingProduct) {
        if (salesProduct.getQuantity() > existingProduct.getQuantityAvailable()) {
            throw new ValidationException(String.format("The product %s is out of stock.", existingProduct.getId()));
        }
    }

    private SalesProductResponse getSalesByProductId(Long productId) {
        try {
            HttpServletRequest currentRequest = RequestUtil.getCurrentRequest();
            String token = currentRequest.getHeader(AUTHORIZATION);
            String transactionid = currentRequest.getHeader(TRANSACTION_ID);
            var serviceid = currentRequest.getAttribute(SERVICE_ID);

            log.info("Sending GET request to orders by productId with data {} | [transactionID: {} | serviceID: {}]", productId, transactionid, serviceid);
            SalesProductResponse response = salesClient.findSalesByProductId(productId, token, transactionid)
                    .orElseThrow(() -> new ValidationException("The sales was not found by this product."));
            log.info("Recieving response from orders by productId with data {} | [transactionID: {} | serviceID: {}]",
                    objectMapper.writeValueAsString(response), transactionid, serviceid);

            return response;
        } catch (Exception ex) {
            log.error("Error trying to call Sales-API: {}", ex.getMessage());
            throw new ValidationException("The sales could not be found.");
        }
    }
}

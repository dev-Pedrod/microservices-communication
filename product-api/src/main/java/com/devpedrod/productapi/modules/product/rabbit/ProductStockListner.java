package com.devpedrod.productapi.modules.product.rabbit;

import com.devpedrod.productapi.modules.product.DTO.ProductStockDTO;
import com.devpedrod.productapi.modules.product.service.IProductService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductStockListner {

    private final IProductService productService;
    private final ObjectMapper mapper;

    @RabbitListener(queues = "${app-config.rabbit.queue.product-stock}")
    public void recieveProductStockMessage(ProductStockDTO product) throws JsonProcessingException {
        log.info("Recieving message with data: {} and TransactionID: {}", mapper.writeValueAsString(product), product.getTransactionid());
        productService.updateProductStock(product);
    }
}

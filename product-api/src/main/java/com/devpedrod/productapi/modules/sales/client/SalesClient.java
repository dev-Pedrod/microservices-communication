package com.devpedrod.productapi.modules.sales.client;

import com.devpedrod.productapi.modules.sales.DTO.SalesProductResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

import java.util.Optional;

@HttpExchange("/api/orders")
public interface SalesClient {

    @GetExchange("/product/{productId}")
    Optional<SalesProductResponse> findSalesByProductId(@PathVariable Long productId,
                                                        @RequestHeader(name = "Authorization") String authorization,
                                                        @RequestHeader(name = "transactionid") String transactionId);
}

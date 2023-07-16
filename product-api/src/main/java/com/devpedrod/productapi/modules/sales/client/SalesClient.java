package com.devpedrod.productapi.modules.sales.client;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

import java.util.List;
import java.util.Optional;

@HttpExchange("/api/orders")
public interface SalesClient {

    @GetExchange("/product/{productId}")
    Optional<List<String>> findSalesByProductId(@PathVariable Long productId,
                                                @RequestHeader(name = "Authorization") String authorization,
                                                @RequestHeader(name = "transactionId") String transactionId);
}

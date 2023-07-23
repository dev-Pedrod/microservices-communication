package com.devpedrod.productapi.modules.sales.rabbit;

import com.devpedrod.productapi.modules.sales.DTO.SalesConfirmationDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SalesConfirmationSender {

    private final RabbitTemplate rabbitTemplate;

    private final ObjectMapper mapper;

    @Value("${app-config.rabbit.exchange.product}")
    private String productTopicExchange;

    @Value("${app-config.rabbit.routing-key.sales-confirmation}")
    private String salesConfirmationKey;

    public void sendSalesConfirmationMessage(SalesConfirmationDTO message) {
        try {
            log.info("Sending message: {}", mapper.writeValueAsString(message));
            rabbitTemplate.convertAndSend(productTopicExchange, salesConfirmationKey, message);
            log.info("Message was sent successfully!");
        } catch (Exception ex) {
            log.info("Error while trying to send sales confirmation message: ", ex);
        }
    }
}


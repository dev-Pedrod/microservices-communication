package com.devpedrod.productapi;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RequestMapping("api/status")
@RestController
public class StatusController {

    @GetMapping
    public ResponseEntity<HashMap<String, Object>> apiStatus() {
        var response = new HashMap<String, Object>();
        response.put("service", "product-api");
        response.put("status", "up");
        response.put("httpStatus", 200);
        return ResponseEntity.ok(response);
    }
}

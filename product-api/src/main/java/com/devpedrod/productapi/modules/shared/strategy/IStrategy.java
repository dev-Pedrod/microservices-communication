package com.devpedrod.productapi.modules.shared.strategy;

import com.devpedrod.productapi.modules.shared.DTO.BaseDTO;
import com.devpedrod.productapi.modules.shared.entity.BaseEntity;

public interface IStrategy {
    BaseEntity applyBusinessRule(BaseDTO baseDTO);
}

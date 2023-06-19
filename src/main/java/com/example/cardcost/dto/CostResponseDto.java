package com.example.cardcost.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class CostResponseDto {
    private String countryCode;
    private BigDecimal cost;
}

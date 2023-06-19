package com.example.cardcost.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ClearingCostDto {
    private String countryCode;
    private Float cost;
}

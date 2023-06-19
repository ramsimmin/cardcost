package com.example.cardcost.model;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.math.BigDecimal;

@RedisHash("cost_matrix")
@Builder
@Getter
@Setter
public class ClearingCost {
    @Id
    private String countryCode;
    private BigDecimal cost;
}

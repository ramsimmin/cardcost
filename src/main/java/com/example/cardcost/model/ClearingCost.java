package com.example.cardcost.model;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash("Clearing_Cost_Matrix")
@Builder
@Getter
@Setter
public class ClearingCostMatrix {
    @Id
    private String countryCode;
    private Integer cost;
}

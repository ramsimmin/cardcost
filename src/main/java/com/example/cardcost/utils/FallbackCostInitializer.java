package com.example.cardcost.utils;

import com.example.cardcost.model.ClearingCost;
import com.example.cardcost.respository.ClearingCostRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
@Slf4j
public class FallbackCostInitializer {

    private final ClearingCostRepository clearingCostRepository;

    @Value("${fallback.cost:10}")
    private BigDecimal fallbackCost;

    @Value("${fallback.country_code:other}")
    private String fallbackCountryCode;

    @PostConstruct
    private void initFallbackCost() {
        // Ensure that the clearing cost matrix always contains the fallback country_code "other" with a default cost
        if (clearingCostRepository.findById(fallbackCountryCode).isEmpty()) {
            clearingCostRepository.save(ClearingCost.builder().countryCode(fallbackCountryCode).cost(fallbackCost).build());
            log.info("Initialized fallback cost: {}, for fallback country_code: {}", fallbackCost, fallbackCountryCode);
        }
    }
}

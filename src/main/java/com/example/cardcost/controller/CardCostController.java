package com.example.cardcost.controller;

import com.example.cardcost.dto.CardCostRequestDto;
import com.example.cardcost.dto.ResponseDto;
import com.example.cardcost.openapi.CardCostControllerOpenApi;
import com.example.cardcost.service.CardCostService;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.util.Set;

@RestController
@RequestMapping(value = "api/payment-cards-cost")
@Slf4j
public class CardCostController implements CardCostControllerOpenApi {

    private final CardCostService cardCostService;
    private final Bucket bucket;


    public CardCostController(CardCostService cardCostService, @Value("${requests.limit_max:7000}") Long requestsMaxLimit) {
        this.cardCostService = cardCostService;

        Bandwidth limit = Bandwidth.classic(requestsMaxLimit, Refill.intervally(requestsMaxLimit, Duration.ofMinutes(1)));
        this.bucket = Bucket.builder()
                .addLimit(limit)
                .build();
    }

    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @Override
    public ResponseEntity<?> getCardCost(@RequestBody CardCostRequestDto cardCostRequestDto) {

        if (!bucket.tryConsume(1)) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(ResponseDto.builder().messages(Set.of("Too many requests")).error(true).build());
        }

        return cardCostService.getCardCost(cardCostRequestDto);
    }



}

package com.example.cardcost.controller;

import com.example.cardcost.dto.CardCostRequestDto;
import com.example.cardcost.service.CardCostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "api/payment-cards-cost")
@RequiredArgsConstructor
@Slf4j
public class CardCostController {

    private final CardCostService cardCostService;

    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getCardCost(@RequestBody CardCostRequestDto cardCostRequestDto) {
        return cardCostService.getCardCost(cardCostRequestDto);
    }


}

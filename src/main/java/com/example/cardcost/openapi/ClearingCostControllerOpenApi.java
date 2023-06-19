package com.example.cardcost.controller;

import com.example.cardcost.dto.ClearingCostDto;
import com.example.cardcost.service.ClearingCostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "api/costs")
@RequiredArgsConstructor
public class ClearingCostController {

    private final ClearingCostService clearingCostService;

    @GetMapping(value = "/{country_code}")
    public ResponseEntity<?> getCost(@PathVariable(name = "country_code") String country_code) {
        return clearingCostService.getCostByCountryCode(country_code);
    }

    @GetMapping
    public ResponseEntity<?> getCosts() {
        return clearingCostService.getCosts();
    }

    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createCost(@RequestBody ClearingCostDto costRegisterDto) {
        return clearingCostService.saveCost(costRegisterDto);
    }

    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateCost(@RequestBody ClearingCostDto costRegisterDto) {
        return clearingCostService.updateCost(costRegisterDto);
    }

    @DeleteMapping(value = "/delete/{country_code}")
    public ResponseEntity<?> deleteCost(@PathVariable(name = "country_code") String country_code) {
        return clearingCostService.deleteCost(country_code);
    }

}

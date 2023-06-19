package com.example.cardcost.controller;

import com.example.cardcost.dto.CostRegisterDto;
import com.example.cardcost.service.ClearingCostMatrixService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "api/cost-matrix")
@RequiredArgsConstructor
public class ClearingCostMatrixController {

    private final ClearingCostMatrixService costMatrixService;

    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createCost(@RequestBody CostRegisterDto costRegisterDto) {
        return costMatrixService.save(costRegisterDto);
    }

    @GetMapping(value = "/{country_code}")
    public ResponseEntity<?> getCost(@PathVariable(name = "country_code") String country_code) {
        return costMatrixService.getCostByCountryCode(country_code);
    }

    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateCost(@RequestBody CostRegisterDto costRegisterDto) {
        return costMatrixService.updateCost(costRegisterDto);
    }

    @DeleteMapping(value = "/delete/{country_code}")
    public ResponseEntity<?> deleteCost(@PathVariable(name = "country_code") String country_code) {
        return costMatrixService.deleteCost(country_code);
    }

}

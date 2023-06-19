package com.example.cardcost.service;

import com.example.cardcost.dao.ClearingCostDao;
import com.example.cardcost.dto.CardCostRequestDto;
import com.example.cardcost.dto.ClearingCostDto;
import com.example.cardcost.dto.ResponseDto;
import com.example.cardcost.validation.CommonValidations;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class CardCostService {

    private final CommonValidations commonValidations;
    private final ClearingCostDao clearingCostDao;
    private final BinTableApiService binTableApiService;


    @Value("${fallback.country_code}")
    String fallbackCountryCode;


    public ResponseEntity<?> getCardCost(CardCostRequestDto cardCostRequestDto) {
        try {
            Set<String> errorMessages = commonValidations.validateCardNumber(cardCostRequestDto);
            if (!errorMessages.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseDto.builder().messages(errorMessages).error(true).build());
            }
            // Get clearing cost
            ClearingCostDto result = calculateCost(cardCostRequestDto.getCardNumber().substring(0, 6));

            if (result == null) {
                // The country code is not registered in the cost matrix, return the value for "other"
                return getDefaultCost();
            }
            return ResponseEntity.ok(result);
        } catch (HttpClientErrorException httpEx) {
            log.error("Unable to calculate cost : {}", httpEx.getMessage());
            if (httpEx.getStatusCode().value() == 404) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                        ResponseDto.builder().messages(Set.of("Requested card number does not belong to a known issuer")).error(true).build());
            }
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(
                    ResponseDto.builder().messages(Set.of("Unable to calculate cost due to: " + httpEx.getStatusCode() + " exception")).error(true).build());
        } catch (Exception ex) {
            log.error("Unable to calculate cost : {}", ex.getMessage());
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(
                    ResponseDto.builder().messages(Set.of("Unable to calculate cost")).error(true).build());
        }
    }

    private ClearingCostDto calculateCost(String issuerIdentificationNumber) {
        // Use the BINTable API to get the country
        String countryCode = binTableApiService.getCountryCodeFromBinTableApi(issuerIdentificationNumber);

        // Look up the clearing cost matrix based on the result code
        ClearingCostDto clearingCostDto = clearingCostDao.findById(countryCode.toLowerCase());

        return clearingCostDto;
    }


    private ResponseEntity<?> getDefaultCost() {
        ClearingCostDto defaultCost = clearingCostDao.findById(fallbackCountryCode);
        if (defaultCost == null) {
            // The fallback entry 'other' does not exist in the cost matrix
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ResponseDto.builder().messages(Set.of("Default cost not found")).error(true).build());
        }
        return ResponseEntity.ok(defaultCost);
    }


}

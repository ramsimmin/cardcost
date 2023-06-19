package com.example.cardcost.service;

import com.example.cardcost.dao.ClearingCostDao;
import com.example.cardcost.dto.ClearingCostDto;
import com.example.cardcost.dto.ResponseDto;
import com.example.cardcost.validation.CommonValidations;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClearingCostService {

    private final ClearingCostDao clearingCostDao;
    private final CommonValidations commonValidations;


    public ResponseEntity<?> saveCost(ClearingCostDto costRegisterDto) {
        try {
            // Validate input
            Set<String> errorMessages = commonValidations.validateCostRequestDto(costRegisterDto);
            if (!errorMessages.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseDto.builder().messages(errorMessages).error(true).build());
            }

            ClearingCostDto clearingCostDto = clearingCostDao.findById(costRegisterDto.getCountryCode().toLowerCase());
            // Validate entry is already present
            if (clearingCostDto != null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseDto.builder().messages(
                                Set.of("Cost entry already exists for country code [" + costRegisterDto.getCountryCode() + "]"))
                        .error(true).build());
            }

            // Save
            clearingCostDao.save(costRegisterDto);
            return ResponseEntity.ok(ResponseDto.builder().messages(Set.of("Successfully saved")).build());
        } catch (Exception e) {
            log.error("Unable to save cost matrix entry : {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ResponseDto.builder().messages(Set.of("Unable to save cost matrix entry due to: " + e.getMessage())).error(true).build());
        }
    }

    public ResponseEntity<?> getCostByCountryCode(String countryCode) {
        try {
            ClearingCostDto clearingCostDto = clearingCostDao.findById(countryCode.toLowerCase());

            if (clearingCostDto == null) {
                ResponseDto responseDto = ResponseDto.builder()
                        .messages(Set.of("Cost entry for country code [" + countryCode + "] does not exist")).error(true).build();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseDto);
            }
            return ResponseEntity.ok(clearingCostDto);
        } catch (Exception e) {
            log.error("Unable to get cost entry for country code {} : error {}", countryCode, e.getMessage());
            ResponseDto responseDto = ResponseDto.builder()
                    .messages(Set.of("Unable to get cost entry for country code [" + countryCode + "] ")).error(true).build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDto);
        }
    }


    public ResponseEntity<?> getCosts() {
        try {
            List<ClearingCostDto> clearingCostDtoList = clearingCostDao.findAll();
            return ResponseEntity.ok().body(clearingCostDtoList);
        } catch (Exception e) {
            log.error("Unable to get cost entries: error {} {}", e.getMessage(), e.getCause());
            e.printStackTrace();
            ResponseDto responseDto = ResponseDto.builder()
                    .messages(Set.of("Unable to get cost entries")).error(true).build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDto);
        }
    }

    public ResponseEntity<?> updateCost(ClearingCostDto costRegisterDto) {
        try {

            // Validate input
            Set<String> errorMessages = commonValidations.validateCostRequestDto(costRegisterDto);
            if (!errorMessages.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseDto.builder().messages(errorMessages).error(true).build());
            }

            ClearingCostDto clearingCostDto = clearingCostDao.findById(costRegisterDto.getCountryCode().toLowerCase());
            // Validate entry presence
            if (clearingCostDto == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseDto.builder()
                        .messages(Set.of("Cost entry for country code [" + costRegisterDto.getCountryCode() + "] does not exist")).error(true).build());
            }

            // Save
            clearingCostDao.save(costRegisterDto);
            return ResponseEntity.ok(ResponseDto.builder().messages(Set.of("Successfully updated")).build());
        } catch (Exception e) {
            log.error("Unable to update cost matrix entry : {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ResponseDto.builder().messages(Set.of("Unable to update cost matrix entry due to: " + e.getMessage())).error(true).build());
        }
    }


    public ResponseEntity<?> deleteCost(String countryCode) {
        try {
            ClearingCostDto clearingCostDto = clearingCostDao.findById(countryCode.toLowerCase());
            if (clearingCostDto != null) {
                clearingCostDao.delete(countryCode.toLowerCase());
                return ResponseEntity.ok(ResponseDto.builder().messages(Set.of("Successfully deleted")).build());
            } else {
                ResponseDto responseDto = ResponseDto.builder().messages(Set.of("Cost entry for country code [" + countryCode + "] does not exist")).error(true).build();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseDto);
            }
        } catch (Exception e) {
            log.error("Unable to perform delete action for country code {} : error {}", countryCode, e.getMessage());
            ResponseDto responseDto = ResponseDto.builder().messages(Set.of("Unable to delete cost entry for country code [" + countryCode + "] ")).error(true).build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDto);
        }
    }



}

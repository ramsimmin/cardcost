package com.example.cardcost.service;

import com.example.cardcost.dao.ClearingCostMatrixDao;
import com.example.cardcost.dto.CostRegisterDto;
import com.example.cardcost.dto.CostResponseDto;
import com.example.cardcost.dto.ResponseDto;
import com.example.cardcost.model.ClearingCostMatrix;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClearingCostMatrixService {

    private final ClearingCostMatrixDao clearingCostMatrixDao;


    public ResponseEntity<?> save(CostRegisterDto costRegisterDto) {
        try {
            ClearingCostMatrix clearingCostMatrix = clearingCostMatrixDao.findById(costRegisterDto.getCountryCode());
            if (clearingCostMatrix != null) {
                // Entry already exists, return error
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseDto.builder().message("Cost entry already exists for country code [" + costRegisterDto.getCountryCode() + "]").error(true).build());
            }
            clearingCostMatrixDao.save(ClearingCostMatrix.builder().countryCode(costRegisterDto.getCountryCode()).cost(costRegisterDto.getCost()).build());
            return ResponseEntity.ok(ResponseDto.builder().message("Successfully saved").build());
        } catch (Exception e) {
            log.error("Unable to save cost matrix entry : {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseDto.builder().message("Unable to save cost matrix entry due to: " + e.getMessage()).error(true).build());
        }
    }

    public ResponseEntity<?> getCostByCountryCode(String countryCode) {
        try {
            ClearingCostMatrix costMatrix = clearingCostMatrixDao.findById(countryCode);
            if (costMatrix != null) {
                return ResponseEntity.ok(CostResponseDto.builder().countryCode(costMatrix.getCountryCode()).cost(costMatrix.getCost()).build());
            } else {
                ResponseDto responseDto = ResponseDto.builder().message("Cost entry for country code [" + countryCode + "] does not exist").error(true).build();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseDto);
            }
        } catch (Exception e) {
            log.error("Unable to get cost entry for country code {} : error {}", countryCode, e.getMessage());
            ResponseDto responseDto = ResponseDto.builder().message("Unable to get cost entry for country code [" + countryCode + "] ").error(true).build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDto);
        }
    }

    public ResponseEntity<?> updateCost(CostRegisterDto costRegisterDto) {
        try {
            ClearingCostMatrix clearingCostMatrix = clearingCostMatrixDao.findById(costRegisterDto.getCountryCode());
            if (clearingCostMatrix == null) {
                // Entry does not exist, return error
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseDto.builder().message("Cost entry for country code [" + costRegisterDto.getCountryCode() + "] does not exist").error(true).build());
            }
            clearingCostMatrixDao.save(ClearingCostMatrix.builder().countryCode(costRegisterDto.getCountryCode()).cost(costRegisterDto.getCost()).build());
            return ResponseEntity.ok(ResponseDto.builder().message("Successfully updated").build());
        } catch (Exception e) {
            log.error("Unable to update cost matrix entry : {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseDto.builder().message("Unable to update cost matrix entry due to: " + e.getMessage()).error(true).build());
        }
    }


    public ResponseEntity<?> deleteCost(String countryCode) {
        try {
            ClearingCostMatrix costMatrix = clearingCostMatrixDao.findById(countryCode);
            if (costMatrix != null) {
                clearingCostMatrixDao.delete(countryCode);
                return ResponseEntity.ok(ResponseDto.builder().message("Successfully deleted").build());
            } else {
                ResponseDto responseDto = ResponseDto.builder().message("Cost entry for country code [" + countryCode + "] does not exist").error(true).build();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseDto);
            }
        } catch (Exception e) {
            log.error("Unable to perform delete action for country code {} : error {}", countryCode, e.getMessage());
            ResponseDto responseDto = ResponseDto.builder().message("Unable to delete cost entry for country code [" + countryCode + "] ").error(true).build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDto);
        }
    }


    //todo get all entries
}

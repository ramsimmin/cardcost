package com.example.cardcost.service;

import com.example.cardcost.dao.ClearingCostDao;
import com.example.cardcost.dto.ClearingCostDto;
import com.example.cardcost.dto.ResponseDto;
import com.example.cardcost.respository.ClearingCostRepository;
import com.example.cardcost.validation.CommonValidations;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(SpringExtension.class)
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ClearingCostServiceTest {

    @Autowired
    ClearingCostDao clearingCostDao;
    @Autowired
    CommonValidations commonValidations;
    @Autowired
    ClearingCostService clearingCostService;
    @Autowired
    private ClearingCostRepository clearingCostRepository;

    @BeforeAll
    public void uploadCosts() {
        clearingCostDao.save(ClearingCostDto.builder().countryCode("eg").cost(new BigDecimal(10)).build());
        clearingCostDao.save(ClearingCostDto.builder().countryCode("gr").cost(new BigDecimal(15)).build());
        clearingCostDao.save(ClearingCostDto.builder().countryCode("other").cost(new BigDecimal(20)).build());
    }

    @Test
    void saveCostForAlreadyExistingCountryCode() {
        ClearingCostDto clearingCostDto = ClearingCostDto.builder().countryCode("eg").cost(new BigDecimal(10)).build();
        ResponseEntity<?> responseEntity = clearingCostService.saveCost(clearingCostDto);
        ResponseDto expectedResponseDto = ResponseDto.builder().messages(Set.of("Cost entry already exists for country code [eg]")).error(true).build();

        Assertions.assertAll(
                () -> Assertions.assertEquals(HttpStatusCode.valueOf(400), responseEntity.getStatusCode(), "Status code must be 400"),
                () -> Assertions.assertEquals(expectedResponseDto, responseEntity.getBody(), "Response is not the expected one")
        );
    }

    @Test
    void saveValidCost() {
        ClearingCostDto clearingCostDto = ClearingCostDto.builder().countryCode("uk").cost(new BigDecimal(12.5d)).build();
        ResponseEntity<?> responseEntity = clearingCostService.saveCost(clearingCostDto);
        ResponseDto expectedResponseDto = ResponseDto.builder().messages(Set.of("Successfully saved")).build();
        Assertions.assertAll(
                () -> Assertions.assertEquals(HttpStatusCode.valueOf(200), responseEntity.getStatusCode(), "Status code must be 200"),
                () -> Assertions.assertEquals(expectedResponseDto, responseEntity.getBody(), "Response is not the expected one")
        );
    }


    @Test
    void getCostByExistingCountryCode() {
        ResponseEntity<?> responseEntity = clearingCostService.getCostByCountryCode("eg");
        ClearingCostDto expectedClearingCostDto = ClearingCostDto.builder().countryCode("eg").cost(new BigDecimal(10)).build();

        Assertions.assertAll(
                () -> Assertions.assertEquals(HttpStatusCode.valueOf(200), responseEntity.getStatusCode(), "Status code must be 200"),
                () -> Assertions.assertEquals(expectedClearingCostDto, responseEntity.getBody(), "Response is not the expected one")
        );
    }

    @Test
    void getCostByNonExistingCountryCode() {
        ResponseEntity<?> responseEntity = clearingCostService.getCostByCountryCode("xx");
        ResponseDto expectedResponseDto = ResponseDto.builder().messages(Set.of("Cost entry for country code [xx] does not exist")).error(true).build();

        Assertions.assertAll(
                () -> Assertions.assertEquals(HttpStatusCode.valueOf(404), responseEntity.getStatusCode(), "Status code must be 404"),
                () -> Assertions.assertEquals(expectedResponseDto, responseEntity.getBody(), "Response is not the expected one")
        );
    }

    @Test
    void listCosts() {
        ResponseEntity<?> responseEntity = clearingCostService.getCosts();
        List<ClearingCostDto> expectedList = List.of(
                ClearingCostDto.builder().countryCode("eg").cost(new BigDecimal(10)).build(),
                ClearingCostDto.builder().countryCode("gr").cost(new BigDecimal(15)).build(),
                ClearingCostDto.builder().countryCode("other").cost(new BigDecimal(20)).build()
        );

        List<ClearingCostDto> actualList = (List<ClearingCostDto>) responseEntity.getBody();
        Assertions.assertAll(
                () -> Assertions.assertEquals(HttpStatusCode.valueOf(200), responseEntity.getStatusCode(), "Status code must be 200"),
                () -> Assertions.assertTrue(actualList.containsAll(expectedList), "Response is not the expected one")
        );
    }

    @Test
    void updateCostForNonExistingCountryCode() {
        ClearingCostDto clearingCostDto = ClearingCostDto.builder().countryCode("xx").cost(new BigDecimal(30)).build();
        ResponseEntity<?> responseEntity = clearingCostService.updateCost(clearingCostDto);
        ResponseDto expectedResponseDto = ResponseDto.builder().messages(Set.of("Cost entry for country code [xx] does not exist")).error(true).build();

        Assertions.assertAll(
                () -> Assertions.assertEquals(HttpStatusCode.valueOf(404), responseEntity.getStatusCode(), "Status code must be 404"),
                () -> Assertions.assertEquals(expectedResponseDto, responseEntity.getBody(), "Response is not the expected one")
        );
    }

    @Test
    void updateValidCost() {
        ClearingCostDto clearingCostDto = ClearingCostDto.builder().countryCode("gr").cost(new BigDecimal(25)).build();
        ResponseEntity<?> responseEntity = clearingCostService.updateCost(clearingCostDto);
        ResponseDto expectedResponseDto = ResponseDto.builder().messages(Set.of("Successfully updated")).build();
        Assertions.assertAll(
                () -> Assertions.assertEquals(HttpStatusCode.valueOf(200), responseEntity.getStatusCode(), "Status code must be 200"),
                () -> Assertions.assertEquals(expectedResponseDto, responseEntity.getBody(), "Response is not the expected one")
        );
    }

    @Test
    void deleteCostCountryCodeNotFound() {
        ResponseEntity<?> responseEntity = clearingCostService.deleteCost("xx");
        ResponseDto expectedResponseDto = ResponseDto.builder().messages(Set.of("Cost entry for country code [xx] does not exist")).error(true).build();
        Assertions.assertAll(
                () -> Assertions.assertEquals(HttpStatusCode.valueOf(404), responseEntity.getStatusCode(), "Status code must be 404"),
                () -> Assertions.assertEquals(expectedResponseDto, responseEntity.getBody(), "Response is not the expected one")
        );
    }

    @AfterAll
    public void cleanUp() {
        clearingCostRepository.deleteAll();
    }
}
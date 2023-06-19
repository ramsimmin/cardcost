package com.example.cardcost.service;

import com.example.cardcost.dao.ClearingCostDao;
import com.example.cardcost.dto.CardCostRequestDto;
import com.example.cardcost.dto.ClearingCostDto;
import com.example.cardcost.dto.ResponseDto;
import com.example.cardcost.model.ClearingCost;
import com.example.cardcost.respository.ClearingCostRepository;
import com.example.cardcost.validation.CommonValidations;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.HttpClientErrorException;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Set;

import static java.util.Optional.ofNullable;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CardCostServiceTest {

    @Autowired
    CommonValidations commonValidations;

    @Autowired
    ClearingCostDao clearingCostDao;

    @Autowired
    CardCostService cardCostService;

    @MockBean
    BinTableApiService binTableApiService;

    @BeforeAll
    public void uploadCosts() {
        clearingCostDao.save(ClearingCostDto.builder().countryCode("eg").cost(new BigDecimal(10)).build());
        clearingCostDao.save(ClearingCostDto.builder().countryCode("gr").cost(new BigDecimal(15)).build());
        clearingCostDao.save(ClearingCostDto.builder().countryCode("other").cost(new BigDecimal(20)).build());
    }

    @Test
    void getValidCardCostWithCountryCodeExistingInMatrix() {
        CardCostRequestDto cardCostRequestDto = CardCostRequestDto.builder().cardNumber("403244111").build();
        Mockito.when(binTableApiService.getCountryCodeFromBinTableApi("403244")).thenReturn("eg");
        ResponseEntity<?> responseEntity = cardCostService.getCardCost(cardCostRequestDto);
        ClearingCostDto expectedClearingCost = ClearingCostDto.builder().countryCode("eg").cost(new BigDecimal(10)).build();

        Assertions.assertAll(
                () -> Assertions.assertEquals(HttpStatusCode.valueOf(200), responseEntity.getStatusCode(), "Status code must be 200"),
                () -> Assertions.assertEquals(expectedClearingCost, responseEntity.getBody(), "Response is not the expected one")
        );
    }

    @Test
    void getValidCardCostWithCountryCodeNotExistingInMatrix() {
        CardCostRequestDto cardCostRequestDto = CardCostRequestDto.builder().cardNumber("403244111").build();
        Mockito.when(binTableApiService.getCountryCodeFromBinTableApi("403244")).thenReturn("fr");
        ResponseEntity<?> responseEntity = cardCostService.getCardCost(cardCostRequestDto);
        ClearingCostDto expectedClearingCost = ClearingCostDto.builder().countryCode("other").cost(new BigDecimal(20)).build();

        Assertions.assertAll(
                () -> Assertions.assertEquals(HttpStatusCode.valueOf(200), responseEntity.getStatusCode(), "Status code must be 200"),
                () -> Assertions.assertEquals(expectedClearingCost, responseEntity.getBody(), "Response is not the expected one")
        );
    }

    @Test
    void testBinTableNotFoundException() {
        CardCostRequestDto cardCostRequestDto = CardCostRequestDto.builder().cardNumber("403244111").build();
        Mockito.when(binTableApiService.getCountryCodeFromBinTableApi("403244")).thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));
        ResponseEntity<?> responseEntity = cardCostService.getCardCost(cardCostRequestDto);

        ResponseDto expectedResponseDto =
                ResponseDto.builder().messages(Set.of("Requested card number does not belong to a known issuer")).error(true).build();

        Assertions.assertAll(
                () -> Assertions.assertEquals(HttpStatusCode.valueOf(400), responseEntity.getStatusCode(), "Status code must be 400"),
                () -> Assertions.assertEquals(expectedResponseDto, responseEntity.getBody(), "Response entity is not the expected one")
        );
    }

    @Test
    void testBinTableInvalidApiKeyException() {
        CardCostRequestDto cardCostRequestDto = CardCostRequestDto.builder().cardNumber("403244111").build();
        Mockito.when(binTableApiService.getCountryCodeFromBinTableApi("403244")).thenThrow(new HttpClientErrorException(HttpStatus.UNAUTHORIZED));
        ResponseEntity<?> responseEntity = cardCostService.getCardCost(cardCostRequestDto);

        ResponseDto expectedResponseDto =
                ResponseDto.builder().messages(Set.of("Unable to calculate cost due to: 401 UNAUTHORIZED exception")).error(true).build();

        Assertions.assertAll(
                () -> Assertions.assertEquals(HttpStatusCode.valueOf(500), responseEntity.getStatusCode(), "Status code must be 500"),
                () -> Assertions.assertEquals(expectedResponseDto, responseEntity.getBody(), "Response entity is not the expected one")
        );
    }


    @AfterAll
    public void cleanUp() {
        clearingCostDao.delete("eg");
        clearingCostDao.delete("gr");
    }


}
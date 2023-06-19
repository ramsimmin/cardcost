package com.example.cardcost.controller;

import com.example.cardcost.dao.ClearingCostDao;
import com.example.cardcost.dto.CardCostRequestDto;
import com.example.cardcost.dto.ClearingCostDto;
import com.example.cardcost.dto.ResponseDto;
import com.example.cardcost.respository.ClearingCostRepository;
import com.example.cardcost.service.BinTableApiService;
import com.example.cardcost.service.CardCostService;
import com.example.cardcost.service.ClearingCostService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureMockMvc
class CardCostControllerTest {

    @Autowired
    CardCostService cardCostService;

    @Autowired
    ClearingCostRepository clearingCostRepository;

    @Autowired
    ClearingCostDao clearingCostDao;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    BinTableApiService binTableApiService;


    @BeforeAll
    public void uploadCosts() {
        clearingCostDao.save(ClearingCostDto.builder().countryCode("eg").cost(new BigDecimal(10)).build());
        clearingCostDao.save(ClearingCostDto.builder().countryCode("other").cost(new BigDecimal(20)).build());
    }

    @Test
    void testGetCardCostInvalidCardNumber() throws Exception {
        Mockito.when(binTableApiService.getCountryCodeFromBinTableApi("403244")).thenReturn("eg");
        String requestBody = objectMapper.writeValueAsString(CardCostRequestDto.builder().cardNumber("403244").build());
        MvcResult mvcResult = mockMvc.perform(post("/api/payment-cards-cost")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        ResponseDto responseDTO = objectMapper.readValue(response, ResponseDto.class);

        Assertions.assertAll(
                () -> Assertions.assertTrue(responseDTO.getError()),
                () -> Assertions.assertEquals(Set.of("Card number must contain numbers from 0 to 9 and have a length between 9 and 18"), responseDTO.getMessages(), "expected message not found")
        );

    }

    @Test
    void testGetValidCardCost() throws Exception {
        Mockito.when(binTableApiService.getCountryCodeFromBinTableApi("403244")).thenReturn("eg");
        String requestBody = objectMapper.writeValueAsString(CardCostRequestDto.builder().cardNumber("403244111").build());
        mockMvc.perform(post("/api/payment-cards-cost")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
    }

    @AfterAll
    public void cleanUp() {
        clearingCostRepository.deleteAll();
    }
}
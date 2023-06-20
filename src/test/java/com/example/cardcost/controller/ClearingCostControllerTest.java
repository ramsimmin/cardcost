package com.example.cardcost.controller;

import com.example.cardcost.dao.ClearingCostDao;
import com.example.cardcost.dto.ClearingCostDto;
import com.example.cardcost.respository.ClearingCostRepository;
import com.example.cardcost.service.ClearingCostService;
import com.example.cardcost.utils.FallbackCostInitializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cache.CacheManager;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Objects;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureMockMvc
class ClearingCostControllerTest {

    @Autowired
    ClearingCostService clearingCostService;

    @Autowired
    ClearingCostRepository clearingCostRepository;

    @Autowired
    ClearingCostDao clearingCostDao;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    CacheManager cacheManager;

    @MockBean
    FallbackCostInitializer fallbackCostInitializer;

    @BeforeAll
    public void uploadCosts() {
        clearingCostDao.save(ClearingCostDto.builder().countryCode("eg").cost(new BigDecimal(10)).build());
        clearingCostDao.save(ClearingCostDto.builder().countryCode("gr").cost(new BigDecimal(15)).build());
        clearingCostDao.save(ClearingCostDto.builder().countryCode("other").cost(new BigDecimal(20)).build());
    }

    @Test
    void testGetCostByCountryCodeNotFound() throws Exception {
        mockMvc.perform(get("/api/costs/xx"))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void testGetCostByExistingCountryCode() throws Exception {
        mockMvc.perform(get("/api/costs/eg"))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
    }

    @Test
    void testGetAllCosts() throws Exception {
        mockMvc.perform(get("/api/costs"))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
    }

    @Test
    void testCreateValidCost() throws Exception {
        String requestBody = objectMapper.writeValueAsString(ClearingCostDto.builder().countryCode("new").cost(new BigDecimal(14)).build());
        mockMvc.perform(post("/api/costs/create")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
    }

    @Test
    void testUpdateValidCost() throws Exception {
        String requestBody = objectMapper.writeValueAsString(ClearingCostDto.builder().countryCode("eg").cost(new BigDecimal(11)).build());
        mockMvc.perform(put("/api/costs/update")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
    }

    @Test
    void testDeleteExistingCost() throws Exception {
        mockMvc.perform(delete("/api/costs/delete/eg")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
    }

    @AfterAll
    public void cleanUp() {
        clearingCostRepository.deleteAll();
        Objects.requireNonNull(cacheManager.getCache("clearing-costs-cache")).invalidate();
    }
}
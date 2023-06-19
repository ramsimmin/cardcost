package com.example.cardcost.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.HttpClientErrorException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BinTableApiServiceTest {

    @Autowired
    BinTableApiService binTableApiService;


    @Test
    void testBinTableApiInvalidApiKey() {

        binTableApiService.binTableApiKey = "test test";
        Exception exception = assertThrows(HttpClientErrorException.class, () -> {
            binTableApiService.getCountryCodeFromBinTableApi("403244111");
        });

        String expectedMessage = "401 Unauthorized: \"{\"result\":401,\"message\":\"Invalid API Key\",\"data\":{}}\"";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));

    }
}
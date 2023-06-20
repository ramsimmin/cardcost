package com.example.cardcost.service;

import com.example.cardcost.utils.FallbackCostInitializer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.client.HttpClientErrorException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@TestPropertySource(properties = {"api.bintable.api_key=InvalidValue"})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BinTableApiUnauthorizedServiceTest {

    @Autowired
    BinTableApiService binTableApiService;

    @MockBean
    FallbackCostInitializer fallbackCostInitializer;

    @Test
    void testBinTableApiInvalidApiKey() {
        Exception exception = assertThrows(HttpClientErrorException.class, () -> {
            binTableApiService.getCountryCodeFromBinTableApi("403244111");
        });

        String expectedMessage = "401 Unauthorized: \"{\"result\":401,\"message\":\"Invalid API Key\",\"data\":{}}\"";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

}
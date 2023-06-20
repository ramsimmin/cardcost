package com.example.cardcost.service;

import com.example.cardcost.utils.FallbackCostInitializer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.TestPropertySource;

import java.util.Objects;
import java.util.Optional;

import static java.util.Optional.ofNullable;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@TestPropertySource(properties = {"api.bintable.api_key=df39ee4320aa8221067864eda1c0f2844f901336"})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BinTableApiServiceTest {

    @Autowired
    BinTableApiService binTableApiService;

    @Autowired
    CacheManager cacheManager;

    @MockBean
    FallbackCostInitializer fallbackCostInitializer;

    @Test
    @Disabled("Disabled due to limited api key balance")
    void testBinTableApiWithValidKey() {
        String iin = "403244";
        String actualCountryCode = binTableApiService.getCountryCodeFromBinTableApi(iin);
        assertEquals("eg", actualCountryCode, "Result is not the expected one");

        // Assert that the country result has been cached in binapi-results-cache
        assertEquals(Optional.of("eg"), getCachedBINCountryCode(iin), "Result country code is expected to be found in cache");

    }

    private Optional<String> getCachedBINCountryCode(String iin) {
        return ofNullable(cacheManager.getCache("binapi-results-cache")).map(c -> c.get(iin, String.class));
    }

    @AfterAll
    public void cleanUp() {
        Objects.requireNonNull(cacheManager.getCache("binapi-results-cache")).invalidate();
    }
}
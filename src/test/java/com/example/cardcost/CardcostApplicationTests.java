package com.example.cardcost;

import com.example.cardcost.utils.FallbackCostInitializer;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class CardcostApplicationTests {

    @MockBean
    FallbackCostInitializer fallbackCostInitializer;

    @Test
    void contextLoads() {
    }

}

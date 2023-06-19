package com.example.cardcost.validation;

import com.example.cardcost.dto.CardCostRequestDto;
import com.example.cardcost.dto.ClearingCostDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class CommonValidationsTest {

    @Autowired
    CommonValidations commonValidations;

    @Test
    void validateCostRequestDtoNullValues() {
        ClearingCostDto clearingCostDto = ClearingCostDto.builder().build();
        Set<String> errorMessages = commonValidations.validateCostRequestDto(clearingCostDto);

        Assertions.assertAll(
                () -> Assertions.assertTrue(errorMessages.contains("Country code must be provided"), "errorMessages does not contain expected message"),
                () -> Assertions.assertTrue(errorMessages.contains("Cost must be provided"), "errorMessages does not contain expected message"),
                () -> Assertions.assertEquals(errorMessages.size() , 2, "errorMessages size is not the expected one")
        );
    }

    @Test
    void validateCostRequestDtoBlankValue() {
        ClearingCostDto clearingCostDto = ClearingCostDto.builder().countryCode(" ").cost(null).build();
        Set<String> errorMessages = commonValidations.validateCostRequestDto(clearingCostDto);

        Assertions.assertAll(
                () -> Assertions.assertTrue(errorMessages.contains("Country code must be provided"), "errorMessages does not contain expected message"),
                () -> Assertions.assertTrue(errorMessages.contains("Cost must be provided"), "errorMessages does not contain expected message"),
                () -> Assertions.assertEquals(errorMessages.size() , 2, "errorMessages size is not the expected one")
        );
    }

    @Test
    void validateCostRequestDtoCostNegativeValue() {
        ClearingCostDto clearingCostDto = ClearingCostDto.builder().countryCode("es").cost(new BigDecimal(-10.54d)).build();
        Set<String> errorMessages = commonValidations.validateCostRequestDto(clearingCostDto);

        Assertions.assertAll(
                () -> Assertions.assertTrue(errorMessages.contains("Cost must be greater than or equal 0"), "errorMessages does not contain expected message"),
                () -> Assertions.assertEquals(errorMessages.size() , 1, "errorMessages size is not the expected one")
        );
    }

    @Test
    void validateCardNumberNull() {
        CardCostRequestDto cardCostRequestDto = CardCostRequestDto.builder().build();
        Set<String> errorMessages = commonValidations.validateCardNumber(cardCostRequestDto);

        Assertions.assertAll(
                () -> Assertions.assertTrue(errorMessages.contains("Card number must be provided"), "errorMessages does not contain expected message"),
                () -> Assertions.assertEquals(errorMessages.size() , 1, "errorMessages size is not the expected one")
        );
    }

    @Test
    void validateCardNumberBlank() {
        CardCostRequestDto cardCostRequestDto = CardCostRequestDto.builder().cardNumber(" ").build();
        Set<String> errorMessages = commonValidations.validateCardNumber(cardCostRequestDto);

        Assertions.assertAll(
                () -> Assertions.assertTrue(errorMessages.contains("Card number must be provided"), "errorMessages does not contain expected message"),
                () -> Assertions.assertEquals(errorMessages.size() , 1, "errorMessages size is not the expected one")
        );
    }

    @Test
    void validateCardNumberInvalidCardNumbers() {
        CardCostRequestDto cardCostRequestDto = CardCostRequestDto.builder().cardNumber(" ").build();
        Set<String> errorMessages = commonValidations.validateCardNumber(cardCostRequestDto);

        Assertions.assertAll(
                () -> Assertions.assertTrue(errorMessages.contains("Card number must be provided"), "errorMessages does not contain expected message"),
                () -> Assertions.assertEquals(errorMessages.size() , 1, "errorMessages size is not the expected one")
        );
    }


    @ParameterizedTest
    @MethodSource("provideInvalidCardNumbers")
    public void validateCardNumberInvalidCardNumbers(CardCostRequestDto cardCostRequestDto)  {
        Set<String> errorMessages = commonValidations.validateCardNumber(cardCostRequestDto);
        Assertions.assertAll(
                () -> Assertions.assertTrue(errorMessages.contains("Card number must contain numbers from 0 to 9 and have a length between 9 and 18"), "errorMessages does not contain expected message"),
                () -> Assertions.assertEquals(errorMessages.size() , 1, "errorMessages size is not the expected one")
        );
    }

    private static List<CardCostRequestDto> provideInvalidCardNumbers() {
        return List.of(
                CardCostRequestDto.builder().cardNumber("1234567").build(),
                CardCostRequestDto.builder().cardNumber("12345678912345678912345678").build(),
                CardCostRequestDto.builder().cardNumber("1234567ab").build(),
                CardCostRequestDto.builder().cardNumber("1234567 9 10").build()
        );
    }

    @Test
    void testValidCardCostRequestDto() {
        CardCostRequestDto cardCostRequestDto = CardCostRequestDto.builder().cardNumber("0123456789").build();
        Set<String> errorMessages = commonValidations.validateCardNumber(cardCostRequestDto);
        Assertions.assertEquals(errorMessages.size() , 0, "errorMessages size expected to be empty");
    }

    @Test
    void testValidClearingCostDto() {
        ClearingCostDto clearingCostDto = ClearingCostDto.builder().countryCode("gr").cost(new BigDecimal(15)).build();
        Set<String> errorMessages = commonValidations.validateCostRequestDto(clearingCostDto);
        Assertions.assertEquals(errorMessages.size() , 0, "errorMessages size expected to be empty");
    }


}
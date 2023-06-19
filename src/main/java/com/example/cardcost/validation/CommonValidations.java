package com.example.cardcost.validation;

import com.example.cardcost.dto.CardCostRequestDto;
import com.example.cardcost.dto.ClearingCostDto;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;


import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.*;

@Service
public class CommonValidations {

    public Set<String> validateCostRequestDto(ClearingCostDto clearingCostDto) {
        Set<String> errorMessages = new HashSet<>();
        errorMessages.addAll(validateCountryCode(clearingCostDto));
        errorMessages.addAll(validateCostAmount(clearingCostDto));
        return errorMessages;
    }

    private Set<String> validateCountryCode(ClearingCostDto clearingCostDto){
        Set<String> errorMessages = new HashSet<>();
        if (StringUtils.isBlank(clearingCostDto.getCountryCode())) {
            errorMessages.add("Country code must be provided");
        }
        return errorMessages;
    }

    private Set<String> validateCostAmount(ClearingCostDto clearingCostDto){
        Set<String> errorMessages = new HashSet<>();
        if (clearingCostDto.getCost() == null) {
            errorMessages.add("Cost must be provided");
        } else if (clearingCostDto.getCost().compareTo(new BigDecimal(0)) < 0) {
            errorMessages.add("Cost must be greater than or equal 0");
        }
        return errorMessages;
    }

    public Set<String> validateCardNumber(CardCostRequestDto cardCostRequestDto){
        Set<String> errorMessages = new HashSet<>();

        if (StringUtils.isBlank(cardCostRequestDto.getCardNumber())) {
            errorMessages.add("Card number must be provided");
            return errorMessages;
        }

        // PAN must be an 8 to 19 digit
        String regex = "^[0-9]{9,18}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(cardCostRequestDto.getCardNumber());

        if (!matcher.matches()) {
            errorMessages.add("Card number must contain numbers from 0 to 9 and have a length between 9 and 18");
            return errorMessages;
        }

        return errorMessages;
    }

}

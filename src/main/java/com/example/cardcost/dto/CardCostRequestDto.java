package com.example.cardcost.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CardCostRequestDto {
    @JsonProperty(value = "card_number")
    @Schema(type = "string", example = "403244111")
    private String cardNumber;
}

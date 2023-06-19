package com.example.cardcost.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@Builder
@JsonPropertyOrder({
        "country_code",
        "cost"
})
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class ClearingCostDto implements Serializable{
    @JsonProperty(value = "country_code")
    @Schema(type = "string", example = "eg")
    private String countryCode;
    @Schema(type = "decimal", example = "15")
    private BigDecimal cost;
}

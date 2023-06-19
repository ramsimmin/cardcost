package com.example.cardcost.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BinApiResponseDto {
    private Integer result;
    private String message;
    private BinApiDataDto data;
}




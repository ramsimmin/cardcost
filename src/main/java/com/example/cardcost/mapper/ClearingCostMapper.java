package com.example.cardcost.mapper;


import com.example.cardcost.dto.ClearingCostDto;
import com.example.cardcost.model.ClearingCost;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.time.Instant;
import java.util.List;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface ClearingCostMapper {

    @Mapping(source = "countryCode", target = "countryCode", qualifiedByName = "toLowercase")
    ClearingCostDto entityToDto(ClearingCost clearingCost);
    @Mapping(source = "countryCode", target = "countryCode", qualifiedByName = "toLowercase")
    ClearingCost dtoToEntity(ClearingCostDto clearingCostDto);
    List<ClearingCostDto> entitiesToDtos(List<ClearingCost> clearingCosts);


    @Named("toLowercase")
    default String toLowercase(String countryCode) {
        return countryCode.toLowerCase();
    }
}

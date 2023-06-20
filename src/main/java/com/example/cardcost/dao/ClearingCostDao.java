package com.example.cardcost.dao;

import com.example.cardcost.dto.ClearingCostDto;

import java.util.List;

public interface ClearingCostDao {

    ClearingCostDto save(ClearingCostDto clearingCostDto);

    ClearingCostDto findById(String countryCode);

    List<ClearingCostDto> findAll();

    void delete(String countryCode);
}

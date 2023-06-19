package com.example.cardcost.dao;

import com.example.cardcost.model.ClearingCostMatrix;

public interface ClearingCostMatrixDao {
    void save(ClearingCostMatrix clearingCostMatrix);

    ClearingCostMatrix findById(String countryCode);

    void delete(String countryCode);
}

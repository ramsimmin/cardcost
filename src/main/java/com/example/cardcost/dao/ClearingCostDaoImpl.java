package com.example.cardcost.dao;

import com.example.cardcost.model.ClearingCostMatrix;
import com.example.cardcost.respository.ClearingCostMatrixRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ClearingCostMatrixDaoImpl implements ClearingCostMatrixDao {

    private final ClearingCostMatrixRepository clearingCostMatrixRepository;

    @Override
    public void save(ClearingCostMatrix clearingCostMatrix) {
        clearingCostMatrixRepository.save(clearingCostMatrix);
    }

    @Override
    public ClearingCostMatrix findById(String countryCode) {
        Optional<ClearingCostMatrix> clearingCostMatrix = clearingCostMatrixRepository.findById(countryCode);
        if (clearingCostMatrix.isPresent()) {
            return clearingCostMatrix.get();
        } else {
            return null;
        }
    }

    @Override
    public void delete(String countryCode) {
        clearingCostMatrixRepository.deleteById(countryCode);
    }
}

package com.example.cardcost.respository;

import com.example.cardcost.model.ClearingCostMatrix;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClearingCostMatrixRepository extends CrudRepository<ClearingCostMatrix, String> {
}

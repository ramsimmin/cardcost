package com.example.cardcost.respository;

import com.example.cardcost.model.ClearingCost;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClearingCostRepository extends CrudRepository<ClearingCost, String> {

    @Override
    List<ClearingCost> findAll();
}

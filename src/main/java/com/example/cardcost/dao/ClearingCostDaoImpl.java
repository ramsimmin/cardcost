package com.example.cardcost.dao;

import com.example.cardcost.dto.ClearingCostDto;
import com.example.cardcost.mapper.ClearingCostMapper;
import com.example.cardcost.model.ClearingCost;
import com.example.cardcost.respository.ClearingCostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ClearingCostDaoImpl implements ClearingCostDao {

    private final ClearingCostRepository clearingCostRepository;
    private final ClearingCostMapper costMapper;

    @Override
    public void save(ClearingCostDto clearingCostDto) {
        clearingCostRepository.save(costMapper.dtoToEntity(clearingCostDto));
    }

    @Override
    public ClearingCostDto findById(String countryCode) {
        Optional<ClearingCost> clearingCostMatrix = clearingCostRepository.findById(countryCode);
        if (clearingCostMatrix.isPresent()) {
            return costMapper.entityToDto(clearingCostMatrix.get());
        } else {
            return null;
        }
    }

    @Override
    public List<ClearingCostDto> findAll() {
        return costMapper.entitiesToDtos(clearingCostRepository.findAll());
    }

    @Override
    public void delete(String countryCode) {
        clearingCostRepository.deleteById(countryCode);
    }
}

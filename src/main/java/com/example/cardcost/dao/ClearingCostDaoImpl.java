package com.example.cardcost.dao;

import com.example.cardcost.dto.ClearingCostDto;
import com.example.cardcost.mapper.ClearingCostMapper;
import com.example.cardcost.model.ClearingCost;
import com.example.cardcost.respository.ClearingCostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ClearingCostDaoImpl implements ClearingCostDao {

    private final ClearingCostRepository clearingCostRepository;
    private final ClearingCostMapper costMapper;

    @Override
    @CachePut(value = "clearing-costs-cache", key = "#clearingCostDto.countryCode")
    public ClearingCostDto save(ClearingCostDto clearingCostDto) {
        return costMapper.entityToDto(clearingCostRepository.save(costMapper.dtoToEntity(clearingCostDto)));
    }

    @Override
    @Cacheable(value = "clearing-costs-cache", key = "#countryCode", unless="#result == null")
    public ClearingCostDto findById(String countryCode) {
        Optional<ClearingCost> clearingCost = clearingCostRepository.findById(countryCode);
        return clearingCost.map(costMapper::entityToDto).orElse(null);
    }

    @Override
    public List<ClearingCostDto> findAll() {
        return costMapper.entitiesToDtos(clearingCostRepository.findAll());
    }

    @Override
    @CacheEvict(value = "clearing-costs-cache", key = "#countryCode")
    public void delete(String countryCode) {
        clearingCostRepository.deleteById(countryCode);
    }
}

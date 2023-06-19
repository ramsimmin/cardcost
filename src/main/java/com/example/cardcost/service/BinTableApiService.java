package com.example.cardcost.service;

import com.example.cardcost.dto.BinApiResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class BinTableApiService {

    @Value("${api.bintable.url}")
    String binTableApiUrl;

    @Value("${api.bintable.api_key}")
    String binTableApiKey;


    @Cacheable(value = "binapi-results-cache", key = "#issuerIdentificationNumber", unless="#result == null")
    public String getCountryCodeFromBinTableApi(String issuerIdentificationNumber) {

        Map<String, String> map = new HashMap<>();
        map.put("iin", issuerIdentificationNumber);
        map.put("api_key", binTableApiKey);
        String url = binTableApiUrl;
        RestTemplate restTemplate = new RestTemplate();

        BinApiResponseDto response = restTemplate.getForObject(url, BinApiResponseDto.class, map);
        if (response == null)
            throw new RuntimeException("Response received from BinTable api is null");
        return response.getData().getCountry().getCode().toLowerCase();
    }
}

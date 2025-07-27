package com.progressoft.clustereddata_warehouse.Service.implementation;

import com.progressoft.clustereddata_warehouse.Service.api.CurrencyVerifier;
import com.progressoft.clustereddata_warehouse.Service.api.FxDealService;
import com.progressoft.clustereddata_warehouse.dto.request.FxDealRequestDto;
import com.progressoft.clustereddata_warehouse.dto.response.FxDealResponseDto;
import com.progressoft.clustereddata_warehouse.entity.FxDeal;
import com.progressoft.clustereddata_warehouse.exception.DuplicateDealException;
import com.progressoft.clustereddata_warehouse.mapper.FxDealMapper;
import com.progressoft.clustereddata_warehouse.repository.FxDealRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class FxDealServiceImpl implements FxDealService {

    private final FxDealRepository fxDealRepository;
    private final FxDealMapper fxDealMapper;
    private final CurrencyVerifier currencyVerifier;

    public FxDealServiceImpl(FxDealRepository fxDealRepository, FxDealMapper fxDealMapper, CurrencyVerifier currencyVerifier) {
        this.fxDealRepository = fxDealRepository;
        this.fxDealMapper = fxDealMapper;
        this.currencyVerifier = currencyVerifier;
    }

    @Override
    public FxDealResponseDto save(FxDealRequestDto requestDto) {
        log.info("Processing FX deal request: ID={}, From={}, To={}, Amount={}",
                requestDto.id(), requestDto.fromCurrency(), requestDto.toCurrency(), requestDto.dealAmount());

        currencyVerifier.validate(requestDto.fromCurrency(), requestDto.toCurrency());

        String dealId = requestDto.id();

        if (fxDealRepository.existsById(dealId)) {
            log.warn("Duplicate deal ID detected: {}", dealId);
            throw new DuplicateDealException("A deal with ID '" + dealId + "' already exists.");
        }

        FxDeal entityToSave = fxDealMapper.toEntity(requestDto);
        FxDeal savedEntity = fxDealRepository.save(entityToSave);

        log.info("Successfully saved FX deal with ID: {}", savedEntity.getId());
        return fxDealMapper.toResponseDto(savedEntity);
    }
}

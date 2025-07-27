package com.progressoft.clustereddata_warehouse.controller;

import com.progressoft.clustereddata_warehouse.Service.api.FxDealService;
import com.progressoft.clustereddata_warehouse.dto.request.FxDealRequestDto;
import com.progressoft.clustereddata_warehouse.dto.response.FxDealResponseDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/deals")
@Validated
public class FxDealController{
    private final FxDealService fxDealService;

    public FxDealController(FxDealService fxDealService) {
        this.fxDealService = fxDealService;
    }
    @PostMapping
    public ResponseEntity<FxDealResponseDto> saveDeal(@RequestBody @Valid FxDealRequestDto requestDto){
        FxDealResponseDto deal = fxDealService.save(requestDto);

        return new ResponseEntity<>(deal, HttpStatus.CREATED);
    }
}

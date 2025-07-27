package com.progressoft.clustereddata_warehouse.Service.api;

import com.progressoft.clustereddata_warehouse.dto.request.FxDealRequestDto;
import com.progressoft.clustereddata_warehouse.dto.response.FxDealResponseDto;

public interface FxDealService {
    FxDealResponseDto save(FxDealRequestDto requestDto);
}

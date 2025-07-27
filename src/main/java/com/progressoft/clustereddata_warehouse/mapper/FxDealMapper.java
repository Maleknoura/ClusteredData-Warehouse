package com.progressoft.clustereddata_warehouse.mapper;

import com.progressoft.clustereddata_warehouse.dto.request.FxDealRequestDto;
import com.progressoft.clustereddata_warehouse.dto.response.FxDealResponseDto;
import com.progressoft.clustereddata_warehouse.entity.FxDeal;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FxDealMapper {


    FxDeal toEntity(FxDealRequestDto dto);

    FxDealResponseDto toResponseDto(FxDeal entity);

}

package com.mecsbalint.backend.controller.dto.isthereanydealapi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ItadGamePriceInfoDto(ItadPriceHistoryLowDto historyLow, List<ItadGamePriceDealDto> deals) {
}

package com.mecsbalint.backend.service.game;

import com.mecsbalint.backend.controller.dto.GamePricesDto;
import com.mecsbalint.backend.controller.dto.isthereanydealapi.*;
import com.mecsbalint.backend.utility.Fetcher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ItadPriceServiceTest {

    @Mock
    private Fetcher fetcherMock;

    private ItadPriceService itadPriceService;

    @BeforeEach
    public void setUp() {
        itadPriceService = new ItadPriceService(fetcherMock, "itad_api_key");
    }

    @Test
    public void getGamePrices_gameFoundAndPricesFound_returnOptionalGamePricesDto() {
        ItadGameInfoDto itadGameInfoDto = new ItadGameInfoDto(new ItadGameInfoGameDto("game_id"));
        when(fetcherMock.fetch(any(), any())).thenReturn(itadGameInfoDto);
        when(fetcherMock.fetch(any(), any(), any(), any(), any())).thenReturn(new ItadGamePriceInfoDto[]{getItadGamePriceInfoDto()});

        double expectedCurrentBestPrice = 10;
        double actualCurrentBestPrice = itadPriceService.getGamePrices(null, null).get().current().amount();

        assertEquals(expectedCurrentBestPrice, actualCurrentBestPrice);
    }

    @Test
    public void getGamePrices_gameNotFound_returnOptionalEmpty() {
        when(fetcherMock.fetch(any(), any())).thenReturn(null);

        Optional<GamePricesDto> expectedResult = Optional.empty();
        Optional<GamePricesDto> actualResult = itadPriceService.getGamePrices(null, null);

        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void getGamePrices_fetchFail_returnOptionalEmpty() {
        when(fetcherMock.fetch(any(), any())).thenThrow(new RuntimeException());

        Optional<GamePricesDto> expectedResult = Optional.empty();
        Optional<GamePricesDto> actualResult = itadPriceService.getGamePrices(null, null);

        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void getGamePrices_gameFoundButPricesNotFound_returnOptionalEmpty() {
        ItadGameInfoDto itadGameInfoDto = new ItadGameInfoDto(new ItadGameInfoGameDto("game_id"));
        when(fetcherMock.fetch(any(), any())).thenReturn(itadGameInfoDto);
        when(fetcherMock.fetch(any(), any(), any(), any(), any())).thenReturn(new ItadGamePriceInfoDto[]{});

        Optional<GamePricesDto> expectedResult = Optional.empty();
        Optional<GamePricesDto> actualResult = itadPriceService.getGamePrices(null, null);

        assertEquals(expectedResult, actualResult);
    }

    private ItadGamePriceInfoDto getItadGamePriceInfoDto() {
        ItadPriceDto itadPriceDto = new ItadPriceDto(10, "currency_one");
        ItadPriceHistoryLowDto itadPriceHistoryLowDto = new ItadPriceHistoryLowDto(itadPriceDto);
        ItadGamePriceDealDto itadGamePriceDealDto = new ItadGamePriceDealDto(itadPriceDto, "shop_url");

        return new ItadGamePriceInfoDto(itadPriceHistoryLowDto, List.of(itadGamePriceDealDto));
    }
}

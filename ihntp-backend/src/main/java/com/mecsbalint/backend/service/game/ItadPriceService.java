package com.mecsbalint.backend.service.game;

import com.mecsbalint.backend.controller.dto.game.GamePriceDto;
import com.mecsbalint.backend.controller.dto.game.GamePricesDto;
import com.mecsbalint.backend.controller.dto.isthereanydealapi.ItadGameInfoDto;
import com.mecsbalint.backend.controller.dto.isthereanydealapi.ItadGamePriceDealDto;
import com.mecsbalint.backend.controller.dto.isthereanydealapi.ItadGamePriceInfoDto;
import com.mecsbalint.backend.controller.dto.isthereanydealapi.ItadPriceDto;
import com.mecsbalint.backend.utility.Fetcher;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.Optional;

@Service
public class ItadPriceService implements GamePriceService {

    private final Fetcher fetcher;

    private final String itadApiKey;

    public ItadPriceService(Fetcher fetcher, @Value("${mecsbalint.app.itadApiKey}") String itadApiKey) {
        this.fetcher = fetcher;
        this.itadApiKey = itadApiKey;
    }

    @Override
    public Optional<GamePricesDto> getGamePrices(String gameName, String userCountry) {
        try {
            String gameInfoFetchUrl = String.format("https://api.isthereanydeal.com/games/lookup/v1?key=%s&title=%s", itadApiKey, gameName);
            ItadGameInfoDto gameInfo = fetcher.fetch(gameInfoFetchUrl, ItadGameInfoDto.class);

            if (gameInfo == null || gameInfo.game() == null) return Optional.empty();

            if (userCountry == null) userCountry = "US";
            String itadGameId = gameInfo.game().id();
            String priceInfoFetchUrl = String.format("https://api.isthereanydeal.com/games/prices/v3?key=%s&deals=false&country=%s", itadApiKey, userCountry);
            ItadGamePriceInfoDto[] gamePrices = fetcher.fetch(priceInfoFetchUrl, ItadGamePriceInfoDto[].class, HttpMethod.POST, "application/json", new String[]{itadGameId});

            if (gamePrices.length == 0) return Optional.empty();

            ItadGamePriceDealDto currentBestPrice = gamePrices[0].deals().stream()
                    .min(Comparator.comparingDouble(a -> a.price().amount())).get();
            ItadPriceDto historyLowPrice = gamePrices[0].historyLow().all();

            GamePriceDto currentPrice = new GamePriceDto(currentBestPrice.price().currency(), currentBestPrice.price().amount(), currentBestPrice.url());
            String priceHistoryUrl = String.format("https://isthereanydeal.com/game/id:%s/history/", itadGameId);
            GamePriceDto historyLow = new GamePriceDto(historyLowPrice.currency(), historyLowPrice.amount(), priceHistoryUrl);
            return Optional.of(new GamePricesDto(currentPrice, historyLow));
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}

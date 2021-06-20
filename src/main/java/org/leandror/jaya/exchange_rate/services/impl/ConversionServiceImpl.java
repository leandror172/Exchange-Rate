package org.leandror.jaya.exchange_rate.services.impl;

import static java.lang.String.join;
import static java.math.RoundingMode.HALF_EVEN;
import static java.time.Instant.ofEpochSecond;
import static java.time.LocalDateTime.ofInstant;
import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;
import static org.leandror.jaya.exchange_rate.utils.Constants.ACCESS_KEY;
import static org.leandror.jaya.exchange_rate.utils.Constants.BANKING_CALCULATION_SCALE;
import static org.leandror.jaya.exchange_rate.utils.Constants.CURRENCY_CODE_EUR;
import static org.leandror.jaya.exchange_rate.utils.Constants.TIME_ZONE_UTC;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import org.javamoney.moneta.Money;
import org.leandror.jaya.exchange_rate.apis.clients.ExchangeRatesApiClient;
import org.leandror.jaya.exchange_rate.dtos.ConversionRequest;
import org.leandror.jaya.exchange_rate.dtos.ConversionResponse;
import org.leandror.jaya.exchange_rate.dtos.MonetaryAmount;
import org.leandror.jaya.exchange_rate.dtos.RatesResponse;
import org.leandror.jaya.exchange_rate.exceptions.ExchangeRateApiUnavailableException;
import org.leandror.jaya.exchange_rate.repositories.ConversionRepository;
import org.leandror.jaya.exchange_rate.services.ConversionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.NonNull;

@Service
public class ConversionServiceImpl implements ConversionService {

  private ExchangeRatesApiClient client;
  private ConversionRepository repository;

  @Autowired
  public ConversionServiceImpl(ExchangeRatesApiClient client,
      ConversionRepository repository) {
    this.client = client;
    this.repository = repository;
  }

  @Override
  public Optional<List<ConversionResponse>> listAll() {
    return Optional.ofNullable(repository.findAll());
  }
  
  @Override
  public ConversionResponse convert(ConversionRequest request) {
    requireNonNull(request, "No exchange rate conversion informed");
    createParamMap(request);
    ConversionResponse response = Optional.ofNullable(client.latest(createParamMap(request)))
                                          .map(responseFunctionMapper)
                                          .orElseThrow(() -> new ExchangeRateApiUnavailableException());
    response = populateResponse(request, response,
                                calculateCurrencyConversionRate(ofNullable(response.getConversionRates()
                                                                                   .get(request.getDesiredCurrency())),
                                                                ofNullable(response.getConversionRates()
                                                                                   .get(request.getOrigin()
                                                                                               .getCurrency()))));
    return repository.save(response);
  }

  private Map<String, String> createParamMap(ConversionRequest request) {
    return Map.of("access_key", ACCESS_KEY, "base", CURRENCY_CODE_EUR, "symbols",
                  join(",", request.getDesiredCurrency(), request.getOrigin()
                                                                 .getCurrency(),
                       CURRENCY_CODE_EUR));
  }

  private BigDecimal calculateCurrencyConversionRate(Optional<BigDecimal> desiredCurrencyRate,
                                                     Optional<BigDecimal> originCurrencyRate) {
    return originCurrencyRate.orElseThrow(() -> new RuntimeException())
                             .divide(desiredCurrencyRate.orElseThrow(() -> new RuntimeException()),
                                     BANKING_CALCULATION_SCALE, HALF_EVEN);
  }

  private ConversionResponse populateResponse(ConversionRequest request,
                                              ConversionResponse response,
                                              BigDecimal conversionRate) {
    response.setConverted(MonetaryAmount.builder()
                                        .withAmount(calculateConversion(request,
                                                                        conversionRate))
                                        .withCurrency(request.getDesiredCurrency())
                                        .build());
    response.setUsedConversionRate(conversionRate);
    response.setOrigin(request.getOrigin());
    response.setUserId(request.getUserId());
    return response;
  }

  private @NonNull BigDecimal calculateConversion(ConversionRequest request,
                                                  BigDecimal conversionRate) {
    return Money.of(request.getOrigin()
                           .getAmount(),
                    request.getOrigin()
                           .getCurrency())
                .divide(conversionRate)
                .getNumber()
                .numberValueExact(BigDecimal.class)
                .setScale(BANKING_CALCULATION_SCALE, HALF_EVEN);
  }

  Function<RatesResponse, ConversionResponse> responseFunctionMapper = (rates) -> {
    if (rates.isSuccess()) {
      return ConversionResponse.builder()
                               .withTransactionDate(ofInstant(ofEpochSecond(rates.getTimestamp()),
                                                              ZoneId.of(TIME_ZONE_UTC)))
                               .withConversionRates(rates.getRates())
                               .build();
    } else {
      return null;
    }
  };

}

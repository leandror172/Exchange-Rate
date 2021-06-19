package org.leandror.jaya.exchange_rate.services.impl;

import static java.time.Instant.ofEpochMilli;
import static java.time.LocalDateTime.ofInstant;
import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;
import static org.leandror.jaya.exchange_rate.utils.Constants.ACCESS_KEY;
import static org.leandror.jaya.exchange_rate.utils.Constants.CURRENCY_CODE_EUR;
import static org.leandror.jaya.exchange_rate.utils.Constants.TIME_ZONE_UTC;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.util.Optional;
import java.util.function.Function;

import org.leandror.jaya.exchange_rate.apis.clients.ExchangeRatesApiClient;
import org.leandror.jaya.exchange_rate.dtos.ConversionRequest;
import org.leandror.jaya.exchange_rate.dtos.ConversionResponse;
import org.leandror.jaya.exchange_rate.dtos.MonetaryAmount;
import org.leandror.jaya.exchange_rate.dtos.RatesResponse;
import org.leandror.jaya.exchange_rate.services.ConversionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConversionServiceImpl implements ConversionService {

  private ExchangeRatesApiClient client;

  @Autowired
  public ConversionServiceImpl(ExchangeRatesApiClient client) {
    this.client = client;
  }

  @Override
  public ConversionResponse convert(ConversionRequest request) {
    requireNonNull(request, "No exchange rate conversion informed");

    ConversionResponse response = Optional.ofNullable(client.latest(ACCESS_KEY,
                                                                    CURRENCY_CODE_EUR,
                                                                    request.getDesiredCurrency()))
                                          .map(responseFunctionMapper)
                                          .orElseThrow(() -> new RuntimeException());
    response = populateResponse(request, response,
                                calculateCurrencyConversionRate(ofNullable(response.getConversionRates()
                                                                                   .get(request.getDesiredCurrency())),
                                                                ofNullable(response.getConversionRates()
                                                                                   .get(request.getOrigin()
                                                                                               .getCurrency()))));
    return response;
  }

  private BigDecimal calculateCurrencyConversionRate(Optional<BigDecimal> desiredCurrencyRate,
                                                     Optional<BigDecimal> originCurrencyRate) {
    return originCurrencyRate.orElseThrow(() -> new RuntimeException())
                             .divide(desiredCurrencyRate.orElseThrow(() -> new RuntimeException()));
  }

  private ConversionResponse populateResponse(ConversionRequest request,
                                              ConversionResponse response,
                                              BigDecimal conversionRate) {
    response.setConverted(MonetaryAmount.builder()
                                        .withAmount(request.getOrigin()
                                                           .getAmount()
                                                           .multiply(conversionRate))
                                        .withCurrency(request.getDesiredCurrency())
                                        .build());
    response.setUsedConversionRate(conversionRate);
    response.setOrigin(request.getOrigin());
    response.setUserId(request.getUserId());
    return response;
  }

  Function<RatesResponse, ConversionResponse> responseFunctionMapper = (rates) -> {
    if (rates.isSuccess()) {
      return ConversionResponse.builder()
                               .withTransactionDate(ofInstant(ofEpochMilli(rates.getTimestamp()),
                                                              ZoneId.of(TIME_ZONE_UTC)))
                               .withConversionRates(rates.getRates())
                               .build();
    } else {
      return null;
    }
  };
}

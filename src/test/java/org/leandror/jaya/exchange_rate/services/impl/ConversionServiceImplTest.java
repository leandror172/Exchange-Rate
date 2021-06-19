package org.leandror.jaya.exchange_rate.services.impl;

import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.TEN;
import static java.time.Instant.ofEpochMilli;
import static java.time.LocalDateTime.ofInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Map;
import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.leandror.jaya.exchange_rate.apis.clients.ExchangeRatesApiClient;
import org.leandror.jaya.exchange_rate.dtos.ConversionRequest;
import org.leandror.jaya.exchange_rate.dtos.ConversionResponse;
import org.leandror.jaya.exchange_rate.dtos.MonetaryAmount;
import org.leandror.jaya.exchange_rate.dtos.RatesResponse;
import org.leandror.jaya.exchange_rate.services.ConversionService;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import io.github.glytching.junit.extension.random.Random;
import io.github.glytching.junit.extension.random.RandomBeansExtension;

@ExtendWith({ MockitoExtension.class, RandomBeansExtension.class })
@SpringBootTest(classes = {})
class ConversionServiceImplTest {

  private static final BigDecimal RATE_BRL_TO_EUR = new BigDecimal(5);
  private static final BigDecimal RATE_USD_TO_EUR = new BigDecimal(2);
  private static final String TIME_ZONE_UTC = "UTC";
  private static final String CURRENCY_CODE_BR = "BRL";
  private static final String CURRENCY_CODE_EUR = "EUR";
  private static final String CURRENCY_CODE_USD = "USD";

  private ConversionService service;
  @Mock
  private ExchangeRatesApiClient client;

  @BeforeEach
  void setUp() throws Exception {
    MockitoAnnotations.openMocks(this);
    service = new ConversionServiceImpl(client);
  }

  @AfterEach
  void tearDown() throws Exception {
  }

  @Test
  void returnTransactionData_when_ConvertBRLToUSD(@Random UUID userId,
                                                  @Random UUID transactionId) {

    ConversionRequest request = ConversionRequest.builder()
                                                 .withUserId(userId)
                                                 .withDesiredCurrency(CURRENCY_CODE_BR)
                                                 .withOrigin(buildMonetaryAmount(TEN,
                                                                                 CURRENCY_CODE_USD))
                                                 .build();

    LocalDateTime date = ofInstant(ofEpochMilli(1624076343L),
                                   ZoneId.of(TIME_ZONE_UTC));
    mockCallClientLatest(date);

    ConversionResponse response = service.convert(request);

    assertThat(response).isNotNull();
    assertThat(response.getUserId()).isEqualTo(userId);
//    assertThat(response.getTransactionId()).isEqualTo(transactionId);
    assertThat(response.getTransactionDate()).isEqualTo(date);
    assertThat(response.getUsedConversionRate()).isEqualTo(RATE_USD_TO_EUR.divide(RATE_BRL_TO_EUR));
    assertThat(response.getConverted()).isNotNull();
    assertThat(response.getConverted()).isEqualTo(buildMonetaryAmount(new BigDecimal(4.0).setScale(1),
                                                                      CURRENCY_CODE_BR));

    verify(client, times(1)).latest(any(), any(), any());
  }
  

  private MonetaryAmount buildMonetaryAmount(BigDecimal amount, String currency) {
    return MonetaryAmount.builder()
                         .withAmount(amount)
                         .withCurrency(currency)
                         .build();
  }

  private void mockCallClientLatest(LocalDateTime date) {

    when(client.latest(any(), any(), any())).thenReturn(RatesResponse.builder()
                                                                     .withSuccess(true)
                                                                     .withTimestamp(1624076343L)
                                                                     .withDate(date.toLocalDate())
                                                                     .withRates(Map.of(CURRENCY_CODE_BR,
                                                                                       RATE_BRL_TO_EUR,
                                                                                       CURRENCY_CODE_EUR,
                                                                                       ONE,
                                                                                       CURRENCY_CODE_USD,
                                                                                       RATE_USD_TO_EUR))
                                                                     .build());
  }

}

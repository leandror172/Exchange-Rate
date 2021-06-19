package org.leandror.jaya.exchange_rate.services.impl;

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
import java.util.HashMap;
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
                                                  @Random UUID transactionId,
                                                  @Random BigDecimal convertedAmount,
                                                  @Random BigDecimal conversionRate) {

    ConversionRequest request = ConversionRequest.builder()
                                                 .withUserId(userId)
                                                 .withDesiredCurrency("BRL")
                                                 .withOrigin(MonetaryAmount.builder()
                                                                           .withAmount(TEN)
                                                                           .withCurrency("BRL")
                                                                           .build())
                                                 .build();

    LocalDateTime date = ofInstant(ofEpochMilli(1624076343L), ZoneId.of("UTC"));
    Map<String, BigDecimal> rates = new HashMap<>();
    rates.put("BRL", new BigDecimal(2));
    when(client.latest(any(), any())).thenReturn(RatesResponse.builder()
                                                              .withSuccess(true)
                                                              .withTimestamp(1624076343L)
                                                              .withDate(date.toLocalDate())
                                                              .withRates(rates)
                                                              .build());

    ConversionResponse response = service.convert(request);

    assertThat(response).isNotNull();
    assertThat(response.getUserId()).isEqualTo(userId);
    assertThat(response.getTransactionId()).isEqualTo(userId);
    assertThat(response.getTransactionDate()).isEqualTo(date);
    assertThat(response.getConversionRate()).isEqualTo(rates.get("BRL"));
    assertThat(response.getConverted()).isNotNull();
    assertThat(response.getConverted()
                       .getCurrency()).isEqualTo("BRL");
    assertThat(response.getConverted()
                       .getAmount()).isEqualTo(conversionRate.multiply(MonetaryAmount.builder()
                                                                                     .withAmount(TEN)
                                                                                     .withCurrency("BRL")
                                                                                     .build()
                                                                                     .getAmount()));

    verify(client, times(1)).latest(any(), any());
  }

}

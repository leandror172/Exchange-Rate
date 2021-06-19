package org.leandror.jaya.exchange_rate.services.impl;

import static java.math.BigDecimal.TEN;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.math.BigDecimal;
import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.leandror.jaya.exchange_rate.apis.clients.ExchangeRatesApiClient;
import org.leandror.jaya.exchange_rate.dtos.ConversionRequest;
import org.leandror.jaya.exchange_rate.dtos.ConversionResponse;
import org.leandror.jaya.exchange_rate.dtos.MonetaryAmount;
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
  void returnTransactionData_when_ConvertBRLToUSD(@Random UUID userId, @Random BigDecimal originAmount,
                                                  @Random UUID transactionId, @Random BigDecimal convertedAmount,
                                                  @Random BigDecimal conversionRate) {

    MonetaryAmount origin = MonetaryAmount.builder().withAmount(TEN).withCurrency("BRL").build();
    ConversionRequest request = ConversionRequest.builder()
                                                 .withUserId(userId)
                                                 .withDesiredCurrency("BRL")
                                                 .withOrigin(origin)
                                                 .build();
    
    ConversionResponse response = service.convert(request);

    assertThat(response).isNotNull();
    assertThat(response.getUserId()).isEqualTo(userId);
    assertThat(response.getTransactionId()).isEqualTo(userId);
    assertThat(response.getConverted().getAmount()).isEqualTo(conversionRate);

    verify(service, times(1)).convert(request);
  }

}

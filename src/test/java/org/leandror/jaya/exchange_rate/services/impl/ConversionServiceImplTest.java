package org.leandror.jaya.exchange_rate.services.impl;

import static java.math.BigDecimal.ONE;
import static java.math.RoundingMode.HALF_EVEN;
import static java.time.Instant.ofEpochSecond;
import static java.time.LocalDateTime.ofInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.leandror.jaya.exchange_rate.utils.Constants.BANKING_CALCULATION_SCALE;
import static org.leandror.jaya.exchange_rate.utils.Constants.CURRENCY_CODE_BR;
import static org.leandror.jaya.exchange_rate.utils.Constants.CURRENCY_CODE_EUR;
import static org.leandror.jaya.exchange_rate.utils.Constants.CURRENCY_CODE_USD;
import static org.leandror.jaya.exchange_rate.utils.Constants.TIME_ZONE_UTC;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
import org.leandror.jaya.exchange_rate.repositories.ConversionRepository;
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

  private static final BigDecimal RATE_EUR_TO_BRL = new BigDecimal(6.039133);
  private static final BigDecimal RATE_EUR_TO_USD = new BigDecimal(1.186435);
  private static final BigDecimal RATE_USD_TO_BRL = new BigDecimal(0.1964578).setScale(BANKING_CALCULATION_SCALE,
                                                                                       HALF_EVEN);
  private BigDecimal brlValue;

  private ConversionService service;
  @Mock
  private ExchangeRatesApiClient client;
  @Mock
  private ConversionRepository repository;

  @BeforeEach
  void setUp() throws Exception {
    brlValue = ONE.divide(RATE_USD_TO_BRL, BANKING_CALCULATION_SCALE, HALF_EVEN);
    MockitoAnnotations.openMocks(this);
    service = new ConversionServiceImpl(client, repository);
  }

  @AfterEach
  void tearDown() throws Exception {
  }

  @Test
  void returnTransactionData_when_convertBRLToUSD(@Random UUID userId) {

    ConversionRequest request = ConversionRequest.builder()
                                                 .withUserId(userId)
                                                 .withDesiredCurrency(CURRENCY_CODE_BR)
                                                 .withOrigin(buildMonetaryAmount(ONE,
                                                                                 CURRENCY_CODE_USD))
                                                 .build();

    LocalDateTime date = ofInstant(ofEpochSecond(1624076343L),
                                   ZoneId.of(TIME_ZONE_UTC));
    mockCallClientLatest(true, date);

    when(repository.save(any())).thenAnswer(i -> i.getArguments()[0]);
    ConversionResponse result = service.convert(request);
    assertThat(result).isNotNull();
    assertThat(result.getUserId()).isEqualTo(userId);
    assertThat(result.getTransactionDate()).isEqualTo(date);
    assertThat(result.getUsedConversionRate()).isEqualTo(RATE_USD_TO_BRL);
    assertThat(result.getConverted()).isNotNull();
    assertThat(result.getConverted()).isEqualTo(buildMonetaryAmount(brlValue,
                                                                    CURRENCY_CODE_BR));

    verify(client, times(1)).latest(any());
  }

  @Test
  void returnTransactionId_when_conversionIsOk(@Random UUID userId,
                                               @Random UUID transactionId) {

    ConversionRequest request = ConversionRequest.builder()
                                                 .withUserId(userId)
                                                 .withDesiredCurrency(CURRENCY_CODE_BR)
                                                 .withOrigin(buildMonetaryAmount(ONE,
                                                                                 CURRENCY_CODE_USD))
                                                 .build();
    ConversionResponse response = ConversionResponse.builder()
                                                    .withId(transactionId)
                                                    .build();

    mockCallClientLatest(true, ofInstant(ofEpochSecond(1624076343L),
                                         ZoneId.of(TIME_ZONE_UTC)));
    when(repository.save(any())).thenReturn(response);

    ConversionResponse result = service.convert(request);
    assertThat(result).isNotNull();
    assertThat(result.getId()).isEqualTo(response.getId());

    verify(client, times(1)).latest(any());
    verify(repository, times(1)).save(any());
  }

  private MonetaryAmount buildMonetaryAmount(BigDecimal amount, String currency) {
    return MonetaryAmount.builder()
                         .withAmount(amount)
                         .withCurrency(currency)
                         .build();
  }

  private void mockCallClientLatest(boolean success, LocalDateTime date) {

    when(client.latest(any())).thenReturn(RatesResponse.builder()
                                                       .withSuccess(success)
                                                       .withTimestamp(1624076343L)
                                                       .withDate(date.toLocalDate())
                                                       .withRates(Map.of(CURRENCY_CODE_BR,
                                                                         RATE_EUR_TO_BRL,
                                                                         CURRENCY_CODE_EUR,
                                                                         ONE,
                                                                         CURRENCY_CODE_USD,
                                                                         RATE_EUR_TO_USD))
                                                       .build());
  }

}

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
import org.leandror.jaya.exchange_rate.dtos.ConversionRequest;
import org.leandror.jaya.exchange_rate.dtos.ConversionResponse;
import org.leandror.jaya.exchange_rate.dtos.MonetaryAmount;
import org.leandror.jaya.exchange_rate.dtos.RatesResponse;
import org.leandror.jaya.exchange_rate.repositories.ConversionRepository;
import org.leandror.jaya.exchange_rate.services.TransactionSearchService;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import io.github.glytching.junit.extension.random.Random;
import io.github.glytching.junit.extension.random.RandomBeansExtension;

@ExtendWith({ MockitoExtension.class, RandomBeansExtension.class })
@SpringBootTest(classes = {})
class TransactionSearchServiceImplTest {

  private static final BigDecimal RATE_EUR_TO_BRL = new BigDecimal(6.039133);
  private static final BigDecimal RATE_EUR_TO_USD = new BigDecimal(1.186435);
  private static final BigDecimal RATE_USD_TO_BRL = new BigDecimal(0.1964578).setScale(BANKING_CALCULATION_SCALE,
                                                                                       HALF_EVEN);
  private BigDecimal brlValue;

  private TransactionSearchService service;
  @Mock
  private ConversionRepository repository;

  @BeforeEach
  void setUp() throws Exception {
    brlValue = ONE.divide(RATE_USD_TO_BRL, BANKING_CALCULATION_SCALE, HALF_EVEN);
    MockitoAnnotations.openMocks(this);
    service = new TransactionSearchServiceImpl(repository);
  }

  @AfterEach
  void tearDown() throws Exception {
  }

  @Test
  void returnTransactionList_when_findTransactions(@Random UUID userId1,
                                                   @Random UUID transactionId1,
                                                   @Random UUID userId2,
                                                   @Random UUID transactionId2) {

    ConversionResponse response1 = ConversionResponse.builder()
                                                     .withId(transactionId1)
                                                     .withUserId(userId1)
                                                     .build();
    ConversionResponse response2 = ConversionResponse.builder()
                                                     .withId(transactionId2)
                                                     .withUserId(userId2)
                                                     .build();
    when(repository.findAll()).thenReturn(List.of(response1, response2));

    Optional<List<ConversionResponse>> result = service.listAll();
    assertThat(result).isNotNull();
    assertThat(result.get()
                     .size()).isEqualTo(2);
    assertThat(result.get()).contains(response1);
    assertThat(result.get()).contains(response2);

    verify(repository, times(1)).findAll();
  }

  @Test
  void returnTransactionList_when_findTransactionsForUserId(@Random UUID userId,
                                                            @Random UUID transactionId1,
                                                            @Random UUID transactionId2) {

    ConversionResponse response1 = ConversionResponse.builder()
                                                     .withId(transactionId1)
                                                     .withUserId(userId)
                                                     .build();
    ConversionResponse response2 = ConversionResponse.builder()
                                                     .withId(transactionId2)
                                                     .withUserId(userId)
                                                     .build();
    when(repository.findByUserId(userId)).thenReturn(Optional.of(List.of(response1,
                                                                         response2)));

    Optional<List<ConversionResponse>> result = service.listFromUser(userId);
    assertThat(result).isNotNull();
    assertThat(result.get()
                     .size()).isEqualTo(2);
    assertThat(result.get()).contains(response1);
    assertThat(result.get()).contains(response2);

    verify(repository, times(1)).findByUserId(userId);
  }

  @Test
  void returnNoTransactions_when_transactionsEmpty() {

    when(repository.findAll()).thenReturn(null);

    Optional<List<ConversionResponse>> result = service.listAll();
    assertThat(result).isNotNull();
    assertThat(result.isEmpty()).isTrue();

    verify(repository, times(1)).findAll();
  }
}

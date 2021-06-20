package org.leandror.jaya.exchange_rate.services.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.leandror.jaya.exchange_rate.dtos.ConversionResponse;
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

  private TransactionSearchService service;
  @Mock
  private ConversionRepository repository;

  @BeforeEach
  void setUp() throws Exception {
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

    List<ConversionResponse> result = service.listAll();
    assertThat(result).isNotNull();
    assertThat(result.size()).isEqualTo(2);
    assertThat(result).contains(response1);
    assertThat(result).contains(response2);

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
    when(repository.findByUserId(userId)).thenReturn((List.of(response1,
                                                              response2)));

    List<ConversionResponse> result = service.listFromUser(userId);
    assertThat(result).isNotNull();
    assertThat(result.size()).isEqualTo(2);
    assertThat(result).contains(response1);
    assertThat(result).contains(response2);

    verify(repository, times(1)).findByUserId(userId);
  }

  @Test
  void returnNoTransactions_when_transactionsEmpty() {

    when(repository.findAll()).thenReturn(List.of());

    List<ConversionResponse> result = service.listAll();
    assertThat(result).isNotNull();
    assertThat(result.isEmpty()).isTrue();

    verify(repository, times(1)).findAll();
  }

  @Test
  void returnNoTransactions_when_transactionsForUserIdIsEmpty(@Random UUID userId) {

    when(repository.findByUserId(userId)).thenReturn(List.of());

    List<ConversionResponse> result = service.listFromUser(userId);
    assertThat(result).isNotNull();
    assertThat(result.isEmpty()).isTrue();

    verify(repository, times(1)).findByUserId(userId);
  }
}

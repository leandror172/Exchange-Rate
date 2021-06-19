package org.leandror.jaya.exchange_rate.controllers;

import static java.time.LocalDateTime.now;
import static java.time.format.DateTimeFormatter.ofPattern;
import static org.hamcrest.CoreMatchers.is;
import static org.leandror.jaya.exchange_rate.utils.Constants.CURRENCY_CODE_BR;
import static org.leandror.jaya.exchange_rate.utils.Constants.CURRENCY_CODE_USD;
import static org.leandror.jaya.exchange_rate.utils.Constants.JSON_LOCALDATETIME_FORMAT;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

import org.javamoney.moneta.Money;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.leandror.jaya.exchange_rate.dtos.ConversionRequest;
import org.leandror.jaya.exchange_rate.dtos.ConversionResponse;
import org.leandror.jaya.exchange_rate.dtos.MonetaryAmount;
import org.leandror.jaya.exchange_rate.services.ConversionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.glytching.junit.extension.random.Random;
import io.github.glytching.junit.extension.random.RandomBeansExtension;

@ExtendWith({ SpringExtension.class, RandomBeansExtension.class })
@WebMvcTest(controllers = ConversionController.class)
class ConversionControllerTest {

  @Autowired
  MockMvc mockMvc;
  @Autowired
  ObjectMapper objectMapper;
  @MockBean
  ConversionService service;

  @BeforeEach
  void setUp() throws Exception {
  }

  @AfterEach
  void tearDown() throws Exception {
  }

  @Test
  void returnBadRequest_when_currencyInvalid(@Random UUID userId,
                                             @Random BigDecimal originAmount,
                                             @Random UUID transactionId,
                                             @Random BigDecimal convertedAmount,
                                             @Random BigDecimal conversionRate)
      throws Exception {
    ConversionRequest request = conversionRequest(userId, originAmount, "AAA",
                                                  CURRENCY_CODE_BR);
    mockMvc.perform(postConversionRequest(request))
           .andExpect(status().isBadRequest())
           .andExpect(content().contentType(MediaType.APPLICATION_JSON));

    request = conversionRequest(userId, originAmount, CURRENCY_CODE_BR, "AAA");
    mockMvc.perform(postConversionRequest(request))
           .andExpect(status().isBadRequest())
           .andExpect(content().contentType(MediaType.APPLICATION_JSON));
  }

  @Test
  void returnBadRequest_when_userIdNull(@Random BigDecimal originAmount,
                                        @Random UUID transactionId,
                                        @Random BigDecimal convertedAmount,
                                        @Random BigDecimal conversionRate)
      throws Exception {
    ConversionRequest request = conversionRequest(null, originAmount, "AAA",
                                                  CURRENCY_CODE_BR);
    mockMvc.perform(postConversionRequest(request))
           .andExpect(status().isBadRequest())
           .andExpect(content().contentType(MediaType.APPLICATION_JSON));

  }

  @Test
  void returnBadRequest_when_requestNull(@Random BigDecimal originAmount,
                                         @Random UUID transactionId,
                                         @Random BigDecimal convertedAmount,
                                         @Random BigDecimal conversionRate)
      throws Exception {
    mockMvc.perform(postConversionRequest(null))
           .andExpect(status().isBadRequest());

  }

  @Test
  void returnConversionResponseData_when_conversionIsOk(@Random UUID userId,
                                                        @Random BigDecimal originAmount,
                                                        @Random UUID transactionId,
                                                        @Random BigDecimal convertedAmount,
                                                        @Random BigDecimal conversionRate)
      throws Exception {

    LocalDateTime transactionDate = now(ZoneId.of("UTC"));
    ConversionRequest request = conversionRequest(userId, originAmount,
                                                  CURRENCY_CODE_BR,
                                                  CURRENCY_CODE_USD);

    when(service.convert(request)).thenReturn(conversionResponse(userId,
                                                                 transactionId,
                                                                 convertedAmount,
                                                                 conversionRate,
                                                                 transactionDate));

    mockMvc.perform(postConversionRequest(request))
           .andExpect(status().isOk())
           .andExpect(content().contentType(MediaType.APPLICATION_JSON))
           .andExpect(jsonPath("$.transactionId").value(is(transactionId.toString())))
           .andExpect(jsonPath("$.converted.amount").value(is(Money.of(convertedAmount,
                                                                       request.getDesiredCurrency())
                                                                   .getNumber()
                                                                   .numberValueExact(BigDecimal.class))))
           .andExpect(jsonPath("$.converted.currency").value(is(CURRENCY_CODE_USD)))
           .andExpect(jsonPath("$.transactionDate").value(is(transactionDate.format(ofPattern(JSON_LOCALDATETIME_FORMAT)))))
           .andExpect(jsonPath("$.usedConversionRate").value(is(conversionRate)));

    verify(service, times(1)).convert(request);
  }

  private ConversionRequest conversionRequest(UUID userId, BigDecimal originAmount,
                                              String originCurrency,
                                              String desiredCurrency) {
    return ConversionRequest.builder()
                            .withUserId(userId)
                            .withDesiredCurrency(desiredCurrency)
                            .withOrigin(MonetaryAmount.builder()
                                                      .withAmount(originAmount)
                                                      .withCurrency(originCurrency)
                                                      .build())
                            .build();
  }

  private ConversionResponse conversionResponse(UUID userId, UUID transactionId,
                                                BigDecimal convertedAmount,
                                                BigDecimal conversionRate,
                                                LocalDateTime transactionDate) {
    return ConversionResponse.builder()
                             .withId(transactionId)
                             .withUserId(userId)
                             .withTransactionDate(transactionDate)
                             .withUsedConversionRate(conversionRate)
                             .withConverted((MonetaryAmount.builder()
                                                           .withAmount(convertedAmount)
                                                           .withCurrency(CURRENCY_CODE_USD)
                                                           .build()))
                             .build();
  }

  private MockHttpServletRequestBuilder postConversionRequest(ConversionRequest payload)
      throws JsonProcessingException {
    return post("/api/v1/conversions").contentType("application/json")
                                      .content(objectMapper.writeValueAsString(payload));
  }
}

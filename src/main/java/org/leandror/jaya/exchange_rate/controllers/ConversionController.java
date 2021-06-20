package org.leandror.jaya.exchange_rate.controllers;

import java.util.List;
import java.util.UUID;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.leandror.jaya.exchange_rate.dtos.ConversionRequest;
import org.leandror.jaya.exchange_rate.dtos.ConversionResponse;
import org.leandror.jaya.exchange_rate.exceptions.NoTransactionsFoundException;
import org.leandror.jaya.exchange_rate.services.ConversionService;
import org.leandror.jaya.exchange_rate.services.TransactionSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.media.Schema;

@RestController
@RequestMapping("/api/v1/conversions")
public class ConversionController {

  private final ConversionService conversionService;
  private final TransactionSearchService transactionSearchService;

  @Autowired
  public ConversionController(final ConversionService conversionService,
      final TransactionSearchService transactionSearchService) {
    this.conversionService = conversionService;
    this.transactionSearchService = transactionSearchService;
  }

  @PostMapping(produces = "application/json")
  public ConversionResponse convert(@RequestBody @Valid @NotNull final ConversionRequest requestPayload) {
    return conversionService.convert(requestPayload);
  }

  @GetMapping(produces = "application/json")
  public List<ConversionResponse> list() {
    final List<ConversionResponse> result = transactionSearchService.listAll();
    if (result.isEmpty()) {
      throw new NoTransactionsFoundException();
    }
    return result;
  }

  @GetMapping(path = "/users/{userId}", produces = "application/json")
  public List<ConversionResponse> listForUser(@PathVariable @Schema(example = "3fa85f64-5717-4562-b3fc-2c963f66afa6") final UUID userId) {
    final List<ConversionResponse> result = transactionSearchService.listFromUser(userId);
    if (result.isEmpty()) {
      throw new NoTransactionsFoundException("No transactions for user %s",
                                             userId.toString());
    }
    return result;
  }

}

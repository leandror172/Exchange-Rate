package org.leandror.jaya.exchange_rate.controllers;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.leandror.jaya.exchange_rate.dtos.ConversionRequest;
import org.leandror.jaya.exchange_rate.dtos.ConversionResponse;
import org.leandror.jaya.exchange_rate.exceptions.NoTransactionsFoundException;
import org.leandror.jaya.exchange_rate.services.ConversionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/conversions")
public class ConversionController {

  private ConversionService service;

  @Autowired
  public ConversionController(ConversionService service) {
    this.service = service;
  }

  @PostMapping(produces = "application/json")
  public ConversionResponse convert(@RequestBody @Valid @NotNull ConversionRequest requestPayload) {
    return service.convert(requestPayload);
  }

  @GetMapping(produces = "application/json")
  public List<ConversionResponse> list() {
    return service.listAll().orElseThrow(() -> new NoTransactionsFoundException());
  }

}

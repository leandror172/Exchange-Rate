package org.leandror.jaya.exchange_rate.controllers;

import javax.validation.Valid;

import org.leandror.jaya.exchange_rate.dtos.ConversionRequest;
import org.leandror.jaya.exchange_rate.dtos.ConversionResponse;
import org.leandror.jaya.exchange_rate.services.ConversionService;
import org.springframework.beans.factory.annotation.Autowired;
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
  public ConversionResponse convert(@RequestBody @Valid ConversionRequest requestPayload) {
    return service.convert(requestPayload);
  }

}

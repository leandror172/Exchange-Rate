package org.leandror.jaya.exchange_rate.controllers;

import javax.validation.Valid;

import org.leandror.jaya.exchange_rate.dtos.ConversionRequest;
import org.leandror.jaya.exchange_rate.dtos.ConversionResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/conversion")
public class ConversionController {

  @GetMapping(path = "/", produces = "application/json")
  public String test() {
    return "Hello!";
  }
  
  @PostMapping(produces = "application/json")
  public ConversionResponse savePartner(@RequestBody @Valid ConversionRequest requestPayload) {
    return new ConversionResponse();
  }

}

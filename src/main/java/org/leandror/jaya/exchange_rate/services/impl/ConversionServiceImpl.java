package org.leandror.jaya.exchange_rate.services.impl;

import org.leandror.jaya.exchange_rate.apis.clients.ExchangeRatesApiClient;
import org.leandror.jaya.exchange_rate.dtos.ConversionRequest;
import org.leandror.jaya.exchange_rate.dtos.ConversionResponse;
import org.leandror.jaya.exchange_rate.services.ConversionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConversionServiceImpl implements ConversionService {

  private ExchangeRatesApiClient client;
  private String accessKey;

  @Autowired
  public ConversionServiceImpl(ExchangeRatesApiClient client) {
    this.client = client;
  }

  @Override
  public ConversionResponse convert(ConversionRequest requestPayload) {
    String symbols = null;
    client.latest(accessKey, symbols);
    return null;
  }

}

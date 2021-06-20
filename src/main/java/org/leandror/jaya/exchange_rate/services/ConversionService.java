package org.leandror.jaya.exchange_rate.services;

import org.leandror.jaya.exchange_rate.dtos.ConversionRequest;
import org.leandror.jaya.exchange_rate.dtos.ConversionResponse;
import org.springframework.stereotype.Service;

@Service
public interface ConversionService {

  public ConversionResponse convert(ConversionRequest requestPayload);
}

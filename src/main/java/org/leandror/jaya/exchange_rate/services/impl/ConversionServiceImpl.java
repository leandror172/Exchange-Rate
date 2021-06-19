package org.leandror.jaya.exchange_rate.services.impl;

import org.leandror.jaya.exchange_rate.dtos.ConversionRequest;
import org.leandror.jaya.exchange_rate.dtos.ConversionResponse;
import org.leandror.jaya.exchange_rate.services.ConversionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConversionServiceImpl implements ConversionService {

  @Autowired
  public ConversionServiceImpl() {
  }

  @Override
  public ConversionResponse convert(ConversionRequest requestPayload) {
    // TODO Auto-generated method stub
    return null;
  }

}

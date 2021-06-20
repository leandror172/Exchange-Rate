package org.leandror.jaya.exchange_rate.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.leandror.jaya.exchange_rate.dtos.ConversionRequest;
import org.leandror.jaya.exchange_rate.dtos.ConversionResponse;
import org.springframework.stereotype.Service;

@Service
public interface ConversionService {

  public ConversionResponse convert(ConversionRequest requestPayload);

  public Optional<List<ConversionResponse>> listAll();

  public Optional<List<ConversionResponse>> listFromUser(UUID userId);
}

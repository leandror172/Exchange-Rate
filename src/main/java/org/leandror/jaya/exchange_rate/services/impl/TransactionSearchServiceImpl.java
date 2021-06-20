package org.leandror.jaya.exchange_rate.services.impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.leandror.jaya.exchange_rate.dtos.ConversionResponse;
import org.leandror.jaya.exchange_rate.repositories.ConversionRepository;
import org.leandror.jaya.exchange_rate.services.TransactionSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionSearchServiceImpl implements TransactionSearchService {

  private ConversionRepository repository;

  @Autowired
  public TransactionSearchServiceImpl(ConversionRepository repository) {
    this.repository = repository;
  }

  @Override
  public List<ConversionResponse> listAll() {
    return repository.findAll();
  }

  @Override
  public List<ConversionResponse> listFromUser(UUID userId) {
    return repository.findByUserId(userId);
  }
}

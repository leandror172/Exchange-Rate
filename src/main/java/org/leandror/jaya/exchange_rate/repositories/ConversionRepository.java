package org.leandror.jaya.exchange_rate.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.leandror.jaya.exchange_rate.dtos.ConversionResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConversionRepository
    extends JpaRepository<ConversionResponse, UUID> {

  Optional<List<ConversionResponse>> findByUserId(UUID userId);

}

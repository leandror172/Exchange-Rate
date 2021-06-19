package org.leandror.jaya.exchange_rate.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema
@Builder(setterPrefix = "with")
public class ConversionResponse {

  private UUID transactionId;
  private UUID userId;
  private MonetaryAmount origin;
  private MonetaryAmount converted;
  private BigDecimal usedConversionRate;
  @JsonFormat(pattern = "yyyy-MM-dd'T'hh:mm:ss")
  private LocalDateTime transactionDate;
  @JsonIgnore
  private Map<String, BigDecimal> conversionRates;
  
}

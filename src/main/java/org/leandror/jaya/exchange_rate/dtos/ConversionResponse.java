package org.leandror.jaya.exchange_rate.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;

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
  private BigDecimal conversionRate;
  @JsonFormat(pattern = "yyyy-MM-dd'T'hh:mm:ss")
  private LocalDateTime transactionDate;
}

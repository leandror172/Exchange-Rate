package org.leandror.jaya.exchange_rate.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

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
public class RatesResponse {

  private boolean success;
  private LocalDate date;
  private Long timestamp;
  private String currency;
  private Map<String, BigDecimal> rates;

}

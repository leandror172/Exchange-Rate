package org.leandror.jaya.exchange_rate.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema
public class RatesResponse {

  private boolean success;
  private LocalDate date;
  private String timestamp;
  private String currency;
  private Map<String, BigDecimal> rates;

}

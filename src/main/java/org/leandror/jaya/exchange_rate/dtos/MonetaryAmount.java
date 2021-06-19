package org.leandror.jaya.exchange_rate.dtos;

import java.math.BigDecimal;
import java.util.UUID;

import javax.validation.constraints.NotNull;

import org.leandror.jaya.exchange_rate.validators.annotations.ValidCurrencyCode;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.format.annotation.NumberFormat.Style;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@Schema
@Builder(setterPrefix = "with")
public class MonetaryAmount {

  @NotNull
  @NonNull
  @Schema(example = "99.96")
  @NumberFormat(style = Style.CURRENCY)
  private BigDecimal amount;

  @NotNull
  @NonNull
  @Schema(example = "USD")
  @ValidCurrencyCode
  private String currency;

}

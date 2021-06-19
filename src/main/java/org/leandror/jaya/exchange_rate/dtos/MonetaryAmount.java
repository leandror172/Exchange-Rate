package org.leandror.jaya.exchange_rate.dtos;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import org.leandror.jaya.exchange_rate.validators.annotations.ValidCurrencyCode;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.format.annotation.NumberFormat.Style;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

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

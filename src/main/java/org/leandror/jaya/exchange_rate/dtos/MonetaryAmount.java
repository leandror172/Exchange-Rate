package org.leandror.jaya.exchange_rate.dtos;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import org.leandror.jaya.exchange_rate.validators.annotations.ValidCurrencyCode;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.format.annotation.NumberFormat.Style;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@Schema
@Builder(setterPrefix = "with")
@RequiredArgsConstructor
@JsonDeserialize(builder = MonetaryAmount.MonetaryAmountBuilder.class)
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

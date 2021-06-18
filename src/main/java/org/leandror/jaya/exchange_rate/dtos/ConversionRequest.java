package org.leandror.jaya.exchange_rate.dtos;

import java.util.UUID;

import javax.validation.constraints.NotNull;

import org.leandror.jaya.exchange_rate.validators.annotations.ValidCurrencyCode;
import org.springdoc.core.converters.models.MonetaryAmount;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema()
public class ConversionRequest {

  private UUID userId;
  private MonetaryAmount origin;
  @Schema(example = "USD")
  @NotNull
  @ValidCurrencyCode
  private String desiredCurrency;

}

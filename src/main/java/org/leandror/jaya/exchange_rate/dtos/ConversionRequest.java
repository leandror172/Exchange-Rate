package org.leandror.jaya.exchange_rate.dtos;

import java.util.UUID;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.leandror.jaya.exchange_rate.validators.annotations.ValidCurrencyCode;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema()
@Builder(setterPrefix = "with")
public class ConversionRequest {

  private @NotNull UUID userId;
  private @Valid MonetaryAmount origin;
  @Schema(example = "BRL")
  @NotNull
  @ValidCurrencyCode
  private String desiredCurrency;

}

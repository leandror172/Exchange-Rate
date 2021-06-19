package org.leandror.jaya.exchange_rate.dtos;

import java.util.UUID;

import javax.validation.constraints.NotNull;

import org.leandror.jaya.exchange_rate.validators.annotations.ValidCurrencyCode;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema()
@Builder(setterPrefix = "with")
public class ConversionRequest {

  private UUID userId;
  private MonetaryAmount origin;
  @Schema(example = "USD")
  @NotNull
  @ValidCurrencyCode
  private String desiredCurrency;

}

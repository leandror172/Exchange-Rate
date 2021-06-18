package org.springdoc.core.converters.models;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import org.leandror.jaya.exchange_rate.validators.annotations.ValidCurrencyCode;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.format.annotation.NumberFormat.Style;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema
public class MonetaryAmount {

    @JsonProperty("amount")
    @Schema(example = "99.96")
    @NotNull
    @NumberFormat(style = Style.CURRENCY)
    private BigDecimal amount;

    @Schema(example = "USD")
    @NotNull
    @ValidCurrencyCode
    private String currency;

}

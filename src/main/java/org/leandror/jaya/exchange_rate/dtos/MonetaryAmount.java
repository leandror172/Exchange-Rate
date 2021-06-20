package org.leandror.jaya.exchange_rate.dtos;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.leandror.jaya.exchange_rate.validators.annotations.ValidCurrencyCode;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.format.annotation.NumberFormat.Style;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
@AllArgsConstructor
@JsonDeserialize(builder = MonetaryAmount.MonetaryAmountBuilder.class)
@Entity
@Table(name = "monetary_amount")
public class MonetaryAmount {

  @Id
  @GeneratedValue
  @JsonIgnore
  private Long id;

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

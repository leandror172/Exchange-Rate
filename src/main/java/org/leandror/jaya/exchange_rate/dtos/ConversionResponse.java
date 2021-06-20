package org.leandror.jaya.exchange_rate.dtos;

import static org.leandror.jaya.exchange_rate.utils.Constants.JSON_LOCALDATETIME_FORMAT;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapKeyColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

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
@Entity
@Table(name = "conversion")
public class ConversionResponse {

  @Id
  @JsonProperty("transactionId")
  @GeneratedValue
  private UUID id;
  private UUID userId;

  @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, optional = false)
  @JoinColumn(name = "origin_id", unique = true, nullable = false)
  private MonetaryAmount origin;

  @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, optional = false)
  @JoinColumn(name = "converted_id", unique = true, nullable = false)
  private MonetaryAmount converted;

  private BigDecimal usedConversionRate;

  @JsonFormat(pattern = JSON_LOCALDATETIME_FORMAT)
  private LocalDateTime transactionDate;

  @JsonIgnore
  @ElementCollection
  @MapKeyColumn(name = "currency")
  @Column(name = "rate")
  @CollectionTable(name = "conversion_rates_mapping")
  private Map<String, BigDecimal> conversionRates;

}

package org.leandror.jaya.exchange_rate.dtos;

import static org.leandror.jaya.exchange_rate.utils.Constants.JSON_LOCALDATETIME_FORMAT;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapKeyColumn;
import javax.persistence.MapsId;
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
  private UUID id;
  private UUID userId;
  @OneToOne
  @MapsId
  private MonetaryAmount origin;
  @OneToOne
  @MapsId
  private MonetaryAmount converted;
  private BigDecimal usedConversionRate;
  @JsonFormat(pattern = JSON_LOCALDATETIME_FORMAT)
  private LocalDateTime transactionDate;
  @JsonIgnore
  @ElementCollection
//  @CollectionTable(name = "conversion_rates_mapping", joinColumns = {
//      @JoinColumn(name = "conversion_id", referencedColumnName = "id") })
  @MapKeyColumn(name = "currency")
  @Column(name = "rate")
  @CollectionTable(name = "conversion_rates_mapping")
  private Map<String, BigDecimal> conversionRates;

}

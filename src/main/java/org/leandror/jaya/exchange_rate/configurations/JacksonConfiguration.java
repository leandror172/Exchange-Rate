package org.leandror.jaya.exchange_rate.configurations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.zalando.jackson.datatype.money.MoneyModule;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Configuration
public class JacksonConfiguration {

  @Bean
  @Primary
  @Autowired
  public ObjectMapper jackson2ObjectMapperBuilder(final Jackson2ObjectMapperBuilder builder) {
    return builder.modules(new MoneyModule().withMoney(), new Jdk8Module(),
                           new JavaTimeModule())
                  .build();
  }
}

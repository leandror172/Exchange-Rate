package org.leandror.jaya.exchange_rate;

import org.leandror.jaya.exchange_rate.exceptions.handlers.RestResponseEntityExceptionHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(RestResponseEntityExceptionHandler.class)
@EnableFeignClients
public class ExchangeRateApplication {

  public static void main(final String[] args) {
    SpringApplication.run(ExchangeRateApplication.class, args);
  }

}

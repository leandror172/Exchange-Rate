package org.leandror.jaya.exchange_rate.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.SERVICE_UNAVAILABLE, reason = "The remote exchangeratesapi.io call did not return conversion rates data")
public class ExchangeRateApiUnavailableException extends RuntimeException {

  private static final long serialVersionUID = 6239645399031433234L;

  public ExchangeRateApiUnavailableException(final String string) {
    super(string);
  }

  public ExchangeRateApiUnavailableException() {
  }

}

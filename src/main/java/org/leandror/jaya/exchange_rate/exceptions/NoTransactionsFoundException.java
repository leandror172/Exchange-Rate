package org.leandror.jaya.exchange_rate.exceptions;

import static java.lang.String.format;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "No transactions found. Try posting the conversions api first")
public class NoTransactionsFoundException extends RuntimeException {

  private static final long serialVersionUID = 5239371985458940763L;

  public NoTransactionsFoundException(String string) {
    super(string);
  }

  public NoTransactionsFoundException() {
    super();
  }

  public NoTransactionsFoundException(String string, Object...args) {
    super(format(string, args));
  }

}

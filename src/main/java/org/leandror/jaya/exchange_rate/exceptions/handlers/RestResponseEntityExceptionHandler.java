package org.leandror.jaya.exchange_rate.exceptions.handlers;

import static java.lang.String.format;

import org.leandror.jaya.exchange_rate.dtos.ErrorResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestResponseEntityExceptionHandler
    extends ResponseEntityExceptionHandler {

  private ErrorResponse response = null;

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                HttpHeaders headers,
                                                                HttpStatus status,
                                                                WebRequest request) {

    response = new ErrorResponse(HttpStatus.BAD_REQUEST.value(),
                                 "Invalid request data.");
    if (ex.getFieldErrors() != null) {
      ex.getFieldErrors()
        .stream()
        .forEach(error -> response.addValidationError(error.getField(),
                                                      format(error.getDefaultMessage(),
                                                             error.getRejectedValue())));
    }
    return handleExceptionInternal(ex, response, headers, status, request);
  }

}

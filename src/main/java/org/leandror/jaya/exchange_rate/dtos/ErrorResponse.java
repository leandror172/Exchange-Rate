package org.leandror.jaya.exchange_rate.dtos;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * @author copied from
 *         {@linkplain https://reflectoring.io/spring-boot-exception-handling/}
 *
 */
@Getter
@Setter
@RequiredArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {
  private final int status;
  private final String message;
  private String stackTrace;
  private List<ValidationError> errors;

  @Getter
  @Setter
  @RequiredArgsConstructor
  private static class ValidationError {
    private final String field;
    private final String message;
  }

  public void addValidationError(final String field, final String message) {
    if (Objects.isNull(errors)) {
      errors = new ArrayList<>();
    }
    errors.add(new ValidationError(field, message));
  }

}

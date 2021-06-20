package org.leandror.jaya.exchange_rate.validators.annotations;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import org.leandror.jaya.exchange_rate.validators.ValidCurrencyCodeValidator;

/**
 * Annotation to validate the ISO Currency code.
 *
 * @author based on code by Robin Raju
 *         {@linkplain https://gist.github.com/robinraju/8cba4fa3b22c34fda3cec73338ca460c}
 */
@Target({ FIELD, PARAMETER })
@Retention(RUNTIME)
@Constraint(validatedBy = ValidCurrencyCodeValidator.class)
@Documented
public @interface ValidCurrencyCode {
  String message() default "Currency code %s is invalid.";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  boolean optional() default false;
}

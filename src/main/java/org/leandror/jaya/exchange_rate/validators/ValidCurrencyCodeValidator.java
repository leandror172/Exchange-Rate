package org.leandror.jaya.exchange_rate.validators;

import java.util.Currency;
import java.util.Set;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.leandror.jaya.exchange_rate.validators.annotations.ValidCurrencyCode;

import com.google.common.base.Strings;

public class ValidCurrencyCodeValidator
    implements ConstraintValidator<ValidCurrencyCode, String> {

  private Boolean isOptional;

  @Override
  public void initialize(final ValidCurrencyCode validCurrencyCode) {
    this.isOptional = validCurrencyCode.optional();
  }

  @Override
  public boolean isValid(final String value,
                         final ConstraintValidatorContext constraintValidatorContext) {
    boolean containsIsoCode = false;

    final Set<Currency> currencies = Currency.getAvailableCurrencies();
    try {
      containsIsoCode = currencies.contains(Currency.getInstance(value));
    } catch (final IllegalArgumentException e) {
    }
    return isOptional ? (containsIsoCode || (Strings.isNullOrEmpty(value)))
        : containsIsoCode;
  }
}

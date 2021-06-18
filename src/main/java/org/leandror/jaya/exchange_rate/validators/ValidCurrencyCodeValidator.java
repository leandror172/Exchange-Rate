package org.leandror.jaya.exchange_rate.validators;

import java.util.Currency;
import java.util.Set;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.leandror.jaya.exchange_rate.validators.annotations.ValidCurrencyCode;

import com.google.common.base.Strings;

public class ValidCurrencyCodeValidator implements ConstraintValidator<ValidCurrencyCode, String> {

    private Boolean isOptional;

    @Override
    public void initialize(ValidCurrencyCode validCurrencyCode) {
        this.isOptional = validCurrencyCode.optional();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        boolean containsIsoCode = false;

        Set<Currency> currencies = Currency.getAvailableCurrencies();
        try {
            containsIsoCode = currencies.contains(Currency.getInstance(value));
        }catch(IllegalArgumentException e){
        }
        return isOptional ? (containsIsoCode || (Strings.isNullOrEmpty(value))) : containsIsoCode;
    }
}
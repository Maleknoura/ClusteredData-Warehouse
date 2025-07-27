package com.progressoft.clustereddata_warehouse.Service.implementation;

import com.progressoft.clustereddata_warehouse.Service.api.CurrencyVerifier;
import com.progressoft.clustereddata_warehouse.exception.InvalidCurrencyException;
import com.progressoft.clustereddata_warehouse.exception.UnknownCurrencyException;
import com.progressoft.clustereddata_warehouse.utils.CurrencyValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class CurrencyVerifierImpl implements CurrencyVerifier {

    private final CurrencyValidator currencyValidator;
    private static final String CURRENCY_REGEX = "^[A-Z]{3}$";
    private static final Pattern CURRENCY_PATTERN = Pattern.compile(CURRENCY_REGEX);

    @Override
    public void validate(String from, String to) {
        validateFormat(from, "fromCurrency");
        validateFormat(to, "toCurrency");
        ensureDifferent(from, to);
        verifyExistence(from);
        verifyExistence(to);
    }

    private void validateFormat(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new InvalidCurrencyException(fieldName + " cannot be null or empty");
        }

        if (!CURRENCY_PATTERN.matcher(value).matches()) {
            throw new InvalidCurrencyException(
                    String.format("%s format is invalid: %s. Expected 3 uppercase letters", fieldName, value)
            );
        }
    }

    private void ensureDifferent(String from, String to) {
        if (from.equalsIgnoreCase(to)) {
            throw new InvalidCurrencyException(
                    String.format("fromCurrency and toCurrency must differ: %s", from)
            );
        }
    }

    private void verifyExistence(String code) {
        if (!currencyValidator.isValid(code.toUpperCase())) {
            throw new UnknownCurrencyException(
                    String.format("Currency not recognized: %s", code)
            );
        }
    }
}

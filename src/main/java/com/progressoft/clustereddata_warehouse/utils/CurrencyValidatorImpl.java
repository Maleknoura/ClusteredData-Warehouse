package com.progressoft.clustereddata_warehouse.utils;

import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class CurrencyValidatorImpl implements CurrencyValidator {

    private final Set<String> validCurrencyCodes;

    public CurrencyValidatorImpl(CurrencyFileLoader loader) {
        this.validCurrencyCodes = loader.loadCurrencies();
        validateISOCodes(this.validCurrencyCodes);
    }

    @Override
    public boolean isValid(String code) {
        return validCurrencyCodes.contains(code);
    }

    private void validateISOCodes(Set<String> codes) {
        if (codes == null) {
            throw new IllegalStateException("Currency codes must not be null.");
        }
        if (codes.isEmpty()) {
            throw new IllegalStateException("Currency list is empty.");
        }
    }
}


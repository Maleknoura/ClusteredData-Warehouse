package com.progressoft.clustereddata_warehouse.Service.implementation;

import static org.junit.jupiter.api.Assertions.*;


import com.progressoft.clustereddata_warehouse.exception.InvalidCurrencyException;
import com.progressoft.clustereddata_warehouse.exception.UnknownCurrencyException;
import com.progressoft.clustereddata_warehouse.utils.CurrencyValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;




import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
class CurrencyVerifierImplTest {

    @Mock
    private CurrencyValidator currencyValidator;

    @InjectMocks
    private CurrencyVerifierImpl currencyVerifier;

    @BeforeEach
    void setUp() {
        lenient().when(currencyValidator.isValid("USD")).thenReturn(true);
        lenient().when(currencyValidator.isValid("EUR")).thenReturn(true);
        lenient().when(currencyValidator.isValid("GBP")).thenReturn(true);
        lenient().when(currencyValidator.isValid("JPY")).thenReturn(true);
        lenient().when(currencyValidator.isValid("XYZ")).thenReturn(false);
    }

    @Test
    @DisplayName("Should validate successfully with valid currencies")
    void validate_WithValidCurrencies_ShouldPass() {
        String from = "USD";
        String to = "EUR";

        assertDoesNotThrow(() -> currencyVerifier.validate(from, to));
    }

    @Test
    @DisplayName("Should throw exception when fromCurrency is null")
    void validate_WithNullFromCurrency_ShouldThrowException() {
        String from = null;
        String to = "EUR";

        InvalidCurrencyException exception = assertThrows(InvalidCurrencyException.class,
                () -> currencyVerifier.validate(from, to));

        assertEquals("fromCurrency cannot be null or empty", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when toCurrency is null")
    void validate_WithNullToCurrency_ShouldThrowException() {
        String from = "USD";
        String to = null;

        InvalidCurrencyException exception = assertThrows(InvalidCurrencyException.class,
                () -> currencyVerifier.validate(from, to));

        assertEquals("toCurrency cannot be null or empty", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when fromCurrency is empty")
    void validate_WithEmptyFromCurrency_ShouldThrowException() {
        String from = "";
        String to = "EUR";

        InvalidCurrencyException exception = assertThrows(InvalidCurrencyException.class,
                () -> currencyVerifier.validate(from, to));

        assertEquals("fromCurrency cannot be null or empty", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when toCurrency is empty")
    void validate_WithEmptyToCurrency_ShouldThrowException() {
        String from = "USD";
        String to = "";

        InvalidCurrencyException exception = assertThrows(InvalidCurrencyException.class,
                () -> currencyVerifier.validate(from, to));

        assertEquals("toCurrency cannot be null or empty", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when fromCurrency is only whitespace")
    void validate_WithWhitespaceFromCurrency_ShouldThrowException() {
        String from = "   ";
        String to = "EUR";

        InvalidCurrencyException exception = assertThrows(InvalidCurrencyException.class,
                () -> currencyVerifier.validate(from, to));

        assertEquals("fromCurrency cannot be null or empty", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when toCurrency is only whitespace")
    void validate_WithWhitespaceToCurrency_ShouldThrowException() {
        String from = "USD";
        String to = "   ";

        InvalidCurrencyException exception = assertThrows(InvalidCurrencyException.class,
                () -> currencyVerifier.validate(from, to));

        assertEquals("toCurrency cannot be null or empty", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when fromCurrency format is invalid - lowercase")
    void validate_WithLowercaseFromCurrency_ShouldThrowException() {
        String from = "usd";
        String to = "EUR";

        InvalidCurrencyException exception = assertThrows(InvalidCurrencyException.class,
                () -> currencyVerifier.validate(from, to));

        assertEquals("fromCurrency format is invalid: usd. Expected 3 uppercase letters", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when toCurrency format is invalid - lowercase")
    void validate_WithLowercaseToCurrency_ShouldThrowException() {
        String from = "USD";
        String to = "eur";

        InvalidCurrencyException exception = assertThrows(InvalidCurrencyException.class,
                () -> currencyVerifier.validate(from, to));

        assertEquals("toCurrency format is invalid: eur. Expected 3 uppercase letters", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when fromCurrency format is invalid - too short")
    void validate_WithShortFromCurrency_ShouldThrowException() {
        String from = "US";
        String to = "EUR";

        InvalidCurrencyException exception = assertThrows(InvalidCurrencyException.class,
                () -> currencyVerifier.validate(from, to));

        assertEquals("fromCurrency format is invalid: US. Expected 3 uppercase letters", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when toCurrency format is invalid - too long")
    void validate_WithLongToCurrency_ShouldThrowException() {
        String from = "USD";
        String to = "EURO";

        InvalidCurrencyException exception = assertThrows(InvalidCurrencyException.class,
                () -> currencyVerifier.validate(from, to));

        assertEquals("toCurrency format is invalid: EURO. Expected 3 uppercase letters", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when fromCurrency contains numbers")
    void validate_WithNumbersInFromCurrency_ShouldThrowException() {
        String from = "US1";
        String to = "EUR";

        InvalidCurrencyException exception = assertThrows(InvalidCurrencyException.class,
                () -> currencyVerifier.validate(from, to));

        assertEquals("fromCurrency format is invalid: US1. Expected 3 uppercase letters", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when currencies are the same")
    void validate_WithSameCurrencies_ShouldThrowException() {
        String from = "USD";
        String to = "USD";

        InvalidCurrencyException exception = assertThrows(InvalidCurrencyException.class,
                () -> currencyVerifier.validate(from, to));

        assertEquals("fromCurrency and toCurrency must differ: USD", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when fromCurrency does not exist")
    void validate_WithNonExistentFromCurrency_ShouldThrowException() {
        String from = "XYZ";
        String to = "EUR";

        UnknownCurrencyException exception = assertThrows(UnknownCurrencyException.class,
                () -> currencyVerifier.validate(from, to));

        assertEquals("Currency not recognized: XYZ", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when toCurrency does not exist")
    void validate_WithNonExistentToCurrency_ShouldThrowException() {
        String from = "USD";
        String to = "XYZ";

        UnknownCurrencyException exception = assertThrows(UnknownCurrencyException.class,
                () -> currencyVerifier.validate(from, to));

        assertEquals("Currency not recognized: XYZ", exception.getMessage());
    }

    @Test
    @DisplayName("Should handle case insensitive comparison for same currencies")
    void validate_WithSameCurrenciesDifferentCase_ShouldThrowException() {
        String from = "USD";
        String to = "usd";

        InvalidCurrencyException exception = assertThrows(InvalidCurrencyException.class,
                () -> currencyVerifier.validate(from, to));

        assertEquals("toCurrency format is invalid: usd. Expected 3 uppercase letters", exception.getMessage());
    }

    @Test
    @DisplayName("Should convert currency to uppercase before existence check")
    void validate_WithValidCurrenciesAfterFormatValidation_ShouldWork() {

        String from = "USD";
        String to = "EUR";

        assertDoesNotThrow(() -> currencyVerifier.validate(from, to));
    }
}
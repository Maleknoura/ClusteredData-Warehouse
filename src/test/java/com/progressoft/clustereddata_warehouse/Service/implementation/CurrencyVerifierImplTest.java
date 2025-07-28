package com.progressoft.clustereddata_warehouse.Service.implementation;

import static org.assertj.core.api.Assertions.*;

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
    @DisplayName("Given valid currencies when validate then should pass")
    void givenValidCurrencies_whenValidate_thenShouldPass() {

        String from = "USD";
        String to = "EUR";


        assertThatNoException().isThrownBy(() -> currencyVerifier.validate(from, to));
    }

    @Test
    @DisplayName("Given null fromCurrency when validate then should throw InvalidCurrencyException")
    void givenNullFromCurrency_whenValidate_thenShouldThrowInvalidCurrencyException() {

        String from = null;
        String to = "EUR";


        assertThatExceptionOfType(InvalidCurrencyException.class)
                .isThrownBy(() -> currencyVerifier.validate(from, to))
                .withMessage("fromCurrency cannot be null or empty");
    }

    @Test
    @DisplayName("Given null toCurrency when validate then should throw InvalidCurrencyException")
    void givenNullToCurrency_whenValidate_thenShouldThrowInvalidCurrencyException() {

        String from = "USD";
        String to = null;


        assertThatExceptionOfType(InvalidCurrencyException.class)
                .isThrownBy(() -> currencyVerifier.validate(from, to))
                .withMessage("toCurrency cannot be null or empty");
    }

    @Test
    @DisplayName("Given empty fromCurrency when validate then should throw InvalidCurrencyException")
    void givenEmptyFromCurrency_whenValidate_thenShouldThrowInvalidCurrencyException() {

        String from = "";
        String to = "EUR";


        assertThatExceptionOfType(InvalidCurrencyException.class)
                .isThrownBy(() -> currencyVerifier.validate(from, to))
                .withMessage("fromCurrency cannot be null or empty");
    }

    @Test
    @DisplayName("Given empty toCurrency when validate then should throw InvalidCurrencyException")
    void givenEmptyToCurrency_whenValidate_thenShouldThrowInvalidCurrencyException() {

        String from = "USD";
        String to = "";


        assertThatExceptionOfType(InvalidCurrencyException.class)
                .isThrownBy(() -> currencyVerifier.validate(from, to))
                .withMessage("toCurrency cannot be null or empty");
    }

    @Test
    @DisplayName("Given whitespace fromCurrency when validate then should throw InvalidCurrencyException")
    void givenWhitespaceFromCurrency_whenValidate_thenShouldThrowInvalidCurrencyException() {

        String from = "   ";
        String to = "EUR";


        assertThatExceptionOfType(InvalidCurrencyException.class)
                .isThrownBy(() -> currencyVerifier.validate(from, to))
                .withMessage("fromCurrency cannot be null or empty");
    }

    @Test
    @DisplayName("Given whitespace toCurrency when validate then should throw InvalidCurrencyException")
    void givenWhitespaceToCurrency_whenValidate_thenShouldThrowInvalidCurrencyException() {

        String from = "USD";
        String to = "   ";


        assertThatExceptionOfType(InvalidCurrencyException.class)
                .isThrownBy(() -> currencyVerifier.validate(from, to))
                .withMessage("toCurrency cannot be null or empty");
    }

    @Test
    @DisplayName("Given lowercase fromCurrency when validate then should throw InvalidCurrencyException")
    void givenLowercaseFromCurrency_whenValidate_thenShouldThrowInvalidCurrencyException() {

        String from = "usd";
        String to = "EUR";


        assertThatExceptionOfType(InvalidCurrencyException.class)
                .isThrownBy(() -> currencyVerifier.validate(from, to))
                .withMessage("fromCurrency format is invalid: usd. Expected 3 uppercase letters");
    }

    @Test
    @DisplayName("Given lowercase toCurrency when validate then should throw InvalidCurrencyException")
    void givenLowercaseToCurrency_whenValidate_thenShouldThrowInvalidCurrencyException() {

        String from = "USD";
        String to = "eur";


        assertThatExceptionOfType(InvalidCurrencyException.class)
                .isThrownBy(() -> currencyVerifier.validate(from, to))
                .withMessage("toCurrency format is invalid: eur. Expected 3 uppercase letters");
    }

    @Test
    @DisplayName("Given short fromCurrency when validate then should throw InvalidCurrencyException")
    void givenShortFromCurrency_whenValidate_thenShouldThrowInvalidCurrencyException() {

        String from = "US";
        String to = "EUR";


        assertThatExceptionOfType(InvalidCurrencyException.class)
                .isThrownBy(() -> currencyVerifier.validate(from, to))
                .withMessage("fromCurrency format is invalid: US. Expected 3 uppercase letters");
    }

    @Test
    @DisplayName("Given long toCurrency when validate then should throw InvalidCurrencyException")
    void givenLongToCurrency_whenValidate_thenShouldThrowInvalidCurrencyException() {

        String from = "USD";
        String to = "EURO";


        assertThatExceptionOfType(InvalidCurrencyException.class)
                .isThrownBy(() -> currencyVerifier.validate(from, to))
                .withMessage("toCurrency format is invalid: EURO. Expected 3 uppercase letters");
    }

    @Test
    @DisplayName("Given fromCurrency with numbers when validate then should throw InvalidCurrencyException")
    void givenFromCurrencyWithNumbers_whenValidate_thenShouldThrowInvalidCurrencyException() {

        String from = "US1";
        String to = "EUR";


        assertThatExceptionOfType(InvalidCurrencyException.class)
                .isThrownBy(() -> currencyVerifier.validate(from, to))
                .withMessage("fromCurrency format is invalid: US1. Expected 3 uppercase letters");
    }

    @Test
    @DisplayName("Given same currencies when validate then should throw InvalidCurrencyException")
    void givenSameCurrencies_whenValidate_thenShouldThrowInvalidCurrencyException() {

        String from = "USD";
        String to = "USD";


        assertThatExceptionOfType(InvalidCurrencyException.class)
                .isThrownBy(() -> currencyVerifier.validate(from, to))
                .withMessage("fromCurrency and toCurrency must differ: USD");
    }

    @Test
    @DisplayName("Given non-existent fromCurrency when validate then should throw UnknownCurrencyException")
    void givenNonExistentFromCurrency_whenValidate_thenShouldThrowUnknownCurrencyException() {

        String from = "XYZ";
        String to = "EUR";


        assertThatExceptionOfType(UnknownCurrencyException.class)
                .isThrownBy(() -> currencyVerifier.validate(from, to))
                .withMessage("Currency not recognized: XYZ");
    }

    @Test
    @DisplayName("Given non-existent toCurrency when validate then should throw UnknownCurrencyException")
    void givenNonExistentToCurrency_whenValidate_thenShouldThrowUnknownCurrencyException() {

        String from = "USD";
        String to = "XYZ";


        assertThatExceptionOfType(UnknownCurrencyException.class)
                .isThrownBy(() -> currencyVerifier.validate(from, to))
                .withMessage("Currency not recognized: XYZ");
    }

    @Test
    @DisplayName("Given same currencies with different case when validate then should throw InvalidCurrencyException for format")
    void givenSameCurrenciesWithDifferentCase_whenValidate_thenShouldThrowInvalidCurrencyExceptionForFormat() {
        // Given
        String from = "USD";
        String to = "usd";


        assertThatExceptionOfType(InvalidCurrencyException.class)
                .isThrownBy(() -> currencyVerifier.validate(from, to))
                .withMessage("toCurrency format is invalid: usd. Expected 3 uppercase letters");
    }

    @Test
    @DisplayName("Given valid currencies after format validation when validate then should work")
    void givenValidCurrenciesAfterFormatValidation_whenValidate_thenShouldWork() {
        // Given
        String from = "USD";
        String to = "EUR";

        assertThatNoException().isThrownBy(() -> currencyVerifier.validate(from, to));
    }
}
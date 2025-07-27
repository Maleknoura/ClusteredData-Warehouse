package com.progressoft.clustereddata_warehouse.utils;

import com.progressoft.clustereddata_warehouse.exception.CurrencyLoadException;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class CurrencyFileLoaderImpl implements CurrencyFileLoader {

    private static final String CURRENCY_FILE_PATH = "currencies.csv";

    @Override
    public Set<String> loadCurrencies() {
        try {
            ClassPathResource resource = new ClassPathResource(CURRENCY_FILE_PATH);
            Path currencyFilePath = Paths.get(resource.getURI());

            return Files.lines(currencyFilePath)
                    .skip(1)
                    .map(this::extractCurrencyCode)
                    .filter(code -> !code.isEmpty())
                    .collect(Collectors.toSet());

        } catch (IOException e) {
            throw new CurrencyLoadException("Failed to load currencies from CSV file", e);
        }
    }

    private String extractCurrencyCode(String line) {
        String[] parts = line.split(",");
        return parts[0].trim();
    }
}

package com.progressoft.clustereddata_warehouse.utils;

import java.util.Set;

@FunctionalInterface
public interface CurrencyFileLoader {
    Set<String> loadCurrencies();
}

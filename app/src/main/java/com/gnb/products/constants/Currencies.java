package com.gnb.products.constants;

import java.util.Arrays;
import java.util.List;

public class Currencies {

    public static final String EURO = "EUR";

    public static final String CANADIAN_DOLLARS = "CAD";

    public static final String US_DOLLARS = "USD";

    public static final String EMIRATI_DIRHAMS = "AUD";

    public static final List<String> SupportedCurrencies =
            Arrays.asList(EURO,
                          US_DOLLARS,
                          CANADIAN_DOLLARS,
                          EMIRATI_DIRHAMS);
}

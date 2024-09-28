package org.translation;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class provides the service of converting country codes to their names.
 */
public class CountryCodeConverter {

    public static final int COUNTRY_IDX = 0;
    public static final int CODE_2_IDX = 1;
    public static final int CODE_3_IDX = 2;
    public static final int NUMERIC_IDX = 3;

    private Map<String, List<String>> countryMap;

    /**
     * Default constructor which will load the country codes from "country-codes.txt"
     * in the resources folder.
     */
    public CountryCodeConverter() {
        this("country-codes.txt");
    }

    /**
     * Overloaded constructor which allows us to specify the filename to load the country code data from.
     * @param filename the name of the file in the resources folder to load the data from
     * @throws RuntimeException if the resource file can't be loaded properly
     */
    public CountryCodeConverter(String filename) {

        try {
            List<String> lines = Files.readAllLines(Paths.get(getClass()
                    .getClassLoader().getResource(filename).toURI()));

            this.countryMap = new HashMap<>();
            for (int i = 1; i < lines.size(); i++) {
                String line = lines.get(i);
                String[] items = line.split("\t");
                countryMap.put(items[COUNTRY_IDX],
                        List.of(items[CODE_2_IDX], items[CODE_3_IDX], items[NUMERIC_IDX]));
            }
        }
        catch (IOException | URISyntaxException ex) {
            throw new RuntimeException(ex);
        }

    }

    /**
     * Returns the name of the country for the given country code.
     * @param code the 3-letter code of the country
     * @return the name of the country corresponding to the code
     */
    public String fromCountryCode(String code) {
        for (String country : countryMap.keySet()) {
            List<String> codes = countryMap.get(country);
            if (codes.get(CODE_2_IDX).equalsIgnoreCase(code)) {
                return country;
            }
        }
        return "";
    }

    /**
     * Returns the code of the country for the given country name.
     * @param country the name of the country
     * @return the 3-letter code of the country
     */
    public String fromCountry(String country) {
        if (countryMap.containsKey(country)) {
            return countryMap.get(country).get(1);
        }
        return "";
    }

    /**
     * Returns how many countries are included in this code converter.
     * @return how many countries are included in this code converter.
     */
    public int getNumCountries() {
        return countryMap.size();
    }
}

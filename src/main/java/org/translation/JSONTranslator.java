package org.translation;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * An implementation of the Translator interface which reads in the translation
 * data from a JSON file. The data is read in once each time an instance of this class is constructed.
 */
public class JSONTranslator implements Translator {

    public static final String ALPHA_3 = "alpha3";

    // TODO Task: pick appropriate instance variables for this class
    private JSONArray jsonContent;

    /**
     * Constructs a JSONTranslator using data from the sample.json resources file.
     */
    public JSONTranslator() {
        this("sample.json");
    }

    /**
     * Constructs a JSONTranslator populated using data from the specified resources file.
     * @param filename the name of the file in resources to load the data from
     * @throws RuntimeException if the resource file can't be loaded properly
     */
    public JSONTranslator(String filename) {
        // read the file to get the data to populate things...
        try {

            String jsonString = Files.readString(Paths.get(getClass().getClassLoader().getResource(filename).toURI()));

            JSONArray jsonArray = new JSONArray(jsonString);

            // TODO Task: use the data in the jsonArray to populate your instance variables
            //            Note: this will likely be one of the most substantial pieces of code you write in this lab.
            this.jsonContent = jsonArray;

        }
        catch (IOException | URISyntaxException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public List<String> getCountryLanguages(String country) {
        // TODO Task: return an appropriate list of language codes,
        //            but make sure there is no aliasing to a mutable object
        List<String> res = new ArrayList<>();
        List<String> extraStr = List.of("id", "alpha2", ALPHA_3);

        for (int i = 0; i < jsonContent.length(); i++) {
            JSONObject jsonObject = jsonContent.getJSONObject(i);
            if (jsonObject.getString(ALPHA_3).equalsIgnoreCase(country)) {
                for (String key : jsonObject.keySet()) {
                    if (!extraStr.contains(key)) {
                        res.add(key);
                    }
                }
            }
        }
        return res;
    }

    @Override
    public List<String> getCountries() {
        List<String> res = new ArrayList<>();
        for (int i = 0; i < jsonContent.length(); i++) {
            JSONObject jsonObject = jsonContent.getJSONObject(i);
            res.add(jsonObject.getString(ALPHA_3));
        }
        return res;
    }

    @Override
    public String translate(String country, String language) {
        for (int i = 0; i < jsonContent.length(); i++) {
            JSONObject jsonObject = jsonContent.getJSONObject(i);
            if (jsonObject.getString(ALPHA_3).equalsIgnoreCase(country)) {
                return jsonObject.getString(language);
            }
        }
        return null;
    }
}

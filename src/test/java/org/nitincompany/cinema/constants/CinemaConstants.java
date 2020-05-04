package org.nitincompany.cinema.constants;

import java.util.*;

/**
 * Author: nitinkumar
 * Created Date: 11/11/19
 * Info: File contains variables/constants that are used by tests
 **/

public class CinemaConstants {

    public static final LinkedHashMap<String, LinkedHashMap<String, String>> validCountryTheatresMap = new LinkedHashMap<String, LinkedHashMap<String, String>>() {{
        put("INDONESIA", new LinkedHashMap<String, String>() {{
            put("3000000000002", "Jakarta_Grand Indonesia_en-id");
            put("3000000000037", "Jakarta_Aeon Mall_en-id");
            put("3000000000006", "Jakarta_Central Park_en-id");
        }});
        put("THAILAND", new LinkedHashMap<String, String>() {{
            put("6600000009001", "Siam_Paragon Cineplex_en-th");
        }});
    }};

    public static final HashMap<String, ArrayList<String>> countryLocalesMap = new HashMap<String, ArrayList<String>>() {{
        put("INDONESIA", new ArrayList<String>() {{
            add("en-id");
            add("id-id");
            add(null);
        }});
        put("THAILAND", new ArrayList<String>() {{
            add("en-th");
            add("th-th");
        }});
    }};

    public static final HashMap<String, ArrayList<String>> countryCurrencyMap = new HashMap<String, ArrayList<String>>() {{
        put("INDONESIA", new ArrayList<String>() {{
            add("IDR");
            //currently Indonesia supports only IDR. In future more currency can be added here.
        }});
        put("THAILAND", new ArrayList<String>() {{
            add("THB");
        }});
    }};

    public static final String VALID_THEATRE_ID_INDONESIA = CinemaConstants.validCountryTheatresMap.get("INDONESIA").keySet().stream().findFirst().get();
    public static final String INVALID_THEATRE_ID = "30000000000055";
    public static final String VALID_THEATRE_ID_THAILAND = CinemaConstants.validCountryTheatresMap.get("THAILAND").keySet().stream().findFirst().get();
    public static final String INVALID_RANDOM_VALUE_WHEN_PRE_TEST_FAILS = "IamRandomValueUsedWhenPreTestFailsToFetchTestData_123";
    public static final String INVALID_VALUE = "87654321";

    public static HashMap<String, String> getTrackingRequestMap() {
        return new HashMap<String, String>() {{
            put("visitId", "testVisitId1");
            put("pageName", "TEST_PAGE_ONE");
            put("pageEvent", "TEST_EVENT_ONE");
        }};
    }

    public static Set<String> getAllSupportedCinemaCurrencies() {
        Set<String> currencies = new HashSet<>();
        for (String country : countryCurrencyMap.keySet()) {
            currencies.addAll(countryCurrencyMap.get(country));
        }
        return currencies;
    }

    public static String[] getSupportedCinemaLocales() {
        return new String[]{"en-id", "id-id", "en-th", "th-th", null}; //null is for default locale
    }

    public static String[] getTheatreIdsForLocales() {  //Info: order and count of theatreIds are same as that of Locale in getSupportedCinemaLocales method.
        return new String[]{VALID_THEATRE_ID_INDONESIA, VALID_THEATRE_ID_INDONESIA, VALID_THEATRE_ID_THAILAND, VALID_THEATRE_ID_THAILAND, VALID_THEATRE_ID_INDONESIA};
    }

    public static ArrayList<String[]> validAndInvalidSupportedLatLng = new ArrayList<String[]>() {{
        add(new String[]{"-7.416984", "110.351167", "validInventoryAndCountryExpected"}); //Indonesia
        add(new String[]{"15.823718", "101.771554", "validInventoryAndCountryExpected"}); //Thailand
        add(new String[]{"19.391234", "103.246469", "invalidInventoryAndCountryExpected"}); //Loas
        add(new String[]{"15.344865", "108.047494", "invalidInventoryAndCountryExpected"}); //Vietnam
    }};

    public static LinkedHashMap<String, HashMap<String, ArrayList<String>>> countryAndDefaultCityDetailsMap = new LinkedHashMap<String, HashMap<String, ArrayList<String>>>() {{
        put("INDONESIA", new HashMap<String, ArrayList<String>>() {{
            put("102813", new ArrayList() {{
                add("Jakarta");
                add(null); //default locale
            }}); //Currently each country has one default city based on which landing page is pre-filled. But we can add more cities here in case landingPageDiscover api queries on the basis of present location/city of user
        }});
        put("THAILAND", new HashMap<String, ArrayList<String>>() {{
            put("30009965", new ArrayList() {{
                add("Siam");
                add("en-th"); //default locale
            }});
        }});
    }};

}

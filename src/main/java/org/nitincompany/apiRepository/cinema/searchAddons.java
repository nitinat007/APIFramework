package org.nitincompany.apiRepository.cinema;

import org.nitincompany.apiRepository.APITemplate;

import java.util.HashMap;

/**
 * Author: nitinkumar
 * Created Date: 19/12/19
 * Info: Info of addons/searchAddons  API
 **/

public class searchAddons extends APITemplate {
    public searchAddons(String baseURL, HashMap<String, String> additionalHeaders, HashMap<String, String> additionalBody, String parameterType, HashMap<String, String> parameters) {
        super("POST", "v2/cinema/addons/searchAddons", baseURL, additionalHeaders, additionalBody, parameterType, parameters);
    }
}

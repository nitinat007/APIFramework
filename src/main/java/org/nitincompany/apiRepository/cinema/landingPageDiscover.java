package org.nitincompany.apiRepository.cinema;

import org.nitincompany.apiRepository.APITemplate;

import java.util.HashMap;

/**
 * Author: nitinkumar
 * Created Date: 23/01/20
 * Info: Info on landingPageDiscover API
 **/

public class landingPageDiscover extends APITemplate {
    public landingPageDiscover(String baseURL, HashMap<String, String> additionalHeaders, HashMap<String, String> additionalBody, String parameterType, HashMap<String, String> parameters) {
        super("POST","v2/cinema/landingPageDiscover", baseURL, additionalHeaders, additionalBody, parameterType, parameters);
    }
}

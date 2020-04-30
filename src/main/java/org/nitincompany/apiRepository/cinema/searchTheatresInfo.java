package org.nitincompany.apiRepository.cinema;

import org.nitincompany.apiRepository.APITemplate;

import java.util.HashMap;

/**
 * Author: nitinkumar
 * Created Date: 17/04/20
 * Info: info of searchTheatresInfo API
 **/

public class searchTheatresInfo extends APITemplate {
    public searchTheatresInfo(String baseURL, HashMap<String, String> additionalHeaders, HashMap<String, String> additionalBody, String parameterType, HashMap<String, String> parameters) {
        super("POST", "v2/cinema/searchTheatresInfo", baseURL, additionalHeaders, additionalBody, parameterType, parameters);
    }
}

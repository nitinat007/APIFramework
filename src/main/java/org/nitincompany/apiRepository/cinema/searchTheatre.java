package org.nitincompany.apiRepository.cinema;

import org.nitincompany.apiRepository.APITemplate;

import java.util.HashMap;

/**
 * Author: nitinkumar
 * Created Date: 07/02/20
 * Info: Info of searchTheatre API
 **/

public class searchTheatre extends APITemplate {
    public searchTheatre(String baseURL, HashMap<String, String> additionalHeaders, HashMap<String, String> additionalBody, String parameterType, HashMap<String, String> parameters) {
        super("POST", "v2/cinema/searchTheatre", baseURL, additionalHeaders, additionalBody, parameterType, parameters);
    }
}

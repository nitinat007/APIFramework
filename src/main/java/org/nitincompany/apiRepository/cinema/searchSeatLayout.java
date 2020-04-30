package org.nitincompany.apiRepository.cinema;

import org.nitincompany.apiRepository.APITemplate;

import java.util.HashMap;

/**
 * Author: nitinkumar
 * Created Date: 17/12/19
 * Info: Details of searchSeatLayout API
 **/

public class searchSeatLayout extends APITemplate {
    public searchSeatLayout(String baseURL, HashMap<String, String> additionalHeaders, HashMap<String, String> additionalBody, String parameterType, HashMap<String, String> parameters) {
        super("POST", "v2/cinema/searchSeatLayout", baseURL, additionalHeaders, additionalBody, parameterType, parameters);
    }
}

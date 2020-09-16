package com.progathon.apiRepository.cinema;


import com.progathon.apiRepository.APITemplate;

import java.util.HashMap;

/**
 * Author: nitinkumar
 * Created Date: 29/01/20
 * Info: Info on searchTheatreDetail API
 **/

public class searchTheatreDetail extends APITemplate {
    public searchTheatreDetail(String baseURL, HashMap<String, String> additionalHeaders, HashMap<String, String> additionalBody, String parameterType, HashMap<String, String> parameters) {
        super("POST", "v2/cinema/searchTheatreDetail", baseURL, additionalHeaders, additionalBody, parameterType, parameters);
    }
}

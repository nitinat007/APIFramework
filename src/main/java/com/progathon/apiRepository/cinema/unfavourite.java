package com.progathon.apiRepository.cinema;

import com.progathon.apiRepository.APITemplate;

import java.util.HashMap;

/**
 * Author: nitinkumar
 * Created Date: 22/01/20
 * Info: Info on unfavourite api
 **/

public class unfavourite extends APITemplate {
    public unfavourite( String baseURL, HashMap<String, String> additionalHeaders, HashMap<String, String> additionalBody, String parameterType, HashMap<String, String> parameters) {
        super("POST", "v2/cinema/unfavourite", baseURL, additionalHeaders, additionalBody, parameterType, parameters);
    }
}

package com.progathon.apiRepository.cinema;

import com.progathon.apiRepository.APITemplate;

import java.util.HashMap;

/**
 * Author: nitinkumar
 * Created Date: 21/01/20
 * Info: Info on saveToFavourite API
 **/

public class saveToFavourite extends APITemplate {
    public saveToFavourite(String baseURL, HashMap<String, String> additionalHeaders, HashMap<String, String> additionalBody, String parameterType, HashMap<String, String> parameters) {
        super("POST", "v2/cinema/saveToFavourite", baseURL, additionalHeaders, additionalBody, parameterType, parameters);
    }
}

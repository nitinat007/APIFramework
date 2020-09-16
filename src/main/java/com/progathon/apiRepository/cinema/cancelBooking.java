package com.progathon.apiRepository.cinema;


import com.progathon.apiRepository.APITemplate;

import java.util.HashMap;

/**
 * Author: nitinkumar
 * Created Date: 16/01/20
 * Info: Info on cancelBooking API
 **/

public class cancelBooking extends APITemplate {
    public cancelBooking( String baseURL, HashMap<String, String> additionalHeaders, HashMap<String, String> additionalBody, String parameterType, HashMap<String, String> parameters) {
        super("POST", "v2/cinema/cancelBooking", baseURL, additionalHeaders, additionalBody, parameterType, parameters);
    }
}

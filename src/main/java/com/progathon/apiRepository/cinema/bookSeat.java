package com.progathon.apiRepository.cinema;


import com.progathon.apiRepository.APITemplate;

import java.util.HashMap;

/**
 * Author: nitinkumar
 * Created Date: 06/01/20
 * Info: info of bookSeat API
 **/

public class bookSeat extends APITemplate {
    public bookSeat( String baseURL, HashMap<String, String> additionalHeaders, HashMap<String, String> additionalBody, String parameterType, HashMap<String, String> parameters) {
        super("POST", "v2/cinema/bookSeat", baseURL, additionalHeaders, additionalBody, parameterType, parameters);
    }
}

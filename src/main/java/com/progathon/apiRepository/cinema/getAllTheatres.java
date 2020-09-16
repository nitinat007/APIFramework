package com.progathon.apiRepository.cinema;

import com.progathon.apiRepository.APITemplate;

import java.util.HashMap;

/**
 * Author: nitinkumar
 * Created Date: 12/11/19
 * Info: Contains Cinema's getAllTheatres API details
 **/

public class getAllTheatres extends APITemplate {

    public getAllTheatres(String baseURL) {
        super("POST", "v2/cinema/getAllTheatres", baseURL);
    }

    public getAllTheatres(String baseURL, HashMap<String,String> additionalHeaders, HashMap<String,String> additionalBody, String parameterType,  HashMap<String,String> parameters) {
        super("POST", "v2/cinema/getAllTheatres", baseURL, additionalHeaders, additionalBody, parameterType, parameters);
    }

    //If this API accepts multiple combination of header,body,param then we can have overloaded version of getAllTheatres() method
}

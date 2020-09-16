package com.progathon.apiRepository.users;

import com.progathon.apiRepository.APITemplate;

import java.util.HashMap;

/**
 * Author: nitinkumar
 * Created Date: 13/02/20
 * Info: RPCs of reviewAsyncServerService
 **/

public class getReviewAsync extends APITemplate {
    public getReviewAsync(String baseURL, HashMap<String, String> additionalHeaders, HashMap<String, String> additionalBody, String parameterType, HashMap<String, String> parameters) {
        super("POST", "reviewAsyncServerService", baseURL, additionalHeaders, additionalBody, parameterType, parameters);
    }
}

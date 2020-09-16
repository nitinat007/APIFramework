package com.progathon.apiRepository.users;

import com.progathon.apiRepository.APITemplate;

import java.util.HashMap;

/**
 * Author: nitinkumar
 * Created Date: 14/02/20
 * Info: submitReviewAsync RPC info
 **/

public class submitReviewAsync extends APITemplate {
    public submitReviewAsync(String baseURL, HashMap<String, String> additionalHeaders, HashMap<String, String> additionalBody, String parameterType, HashMap<String, String> parameters) {
        super("POST", "reviewAsyncServerService", baseURL, additionalHeaders, additionalBody, parameterType, parameters);
    }
}

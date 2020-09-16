package com.progathon.apiRepository.users;

import com.progathon.apiRepository.APITemplate;

import java.util.HashMap;

/**
 * Author: nitinkumar
 * Created Date: 25/02/20
 * Info: Info on getReviewsAsync
 **/

public class getReviewsAsync extends APITemplate {
    public getReviewsAsync(String baseURL, HashMap<String, String> additionalHeaders, HashMap<String, String> additionalBody, String parameterType, HashMap<String, String> parameters) {
        super("POST", "reviewAsyncServerService", baseURL, additionalHeaders, additionalBody, parameterType, parameters);
    }
}

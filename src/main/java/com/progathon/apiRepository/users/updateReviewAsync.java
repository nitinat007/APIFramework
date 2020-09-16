package com.progathon.apiRepository.users;

import com.progathon.apiRepository.APITemplate;

import java.util.HashMap;

/**
 * Author: nitinkumar
 * Created Date: 23/04/20
 * Info: Info on updateReviewAsync API
 **/

public class updateReviewAsync extends APITemplate {
    public updateReviewAsync(String baseURL, HashMap<String, String> additionalHeaders, HashMap<String, String> additionalBody, String parameterType, HashMap<String, String> parameters) {
        super("POST", "reviewAsyncServerService", baseURL, additionalHeaders, additionalBody, parameterType, parameters);
    }
}

package org.nitincompany.apiRepository.cinema;


import org.nitincompany.apiRepository.APITemplate;

import java.util.HashMap;

/**
 * Author: nitinkumar
 * Created Date: 16/01/20
 * Info: info on getBookingReview api
 **/

public class getBookingReview extends APITemplate {
    public getBookingReview(String baseURL, HashMap<String, String> additionalHeaders, HashMap<String, String> additionalBody, String parameterType, HashMap<String, String> parameters) {
        super("POST", "v2/cinema/getBookingReview", baseURL, additionalHeaders, additionalBody, parameterType, parameters);
    }
}

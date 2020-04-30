package org.nitincompany.apiRepository.cinema;

import org.nitincompany.apiRepository.APITemplate;

import java.util.HashMap;

/**
 * Author: nitinkumar
 * Created Date: 04/12/19
 * Info: API info of landingPageQuickBuy
 **/

public class landingPageQuickBuy extends APITemplate {

    public landingPageQuickBuy(String baseURL, HashMap<String, String> additionalHeaders, HashMap<String, String> additionalBody, String parameterType, HashMap<String, String> parameters) {
        super("POST", "v2/cinema/landingPageQuickBuy", baseURL, additionalHeaders, additionalBody, parameterType, parameters);
    }
}

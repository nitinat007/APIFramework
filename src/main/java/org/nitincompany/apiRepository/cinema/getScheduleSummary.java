package org.nitincompany.apiRepository.cinema;


import org.nitincompany.apiRepository.APITemplate;

import java.util.HashMap;

/**
 * Author: nitinkumar
 * Created Date: 13/12/19
 * Info: Info of getScheduleSummary API
 **/

public class getScheduleSummary extends APITemplate {
    public getScheduleSummary(String baseURL, HashMap<String, String> additionalHeaders, HashMap<String, String> additionalBody, String parameterType, HashMap<String, String> parameters) {
        super("POST", "v2/cinema/getScheduleSummary", baseURL, additionalHeaders, additionalBody, parameterType, parameters);
    }
}

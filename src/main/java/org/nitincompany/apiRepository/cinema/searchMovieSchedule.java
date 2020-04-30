package org.nitincompany.apiRepository.cinema;

import org.nitincompany.apiRepository.APITemplate;

import java.util.HashMap;

/**
 * Author: nitinkumar
 * Created Date: 09/13/19
 * Info: Info of searchMovieSchedule API
 **/

public class searchMovieSchedule extends APITemplate {
    public searchMovieSchedule(String baseURL, HashMap<String, String> additionalHeaders, HashMap<String, String> additionalBody, String parameterType, HashMap<String, String> parameters) {
        super("POST", "v2/cinema/searchMovieSchedule", baseURL, additionalHeaders, additionalBody, parameterType, parameters);
    }
}

package org.nitincompany.apiRepository.cinema;

import org.nitincompany.apiRepository.APITemplate;

import java.util.HashMap;

/**
 * Author: nitinkumar
 * Created Date: 09/12/19
 * Info: searchMovieDetail API info
 **/

public class searchMovieDetail extends APITemplate {
    public searchMovieDetail(String baseURL, HashMap<String, String> additionalHeaders, HashMap<String, String> additionalBody, String parameterType, HashMap<String, String> parameters) {
        super("POST", "v2/cinema/searchMovieDetail", baseURL, additionalHeaders, additionalBody, parameterType, parameters);
    }
}

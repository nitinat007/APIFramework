package com.progathon.cinema.tests;

import com.progathon.cinema.dp.CinemaDP;
import io.restassured.response.ValidatableResponse;
import com.progathon.cinema.dp.CinemaDP;
import com.progathon.framework.initializers.APIFramework;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Author: nitinkumar
 * Created Date: 13/12/19
 * Info: All tests related to getScheduleSummary API
 **/

public class GetScheduleSummaryAPITestSuite extends CinemaBase {
    public ValidatableResponse response;
    APIFramework cinemaAPIFrameworkInstance = new APIFramework("cinema");

    @Test(description = "Verify response by passing valid and invalid schedule data (movieId, theatreId, date, auditoriumTypeId, showTimeId) of movie for each supported locale. Also validate nullability rule in response body", dataProviderClass = CinemaDP.class, dataProvider = "getValidAndInvalidMovieSchedulesForEachCountry", groups = {"sanity", "regression"}, priority = 1)
    public void verifyResponseOfValidAndInvalidMovieSchedule(String movieId, String theatreId, Map<String, String> date, String auditoriumTypeId, String showTimeId, String scheduleType) {
        response = cinemaAPIFrameworkInstance.sendRequest("getScheduleSummary", null, new HashMap<String, String>() {{
            put("movieId", movieId);
            put("theatreId", theatreId);
            putAll(date);
            put("auditoriumTypeId", auditoriumTypeId);
            put("showTimeId", showTimeId);
        }}, null, null);

        if (scheduleType.equalsIgnoreCase("valid")) {
            response.assertThat().statusCode(200);
            softAssert.assertTrue(response.extract().jsonPath().get("data.remainingSeats") != null);
            softAssert.assertAll();

        } else if (scheduleType.equalsIgnoreCase("invalid")) {
            response.assertThat().statusCode(500);
            //Assert.assertTrue(response.extract().jsonPath().get("errorMessage").toString().contains("java.lang.IllegalArgumentException:")," Error is not due to invalid test data. This could be due to RPC failure.");
        }
    }

    @Test(description = "API should work with all supported locale", dataProviderClass = CinemaDP.class, dataProvider = "getLocaleAndValidMovieSchedule", groups = {"sanity", "regression"}, priority = 2)
    public void testAllSupportedLocale(String locale, String movieId, String theatreId, Map<String, String> date, String auditoriumTypeId, String showTimeId) {
        cinemaAPIFrameworkInstance.setLocale(locale);
        response = cinemaAPIFrameworkInstance.sendRequest("getScheduleSummary", null, new HashMap<String, String>() {{
            put("movieId", movieId);
            put("theatreId", theatreId);
            putAll(date);
            put("auditoriumTypeId", auditoriumTypeId);
            put("showTimeId", showTimeId);
        }}, null, null);
        response.assertThat().statusCode(200);
        Assert.assertTrue(response.extract().jsonPath().get("data") != null, "'data' is not present in response for locale " + locale + ", movieId " + movieId + " showTimeId " + showTimeId);
        Assert.assertTrue(response.extract().jsonPath().get("data.remainingSeats") != null && response.extract().jsonPath().get("data.remainingSeats") != "", "remainingSeats for movieId " + movieId + " showTimeId " + showTimeId + " for locale " + locale + " is either blank or null");

    }
/*
    @Test(description = "Schema Validation", groups = {"sanity", "regression"}, dataProviderClass = CinemaDP.class, dataProvider = "getAValidMovieSchedule", priority = 3)
    public void testSchemaValidation(String movieId, String theatreId, Map<String, String> date, String auditoriumTypeId, String showTimeId) {
        response = cinemaAPIFrameworkInstance.sendRequest("getScheduleSummary", null, new HashMap<String, String>() {{
            put("movieId", movieId);
            put("theatreId", theatreId);
            putAll(date);
            put("auditoriumTypeId", auditoriumTypeId);
            put("showTimeId", showTimeId);
        }}, null, null);
        Assert.assertTrue(cinemaAPIFrameworkInstance.validateSchema(response));
    }
 */

}

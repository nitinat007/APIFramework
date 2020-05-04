package org.nitincompany.cinema.tests;

import io.restassured.response.ValidatableResponse;
import org.nitincompany.cinema.dp.CinemaDP;
import org.nitincompany.cinema.utils.CinemaUtils;
import org.nitincompany.framework.initializers.APIFramework;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Author: nitinkumar
 * Created Date: 09/13/19
 * Info: API tests of searchMovieSchedule API
 **/

public class SearchMovieScheduleAPITestSuite extends CinemaBase {
    public ValidatableResponse response;
    APIFramework cinemaAPIFrameworkInstance = new APIFramework("cinema");

    @Test(description = "Verify response by passing valid and invalid request fields (theatreId, movieId). Also validate nullability rule in response body.", dataProviderClass = CinemaDP.class, dataProvider = "getValidAndInvalidTheatreIdMovieId", groups = {"sanity", "regression"}, priority = 1)
    public void verifyResponseWithValidAndInvalidTheatreIdMovieId(String theatreId, String movieId, String testcaseType) {
        response = cinemaAPIFrameworkInstance.sendRequest("searchMovieSchedule", null, new HashMap<String, String>() {{
            put("theatreId", theatreId);
            put("movieId", movieId);
        }}, null, null);

        if (testcaseType.equalsIgnoreCase("valid")) {
            response.assertThat().statusCode(200);
            softAssert.assertTrue(response.extract().jsonPath().get("data.isPresale") != null);
            softAssert.assertTrue(response.extract().jsonPath().get("data.todayDate") != null);
            softAssert.assertTrue(response.extract().jsonPath().get("data.todayDate.month") != null);
            softAssert.assertTrue(response.extract().jsonPath().get("data.todayDate.day") != null);
            softAssert.assertTrue(response.extract().jsonPath().get("data.todayDate.year") != null);
            softAssert.assertTrue(response.extract().jsonPath().get("data.availableDates") != null);
            softAssert.assertTrue(response.extract().jsonPath().get("data.movieSchedules") != null);
            List<Object> listOfAvailableDates = response.extract().jsonPath().getList("data.availableDates");
            softAssert.assertTrue(listOfAvailableDates.size() != 0, "No available dates for theatreId " + theatreId + ", movieId " + movieId);
            for (int availableDatesCount = 0; availableDatesCount < listOfAvailableDates.size(); availableDatesCount++) {
                softAssert.assertTrue(response.extract().jsonPath().get("data.availableDates[" + availableDatesCount + "].date") != null);
                softAssert.assertTrue(response.extract().jsonPath().get("data.availableDates[" + availableDatesCount + "].isAvailable") != null);
                softAssert.assertTrue(response.extract().jsonPath().get("data.availableDates[" + availableDatesCount + "].date.month") != null);
                softAssert.assertTrue(response.extract().jsonPath().get("data.availableDates[" + availableDatesCount + "].date.day") != null);
                softAssert.assertTrue(response.extract().jsonPath().get("data.availableDates[" + availableDatesCount + "].date.year") != null);
            }
            List<Object> listOfMovieSchedules = response.extract().jsonPath().getList("data.movieSchedules");
            softAssert.assertTrue(listOfMovieSchedules.size() != 0, "No MovieSchedules for theatreId " + theatreId + ", movieId " + movieId);
            for (int movieSchedulesCount = 0; movieSchedulesCount < listOfMovieSchedules.size(); movieSchedulesCount++) {
                softAssert.assertTrue(response.extract().jsonPath().get("data.movieSchedules[" + movieSchedulesCount + "].id") != null);
                softAssert.assertTrue(response.extract().jsonPath().get("data.movieSchedules[" + movieSchedulesCount + "].label") != null);
                softAssert.assertTrue(response.extract().jsonPath().get("data.movieSchedules[" + movieSchedulesCount + "].showTimes") != null, "data.movieSchedules[" + movieSchedulesCount + "].showTimes is null");
                int numberOfShowTimes = ((ArrayList) response.extract().jsonPath().get("data.movieSchedules[" + movieSchedulesCount + "].showTimes")).size();
                for (int countOfShowTimes = 0; countOfShowTimes < numberOfShowTimes; countOfShowTimes++) {
                    softAssert.assertTrue(response.extract().jsonPath().get("data.movieSchedules[" + movieSchedulesCount + "].showTimes[" + countOfShowTimes + "].id") != null, "data.movieSchedules[" + movieSchedulesCount + "].showTimes[" + countOfShowTimes + "].id is null");
                    softAssert.assertTrue(response.extract().jsonPath().get("data.movieSchedules[" + movieSchedulesCount + "].showTimes[" + countOfShowTimes + "].label") != null, "data.movieSchedules[" + movieSchedulesCount + "].showTimes[" + countOfShowTimes + "].label is null");
                    softAssert.assertTrue(response.extract().jsonPath().get("data.movieSchedules[" + movieSchedulesCount + "].showTimes[" + countOfShowTimes + "].seatsAvailable") != null && CinemaUtils.stringHasIntegerValue(response.extract().jsonPath().get("data.movieSchedules[" + movieSchedulesCount + "].showTimes[" + countOfShowTimes + "].seatsAvailable")), "data.movieSchedules[" + movieSchedulesCount + "].showTimes[" + countOfShowTimes + "].seatsAvailable is null or non integer");
                    softAssert.assertTrue(CinemaUtils.isBoolean(response.extract().jsonPath().get("data.movieSchedules[" + movieSchedulesCount + "].showTimes[" + countOfShowTimes + "].isAvailable")), "data.movieSchedules[" + movieSchedulesCount + "].showTimes[" + countOfShowTimes + "].isAvailable is non boolean");
                }
                softAssert.assertTrue(response.extract().jsonPath().get("data.movieSchedules[" + movieSchedulesCount + "].seatTypes") != null);
                softAssert.assertTrue(response.extract().jsonPath().get("data.movieSchedules[" + movieSchedulesCount + "].convenienceFee") != null);
            }
            softAssert.assertAll();
        } else if (testcaseType.equalsIgnoreCase("invalid")) {
            response.assertThat().statusCode(200);
            List<Object> listOfAvailableDates = response.extract().jsonPath().getList("data.availableDates");
            softAssert.assertTrue(listOfAvailableDates.size() == 0, "Available dates for invalid theatreId " + theatreId + ", invalid movieId " + movieId + " is not empty. listOfAvailableDates=" + listOfAvailableDates);
            List<Object> listOfMovieSchedules = response.extract().jsonPath().getList("data.movieSchedules");
            softAssert.assertTrue(listOfMovieSchedules.size() == 0, "Available schedules for invalid theatreId " + theatreId + ", invalid movieId " + movieId + " is not empty. listOfMovieSchedules=" + listOfMovieSchedules);
            softAssert.assertAll();
        }
    }

    @Test(description = "Verify response for past and first available date", dataProviderClass = CinemaDP.class, dataProvider = "getValidTheatreIdMovieIdDate", groups = {"sanity", "regression"}, priority = 2)
    public void verifyResponseForDifferentDate(String theatreId, String movieId, HashMap date, boolean isMovieSchedulesExpectedInResponse) {
        response = cinemaAPIFrameworkInstance.sendRequest("searchMovieSchedule", null, new HashMap<String, String>() {{
            put("theatreId", theatreId);
            put("movieId", movieId);
            putAll(date);
        }}, null, null);
        response.assertThat().statusCode(200);
        Assert.assertTrue(response.extract().jsonPath().getMap("data.todayDate") != null, "todayDate is not present under data for theatreId " + theatreId);
        if (isMovieSchedulesExpectedInResponse) {
            Assert.assertTrue(response.extract().jsonPath().getList("data.movieSchedules").size() != 0, "movieSchedules is not present under data for theatreId " + theatreId + " movieId " + movieId + " date " + date);
        }
    }

    @Test(description = "Verify nullability rule of request body as per the API contract", dataProviderClass = CinemaDP.class, dataProvider = "getTheatreIdMovieIdCurrencyDayMonthYear", groups = {"sanity", "regression"}, priority = 3)
    public void verifyNullabilityRule(String theatreId, String movieId, String currency, HashMap date) {
        response = cinemaAPIFrameworkInstance
                .sendRequest(
                        "searchMovieSchedule",
                        null,
                        new HashMap<String, String>() {{
                            put("theatreId", theatreId);
                            put("movieId", movieId);
                            put("currency", currency);
                            if (date != null) {
                                putAll(date);
                            }
                        }},
                        null,
                        null);
        response.assertThat().statusCode(200);
        Assert.assertTrue(response.extract().jsonPath().getList("data.movieSchedules") != null, "MovieSchedules list is not present under data for movieId " + movieId + ", theatreId " + theatreId);
        Assert.assertTrue(response.extract().jsonPath().get("data.todayDate") != null, "todayDate is not present under data for movieId " + movieId + ", theatreId " + theatreId);
        softAssert.assertAll();
    }

    @Test(description = "API should work with all supported locale", dataProviderClass = CinemaDP.class, dataProvider = "getLocaleTheatreIdMovieId", groups = {"sanity", "regression"}, priority = 4)
    public void testAllSupportedLocale(String locale, String theatreId, String movieId) {
        cinemaAPIFrameworkInstance.setLocale(locale);
        response = cinemaAPIFrameworkInstance.sendRequest("searchMovieSchedule", null, new HashMap<String, String>() {{
            put("theatreId", theatreId);
            put("movieId", movieId);
        }}, null, null);
        response.assertThat().statusCode(200);
        Assert.assertTrue(response.extract().jsonPath().getMap("data.todayDate") != null, "todayDate is not present under data for locale " + locale + ", theatreId " + theatreId);
    }

    /*
    @Test(description = "Schema Validation", groups = {"sanity", "regression"}, dataProviderClass = CinemaDP.class, dataProvider = "getValidTheatreIdMovieId", priority = 5)
    public void testSchemaValidation(String theatreId, String movieId) {
        response = cinemaAPIFrameworkInstance.sendRequest("searchMovieSchedule", null, new HashMap<String, String>() {{
            put("theatreId", theatreId);
            put("movieId", movieId);
        }}, null, null);
        response.assertThat().statusCode(200);
        Assert.assertTrue(cinemaAPIFrameworkInstance.validateSchema(response));
    }

     */
}

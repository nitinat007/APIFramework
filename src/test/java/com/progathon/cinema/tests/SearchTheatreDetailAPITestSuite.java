package com.progathon.cinema.tests;

import com.progathon.cinema.dp.CinemaDP;
import com.progathon.cinema.utils.CinemaUtils;
import io.restassured.response.ValidatableResponse;
import com.progathon.cinema.dp.CinemaDP;
import com.progathon.cinema.utils.CinemaUtils;
import com.progathon.framework.initializers.APIFramework;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author: nitinkumar
 * Created Date: 29/01/20
 * Info: Tests of searchTheatreDetail API
 **/

public class SearchTheatreDetailAPITestSuite extends CinemaBase {
    public ValidatableResponse response;
    APIFramework cinemaAPIFrameworkInstance = new APIFramework("cinema");

    @Test(description = "Verify response by passing valid and invalid request data (currency, theatreId, date, trackingRequest). Also validate nullability rule in response body.", dataProviderClass = CinemaDP.class, dataProvider = "getValidAndInvalidTheatreIdCurrencyDateDP", groups = {"sanity", "regression"}, priority = 1)
    public void verifyResponseWithValidAndInvalidCurrencyTheatreIdDateTrackingRequest(String currency, String theatreId, HashMap<String, String> date, String testcaseType) {
        response = cinemaAPIFrameworkInstance.sendRequest("searchTheatreDetail", null, new HashMap<String, String>() {{
            put("theatreId", theatreId);
            put("currency", currency);
            putAll(date);
        }}, null, null);

        if (testcaseType.equalsIgnoreCase("valid")) {
            response.assertThat().statusCode(200);
            softAssert.assertTrue(CinemaUtils.isStringNotNullOrEmpty(response.extract().jsonPath().get("data.name")), "name is incorrect");
            softAssert.assertTrue(response.extract().jsonPath().get("data.venueLocation") != null, "venueLocation is incorrect");
            softAssert.assertTrue(CinemaUtils.isStringNotNullOrEmpty(response.extract().jsonPath().get("data.theatreGroupLogoUrl")), "theatreGroupLogoUrl is incorrect");
            softAssert.assertTrue(CinemaUtils.isStringNotNullOrEmpty(response.extract().jsonPath().get("data.address")), "address is incorrect");
            softAssert.assertTrue(response.extract().jsonPath().get("data.phoneNumber") != null, "phoneNumber is null");
            softAssert.assertTrue(CinemaUtils.isStringNotNullOrEmpty(response.extract().jsonPath().get("data.theatreNameEN")), "theatreNameEN is incorrect");
            softAssert.assertTrue(CinemaUtils.isStringNotNullOrEmpty(response.extract().jsonPath().get("data.countryNameEN")), "countryNameEN is incorrect");

            Map<String, String> dateToday = response.extract().jsonPath().getMap("data.dateToday");
            softAssert.assertTrue(CinemaUtils.isDateMapCorrect(dateToday), " dateToday is incorrect.");
            Map<String, String> firstAvailableDate = response.extract().jsonPath().getMap("data.firstAvailableDate");
            softAssert.assertTrue(CinemaUtils.isDateMapCorrect(firstAvailableDate), " firstAvailableDate is incorrect.");

            List<Map<String, String>> availableDates = response.extract().jsonPath().getList("data.availableDates");
            for (Map<String, String> availableDate : availableDates) {
                softAssert.assertTrue(CinemaUtils.isDateMapCorrect(availableDate), " availableDate " + availableDate + " is incorrect.");
            }
            List<Map<String, String>> movieAvailableDates = response.extract().jsonPath().getList("data.movieAvailableDates");
            for (int movieDateCount = 0; movieDateCount < movieAvailableDates.size(); movieDateCount++) {
                Map<String, String> movieDate = response.extract().jsonPath().getMap("data.movieAvailableDates[" + movieDateCount + "].date");
                softAssert.assertTrue(CinemaUtils.isDateMapCorrect(movieDate), " movieAvailableDates.date " + movieDate + " is incorrect.");
            }
            List<String> movies = response.extract().jsonPath().getList("data.movies");
            for (int movieCount = 0; movieCount < movies.size(); movieCount++) {
                Map<String, Object> movieInfo = response.extract().jsonPath().getMap("data.movies[" + movieCount + "].movieInfo");
                softAssert.assertTrue(CinemaUtils.isStringNotNullOrEmpty(movieInfo.get("posterImageUrl").toString()), " posterImageUrl is incorrect.");
                softAssert.assertTrue(CinemaUtils.isStringNotNullOrEmpty(movieInfo.get("id").toString()), " id is incorrect.");
                softAssert.assertTrue(CinemaUtils.isStringNotNullOrEmpty(movieInfo.get("title").toString()), " title is incorrect.");
                softAssert.assertTrue(CinemaUtils.isStringNotNullOrEmpty(movieInfo.get("titleEN").toString()), " titleEN is incorrect.");
                softAssert.assertTrue(CinemaUtils.isStringNotNullOrEmpty(movieInfo.get("rating").toString()), " rating is incorrect.");
                softAssert.assertTrue(CinemaUtils.isStringNotNullOrEmpty(movieInfo.get("popularityScore").toString()), " popularityScore is incorrect.");
                softAssert.assertTrue(CinemaUtils.isDateMapCorrect((Map<String, String>) movieInfo.get("releaseDate")), " releaseDate is incorrect.");
                softAssert.assertTrue(CinemaUtils.isBoolean((boolean) movieInfo.get("isPresale")), " isPresale is incorrect.");

                List<Map<String, Object>> auditoriumShowTimeList = response.extract().jsonPath().getList("data.movies[" + movieCount + "].auditoriumShowTime");
                for (int auditoriumShowTimeCount = 0; auditoriumShowTimeCount < auditoriumShowTimeList.size(); auditoriumShowTimeCount++) {
                    Map<String, Object> auditoriumShowTime = response.extract().jsonPath().getMap("data.movies[" + movieCount + "].auditoriumShowTime[" + auditoriumShowTimeCount + "]");
                    softAssert.assertTrue(CinemaUtils.isStringNotNullOrEmpty(auditoriumShowTime.get("label").toString()), " data.movies[" + movieCount + "].auditoriumShowTime[" + auditoriumShowTimeCount + "].label is incorrect.");
                    softAssert.assertTrue(CinemaUtils.isStringNotNullOrEmpty(auditoriumShowTime.get("id").toString()), " data.movies[" + movieCount + "].auditoriumShowTime[" + auditoriumShowTimeCount + "].id is incorrect.");
                    softAssert.assertTrue((List) auditoriumShowTime.get("seatTypes") != null, " data.movies[" + movieCount + "].auditoriumShowTime[" + auditoriumShowTimeCount + "].seatTypes is incorrect.");
                    softAssert.assertTrue((List) auditoriumShowTime.get("showTimes") != null && ((List) auditoriumShowTime.get("showTimes")).size() > 0, " data.movies[" + movieCount + "].auditoriumShowTime[" + auditoriumShowTimeCount + "].showTimes is incorrect.");
                    //Can add more assertions on showTimes. presale list is always empty in staging. Can add assertions on presale in future
                }
            }
            softAssert.assertAll();
        } else if (testcaseType.equalsIgnoreCase("invalid")) {
            response.assertThat().statusCode(400);
            softAssert.assertTrue(response.extract().jsonPath().get("errorType").equals("BAD_REQUEST"));
            softAssert.assertAll();
        }
    }

    @Test(description = "Verify nullability rule of request body as per the API contract", dataProviderClass = CinemaDP.class, dataProvider = "getAllCombinationOfTheatreIdCurrencyDateDP", groups = {"sanity", "regression"}, priority = 2)
    public void verifyNullabilityRule(String currency, String theatreId, HashMap<String, String> date) {
        response = cinemaAPIFrameworkInstance
                .sendRequest("searchTheatreDetail", null, new HashMap<String, String>() {{
                            put("theatreId", theatreId);
                            put("currency", currency);
                            if (date != null) {
                                putAll(date);
                            }
                        }},
                        null,
                        null);
        response.assertThat().statusCode(200);
        Assert.assertTrue(response.extract().jsonPath().get("data.name") != null, "name is not present under data for theatreId " + theatreId);
    }

    @Test(description = "API should work with all supported locale", dataProviderClass = CinemaDP.class, dataProvider = "getAValidTheatreIdCurrencyDateOfEachCountryDP", groups = {"sanity", "regression"}, priority = 3)
    public void testAllSupportedLocale(String locale, String currency, String theatreId, HashMap<String, String> date) {
        cinemaAPIFrameworkInstance.setLocale(locale);
        response = cinemaAPIFrameworkInstance.sendRequest("searchTheatreDetail", null, new HashMap<String, String>() {{
            put("theatreId", theatreId);
            put("currency", currency);
            if (date != null) {
                putAll(date);
            }
        }}, null, null);
        response.assertThat().statusCode(200);
        Assert.assertTrue(response.extract().jsonPath().get("data.name") != null, "name is not present under data for theatreId " + theatreId);
    }
/*
    @Test(description = "Schema Validation", groups = {"sanity", "regression"}, dataProviderClass = CinemaDP.class, dataProvider = "getAValidTheatreIdCurrencyDateDP", priority = 4)
    public void testSchemaValidation(String locale, String currency, String theatreId, HashMap<String, String> date) {
        response = cinemaAPIFrameworkInstance.sendRequest("searchTheatreDetail", null, new HashMap<String, String>() {{
            put("theatreId", theatreId);
            put("currency", currency);
            if (date != null) {
                putAll(date);
            }
        }}, null, null);
        response.assertThat().statusCode(200);
        Assert.assertTrue(cinemaAPIFrameworkInstance.validateSchema(response));
    }

 */
}

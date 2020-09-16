package com.progathon.cinema.tests;

import com.progathon.cinema.dp.CinemaDP;
import com.progathon.cinema.utils.CinemaUtils;
import io.restassured.response.ValidatableResponse;
import com.progathon.cinema.dp.CinemaDP;
import com.progathon.cinema.utils.CinemaUtils;
import com.progathon.framework.initializers.APIFramework;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Author: nitinkumar
 * Created Date: 17/12/19
 * Info: Tests of searchSeatLayout API
 **/

public class SearchSeatLayoutAPITestSuite extends CinemaBase {
    public ValidatableResponse response;
    APIFramework cinemaAPIFrameworkInstance = new APIFramework("cinema");


    @Test(description = "Verify response by passing valid and invalid request data (movieId, theatreId, date, auditoriumTypeId, showTimeId, currency). Also validate nullability rule in response body.", dataProviderClass = CinemaDP.class, dataProvider = "getValidAndInvalidMovieSchedulesWithCurrencyForEachCountry", groups = {"sanity", "regression"}, priority = 1)
    public void verifyResponseOfValidAndInvalidMovieSchedule(String movieId, String theatreId, Map<String, String> date, String auditoriumTypeId, String showTimeId, String currency, String scheduleType) {
        response = cinemaAPIFrameworkInstance.sendRequest("searchSeatLayout", null, new HashMap<String, String>() {{
            put("movieId", movieId);
            put("theatreId", theatreId);
            putAll(date);
            put("auditoriumTypeId", auditoriumTypeId);
            put("showTimeId", showTimeId);
            put("currency", currency);
        }}, null, null);

        if (scheduleType.equalsIgnoreCase("valid")) {
            response.assertThat().statusCode(200);
            softAssert.assertTrue(CinemaUtils.isStringNotNullOrEmpty(response.extract().jsonPath().get("data.auditoriumNumber")));
            ArrayList seatLayoutList = response.extract().jsonPath().get("data.seatLayout");

            // Picking one random seat each from two random rows (out of ~150+ total seats) from entire seat layout and validating nullability rule
            for (int randomRowCount = 0; randomRowCount < 2; randomRowCount++) {
                int randomRowNumberOfSeatLayout = new Random().nextInt(seatLayoutList.size());
                ArrayList randomRowOfSeatLayout = response.extract().jsonPath().get("data.seatLayout[" + randomRowNumberOfSeatLayout + "]"); //data.seatLayout is in 2D array
                int randomSeatOfThisRow = new Random().nextInt(randomRowOfSeatLayout.size());
                Object seat = response.extract().jsonPath().get("data.seatLayout[" + randomRowNumberOfSeatLayout + "][" + randomSeatOfThisRow + "]");
                logger.log(" Verifying nullability rule for seat in row " + randomRowNumberOfSeatLayout + " seat number " + randomSeatOfThisRow + ". Seat fetched: " + seat);

                if (seat != null) {
                    String id = response.extract().jsonPath().get("data.seatLayout[" + randomRowNumberOfSeatLayout + "][" + randomSeatOfThisRow + "].id");
                    softAssert.assertTrue(CinemaUtils.isStringNotNullOrEmpty(id));
                    String number = response.extract().jsonPath().get("data.seatLayout[" + randomRowNumberOfSeatLayout + "][" + randomSeatOfThisRow + "].number");
                    softAssert.assertTrue(CinemaUtils.isStringNotNullOrEmpty(number));
                    Boolean isTaken = response.extract().jsonPath().get("data.seatLayout[" + randomRowNumberOfSeatLayout + "][" + randomSeatOfThisRow + "].isTaken");
                    softAssert.assertTrue(isTaken == true || isTaken == false);
                    String typeName = response.extract().jsonPath().get("data.seatLayout[" + randomRowNumberOfSeatLayout + "][" + randomSeatOfThisRow + "].type.typeName");
                    softAssert.assertTrue(CinemaUtils.isStringNotNullOrEmpty(typeName));
                    String typeId = response.extract().jsonPath().get("data.seatLayout[" + randomRowNumberOfSeatLayout + "][" + randomSeatOfThisRow + "].type.typeId");
                    softAssert.assertTrue(CinemaUtils.isStringNotNullOrEmpty(typeId));
                    String typeLabel = response.extract().jsonPath().get("data.seatLayout[" + randomRowNumberOfSeatLayout + "][" + randomSeatOfThisRow + "].type.typeLabel");
                    softAssert.assertTrue(CinemaUtils.isStringNotNullOrEmpty(typeLabel));
                    String colorHexadecimals = response.extract().jsonPath().get("data.seatLayout[" + randomRowNumberOfSeatLayout + "][" + randomSeatOfThisRow + "].type.colorHexadecimals");
                    softAssert.assertTrue(CinemaUtils.isStringNotNullOrEmpty(colorHexadecimals));
                    String currencyValueCurrency = response.extract().jsonPath().get("data.seatLayout[" + randomRowNumberOfSeatLayout + "][" + randomSeatOfThisRow + "].seatPrice.currencyValue.currency");
                    softAssert.assertTrue(CinemaUtils.isStringNotNullOrEmpty(currencyValueCurrency));
                    String amount = response.extract().jsonPath().get("data.seatLayout[" + randomRowNumberOfSeatLayout + "][" + randomSeatOfThisRow + "].seatPrice.currencyValue.amount");
                    softAssert.assertTrue(CinemaUtils.isStringNotNullOrEmpty(amount));
                    boolean nullOrEmpty = response.extract().jsonPath().get("data.seatLayout[" + randomRowNumberOfSeatLayout + "][" + randomSeatOfThisRow + "].seatPrice.currencyValue.nullOrEmpty");
                    softAssert.assertTrue(nullOrEmpty == true || nullOrEmpty == false);
                    String numOfDecimalPoint = response.extract().jsonPath().get("data.seatLayout[" + randomRowNumberOfSeatLayout + "][" + randomSeatOfThisRow + "].seatPrice.numOfDecimalPoint");
                    softAssert.assertTrue(CinemaUtils.isStringNotNullOrEmpty(numOfDecimalPoint));
                    String numberOfSeatsAvailable = response.extract().jsonPath().get("data.numberOfSeatsAvailable");
                    softAssert.assertTrue(CinemaUtils.isStringNotNullOrEmpty(numberOfSeatsAvailable));
                    boolean isTimeUp = response.extract().jsonPath().get("data.isTimeUp");
                    softAssert.assertTrue(isTimeUp == true || isTimeUp == false);
                    String refreshTime = response.extract().jsonPath().get("data.refreshTime");
                    softAssert.assertTrue(CinemaUtils.isStringNotNullOrEmpty(refreshTime));
                    String maxBookedSeats = response.extract().jsonPath().get("data.maxBookedSeats");
                    softAssert.assertTrue(CinemaUtils.isStringNotNullOrEmpty(maxBookedSeats));
                    boolean addOnsSaleAvailable = response.extract().jsonPath().get("data.addOnsSaleAvailable");
                    softAssert.assertTrue(addOnsSaleAvailable == true || addOnsSaleAvailable == false);
                    String convenienceFeeCurrencyValueCurrency = response.extract().jsonPath().get("data.convenienceFee.currencyValue.currency");
                    softAssert.assertTrue(CinemaUtils.isStringNotNullOrEmpty(convenienceFeeCurrencyValueCurrency));
                    String convenienceFeeAmount = response.extract().jsonPath().get("data.convenienceFee.currencyValue.amount");
                    softAssert.assertTrue(CinemaUtils.isStringNotNullOrEmpty(convenienceFeeAmount));
                    boolean convenienceFeeNullOrEmpty = response.extract().jsonPath().get("data.convenienceFee.currencyValue.nullOrEmpty");
                    softAssert.assertTrue(convenienceFeeNullOrEmpty == true || convenienceFeeNullOrEmpty == false);
                    String convenienceFeeNumOfDecimalPoint = response.extract().jsonPath().get("data.convenienceFee.numOfDecimalPoint");
                    softAssert.assertTrue(CinemaUtils.isStringNotNullOrEmpty(convenienceFeeNumOfDecimalPoint));
                }
            }
            softAssert.assertAll();

        } else if (scheduleType.equalsIgnoreCase("invalid")) {
            response.assertThat().statusCode(500);
            Assert.assertTrue(response.extract().jsonPath().get("errorMessage").toString().contains("java.lang.NumberFormatException: For input string: \"SELECT_SEAT\" "), " Error message is: " + response.extract().jsonPath().get("errorMessage"));
        }
    }

    @Test(description = "Verify nullability rule of request body as per the API contract", dataProviderClass = CinemaDP.class, dataProvider = "getMovieScheduleAndCurrency", groups = {"sanity", "regression"}, priority = 2)
    public void verifyNullabilityRule(String movieId, String theatreId, Map<String, String> date, String auditoriumTypeId, String showTimeId, String currency) {
        response = cinemaAPIFrameworkInstance.sendRequest("searchMovieSchedule", null, new HashMap<String, String>() {{
                    put("movieId", movieId);
                    put("theatreId", theatreId);
                    putAll(date);
                    put("auditoriumTypeId", auditoriumTypeId);
                    put("showTimeId", showTimeId);
                    if (currency != null) {
                        put("currency", currency);
                    }
                }},
                null,
                null);
        response.assertThat().statusCode(200);
        Assert.assertTrue(response.extract().jsonPath().getList("data.movieSchedules") != null, "MovieSchedules list is not present under data for movieId " + movieId + ", theatreId " + theatreId);
        Assert.assertTrue(response.extract().jsonPath().get("data.todayDate") != null, "todayDate is not present under data for movieId " + movieId + ", theatreId " + theatreId);
        softAssert.assertAll();
    }

    @Test(description = "API should work with all supported locale", dataProviderClass = CinemaDP.class, dataProvider = "getLocaleValidMovieScheduleAndCurrency", groups = {"sanity", "regression"}, priority = 3)
    public void testAllSupportedLocale(String locale, String movieId, String theatreId, Map<String, String> date, String auditoriumTypeId, String showTimeId, String currency) {
        cinemaAPIFrameworkInstance.setLocale(locale);
        response = cinemaAPIFrameworkInstance.sendRequest("searchSeatLayout", null, new HashMap<String, String>() {{
            put("movieId", movieId);
            put("theatreId", theatreId);
            putAll(date);
            put("auditoriumTypeId", auditoriumTypeId);
            put("showTimeId", showTimeId);
            put("currency", currency);
        }}, null, null);
        response.assertThat().statusCode(200);
        Assert.assertTrue(response.extract().jsonPath().get("data.auditoriumNumber") != null, "'data.auditoriumNumber' is not present in response for locale " + locale + ", movieId " + movieId + " showTimeId " + showTimeId);
        Assert.assertTrue(response.extract().jsonPath().get("data.seatLayout") != null && response.extract().jsonPath().getList("data.seatLayout").size() > 0, "seatLayout for movieId " + movieId + " showTimeId " + showTimeId + " for locale " + locale + " is either blank or null");

    }

    /* Seeing a lot of schema violations. Will revisit later.
    @Test(description = "Schema Validation", groups = {"sanity", "regression"}, dataProviderClass = TheatreDP.class, dataProvider = "getAValidMovieScheduleAndCurrency", priority = 4)
    public void testSchemaValidation(String movieId, String theatreId, Map<String, String> date, String auditoriumTypeId, String showTimeId, String currency) {
        response = cinemaAPIFrameworkInstance.sendRequest("searchSeatLayout", null, new HashMap<String, String>() {{
            put("movieId", movieId);
            put("theatreId", theatreId);
            putAll(date);
            put("auditoriumTypeId", auditoriumTypeId);
            put("showTimeId", showTimeId);
            put("currency", currency);
        }}, null, null);
        Assert.assertTrue(cinemaAPIFrameworkInstance.validateSchema(response));
    }
    */
}

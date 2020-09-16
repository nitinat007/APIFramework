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

/**
 * Author: nitinkumar
 * Created Date: 23/12/19
 * Info: Tests of addons/searchAddons API
 **/

public class SearchAddonsAPITestSuite extends CinemaBase {
    public ValidatableResponse response;
    APIFramework cinemaAPIFrameworkInstance = new APIFramework("cinema");

    @Test(description = "Verify response by passing valid and invalid request data (movieId, theatreId, date, showTimeId, currency). Also validate nullability rule in response body.", dataProviderClass = CinemaDP.class, dataProvider = "getValidAndInvalidMovieSchedulesWithCurrencyForEachCountryForAddOns", groups = {"sanity", "regression"}, priority = 1)
    public void verifyResponseOfValidAndInvalidMovieSchedule(String movieId, String theatreId, Map<String, String> date, String showTimeId, String currency, String scheduleType) {
        response = cinemaAPIFrameworkInstance.sendRequest("searchAddons", null, new HashMap<String, String>() {{
            put("movieId", movieId);
            put("theatreId", theatreId);
            putAll(date);
            put("showTimeId", showTimeId);
            put("currency", currency);
        }}, null, null);

        if (scheduleType.equalsIgnoreCase("valid")) {
            response.assertThat().statusCode(200);
            HashMap<String, Object> addOnsMenuMap = response.extract().jsonPath().get("data.addOnsMenuMap");
            Assert.assertTrue(!addOnsMenuMap.isEmpty(), "addOnsMenuMap is empty");
            ArrayList<String> addOnsCategories = response.extract().jsonPath().get("data.addOnsCategories");
            Assert.assertTrue(!addOnsCategories.isEmpty(), "addOnsCategories is empty");
            for (String addOnsMenuMapKey : addOnsMenuMap.keySet()) {
                String addOnsId = response.extract().jsonPath().get("data.addOnsMenuMap." + addOnsMenuMapKey + ".addOnsId");
                softAssert.assertTrue(CinemaUtils.isStringNotNullOrEmpty(addOnsId), "addOnsId is null or empty");
                String addOnsName = response.extract().jsonPath().get("data.addOnsMenuMap." + addOnsMenuMapKey + ".addOnsName");
                softAssert.assertTrue(CinemaUtils.isStringNotNullOrEmpty(addOnsName), "addOnsName is null or empty");
                String addOnsDescription = response.extract().jsonPath().get("data.addOnsMenuMap." + addOnsMenuMapKey + ".addOnsDescription");
                softAssert.assertTrue(CinemaUtils.isStringNotNullOrEmpty(addOnsDescription), "addOnsDescription is null or empty");
                /* Commented as addOnsImageUrl might be empty in staging.
                String addOnsImageUrl = response.extract().jsonPath().get("data.addOnsMenuMap." + addOnsMenuMapKey + ".addOnsImageUrl");
                softAssert.assertTrue(CinemaUtils.isStringNotNullOrEmpty(addOnsImageUrl), "addOnsImageUrl is null or empty");
                 */
                String addOnsCategory = response.extract().jsonPath().get("data.addOnsMenuMap." + addOnsMenuMapKey + ".addOnsCategory");
                softAssert.assertTrue(CinemaUtils.isStringNotNullOrEmpty(addOnsCategory), "addOnsCategory is null or empty");
                String maximumPurchaseQuantity = response.extract().jsonPath().get("data.addOnsMenuMap." + addOnsMenuMapKey + ".maximumPurchaseQuantity");
                softAssert.assertTrue(CinemaUtils.isStringNotNullOrEmpty(maximumPurchaseQuantity) && CinemaUtils.stringHasIntegerValue(maximumPurchaseQuantity), "maximumPurchaseQuantity is null or empty or is not Integer");
                String originalPriceCurrencyValueCurrency = response.extract().jsonPath().get("data.addOnsMenuMap." + addOnsMenuMapKey + ".originalPrice.currencyValue.currency");
                softAssert.assertTrue(CinemaUtils.isStringNotNullOrEmpty(originalPriceCurrencyValueCurrency), ".originalPrice.currencyValue.currency is null or empty");
                String originalPriceCurrencyValueAmount = response.extract().jsonPath().get("data.addOnsMenuMap." + addOnsMenuMapKey + ".originalPrice.currencyValue.amount");
                softAssert.assertTrue(CinemaUtils.isStringNotNullOrEmpty(originalPriceCurrencyValueAmount), ".originalPrice.currencyValue.amount is null or empty");
                boolean originalPriceCurrencyValueNullOrEmpty = response.extract().jsonPath().get("data.addOnsMenuMap." + addOnsMenuMapKey + ".originalPrice.currencyValue.nullOrEmpty");
                softAssert.assertTrue(originalPriceCurrencyValueNullOrEmpty == true || originalPriceCurrencyValueNullOrEmpty == false, ".originalPrice.currencyValue.nullOrEmpty is null or empty");
                boolean isAvailable = response.extract().jsonPath().get("data.addOnsMenuMap." + addOnsMenuMapKey + ".isAvailable");
                softAssert.assertTrue(isAvailable == true || isAvailable == false);
            }
            softAssert.assertAll();

        } else if (scheduleType.equalsIgnoreCase("invalid")) {
            response.assertThat().statusCode(500);
            Assert.assertTrue(response.extract().jsonPath().get("userErrorMessage").toString().contains("Unexpected server error occurred."), " Error message is: " + response.extract().jsonPath().get("errorMessage"));
        }
    }

    @Test(description = "Verify nullability rule of request body as per the API contract", dataProviderClass = CinemaDP.class, dataProvider = "getMovieScheduleAndCurrencyForAddons", groups = {"sanity", "regression"}, priority = 2)
    public void verifyNullabilityRule(String movieId, String theatreId, Map<String, String> date, String showTimeId, String currency) {
        response = cinemaAPIFrameworkInstance.sendRequest("searchAddons", null, new HashMap<String, String>() {{
                    put("movieId", movieId);
                    put("theatreId", theatreId);
                    putAll(date);
                    put("showTimeId", showTimeId);
                    if (currency != null) {
                        put("currency", currency);
                    }
                }},
                null,
                null);
        response.assertThat().statusCode(200);
        softAssert.assertTrue(!response.extract().jsonPath().getMap("data.addOnsMenuMap").isEmpty(), "addOnsMenuMap map is empty for movieId " + movieId + ", theatreId " + theatreId + " showTimeId " + showTimeId);
        softAssert.assertTrue(response.extract().jsonPath().getList("data.addOnsCategories") != null, "addOnsCategories is not present under data for movieId " + movieId + ", theatreId " + theatreId);
        softAssert.assertAll();
    }

    @Test(description = "API should work with all supported locale", dataProviderClass = CinemaDP.class, dataProvider = "getLocaleValidMovieScheduleAndCurrencyForAddons", groups = {"sanity", "regression"}, priority = 3)
    public void testAllSupportedLocale(String locale, String movieId, String theatreId, Map<String, String> date, String showTimeId, String currency) {
        cinemaAPIFrameworkInstance.setLocale(locale);
        response = cinemaAPIFrameworkInstance.sendRequest("searchAddons", null, new HashMap<String, String>() {{
            put("movieId", movieId);
            put("theatreId", theatreId);
            putAll(date);
            put("showTimeId", showTimeId);
            put("currency", currency);
        }}, null, null);
        response.assertThat().statusCode(200);
        softAssert.assertTrue(!response.extract().jsonPath().getMap("data.addOnsMenuMap").isEmpty(), "addOnsMenuMap map is empty for movieId " + movieId + ", theatreId " + theatreId + " showTimeId " + showTimeId);
        softAssert.assertTrue(response.extract().jsonPath().getList("data.addOnsCategories") != null, "addOnsCategories is not present under data for movieId " + movieId + ", theatreId " + theatreId);
        softAssert.assertAll();
    }
/*
    @Test(description = "Schema Validation", groups = {"sanity", "regression"}, dataProviderClass = CinemaDP.class, dataProvider = "getAValidMovieScheduleAndCurrencyForAddons", priority = 4)
    public void testSchemaValidation(String movieId, String theatreId, Map<String, String> date, String showTimeId, String currency) {
        response = cinemaAPIFrameworkInstance.sendRequest("searchAddons", null, new HashMap<String, String>() {{
            put("movieId", movieId);
            put("theatreId", theatreId);
            putAll(date);
            put("showTimeId", showTimeId);
            put("currency", currency);
        }}, null, null);
        Assert.assertTrue(cinemaAPIFrameworkInstance.validateSchema(response));
    }

 */

}

package org.nitincompany.cinema.tests;

import io.restassured.response.ValidatableResponse;
import org.nitincompany.cinema.dp.CinemaDP;
import org.nitincompany.cinema.utils.CinemaUtils;
import org.nitincompany.framework.initializers.APIFramework;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.HashMap;

/**
 * Author: nitinkumar
 * Created Date: 21/01/20
 * Info: Tests of saveToFavourite API
 **/

public class SaveToFavouriteAPITestSuite extends CinemaBase {
    public ValidatableResponse response;
    APIFramework cinemaAPIFrameworkInstance = new APIFramework("cinema");

    @Test(description = "Verify response by passing valid and invalid request data (theatreId)", dataProviderClass = CinemaDP.class, dataProvider = "getValidAndInvalidTheatreId", groups = {"sanity", "regression"}, priority = 1)
    public void verifyResponseForValidAndInvalidTheatreId(String theatreId, String theatreType) {
        response = cinemaAPIFrameworkInstance.sendRequest("saveToFavourite", null, new HashMap<String, String>() {{
            put("theatreId", theatreId);
        }}, null, null);
        response.assertThat().statusCode(200);

        if (theatreType.equalsIgnoreCase("valid")) {
            Assert.assertFalse(CinemaUtils.isStringNotNullOrEmpty(response.extract().jsonPath().get("data.errorMessage")), "Error in saving theatre to user's favourite. Error: " + response.extract().jsonPath().get("data.errorMessage"));
            Assert.assertTrue(theatreId.equals(CinemaUtils.getFavouriteTheatreID()));
        } else if (theatreType.equalsIgnoreCase("invalid")) {
            Assert.assertTrue(CinemaUtils.getFavouriteTheatreID() == null);
            //Below assert fails due to CNM-1253
            Assert.assertTrue(CinemaUtils.isStringNotNullOrEmpty(response.extract().jsonPath().get("data.errorMessage")), "No error in saving invalid theatre to user's favourite. Error: " + response.extract().jsonPath().get("data.errorMessage"));
        }
    }

    @Test(description = "Verify nullability rule of request body as per the API contract", dataProviderClass = CinemaDP.class, dataProvider = "getTheatreIDAndTrackingRequest", groups = {"sanity", "regression"}, priority = 2)
    public void verifyNullabilityRule(String theatreId, HashMap trackingRequestMap) {
        response = cinemaAPIFrameworkInstance.sendRequest("saveToFavourite", null, new HashMap<String, String>() {{
            put("theatreId", theatreId);
            if (trackingRequestMap != null) {
                putAll(trackingRequestMap);
            }
        }}, null, null);
        response.assertThat().statusCode(200);
        Assert.assertFalse(CinemaUtils.isStringNotNullOrEmpty(response.extract().jsonPath().get("data.errorMessage")), "Error in saving theatre to user's favourite. Error: " + response.extract().jsonPath().get("data.errorMessage"));
        //two cases will start failing once CNM-1253 is fixed
    }

    @Test(description = "API should work with all supported locale", dataProviderClass = CinemaDP.class, dataProvider = "getLocaleAndTheatreId", groups = {"sanity", "regression"}, priority = 3)
    public void testAllSupportedLocale(String locale, String theatreId) {
        cinemaAPIFrameworkInstance.setLocale(locale);
        response = cinemaAPIFrameworkInstance.sendRequest("saveToFavourite", null, new HashMap<String, String>() {{
            put("theatreId", theatreId);
        }}, null, null);
        response.assertThat().statusCode(200);
        Assert.assertFalse(CinemaUtils.isStringNotNullOrEmpty(response.extract().jsonPath().get("data.errorMessage")), "Error in saving theatre to user's favourite. Error: " + response.extract().jsonPath().get("data.errorMessage"));
    }
/*
    @Test(description = "Schema Validation", groups = {"sanity", "regression"}, dataProviderClass = CinemaDP.class, dataProvider = "getValidTheatreId", priority = 4)
    public void testSchemaValidation(String theatreId) {
        response = cinemaAPIFrameworkInstance.sendRequest("saveToFavourite", null, new HashMap<String, String>() {{
            put("theatreId", theatreId);
        }}, null, null);
        response.assertThat().statusCode(200);
        Assert.assertTrue(cinemaAPIFrameworkInstance.validateSchema(response));
    }

 */

}

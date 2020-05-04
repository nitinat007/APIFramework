package org.nitincompany.cinema.tests;

import io.restassured.response.ValidatableResponse;
import org.nitincompany.cinema.dp.CinemaDP;
import org.nitincompany.cinema.utils.CinemaUtils;
import org.nitincompany.framework.initializers.APIFramework;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Author: nitinkumar
 * Created Date: 22/01/20
 * Info: TestSuite of unfavourite API
 **/

public class UnFavouriteAPITestSuite extends CinemaBase {
    public ValidatableResponse response;
    APIFramework cinemaAPIFrameworkInstance = new APIFramework("cinema");

    @Test(description = "Verify response", groups = {"sanity", "regression"}, priority = 1)
    public void verifyResponseOfUnFavouriteAPI() {
        response = cinemaAPIFrameworkInstance.sendRequest("unfavourite", null, null, null, null);
        response.assertThat().statusCode(200);
        Assert.assertFalse(CinemaUtils.isStringNotNullOrEmpty(response.extract().jsonPath().get("data.error")), "Error in resetting favourite theatre for user. Error: " + response.extract().jsonPath().get("data.error"));
        Assert.assertTrue(CinemaUtils.getFavouriteTheatreID() == null);
    }

    @Test(description = "API should work with all supported locale", dataProviderClass = CinemaDP.class, dataProvider = "getLocalesDP", groups = {"sanity", "regression"}, priority = 2)
    public void testAllSupportedLocale(String locale) {
        cinemaAPIFrameworkInstance.setLocale(locale);
        response = cinemaAPIFrameworkInstance.sendRequest("unfavourite", null, null, null, null);
        response.assertThat().statusCode(200);
        Assert.assertFalse(CinemaUtils.isStringNotNullOrEmpty(response.extract().jsonPath().get("data.error")), "Error in resetting favourite theatre for user. Error: " + response.extract().jsonPath().get("data.error"));
    }
/*
    @Test(description = "Schema Validation", groups = {"sanity", "regression"}, priority = 3)
    public void testSchemaValidation() {
        response = cinemaAPIFrameworkInstance.sendRequest("unfavourite", null, null, null, null);
        response.assertThat().statusCode(200);
        Assert.assertTrue(cinemaAPIFrameworkInstance.validateSchema(response));
    }

 */
}

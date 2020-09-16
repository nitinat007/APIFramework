package com.progathon.cinema.tests;

import com.progathon.cinema.dp.CinemaDP;
import com.progathon.cinema.utils.CinemaUtils;
import io.restassured.response.ValidatableResponse;
import com.progathon.framework.initializers.APIFramework;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Author: nitinkumar
 * Created Date: 17/04/20
 * Info: Tests of searchTheatresInfo API. This API is used in Desktop web client
 **/

public class SearchTheatresInfoAPITestSuite extends CinemaBase {
    public ValidatableResponse response;
    APIFramework cinemaAPIFrameworkInstance = new APIFramework("cinema");

    @Test(description = "Verify response by passing valid and invalid request data (theatreIds). Also validate nullability rule in response body.", dataProviderClass = CinemaDP.class, dataProvider = "getValidAndInvalidListsOfTheatreIdDP", groups = {"sanity", "regression"}, priority = 1)
    public void testDefaultCity(ArrayList<String> theatreIds, String testCaseType) {
        response = cinemaAPIFrameworkInstance.sendRequest("searchTheatresInfo", null, new HashMap<String, String>() {{
            put("theatreIds", String.valueOf(theatreIds));
        }}, null, null);
        response.assertThat().statusCode(200);
        List theatresListInResponse = response.extract().jsonPath().getList("data.theatres");
        if (testCaseType.equalsIgnoreCase("allTheatreIdsValidInList")) {
            softAssert.assertTrue(theatresListInResponse.size() == theatreIds.size(), " Expected number of theatres in response is " + theatreIds.size() + " but found " + theatresListInResponse.size());
        } else if (testCaseType.equalsIgnoreCase("oneTheatreIdInvalidInList")) {
            softAssert.assertTrue(theatresListInResponse.size() == (theatreIds.size() - 1), " Expected number of theatres in response is " + (theatreIds.size() - 1) + " but found " + theatresListInResponse.size());
        } else if (testCaseType.equalsIgnoreCase("valueInvalidInList")) {
            softAssert.assertTrue(theatresListInResponse.size() == 0, " Expected number of theatres in response is 0 but found " + theatresListInResponse.size());
        }
        for (int theatrePosition = 0; theatrePosition < theatresListInResponse.size(); theatrePosition++) {
            long theatreId = response.extract().jsonPath().getLong("data.theatres[" + theatrePosition + "].theatreId");
            softAssert.assertTrue(CinemaUtils.isStringNotNullOrEmpty(Long.toString(theatreId)), "theatreId is null or empty");
            String name = response.extract().jsonPath().getString("data.theatres[" + theatrePosition + "].name");
            softAssert.assertTrue(CinemaUtils.isStringNotNullOrEmpty(name), "theatre name is null or empty");
            String venueLocation = response.extract().jsonPath().getString("data.theatres[" + theatrePosition + "].venueLocation");
            softAssert.assertTrue(CinemaUtils.isStringNotNullOrEmpty(venueLocation), "theatre venueLocation is null or empty");
            String providerLogoUrl = response.extract().jsonPath().getString("data.theatres[" + theatrePosition + "].providerLogoUrl");
            softAssert.assertTrue(providerLogoUrl != null, "theatre providerLogoUrl is null");
            String address = response.extract().jsonPath().getString("data.theatres[" + theatrePosition + "].address");
            softAssert.assertTrue(CinemaUtils.isStringNotNullOrEmpty(address), "theatre address is null or empty");
            String phoneNumber = response.extract().jsonPath().getString("data.theatres[" + theatrePosition + "].phoneNumber");
            softAssert.assertTrue(phoneNumber != null, "theatre phoneNumber is null");
        }
        softAssert.assertAll();
    }

    @Test(description = "API should work with all supported locale", dataProviderClass = CinemaDP.class, dataProvider = "getValidListsOfTheatreIdAndLocaleDP", groups = {"sanity", "regression"}, priority = 2)
    public void testAllSupportedLocale(ArrayList<String> theatreIds, String locale) {
        cinemaAPIFrameworkInstance.setLocale(locale);
        response = cinemaAPIFrameworkInstance.sendRequest("searchTheatresInfo", null, new HashMap<String, String>() {{
            put("theatreIds", String.valueOf(theatreIds));
        }}, null, null);
        response.assertThat().statusCode(200);
        Assert.assertTrue(response.extract().jsonPath().getList("data.theatres").size() != 0, "Number of theatres for locale " + locale + " is zero");
    }
/*
    @Test(description = "Schema Validation", dataProviderClass = CinemaDP.class, dataProvider = "getAValidListsOfTheatreIdDP", groups = {"sanity", "regression"}, priority = 3)
    public void testSchemaValidation(ArrayList<String> theatreIds) {
        response = cinemaAPIFrameworkInstance.sendRequest("searchTheatresInfo", null, new HashMap<String, String>() {{
            put("theatreIds", String.valueOf(theatreIds));
        }}, null, null);
        response.assertThat().statusCode(200);
        Assert.assertTrue(cinemaAPIFrameworkInstance.validateSchema(response));
    }

 */


}

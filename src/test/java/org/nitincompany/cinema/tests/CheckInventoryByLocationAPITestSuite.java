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
 * Created Date: 22/01/20
 * Info: Tests of checkInventoryByLocation API
 **/

public class CheckInventoryByLocationAPITestSuite extends CinemaBase {
    public ValidatableResponse response;
    APIFramework cinemaAPIFrameworkInstance = new APIFramework("cinema");

    @Test(description = "Verify response with serviceable and unserviceable lat, lon. Also validate nullability rule in response body", dataProviderClass = CinemaDP.class, dataProvider = "getValidAndInvalidLatLong", groups = {"sanity", "regression"}, priority = 1)
    public void verifyResponseForServiceableAndUnserviceableRegion(String lat, String lon, String latLngType) {
        response = cinemaAPIFrameworkInstance.sendRequest("checkInventoryByLocation", null, new HashMap<String, String>() {{
            put("lat", lat);
            put("lon", lon);
        }}, null, null);
        response.assertThat().statusCode(200);
        if (latLngType.equalsIgnoreCase("validInventoryAndCountryExpected")) {
            Assert.assertTrue(response.extract().jsonPath().get("data.hasInventory"), " No inventory for the given lat lng. hasInventory: " + response.extract().jsonPath().get("data.hasInventory"));
            Assert.assertTrue(CinemaUtils.isStringNotNullOrEmpty(response.extract().jsonPath().getString("data.countryId")) && response.extract().jsonPath().getString("data.countryId").length() == 2, "countryId is not correct for valid lat lng. Received: " + response.extract().jsonPath().getString("data.countryId"));
        } else if (latLngType.equalsIgnoreCase("invalidInventoryAndCountryExpected")) {
            Assert.assertTrue(response.extract().jsonPath().getBoolean("data.hasInventory") == false, " No inventory for the given lat lng. hasInventory: " + response.extract().jsonPath().get("data.hasInventory"));
        }
    }

    @Test(description = "API should work with all supported locale", dataProviderClass = CinemaDP.class, dataProvider = "getLocalesAndAValidLatLonDP", groups = {"sanity", "regression"}, priority = 2)
    public void testAllSupportedLocale(String locale, String lat, String lon) {
        cinemaAPIFrameworkInstance.setLocale(locale);
        response = cinemaAPIFrameworkInstance.sendRequest("checkInventoryByLocation", null, new HashMap<String, String>() {{
            put("lat", lat);
            put("lon", lon);
        }}, null, null);
        response.assertThat().statusCode(200);
        Assert.assertTrue(response.extract().jsonPath().get("data.hasInventory"), " No inventory for the given lat lng. hasInventory: " + response.extract().jsonPath().get("data.hasInventory"));
        Assert.assertTrue(CinemaUtils.isStringNotNullOrEmpty(response.extract().jsonPath().getString("data.countryId")) && response.extract().jsonPath().getString("data.countryId").length() == 2, "countryId is not correct for valid lat lng. Received: " + response.extract().jsonPath().getString("data.countryId"));
    }
/*
    @Test(description = "Schema Validation", dataProviderClass = CinemaDP.class, dataProvider = "getAValidLatLonDP", groups = {"sanity", "regression"}, priority = 3)
    public void testSchemaValidation(String lat, String lon) {
        response = cinemaAPIFrameworkInstance.sendRequest("checkInventoryByLocation", null, new HashMap<String, String>() {{
            put("lat", lat);
            put("lon", lon);
        }}, null, null);
        response.assertThat().statusCode(200);
        Assert.assertTrue(cinemaAPIFrameworkInstance.validateSchema(response));
    }

 */
}

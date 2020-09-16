package com.progathon.cinema.tests;

import com.progathon.cinema.dp.CinemaDP;
import com.progathon.cinema.utils.CinemaUtils;
import io.restassured.response.ValidatableResponse;
import com.progathon.framework.initializers.APIFramework;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.HashMap;

/**
 * Author: nitinkumar
 * Created Date: 16/01/20
 * Info: Tests of cancelBooking API
 **/

public class CancelBookingAPITestSuite extends CinemaBase {
    public ValidatableResponse response;
    APIFramework cinemaAPIFrameworkInstance = new APIFramework("cinema");

    @Test(description = "Verify response by passing valid and invalid request data (bookingId, invoiceId, auth ). Also validate nullability rule in response body.", dataProviderClass = CinemaDP.class, dataProvider = "getValidAndInvalidBookingInformation", groups = {"regression"}, priority = 1)
    public void verifyResponseOfValidAndInvalidBookingCancellation(String invoiceId, String bookingId, String auth, HashMap trackingRequestMap, String bookingType) {

        response = cinemaAPIFrameworkInstance.sendRequest("cancelBooking", null, new HashMap<String, String>() {{
            put("invoiceId", invoiceId);
            put("bookingId", bookingId);
            put("auth", auth);
        }}, null, null);

        if (bookingType.equalsIgnoreCase("valid")) {
            response.assertThat().statusCode(200);
            softAssert.assertFalse(CinemaUtils.isStringNotNullOrEmpty(response.extract().jsonPath().get("data.errorMessage")));
            boolean success = response.extract().jsonPath().get("data.success");
            softAssert.assertTrue(success);
            softAssert.assertAll();
        } else if (bookingType.equalsIgnoreCase("invalid")) {
            response.assertThat().statusCode(200);
            Assert.assertFalse(response.extract().jsonPath().get("data.success"));
            Assert.assertTrue(response.extract().jsonPath().get("data.errorMessage").toString().equalsIgnoreCase("Pesanan tidak dapat diproses saat ini. Silakan coba nanti."), " Error message is: " + response.extract().jsonPath().get("data.errorMessage"));
        }

    }

    @Test(description = "API should work with all supported locale", dataProviderClass = CinemaDP.class, dataProvider = "getLocaleValidBookingInfo", groups = {"regression"}, priority = 2)
    public void testAllSupportedLocale(String locale, String invoiceId, String bookingId, String auth, HashMap trackingRequestMap) {

        cinemaAPIFrameworkInstance.setLocale(locale);
        response = cinemaAPIFrameworkInstance.sendRequest("cancelBooking", null, new HashMap<String, String>() {{
            put("invoiceId", invoiceId);
            put("bookingId", bookingId);
            put("auth", auth);
        }}, null, null);

        response.assertThat().statusCode(200);
        softAssert.assertFalse(CinemaUtils.isStringNotNullOrEmpty(response.extract().jsonPath().get("data.errorMessage")));
        boolean success = response.extract().jsonPath().get("data.success");
        softAssert.assertTrue(success);
        softAssert.assertAll();
    }
/*
    @Test(description = "Schema Validation", dataProviderClass = CinemaDP.class, dataProvider = "getAValidBookingInformation", groups = {"regression"}, priority = 3)
    public void testSchemaValidation(String invoiceId, String bookingId, String auth, HashMap trackingRequestMap) {

        response = cinemaAPIFrameworkInstance.sendRequest("cancelBooking", null, new HashMap<String, String>() {{
            put("invoiceId", invoiceId);
            put("bookingId", bookingId);
            put("auth", auth);
        }}, null, null);

        response.assertThat().statusCode(200);
        Assert.assertTrue(cinemaAPIFrameworkInstance.validateSchema(response));
    }

 */

}

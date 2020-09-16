package com.progathon.cinema.tests;

import com.progathon.cinema.dp.CinemaDP;
import com.progathon.cinema.utils.CinemaUtils;
import io.restassured.response.ValidatableResponse;
import com.progathon.framework.initializers.APIFramework;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Author: nitinkumar
 * Created Date: 16/01/20
 * Info: Tests of getBookingReview api
 **/

public class getBookingReviewAPITestSuite extends CinemaBase {
    public ValidatableResponse response;
    APIFramework cinemaAPIFrameworkInstance = new APIFramework("cinema");
    ArrayList<String[]> allBookingsDoneByDPs = new ArrayList<>();

    @Test(description = "Verify response by passing valid and invalid request data (invoiceId, bookingId, auth, trackingRequest).", dataProviderClass = CinemaDP.class, dataProvider = "getValidAndInvalidBookingInformation", groups = {"regression"}, priority = 1)
    public void verifyResponseOfValidAndInvalidSeatBookingReview(String invoiceId, String bookingId, String auth, HashMap trackingRequestMap, String bookingType) {

        response = cinemaAPIFrameworkInstance.sendRequest("getBookingReview", null, new HashMap<String, String>() {{
            put("invoiceId", invoiceId);
            put("bookingId", bookingId);
            put("auth", auth);
            putAll(trackingRequestMap);
        }}, null, null);

        if (bookingType.equalsIgnoreCase("valid")) {
            response.assertThat().statusCode(200);
            allBookingsDoneByDPs.add(new String[]{bookingId, invoiceId, auth});
            softAssert.assertTrue(CinemaUtils.isStringNotNullOrEmpty(response.extract().jsonPath().get("data.movieTitle")));
            softAssert.assertTrue(CinemaUtils.isStringNotNullOrEmpty(response.extract().jsonPath().get("data.posterImageUrl")));
            softAssert.assertTrue(CinemaUtils.isStringNotNullOrEmpty(response.extract().jsonPath().get("data.theatreName")));
            softAssert.assertTrue(CinemaUtils.isStringNotNullOrEmpty(response.extract().jsonPath().get("data.auditoriumFormatName")));
            softAssert.assertTrue(CinemaUtils.isStringNotNullOrEmpty(response.extract().jsonPath().get("data.showDate.month")));
            softAssert.assertTrue(CinemaUtils.isStringNotNullOrEmpty(response.extract().jsonPath().get("data.showDate.day")));
            softAssert.assertTrue(CinemaUtils.isStringNotNullOrEmpty(response.extract().jsonPath().get("data.showDate.year")));
            softAssert.assertTrue(CinemaUtils.isStringNotNullOrEmpty(response.extract().jsonPath().get("data.showTime")));
            softAssert.assertTrue(CinemaUtils.isStringNotNullOrEmpty(response.extract().jsonPath().get("data.seatNumbers")));
            softAssert.assertTrue(CinemaUtils.isStringNotNullOrEmpty(response.extract().jsonPath().get("data.numOfSeats")));
            HashMap totalPrice = response.extract().jsonPath().get("data.totalPrice.currencyValue");
            softAssert.assertTrue(CinemaUtils.isStringNotNullOrEmpty((String) totalPrice.get("amount")));
            softAssert.assertTrue(CinemaUtils.isStringNotNullOrEmpty((String) totalPrice.get("currency")));
            softAssert.assertTrue(CinemaUtils.isBoolean((boolean) totalPrice.get("nullOrEmpty")));
            HashMap totalTicketPrice = response.extract().jsonPath().get("data.totalTicketPrice.currencyValue");
            softAssert.assertTrue(CinemaUtils.isStringNotNullOrEmpty((String) totalTicketPrice.get("amount")));
            softAssert.assertTrue(CinemaUtils.isStringNotNullOrEmpty((String) totalTicketPrice.get("currency")));
            softAssert.assertTrue(CinemaUtils.isBoolean((boolean) totalTicketPrice.get("nullOrEmpty")));
            HashMap totalAddonPrice = response.extract().jsonPath().get("data.totalAddonPrice.currencyValue");
            softAssert.assertTrue(CinemaUtils.isStringNotNullOrEmpty((String) totalAddonPrice.get("amount")));
            softAssert.assertTrue(CinemaUtils.isStringNotNullOrEmpty((String) totalAddonPrice.get("currency")));
            softAssert.assertTrue(CinemaUtils.isBoolean((boolean) totalAddonPrice.get("nullOrEmpty")));
            softAssert.assertTrue(CinemaUtils.isStringNotNullOrEmpty(response.extract().jsonPath().get("data.totalAddonPrice.numOfDecimalPoint")));
            softAssert.assertTrue(CinemaUtils.isBoolean(response.extract().jsonPath().get("data.isPresale")));

            ArrayList<Object> addonsBookingList = response.extract().jsonPath().get("data.addonsBookingList");
            System.out.println(addonsBookingList);
            for (int addonIndex = 0; addonIndex < addonsBookingList.size(); addonIndex++) {
                HashMap addonsBooking = (HashMap) response.extract().jsonPath().getMap("data.addonsBookingList[" + addonIndex + "]");
                softAssert.assertTrue(CinemaUtils.isStringNotNullOrEmpty((String) addonsBooking.get("addonsId")));
                softAssert.assertTrue(CinemaUtils.isStringNotNullOrEmpty((String) addonsBooking.get("addonsName")));
                softAssert.assertTrue(CinemaUtils.isStringNotNullOrEmpty((String) addonsBooking.get("quantity")));
                HashMap currencyValue = (HashMap) response.extract().jsonPath().getMap("data.addonsBookingList[" + addonIndex + "].itemPrice.currencyValue");
                softAssert.assertTrue(CinemaUtils.isStringNotNullOrEmpty((String) currencyValue.get("currency")));
                softAssert.assertTrue(CinemaUtils.isStringNotNullOrEmpty((String) currencyValue.get("amount")));
                softAssert.assertTrue(CinemaUtils.isBoolean((boolean) currencyValue.get("nullOrEmpty")));
            }
            softAssert.assertAll();
        } else if (bookingType.equalsIgnoreCase("invalid")) {
            response.assertThat().statusCode(500); //reported CNM-1297
            Assert.assertTrue(response.extract().jsonPath().get("errorType").toString().equalsIgnoreCase("SERVER_ERROR"), " Error message is: " + response.extract().jsonPath().get("errorMessage"));
        }

    }


    @Test(description = "Verify nullability rule of request body as per the API contract", dataProviderClass = CinemaDP.class, dataProvider = "getValidBookingInformationWithAndWithoutTrackingRequest", groups = {"regression"}, priority = 2)
    public void verifyNullabilityRule(String invoiceId, String bookingId, String auth, HashMap trackingRequestMap) {

        response = cinemaAPIFrameworkInstance.sendRequest("getBookingReview", null, new HashMap<String, String>() {{
            put("invoiceId", invoiceId);
            put("bookingId", bookingId);
            put("auth", auth);
            if (trackingRequestMap != null)
                putAll(trackingRequestMap);
        }}, null, null);

        response.assertThat().statusCode(200);
        allBookingsDoneByDPs.add(new String[]{bookingId, invoiceId, auth});
        Assert.assertTrue(CinemaUtils.isStringNotNullOrEmpty(response.extract().jsonPath().get("data.numOfSeats")), "numOfSeats is null or empty. Actual result: " + response.extract().jsonPath().get("data.numOfSeats"));
    }

    @Test(description = "API should work with all supported locale", dataProviderClass = CinemaDP.class, dataProvider = "getLocaleValidBookingInfo", groups = {"regression"}, priority = 3)
    public void testAllSupportedLocale(String locale, String invoiceId, String bookingId, String auth, HashMap trackingRequestMap) {

        cinemaAPIFrameworkInstance.setLocale(locale);
        response = cinemaAPIFrameworkInstance.sendRequest("getBookingReview", null, new HashMap<String, String>() {{
            put("invoiceId", invoiceId);
            put("bookingId", bookingId);
            put("auth", auth);
            putAll(trackingRequestMap);
        }}, null, null);

        response.assertThat().statusCode(200);
        allBookingsDoneByDPs.add(new String[]{bookingId, invoiceId, auth});
        Assert.assertTrue(CinemaUtils.isStringNotNullOrEmpty(response.extract().jsonPath().get("data.numOfSeats")), "numOfSeats is null or empty. Actual result: " + response.extract().jsonPath().get("data.numOfSeats"));
    }

/*
    @Test(description = "Schema Validation", dataProviderClass = CinemaDP.class, dataProvider = "getAValidBookingInformation", groups = {"regression"}, priority = 4)
    public void testSchemaValidation(String invoiceId, String bookingId, String auth, HashMap trackingRequestMap) {

        response = cinemaAPIFrameworkInstance.sendRequest("getBookingReview", null, new HashMap<String, String>() {{
            put("invoiceId", invoiceId);
            put("bookingId", bookingId);
            put("auth", auth);
            putAll(trackingRequestMap);
        }}, null, null);

        response.assertThat().statusCode(200);
        allBookingsDoneByDPs.add(new String[]{bookingId, invoiceId, auth});
        Assert.assertTrue(cinemaAPIFrameworkInstance.validateSchema(response));
    }

 */

    @AfterClass(description = "This method releases/cancels all booking done by the test Suite")
    public void cancelAllBookings() {
        for (String[] bookingInfo : allBookingsDoneByDPs) {
            CinemaUtils.cancelBooking(bookingInfo);
        }
    }

}


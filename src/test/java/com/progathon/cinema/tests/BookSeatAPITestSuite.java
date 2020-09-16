package com.progathon.cinema.tests;

import com.progathon.cinema.dp.CinemaDP;
import com.progathon.cinema.utils.CinemaUtils;
import com.progathon.cinema.utils.requestFields.CinemaBookSeatRequestFields;
import io.restassured.response.ValidatableResponse;
import com.progathon.cinema.dp.CinemaDP;
import com.progathon.cinema.utils.CinemaUtils;
import com.progathon.cinema.utils.requestFields.CinemaBookSeatRequestFields;
import com.progathon.framework.initializers.APIFramework;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Author: nitinkumar
 * Created Date: 06/01/20
 * Info: Tests of bookSeat API
 **/

public class BookSeatAPITestSuite extends CinemaBase {
    public ValidatableResponse response;
    APIFramework cinemaAPIFrameworkInstance = new APIFramework("cinema");
    ArrayList<String[]> allBookingsDoneInThisSuite = new ArrayList<>();

    @Test(description = "Verify response by passing valid and invalid request data (movieId, theatreId, date, showTime, showTimeId, currency, addOnsBookingList, auditoriumNumber, auditoriumId, bookedSeats). Also validate nullability rule in response body.", dataProviderClass = CinemaDP.class, dataProvider = "getValidAndInvalidBookingRequestFieldsWithCurrencyForEachCountry", groups = {"regression"}, priority = 1)
    public void verifyResponseOfValidAndInvalidSeatBooking(ArrayList<CinemaBookSeatRequestFields.Seat> bookedSeatList, ArrayList<CinemaBookSeatRequestFields.AddOn> addOnsBookingList, String movieId, String theatreId, Map<String, String> date, String showTimeId, String currency, String showTime, String auditoriumNumber, String auditoriumId, String bookingType) {

        response = cinemaAPIFrameworkInstance.sendRequest("bookSeat", null, new HashMap<String, String>() {{
            if (bookedSeatList.size() > 0) {
                put("bookedSeats", String.valueOf(bookedSeatList));
            }
            if (addOnsBookingList.size() > 0) {
                put("addOnsBookingList", String.valueOf(addOnsBookingList));
            }
            put("movieId", movieId);
            put("theatreId", theatreId);
            putAll(date);
            put("showTimeId", showTimeId);
            put("currency", currency);
            put("showTime", showTime);
            put("auditoriumNumber", auditoriumNumber);
            put("auditoriumId", auditoriumId);
        }}, null, null);

        if (bookingType.equalsIgnoreCase("valid")) {
            response.assertThat().statusCode(200);
            Assert.assertTrue(response.extract().jsonPath().get("data.bookingResultStatus").equals("SUCCESS"), "bookingResultStatus is not SUCCESS. Actual result: " + response.extract().jsonPath().get("data.bookingResultStatus"));
            String bookingId = response.extract().jsonPath().get("data.bookingInfo.bookingId");
            String invoiceId = response.extract().jsonPath().get("data.bookingInfo.invoiceId");
            String auth = response.extract().jsonPath().get("data.bookingInfo.auth");
            softAssert.assertTrue(bookingId != null && bookingId.length() > 0, "bookingId is " + bookingId);
            softAssert.assertTrue(invoiceId != null && invoiceId.length() > 0, "invoiceId is " + invoiceId);
            softAssert.assertTrue(auth != null && auth.length() > 0, "auth is " + auth);
            allBookingsDoneInThisSuite.add(new String[]{bookingId, invoiceId, auth});
            softAssert.assertAll();
        } else if (bookingType.equalsIgnoreCase("invalid")) {
            response.assertThat().statusCode(400);
            Assert.assertTrue(response.extract().jsonPath().get("errorMessage").toString().contains("Invalid field: data.bookedSeats"), " Error message is: " + response.extract().jsonPath().get("errorMessage"));
        } else if (bookingType.equalsIgnoreCase("validSeatInvalidAddon")) {
            response.assertThat().statusCode(200);
            Assert.assertTrue(response.extract().jsonPath().get("data.bookingResultStatus").equals("TICKET_SUCCESS_ADDON_FAILED"), "Received bookingResultStatus is " + response.extract().jsonPath().get("data.bookingResultStatus") + ". Expected is TICKET_SUCCESS_ADDON_FAILED .");
        }

    }

    @Test(description = "Hitting the same request 2nd time should give message 'Seat already taken'. Hitting the API with already booked seats should return the same response.", dataProviderClass = CinemaDP.class, dataProvider = "getAValidBookingRequestFieldsWithCurrency", groups = {"regression"}, priority = 2)
    public void verifyNotAbleToBookAlreadyBookedSeat(ArrayList<CinemaBookSeatRequestFields.Seat> bookedSeatList, ArrayList<CinemaBookSeatRequestFields.AddOn> addOnsBookingList, String movieId, String theatreId, Map<String, String> date, String showTimeId, String currency, String showTime, String auditoriumNumber, String auditoriumId) {

        for (int apiHitCount = 0; apiHitCount < 2; apiHitCount++) {
            response = cinemaAPIFrameworkInstance.sendRequest("bookSeat", null, new HashMap<String, String>() {{
                if (bookedSeatList.size() > 0) {
                    put("bookedSeats", String.valueOf(bookedSeatList));
                }
                if (addOnsBookingList.size() > 0) {
                    put("addOnsBookingList", String.valueOf(addOnsBookingList));
                }
                put("movieId", movieId);
                put("theatreId", theatreId);
                putAll(date);
                put("showTimeId", showTimeId);
                put("currency", currency);
                put("showTime", showTime);
                put("auditoriumNumber", auditoriumNumber);
                put("auditoriumId", auditoriumId);
            }}, null, null);
        }

        response.assertThat().statusCode(200);
        Assert.assertTrue(response.extract().jsonPath().get("data.bookingResultStatus").equals("FAILED"), "bookingResultStatus is not FAILED. Actual result: " + response.extract().jsonPath().get("data.bookingResultStatus"));
        String bookingInfo = response.extract().jsonPath().get("data.bookingInfo");
        String errorMessage = response.extract().jsonPath().get("data.errorMessage");
        softAssert.assertTrue(bookingInfo == null, "bookingInfo is " + bookingInfo);
        softAssert.assertTrue(errorMessage != null && errorMessage.length() > 0, "errorMessage is " + errorMessage);
        softAssert.assertAll();
    }

    @Test(description = "Verify nullability rule of request body as per the API contract", dataProviderClass = CinemaDP.class, dataProvider = "getValidBookingRequestFieldsWithAndWithoutCurrency", groups = {"regression"}, priority = 3)
    public void verifyNullabilityRule(ArrayList<CinemaBookSeatRequestFields.Seat> bookedSeatList, ArrayList<CinemaBookSeatRequestFields.AddOn> addOnsBookingList, String movieId, String theatreId, Map<String, String> date, String showTimeId, String currency, String showTime, String auditoriumNumber, String auditoriumId) {

        response = cinemaAPIFrameworkInstance.sendRequest("bookSeat", null, new HashMap<String, String>() {{
            if (bookedSeatList.size() > 0) {
                put("bookedSeats", String.valueOf(bookedSeatList));
            }
            if (addOnsBookingList.size() > 0) {
                put("addOnsBookingList", String.valueOf(addOnsBookingList));
            }
            put("movieId", movieId);
            put("theatreId", theatreId);
            putAll(date);
            put("showTimeId", showTimeId);
            if (currency != null) {
                put("currency", currency);
            }
            put("showTime", showTime);
            put("auditoriumNumber", auditoriumNumber);
            put("auditoriumId", auditoriumId);
        }}, null, null);

        response.assertThat().statusCode(200);
        String bookingId = response.extract().jsonPath().get("data.bookingInfo.bookingId");
        String invoiceId = response.extract().jsonPath().get("data.bookingInfo.invoiceId");
        String auth = response.extract().jsonPath().get("data.bookingInfo.auth");
        allBookingsDoneInThisSuite.add(new String[]{bookingId, invoiceId, auth});
        Assert.assertTrue(response.extract().jsonPath().get("data.bookingResultStatus").equals("SUCCESS"), "bookingResultStatus is not SUCCESS. Actual result: " + response.extract().jsonPath().get("data.bookingResultStatus"));
    }

    @Test(description = "API should work with all supported locale", dataProviderClass = CinemaDP.class, dataProvider = "getLocaleValidBookingRequestFields", groups = {"regression"}, priority = 4)
    public void testAllSupportedLocale(String locale, ArrayList<CinemaBookSeatRequestFields.Seat> bookedSeatList, ArrayList<CinemaBookSeatRequestFields.AddOn> addOnsBookingList, String movieId, String theatreId, Map<String, String> date, String showTimeId, String currency, String showTime, String auditoriumNumber, String auditoriumId) {

        cinemaAPIFrameworkInstance.setLocale(locale);
        response = cinemaAPIFrameworkInstance.sendRequest("bookSeat", null, new HashMap<String, String>() {{
            if (bookedSeatList.size() > 0) {
                put("bookedSeats", String.valueOf(bookedSeatList));
            }
            if (addOnsBookingList.size() > 0) {
                put("addOnsBookingList", String.valueOf(addOnsBookingList));
            }
            put("movieId", movieId);
            put("theatreId", theatreId);
            if (date != null) {
                putAll(date);
            }
            put("showTimeId", showTimeId);
            if (currency != null) {
                put("currency", currency);
            }
            put("showTime", showTime);
            put("auditoriumNumber", auditoriumNumber);
            put("auditoriumId", auditoriumId);
        }}, null, null);

        response.assertThat().statusCode(200);
        String bookingId = response.extract().jsonPath().get("data.bookingInfo.bookingId");
        String invoiceId = response.extract().jsonPath().get("data.bookingInfo.invoiceId");
        String auth = response.extract().jsonPath().get("data.bookingInfo.auth");
        allBookingsDoneInThisSuite.add(new String[]{bookingId, invoiceId, auth});
        Assert.assertTrue(response.extract().jsonPath().get("data.bookingResultStatus").equals("SUCCESS"), "bookingResultStatus is not SUCCESS. Actual result: " + response.extract().jsonPath().get("data.bookingResultStatus"));

    }
/*
    @Test(description = "Schema Validation", dataProviderClass = CinemaDP.class, dataProvider = "getAValidBookingRequestFieldsWithoutCurrencyOfIndonesia", groups = {"regression"}, priority = 5)
    public void testSchemaValidation(ArrayList<CinemaBookSeatRequestFields.Seat> bookedSeatList, ArrayList<CinemaBookSeatRequestFields.AddOn> addOnsBookingList, String movieId, String theatreId, Map<String, String> date, String showTimeId, String showTime, String auditoriumNumber, String auditoriumId) {

        response = cinemaAPIFrameworkInstance.sendRequest("bookSeat", null, new HashMap<String, String>() {{
            if (bookedSeatList.size() > 0) {
                put("bookedSeats", String.valueOf(bookedSeatList));
            }
            if (addOnsBookingList.size() > 0) {
                put("addOnsBookingList", String.valueOf(addOnsBookingList));
            }
            put("movieId", movieId);
            put("theatreId", theatreId);
            if (date != null) {
                putAll(date);
            }
            put("showTimeId", showTimeId);
            put("showTime", showTime);
            put("auditoriumNumber", auditoriumNumber);
            put("auditoriumId", auditoriumId);
        }}, null, null);

        response.assertThat().statusCode(200);
        String bookingId = response.extract().jsonPath().get("data.bookingInfo.bookingId");
        String invoiceId = response.extract().jsonPath().get("data.bookingInfo.invoiceId");
        String auth = response.extract().jsonPath().get("data.bookingInfo.auth");
        allBookingsDoneInThisSuite.add(new String[]{bookingId, invoiceId, auth});
        Assert.assertTrue(cinemaAPIFrameworkInstance.validateSchema(response));
    }

 */

    @AfterClass(description = "This method releases/cancels all booking done by the test Suite")
    public void cancelAllBookings() {
        for (String[] bookingInfo : allBookingsDoneInThisSuite) {
            CinemaUtils.cancelBooking(bookingInfo);
        }
    }

}

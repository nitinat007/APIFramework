package org.nitincompany.cinema.tests;

import io.restassured.response.ValidatableResponse;
import org.nitincompany.cinema.dp.CinemaDP;
import org.nitincompany.cinema.utils.CinemaUtils;
import org.nitincompany.framework.initializers.APIFramework;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.List;

/**
 * Author: nitinkumar
 * Created Date: 27/01/20
 * Info: Tests of landingPageDiscover API
 **/

public class LandingPageDiscoverAPITestSuite extends CinemaBase {
    public ValidatableResponse response;
    APIFramework cinemaAPIFrameworkInstance = new APIFramework("cinema");

    @Test(description = "Verify response by passing valid and invalid request data (cityId). Also validate nullability rule in response body.", dataProviderClass = CinemaDP.class, dataProvider = "getValidAndInvalidCityIdDP", groups = {"sanity", "regression"}, priority = 1)
    public void verifyResponseOfValidAndInvalidCityId(String cityId, String locale, String cityType) {
        cinemaAPIFrameworkInstance.setLocale(locale); //Logged CNM-1316. Will remove this line once bug is fixed
        response = cinemaAPIFrameworkInstance.sendRequest("landingPageDiscover", null, new HashMap<String, String>() {{
            put("cityId", cityId);
        }}, null, null);
        response.assertThat().statusCode(200);

        if (cityType.equalsIgnoreCase("valid")) {
            List<Object> listOfNowPlayingMovies = response.extract().jsonPath().getList("data.nowPlaying");
            softAssert.assertTrue(listOfNowPlayingMovies.size() != 0, "No nowPlaying movie is available for valid cityId " + cityId);
            for (int movieCount = 0; movieCount < listOfNowPlayingMovies.size(); movieCount++) {
                softAssert.assertTrue(response.extract().jsonPath().get("data.nowPlaying[" + movieCount + "].movieInfo.posterImageUrl") != null);
                softAssert.assertTrue(response.extract().jsonPath().get("data.nowPlaying[" + movieCount + "].movieInfo.id") != null);
                softAssert.assertTrue(response.extract().jsonPath().get("data.nowPlaying[" + movieCount + "].movieInfo.title") != null);
                softAssert.assertTrue(response.extract().jsonPath().get("data.nowPlaying[" + movieCount + "].movieInfo.titleEN") != null);
                softAssert.assertTrue(response.extract().jsonPath().get("data.nowPlaying[" + movieCount + "].movieInfo.rating") != null);
                softAssert.assertTrue(response.extract().jsonPath().get("data.nowPlaying[" + movieCount + "].movieInfo.popularityScore") != null);
                softAssert.assertTrue(response.extract().jsonPath().get("data.nowPlaying[" + movieCount + "].movieInfo.releaseDate.month") != null);
                softAssert.assertTrue(response.extract().jsonPath().get("data.nowPlaying[" + movieCount + "].movieInfo.releaseDate.day") != null);
                softAssert.assertTrue(response.extract().jsonPath().get("data.nowPlaying[" + movieCount + "].movieInfo.releaseDate.year") != null);
                softAssert.assertTrue(CinemaUtils.isBoolean(response.extract().jsonPath().getBoolean("data.nowPlaying[" + movieCount + "].movieInfo.isPresale")));
                softAssert.assertTrue(CinemaUtils.isBoolean(response.extract().jsonPath().getBoolean("data.nowPlaying[" + movieCount + "].movieInfo.presale")));
                softAssert.assertTrue(response.extract().jsonPath().getList("data.nowPlaying[" + movieCount + "].availableDates") != null);

                List<Object> listOAvailableDates = response.extract().jsonPath().getList("data.nowPlaying[" + movieCount + "].availableDates");
                for (int movieAvailableDateCount = 0; movieAvailableDateCount < listOAvailableDates.size(); movieAvailableDateCount++) {
                    softAssert.assertTrue(response.extract().jsonPath().get("data.nowPlaying[" + movieCount + "].availableDates[" + movieAvailableDateCount + "].date") != null);
                    softAssert.assertTrue(CinemaUtils.isBoolean(response.extract().jsonPath().getBoolean("data.nowPlaying[" + movieCount + "].availableDates[" + movieAvailableDateCount + "].isAvailable")));
                }
            }
            List<Object> listOfPresaleMovies = response.extract().jsonPath().getList("data.presale");
            // preSale movie might not be present every time. softAssert.assertTrue(listOfPresaleMovies.size() != 0, "No presale movie is available for valid cityId " + cityId);
            for (int movieCount = 0; movieCount < listOfPresaleMovies.size(); movieCount++) {
                softAssert.assertTrue(response.extract().jsonPath().get("data.presale[" + movieCount + "].movieInfo.posterImageUrl") != null);
                softAssert.assertTrue(response.extract().jsonPath().get("data.presale[" + movieCount + "].movieInfo.id") != null);
                softAssert.assertTrue(response.extract().jsonPath().get("data.presale[" + movieCount + "].movieInfo.title") != null);
                softAssert.assertTrue(response.extract().jsonPath().get("data.presale[" + movieCount + "].movieInfo.titleEN") != null);
                softAssert.assertTrue(response.extract().jsonPath().get("data.presale[" + movieCount + "].movieInfo.rating") != null);
                softAssert.assertTrue(response.extract().jsonPath().get("data.presale[" + movieCount + "].movieInfo.popularityScore") != null);
                softAssert.assertTrue(response.extract().jsonPath().get("data.presale[" + movieCount + "].movieInfo.releaseDate.month") != null);
                softAssert.assertTrue(response.extract().jsonPath().get("data.presale[" + movieCount + "].movieInfo.releaseDate.day") != null);
                softAssert.assertTrue(response.extract().jsonPath().get("data.presale[" + movieCount + "].movieInfo.releaseDate.year") != null);
                softAssert.assertTrue(CinemaUtils.isBoolean(response.extract().jsonPath().getBoolean("data.presale[" + movieCount + "].movieInfo.isPresale")));
                softAssert.assertTrue(CinemaUtils.isBoolean(response.extract().jsonPath().getBoolean("data.presale[" + movieCount + "].movieInfo.presale")));
                softAssert.assertTrue(response.extract().jsonPath().getList("data.presale[" + movieCount + "].availableDates") != null);

                List<Object> listOAvailableDates = response.extract().jsonPath().getList("data.presale[" + movieCount + "].availableDates");
                for (int movieAvailableDateCount = 0; movieAvailableDateCount < listOAvailableDates.size(); movieAvailableDateCount++) {
                    softAssert.assertTrue(response.extract().jsonPath().get("data.presale[" + movieCount + "].availableDates[" + movieAvailableDateCount + "].date") != null);
                    softAssert.assertTrue(CinemaUtils.isBoolean(response.extract().jsonPath().getBoolean("data.presale[" + movieCount + "].availableDates[" + movieAvailableDateCount + "].isAvailable")));
                }
            }
            List<Object> listOfComingSoonMovies = response.extract().jsonPath().getList("data.comingSoon");
            softAssert.assertTrue(listOfComingSoonMovies.size() != 0, "No comingSoon movie is available for valid cityId " + cityId);
            for (int movieCount = 0; movieCount < listOfComingSoonMovies.size(); movieCount++) {
                softAssert.assertTrue(response.extract().jsonPath().get("data.comingSoon[" + movieCount + "].movieInfo.posterImageUrl") != null);
                softAssert.assertTrue(response.extract().jsonPath().get("data.comingSoon[" + movieCount + "].movieInfo.id") != null);
                softAssert.assertTrue(response.extract().jsonPath().get("data.comingSoon[" + movieCount + "].movieInfo.title") != null);
                softAssert.assertTrue(response.extract().jsonPath().get("data.comingSoon[" + movieCount + "].movieInfo.titleEN") != null);
                softAssert.assertTrue(response.extract().jsonPath().get("data.comingSoon[" + movieCount + "].movieInfo.rating") != null);
                softAssert.assertTrue(response.extract().jsonPath().get("data.comingSoon[" + movieCount + "].movieInfo.popularityScore") != null);
                softAssert.assertTrue(response.extract().jsonPath().get("data.comingSoon[" + movieCount + "].movieInfo.releaseDate.month") != null);
                softAssert.assertTrue(response.extract().jsonPath().get("data.comingSoon[" + movieCount + "].movieInfo.releaseDate.day") != null);
                softAssert.assertTrue(response.extract().jsonPath().get("data.comingSoon[" + movieCount + "].movieInfo.releaseDate.year") != null);
                softAssert.assertTrue(CinemaUtils.isBoolean(response.extract().jsonPath().getBoolean("data.comingSoon[" + movieCount + "].movieInfo.isPresale")));
                softAssert.assertTrue(CinemaUtils.isBoolean(response.extract().jsonPath().getBoolean("data.comingSoon[" + movieCount + "].movieInfo.presale")));
                softAssert.assertTrue(response.extract().jsonPath().getList("data.comingSoon[" + movieCount + "].availableDates") != null);

                List<Object> listOAvailableDates = response.extract().jsonPath().getList("data.comingSoon[" + movieCount + "].availableDates");
                for (int movieAvailableDateCount = 0; movieAvailableDateCount < listOAvailableDates.size(); movieAvailableDateCount++) {
                    softAssert.assertTrue(response.extract().jsonPath().get("data.comingSoon[" + movieCount + "].availableDates[" + movieAvailableDateCount + "].date") != null);
                    softAssert.assertTrue(CinemaUtils.isBoolean(response.extract().jsonPath().getBoolean("data.comingSoon[" + movieCount + "].availableDates[" + movieAvailableDateCount + "].isAvailable")));
                }
            }
            softAssert.assertAll();
        } else if (cityType.equalsIgnoreCase("invalid")) {
            Assert.assertTrue(response.extract().jsonPath().getList("data.nowPlaying").isEmpty(), "nowPlaying list is not empty");
            Assert.assertTrue(response.extract().jsonPath().getList("data.comingSoon").isEmpty(), "comingSoon list is not empty\"");
            Assert.assertTrue(response.extract().jsonPath().getList("data.presale").isEmpty(), "presale list is not empty\"");
        }
    }

    @Test(description = "Verify nullability rule of request body as per the API contract", dataProviderClass = CinemaDP.class, dataProvider = "getCityIDCurrencyAndTrackingRequest", groups = {"sanity", "regression"}, priority = 2)
    public void verifyNullabilityRule(String cityId, String currency, HashMap trackingRequestMap) {
        response = cinemaAPIFrameworkInstance
                .sendRequest(
                        "landingPageDiscover",
                        null,
                        new HashMap<String, String>() {{
                            put("cityId", cityId);
                            put("currency", currency);
                            if (trackingRequestMap != null) {
                                putAll(trackingRequestMap);
                            }
                        }},
                        null,
                        null);
        response.assertThat().statusCode(200);
        Assert.assertTrue(response.extract().jsonPath().getList("data.nowPlaying") != null, "nowPlaying Movie list is not present under data");
        Assert.assertTrue(response.extract().jsonPath().getList("data.comingSoon") != null, "comingSoon Movie list is not present under data");
        softAssert.assertAll();
    }

    @Test(description = "API should work with all supported locale", dataProviderClass = CinemaDP.class, dataProvider = "getLocaleAndCityIDofEachCountry", groups = {"sanity", "regression"}, priority = 3)
    public void testAllSupportedLocale(String locale, String cityId) {
        cinemaAPIFrameworkInstance.setLocale(locale);
        response = cinemaAPIFrameworkInstance.sendRequest("landingPageDiscover", null, new HashMap<String, String>() {{
            put("cityId", cityId);
        }}, null, null);
        response.assertThat().statusCode(200);
        Assert.assertTrue(response.extract().jsonPath().getList("data.nowPlaying") != null, "nowPlaying Movie list is not present under data for locale " + locale + ", cityId " + cityId);
    }
/*
    @Test(description = "Schema Validation", groups = {"sanity", "regression"}, dataProviderClass = CinemaDP.class, dataProvider = "getAValidCityIDofIndonesia", priority = 4)
    public void testSchemaValidation(String cityId) {
        response = cinemaAPIFrameworkInstance.sendRequest("landingPageDiscover", null, new HashMap<String, String>() {{
            put("cityId", cityId);
        }}, null, null);
        response.assertThat().statusCode(200);
        Assert.assertTrue(cinemaAPIFrameworkInstance.validateSchema(response));
    }
 */

}

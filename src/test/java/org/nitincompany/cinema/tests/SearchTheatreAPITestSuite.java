package org.nitincompany.cinema.tests;

import io.restassured.response.ValidatableResponse;
import org.nitincompany.cinema.dp.CinemaDP;
import org.nitincompany.cinema.utils.CinemaUtils;
import org.nitincompany.framework.initializers.APIFramework;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Author: nitinkumar
 * Created Date: 07/02/20
 * Info: Tests of searchTheatre API
 **/

public class SearchTheatreAPITestSuite extends CinemaBase {
    public ValidatableResponse response;
    APIFramework cinemaAPIFrameworkInstance = new APIFramework("cinema");

    @Test(description = "Verify response by passing valid and invalid request data (cityId, movieId).", dataProviderClass = CinemaDP.class, dataProvider = "getValidAndInvalidCityIdMovieIdDP", groups = {"sanity", "regression"}, priority = 1)
    public void verifyResponseWithValidAndInvalidCityIdMovieId(String cityId, String movieId, String movieType) {
        response = cinemaAPIFrameworkInstance.sendRequest("searchTheatre", null, new HashMap<String, String>() {{
            put("cityId", cityId);
            put("movieId", movieId);
        }}, null, null);

        if (movieType.equalsIgnoreCase("nowPlaying")) {
            response.assertThat().statusCode(200);
            List<Map<String, Object>> theatreList = response.extract().jsonPath().getList("data.theatreList");
            for (int theatreCount = 0; theatreCount < theatreList.size(); theatreCount++) {
                softAssert.assertTrue(CinemaUtils.isStringNotNullOrEmpty(response.extract().jsonPath().get("data.theatreList[" + theatreCount + "].name")), "name at " + theatreCount + " position in theatreList is empty or null");
                softAssert.assertTrue(CinemaUtils.isStringNotNullOrEmpty(response.extract().jsonPath().get("data.theatreList[" + theatreCount + "].nameEN")), "nameEN at " + theatreCount + " position in theatreList is empty or null");
                softAssert.assertTrue(CinemaUtils.isStringNotNullOrEmpty(response.extract().jsonPath().get("data.theatreList[" + theatreCount + "].id")), "id at " + theatreCount + " position in theatreList is empty or null");
                softAssert.assertTrue(CinemaUtils.isStringNotNullOrEmpty(response.extract().jsonPath().get("data.theatreList[" + theatreCount + "].providerId")), "providerId at " + theatreCount + " position in theatreList is empty or null");
                softAssert.assertTrue(CinemaUtils.isStringNotNullOrEmpty(response.extract().jsonPath().get("data.theatreList[" + theatreCount + "].theatreGroupId")), "theatreGroupId at " + theatreCount + " position in theatreList is empty or null");
                softAssert.assertTrue(CinemaUtils.isBoolean(response.extract().jsonPath().getBoolean("data.theatreList[" + theatreCount + "].isFavourite")), "isFavourite at " + theatreCount + " position in theatreList is not boolean");
            }
            Map<String, Object> providerNameAndProviderDetailsMap = response.extract().jsonPath().getMap("data.providerValues");
            Set<String> allProviders = providerNameAndProviderDetailsMap.keySet();
            List<String> allProvidersInTheatreList = response.extract().jsonPath().get("data.theatreList.providerId");
            Set<String> setOfAllProvidersInTheatreList = allProvidersInTheatreList.stream().collect(Collectors.toSet());
            Assert.assertTrue(setOfAllProvidersInTheatreList.equals(allProviders), "Set of providers in theatreList is not the same as that in providerValues");
            softAssert.assertAll();
        } else if (movieType.equalsIgnoreCase("comingSoon")) {
            response.assertThat().statusCode(200);
            List<Map<String, Object>> theatreList = response.extract().jsonPath().getList("data.theatreList");
            Assert.assertTrue(theatreList.size() == 0, "comingSoon movie should have theatreList as empty. List is " + theatreList);
        } else if (movieType.equalsIgnoreCase("invalid")) {
            response.assertThat().statusCode(200);
            Assert.assertTrue(response.extract().jsonPath().getList("data.theatreList").size() == 0, "searchTheatre API call with invalid movieId and cityId should have theatreList as empty.");
        } else {
            Assert.fail("Category " + movieType + " not tested");
        }
    }

    @Test(description = "Verify nullability rule of request body as per the API contract", dataProviderClass = CinemaDP.class, dataProvider = "getValidCombinationOfCityIdMovieIdTrackingRequestDP", groups = {"sanity", "regression"}, priority = 2)
    public void verifyNullabilityRule(String cityId, String movieId, HashMap<String, String> trackingRequest) {
        response = cinemaAPIFrameworkInstance.sendRequest("searchTheatre", null, new HashMap<String, String>() {{
            put("cityId", cityId);
            put("movieId", movieId);
            if (trackingRequest != null) {
                putAll(trackingRequest);
            }
        }}, null, null);
        response.assertThat().statusCode(200);
        Assert.assertTrue(response.extract().jsonPath().getList("data.theatreList").size() > 0, "theatreList is empty for cityId:" + cityId + " movieId:" + movieId + " trackingRequest:" + trackingRequest);
    }

    @Test(description = "API should work with all supported locale", dataProviderClass = CinemaDP.class, dataProvider = "getLocaleAndAllRequestParameterOfEachCountryDP", groups = {"sanity", "regression"}, priority = 3)
    public void testAllSupportedLocale(String locale, String cityId, String movieId, HashMap<String, String> trackingRequest) {
        cinemaAPIFrameworkInstance.setLocale(locale);
        response = cinemaAPIFrameworkInstance.sendRequest("searchTheatre", null, new HashMap<String, String>() {{
            put("cityId", cityId);
            put("movieId", movieId);
            if (trackingRequest != null) {
                putAll(trackingRequest);
            }
        }}, null, null);
        response.assertThat().statusCode(200);
        Assert.assertTrue(response.extract().jsonPath().getList("data.theatreList").size() > 0, "theatreList  is empty for cityId " + cityId + " movieId:" + movieId + " trackingRequest:" + trackingRequest);
    }
/*
    @Test(description = "Schema Validation", groups = {"sanity", "regression"}, dataProviderClass = CinemaDP.class, dataProvider = "getAValidCityIdMovieIdTrackingRequestDP", priority = 4)
    public void testSchemaValidation(String cityId, String movieId, HashMap<String, String> trackingRequest) {
        response = cinemaAPIFrameworkInstance.sendRequest("searchTheatre", null, new HashMap<String, String>() {{
            put("cityId", cityId);
            put("movieId", movieId);
            if (trackingRequest != null) {
                putAll(trackingRequest);
            }
        }}, null, null);
        response.assertThat().statusCode(200);
        Assert.assertTrue(cinemaAPIFrameworkInstance.validateSchema(response));
    }

 */
}

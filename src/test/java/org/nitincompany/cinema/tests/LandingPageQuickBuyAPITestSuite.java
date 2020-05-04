package org.nitincompany.cinema.tests;

import io.restassured.response.ValidatableResponse;
import org.nitincompany.cinema.dp.CinemaDP;
import org.nitincompany.framework.initializers.APIFramework;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.List;

/**
 * Author: nitinkumar
 * Created Date: 04/12/19
 * Info: Tests for landingPageQuickBuy API
 **/

public class LandingPageQuickBuyAPITestSuite extends CinemaBase{
    public ValidatableResponse response;
    APIFramework cinemaAPIFrameworkInstance = new APIFramework("cinema");

    @Test(description = "Verify response with valid and invalid theatreId. Also validate nullability rule in response body", dataProviderClass = CinemaDP.class, dataProvider = "getValidAndInvalidTheatreId", groups = {"sanity", "regression"}, priority = 1)
    public void verifyResponseOfValidAndInvalidTheatreId(String theatreId, String theatreType) {
        response = cinemaAPIFrameworkInstance.sendRequest("landingPageQuickBuy", null, new HashMap<String, String>() {{ put("theatreId", theatreId); }}, null, null);
        response.assertThat().statusCode(200);

        if(theatreType.equalsIgnoreCase("valid")){
            List<Object> listOfMovies = response.extract().jsonPath().getList("data.movies");
            softAssert.assertTrue(listOfMovies.size()!=0,"No movie is available for valid theatreId "+theatreId);
            for(int movieCount=0; movieCount<listOfMovies.size(); movieCount++){
                softAssert.assertTrue(response.extract().jsonPath().get("data.movies["+movieCount+"].movieInfo.id")!=null);
                softAssert.assertTrue(response.extract().jsonPath().get("data.movies["+movieCount+"].movieInfo.title")!=null);
                softAssert.assertTrue(response.extract().jsonPath().get("data.movies["+movieCount+"].movieInfo.titleEN")!=null);
                softAssert.assertTrue(response.extract().jsonPath().get("data.movies["+movieCount+"].movieInfo.posterImageUrl")!=null);
                softAssert.assertTrue(response.extract().jsonPath().get("data.movies["+movieCount+"].movieInfo.rating")!=null);
                softAssert.assertTrue(response.extract().jsonPath().get("data.movies["+movieCount+"].movieInfo.popularityScore")!=null);
                softAssert.assertTrue(response.extract().jsonPath().get("data.movies["+movieCount+"].movieInfo.releaseDate")!=null);
                softAssert.assertTrue(response.extract().jsonPath().get("data.movies["+movieCount+"].movieInfo.isPresale")!=null);

                List<Object> listOAvailableDates = response.extract().jsonPath().getList("data.movies["+movieCount+"].availableDates");
                for(int movieAvailableDateCount=0; movieAvailableDateCount<listOAvailableDates.size(); movieAvailableDateCount++) {
                    softAssert.assertTrue(response.extract().jsonPath().get("data.movies[" + movieCount + "].availableDates["+movieAvailableDateCount+"].date") != null);
                    softAssert.assertTrue(response.extract().jsonPath().get("data.movies[" + movieCount + "].availableDates["+movieAvailableDateCount+"].isAvailable") != null);
                }
            }
            softAssert.assertAll();
        }
    }

    @Test(description = "Verify nullability rule of request body as per the API contract", dataProviderClass = CinemaDP.class, dataProvider = "getTheatreIDCurrencyAndTrackingRequest", groups = {"sanity", "regression"}, priority = 2)
    public void verifyNullabilityRule(String theatreId, String currency, HashMap trackingRequestMap) {
        response = cinemaAPIFrameworkInstance
                .sendRequest(
                        "landingPageQuickBuy",
                        null,
                        new HashMap<String, String>() {{
                            put("theatreId", theatreId);
                            put("currency", currency);
                            if (trackingRequestMap != null) {
                                putAll(trackingRequestMap);
                            }
                        }},
                        null,
                        null);
        response.assertThat().statusCode(200);
        Assert.assertTrue(response.extract().jsonPath().getList("data.movies")!=null,"Movie list is not present under data");
        softAssert.assertAll();
    }

    @Test(description = "API should work with all supported locale", dataProviderClass = CinemaDP.class, dataProvider = "getLocaleAndTheatreId", groups = {"sanity","regression"}, priority = 3)
    public void testAllSupportedLocale(String locale, String theatreId) {
        cinemaAPIFrameworkInstance.setLocale(locale);
        response = cinemaAPIFrameworkInstance.sendRequest("landingPageQuickBuy", null, new HashMap<String, String>(){{put("theatreId",theatreId);}}, null, null);
        response.assertThat().statusCode(200);
        Assert.assertTrue(response.extract().jsonPath().getList("data.movies")!=null,"Movie list is not present under data for locale "+locale+", theatreId "+theatreId);
        /** Need to confirm below assert
        Assert.assertTrue(response.extract().jsonPath().getList("data.movies").size() != 0,"Number of movies for locale " + locale + ", theatreId "+theatreId+" is zero.");
         */
    }
/*
    @Test(description = "Schema Validation", groups = {"sanity","regression"}, dataProviderClass = CinemaDP.class, dataProvider = "getValidTheatreId", priority = 4)
    public void testSchemaValidation(String theatreId) {
        response = cinemaAPIFrameworkInstance.sendRequest("landingPageQuickBuy", null, new HashMap<String, String>(){{put("theatreId",theatreId);}}, null, null);
        response.assertThat().statusCode(200);
        Assert.assertTrue(cinemaAPIFrameworkInstance.validateSchema(response));
    }
 */

}

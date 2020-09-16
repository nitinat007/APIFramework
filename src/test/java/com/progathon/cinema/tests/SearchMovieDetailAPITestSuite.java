package com.progathon.cinema.tests;

import com.progathon.cinema.dp.CinemaDP;
import io.restassured.response.ValidatableResponse;
import com.progathon.cinema.dp.CinemaDP;
import com.progathon.framework.initializers.APIFramework;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author: nitinkumar
 * Created Date: 09/12/19
 * Info: Tests for searchMovieDetail API
 **/

public class SearchMovieDetailAPITestSuite extends CinemaBase {
    public ValidatableResponse response;
    APIFramework cinemaAPIFrameworkInstance = new APIFramework("cinema");

    @Test(description = "Verify response with valid and invalid movieId. Also validate nullability rule in response body", dataProviderClass = CinemaDP.class, dataProvider = "getValidAndInvalidMovieId", groups = {"sanity", "regression"}, priority = 1)
    public void verifyResponseOfValidAndInvalidMovieId(String movieId, String movieType) {
        response = cinemaAPIFrameworkInstance.sendRequest("searchMovieDetail", null, new HashMap<String, String>() {{ put("movieId", movieId); }}, null, null);

        if(movieType.equalsIgnoreCase("valid")){
            response.assertThat().statusCode(200);
            softAssert.assertTrue(response.extract().jsonPath().get("data.title")!=null);
            softAssert.assertTrue(response.extract().jsonPath().get("data.imageUrl")!=null);
            softAssert.assertTrue(response.extract().jsonPath().get("data.rating")!=null);
            softAssert.assertTrue(response.extract().jsonPath().get("data.releaseDate")!=null);
            softAssert.assertTrue(response.extract().jsonPath().get("data.isReleased")!=null);
            softAssert.assertTrue(response.extract().jsonPath().get("data.format")!=null);
            softAssert.assertTrue(response.extract().jsonPath().get("data.synopsis")!=null);
            softAssert.assertTrue(response.extract().jsonPath().get("data.castsAndCrews")!=null);
            softAssert.assertTrue(response.extract().jsonPath().get("data.duration")!=null);
            softAssert.assertTrue(response.extract().jsonPath().get("data.isPresale")!=null);
            softAssert.assertTrue(response.extract().jsonPath().get("data.isComingSoon")!=null);
            softAssert.assertTrue(response.extract().jsonPath().get("data.titleEN")!=null);
            List<Object> castCrewObjects = response.extract().jsonPath().getList("data.castsAndCrews");
            for(int castAndCrewCount=0; castAndCrewCount < castCrewObjects.size(); castAndCrewCount++){
                Map<Object,Object> castCrewObject = response.extract().jsonPath().getMap("data.castsAndCrews.get("+castAndCrewCount+")");
                softAssert.assertTrue(castCrewObject.get("label")!=null);
                softAssert.assertTrue(castCrewObject.get("names")!=null && ((ArrayList)castCrewObject.get("names")).size()!= 0);
            }
            softAssert.assertAll();

        }else if(movieType.equalsIgnoreCase("invalid")){
            response.assertThat().statusCode(500);
            String errorMsg = response.extract().jsonPath().get("errorMessage");
            Assert.assertTrue(errorMsg.contains("No movie found!"));
        }
    }

    @Test(description = "API should work with all supported locale", dataProviderClass = CinemaDP.class, dataProvider = "getLocaleAndMovieId", groups = {"sanity","regression"}, priority = 2)
    public void testAllSupportedLocale(String locale, String movieId) {
        cinemaAPIFrameworkInstance.setLocale(locale);
        response = cinemaAPIFrameworkInstance.sendRequest("searchMovieDetail", null, new HashMap<String, String>() {{ put("movieId", movieId); }}, null, null);
        response.assertThat().statusCode(200);
        Assert.assertTrue(response.extract().jsonPath().get("data")!=null,"Movie Details is not present under data for locale "+locale+", movieId "+movieId);
        Assert.assertTrue(response.extract().jsonPath().get("data.title") != null && response.extract().jsonPath().get("data.title") != "","Title of movieId "+movieId+" for locale " + locale + " is either blank or null");

    }
/*
    @Test(description = "Schema Validation", groups = {"sanity","regression"}, dataProviderClass = CinemaDP.class, dataProvider = "getValidMovieId", priority = 3)
    public void testSchemaValidation(String movieId) {
        response = cinemaAPIFrameworkInstance.sendRequest("searchMovieDetail", null, new HashMap<String, String>() {{ put("movieId", movieId); }}, null, null);
        response.assertThat().statusCode(200);
        Assert.assertTrue(cinemaAPIFrameworkInstance.validateSchema(response));
    }

 */
}

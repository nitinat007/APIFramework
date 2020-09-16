package com.progathon.cinema.tests;

import com.progathon.cinema.dp.CinemaDP;
import com.progathon.cinema.utils.CinemaUtils;
import io.restassured.response.ValidatableResponse;
import com.progathon.cinema.dp.CinemaDP;
import com.progathon.cinema.utils.CinemaUtils;
import com.progathon.framework.initializers.APIFramework;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Author: nitinkumar
 * Created Date: 08/11/19
 * Info: Test for getAllTheatres API
 **/

public class GetAllTheatresAPITestSuite extends CinemaBase {
    public ValidatableResponse response;
    APIFramework cinemaAPIFrameworkInstance = new APIFramework("cinema");

    @Test(description = "API should return cites and theatres in sorted order.", groups = {"sanity","regression"}, priority = 1)
    public void testCityAndTheatreOrder() {
        response = cinemaAPIFrameworkInstance.sendRequest("getAllTheatres", null, null, null, null);
        response.assertThat().statusCode(200);
        List<Object> listOfCity = response.extract().jsonPath().getList("data.cinemaCities.name");
        softAssert.assertTrue(CinemaUtils.isSorted(listOfCity)," Cities are not in sorted order. "+listOfCity);
        for(int cinemaCitiesCount=0; cinemaCitiesCount<listOfCity.size();cinemaCitiesCount++){
            List<Object> listOfTheatre = response.extract().jsonPath().getList("data.cinemaCities["+cinemaCitiesCount+"].theatreList.name");
            softAssert.assertTrue(CinemaUtils.isSorted(listOfTheatre)," Theatres are not in sorted order. "+listOfTheatre);
            softAssert.assertTrue(listOfTheatre.size()!=0, "City without any theatre exists for "+response.extract().jsonPath().getString("data.cinemaCities["+cinemaCitiesCount+"].nameEN"));
        }
        softAssert.assertAll();
    }

    @Test(description = "Verify that defaultCityIndex attribute has non empty and valid value.", dataProviderClass = CinemaDP.class, dataProvider = "getLocalesDP", groups = {"sanity","regression"}, priority = 2)
    public void testDefaultCity(String locale) {
        cinemaAPIFrameworkInstance.setLocale(locale);
        response = cinemaAPIFrameworkInstance.sendRequest("getAllTheatres", null, null, null, null);
        response.assertThat().statusCode(200);
        String defaultCityIndex = response.extract().jsonPath().getString("data.defaultCityIndex");
        Assert.assertTrue(defaultCityIndex != null && defaultCityIndex.length() > 0,"defaultCityIndex for locale " + locale + " is either null or blank");
        String defaultCityName = response.extract().jsonPath().getString("data.cinemaCities["+defaultCityIndex+"].name");
        Assert.assertTrue(defaultCityName != null," Default city is missing in response for locale "+locale);
    }

    @Test(description = "Verify that all theatreGroupId listed under cinemaCities->theatreList are also listed under theatreGroupValues", groups = {"sanity","regression"}, priority = 3)
    public void testtheatreGroupValues() {
        response = cinemaAPIFrameworkInstance.sendRequest("getAllTheatres", null, null, null, null);
        response.assertThat().statusCode(200);
        Set<Object> availableTheatreGroups = response.extract().jsonPath().getMap("data.theatreGroupValues").keySet();
        Set<String> theatreGroupOfTheatres = new LinkedHashSet<String>();
        for(int countOfCity=0; countOfCity<response.extract().jsonPath().getList("data.cinemaCities.id").size(); countOfCity++){
            List<String> allTheatreGrp = response.extract().jsonPath().getList("data.cinemaCities["+countOfCity+"].theatreList.theatreGroupId");
            theatreGroupOfTheatres.addAll(allTheatreGrp);
        }
        Assert.assertTrue( availableTheatreGroups.containsAll(theatreGroupOfTheatres),"Few theatreGroupIds listed under cinemaCities->theatreList are not present under theatreGroupValues. They are: "+ theatreGroupOfTheatres.removeAll(availableTheatreGroups));
    }

    @Test(description = "API should work with all supported locale", dataProviderClass = CinemaDP.class, dataProvider = "getLocalesDP", groups = {"sanity","regression"}, priority = 4)
    public void testAllSupportedLocale(String locale) {
        cinemaAPIFrameworkInstance.setLocale(locale);
        response = cinemaAPIFrameworkInstance.sendRequest("getAllTheatres", null, null, null, null);
        response.assertThat().statusCode(200);
        Assert.assertTrue(response.extract().jsonPath().getList("data.cinemaCities.id").size() != 0,"Number of cities for locale " + locale + " is zero");
    }
/*
    @Test(description = "Schema Validation", groups = {"sanity","regression"}, priority = 5)
    public void testSchemaValidation() {
        response = cinemaAPIFrameworkInstance.sendRequest("getAllTheatres", null, null, null, null);
        response.assertThat().statusCode(200);
        Assert.assertTrue(cinemaAPIFrameworkInstance.validateSchema(response));
    }
 */
}


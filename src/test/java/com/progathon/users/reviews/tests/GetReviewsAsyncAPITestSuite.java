package com.progathon.users.reviews.tests;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import io.restassured.response.ValidatableResponse;
import com.progathon.framework.readers.CSVParametersProvider;
import com.progathon.framework.readers.DataFileParameters;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static com.progathon.users.reviews.utils.UsersReviewUtils.*;

/**
 * Author: nitinkumar
 * Created Date: 26/02/20
 * Info: Tests of GetReviewsAsync. This suite should run after GetReviewAsyncAPITestSuite
 **/

public class GetReviewsAsyncAPITestSuite extends UGCBase {
    ArrayList<String> createdTimestamps;

    @BeforeTest(description = "This method gets the time frame from  CSV to be used in the tests")
    public void getTimestampForTest() {
        createdTimestamps = getColumnValuesOfTestsAsList("Reviews", "/resources/input-data/Users/", "createdTimestamp");
    }

    @Test(description = "Test reads data from csv and sends getReviewsAsync request for each of the test data. And then compares response of API call with corresponding data from CSV (after applying filter)", dataProvider = "csv", dataProviderClass = CSVParametersProvider.class, groups = {"regression", "testGetReviewsAsync"}, priority = 1)
    @DataFileParameters(name = "getReviews.csv", path = "/resources/input-data/Users")
    public void testGetReviewsAsync(String testcaseNumber, String testcaseInfo, String reviewerId, String objectId, String language, String tags, String context, String contentTypes, String reviewStatusSet, String languageSet, String skip, String limit, String sortBy, String sortOrder, String createdTimestampStart, String createdTimestampEnd, String updatedTimestampStart, String updatedTimestampEnd, String enableReviewCount) {

        logger.log(" -- " + testcaseInfo + " --");
        LimitsOnTimeStamp createdTimestampLimits = new LimitsOnTimeStamp(LimitsOnTimeStamp.timeStampType.createdTimestamp);//variable used to store start and end time limit of reviews based on createdTimeStamp
        ValidatableResponse response = usersReviewAPIFrameworkInstance.sendRequest("reviewAsyncServerService.getReviewsAsync", null, new HashMap<String, String>() {{
            put("reviewerId", reviewerId);
            put("objectId", objectId);
            put("language", language);
            put("tags", tags);
            put("context", context);
            put("contentTypes", contentTypes);
            put("reviewStatusSet", reviewStatusSet);
            put("languageSet", languageSet);
            put("skip", skip);
            put("limit", limit);
            put("sortBy", sortBy);
            put("sortOrder", sortOrder);
            put("updatedTimestampStart", updatedTimestampStart);
            put("updatedTimestampEnd", updatedTimestampEnd);
            if (testcaseInfo.contains("createdTimestampStart as timestamp of 2nd last submission")) { //for TC 61
                put("createdTimestampStart", createdTimestamps.get(createdTimestamps.size() - 2));
                put("createdTimestampEnd", Collections.max(createdTimestamps));
                createdTimestampLimits.start = createdTimestamps.get(createdTimestamps.size() - 2);
            } else if (testcaseInfo.contains("createdTimestampEnd as 2nd last submission")) { //for TC 62.
                put("createdTimestampStart", Collections.min(createdTimestamps));
                put("createdTimestampEnd", createdTimestamps.get(createdTimestamps.size() - 2));
                createdTimestampLimits.end = createdTimestamps.get(createdTimestamps.size() - 2);
            } else if (!(testcaseInfo.contains("only") && testcaseInfo.contains("present")) || testcaseInfo.contains("sortOrder")) { //no need to pass updatedTimestampStart & updatedTimestampEnd if testcase is of type 'only present'. Eg : "reviewerId only present". But ignore this statement if filter for sorting is there
                put("createdTimestampStart", Collections.min(createdTimestamps));
                put("createdTimestampEnd", Collections.max(createdTimestamps));
            }
            put("enableReviewCount", enableReviewCount);
        }}, null, null);

        response.assertThat().statusCode(200);
        List reviews = response.extract().jsonPath().getList("result.reviews");

        if (testcaseInfo.startsWith("POS:")) { //For +ve TC
            Assert.assertTrue(reviews.size() > 0, "No reviews found");
            JsonArray reviewsArrayFromCSV = getReviewsFromCSVAfterApplyingFilters("Reviews", "/resources/input-data/Users/", reviews, contentTypes, context, sortBy, sortOrder, skip, limit, reviewStatusSet, createdTimestampLimits);
            List<JsonElement> reviewIdListInResponse = response.extract().jsonPath().getList("result.reviews.reviewId");
//            System.out.println("\nreviewIdListInResponse: " + reviewIdListInResponse);
            Gson gson = new Gson();
            JsonArray reviewIdArrayFromResponse = gson.toJsonTree(reviewIdListInResponse).getAsJsonArray();
            JsonArray reviewIdArrayFromCSV = new JsonArray();

            for (int i = 0; i < reviewsArrayFromCSV.size(); i++) {
                reviewIdArrayFromCSV.add(reviewsArrayFromCSV.get(i).getAsJsonObject().get("reviewId"));
            }

            Assert.assertTrue(reviewIdArrayFromCSV.size() == reviewIdArrayFromResponse.size(), "Number of Reviews expected (from csv data) is " + reviewIdArrayFromCSV.size() + " but found " + reviewIdArrayFromResponse.size() + " in response. reviewIdArrayFromCSV: " + reviewIdArrayFromCSV + " reviewIdArrayFromResponse: " + reviewIdArrayFromResponse);
            boolean isOrderOfReviewIdCorrect = true;
            for (int countOfReviewIdArrayFromResponse = 0; countOfReviewIdArrayFromResponse < reviewIdArrayFromResponse.size(); countOfReviewIdArrayFromResponse++) {
                if (!reviewIdArrayFromCSV.get(countOfReviewIdArrayFromResponse).equals(reviewIdArrayFromResponse.get(countOfReviewIdArrayFromResponse))) {
                    isOrderOfReviewIdCorrect = false;
                }
            }
            Assert.assertTrue(isOrderOfReviewIdCorrect, " Order of reviews incorrect. reviewIdArrayFromCSV: " + reviewIdArrayFromCSV + " reviewIdArrayFromResponse: " + reviewIdArrayFromResponse);

        } else if (testcaseInfo.startsWith("NEG:")) { //For -ve TC
            Assert.assertTrue(reviews.size() == 0, "Count of reviews is not 0.");
        } else {
            Assert.assertFalse(true, "Check test data. Can't identify if TC is +ve or -ve.");
        }

        Assert.assertTrue(enableReviewCount == "true" ? response.extract().jsonPath().get("reviewCount") != null : response.extract().jsonPath().get("reviewCount") == null, "reviewCount is " + response.extract().jsonPath().get("reviewCount"));
    }

}

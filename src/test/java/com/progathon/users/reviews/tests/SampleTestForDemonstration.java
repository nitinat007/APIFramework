package com.progathon.users.reviews.tests;

import io.restassured.response.ValidatableResponse;
import com.progathon.framework.initializers.APIFramework;
import com.progathon.framework.readers.CSVParametersProvider;
import com.progathon.framework.readers.DataFileParameters;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.StringTokenizer;

import static com.progathon.users.reviews.utils.UsersReviewUtils.*;

/**
 * Author: nitinkumar
 * Created Date: 13/02/20
 * Info: These are not tests. This is POC on how different API tests of Users feature will be invoked in the automation suite using the new approach
 **/


//To delete this later
public class SampleTestForDemonstration {
    public ValidatableResponse response;
    APIFramework usersReviewAPIFrameworkInstance = new APIFramework("users");

    @Test(description = "testGetReviewAsync", groups = {"sanity", "regression"}, priority = 1)
    public void testGetReviewAsync() {

        // verified that reviewId is replaced in request Body
        response = usersReviewAPIFrameworkInstance.sendRequest("reviewAsyncServerService.getReviewAsync", null, new HashMap<String, String>() {{
            put("reviewId", "1657698552384594271");
        }}, null, null);

        // verified that reviewId is removed in request Body
        response = usersReviewAPIFrameworkInstance.sendRequest("reviewAsyncServerService.getReviewAsync", null, new HashMap<String, String>() {{
        }}, null, null);

    }

    @Test(description = "testSubmitReviewAsync", groups = {"sanity", "regression"}, priority = 1)
    public void testSubmitReviewAsync() {

        // Type1: verified that filters as JsonElement directly under 'params[1]' is replaced as well as removed from request Body
        response = usersReviewAPIFrameworkInstance.sendRequest("reviewAsyncServerService.submitReviewAsync", null, new HashMap<String, String>() {{
            put("objectId", "77777");
            put("reviewerId", "2345678");
            put("overallRating", "10.0");
        }}, null, null);

        //Type2: verified that filters as JsonArray directly under 'params[1]' is replaced from request Body
        String val1 = "TEXT";
        String val2 = "MEDIA";
        HashMap<String, String> ratingsMap = new HashMap<String, String>() {{
            put("ratingId", "12345");
            put("ratingTags", "HELLO");
        }};
        response = usersReviewAPIFrameworkInstance.sendRequest("reviewAsyncServerService.submitReviewAsync", null, new HashMap<String, String>() {{
            put("objectId", "77777");
            put("reviewerId", "2345678");
            put("contentTypes", val1 + "\",\"" + val2);
            putAll(ratingsMap);
            put("tagId", "Hello");

        }}, null, null);

    }

    //Verified that tests can reading from CSV and updating any value in the CSV. Other tests can rely on the updated data for testing. Also verified that complex filters of requestBody works in this approach of API framework.
    @Test(description = "testSubmitReviewAsyncUsingCSV", dataProvider = "csv", dataProviderClass = CSVParametersProvider.class, groups = {"sanity", "regression"}, priority = 1)
    @DataFileParameters(name = "ReviewsSample.csv", path = "/resources/input-data/Users")
    public void testSubmitReviewAsyncUsingCSV(String testcaseNumber, String objectId, String objectName, String objectNames, String contentTypes, String language, String reviewText, String ratings, String overallRating, String maximumRating, String tags, String reviewerId, String reviewerName, String anonymous, String contexts, String media, String needModeration, String reviewId, String createdTimestampStart) {
        //  System.out.println("params: " + testcaseNumber + " - " + objectId + " - " + objectName + " -- " + objectNames + " - " + contentTypes + " - " + language + " - " + reviewText + " - " + ratings + " - " + overallRating + " - " + maximumRating + " - " + tags + " - " + reviewerId + " - " + reviewerName + " - " + anonymous + " -- " + contexts + " - " + media + " - " + needModeration + " - " + createdTimestampStart);

        response = usersReviewAPIFrameworkInstance.sendRequest("reviewAsyncServerService.submitReviewAsync", null, new HashMap<String, String>() {{
            put("objectId", objectId);
            put("objectName", objectName);
            put("objectNames", objectNames);
            put("contentTypes", contentTypes);
            put("reviewText", reviewText);
            put("language", language);
            put("overallRating", overallRating);
            put("maximumRating", maximumRating);
            put("ratings", ratings);
            put("tagId", tags);
            put("reviewerId", reviewerId);
            put("reviewerName", reviewerName);
            put("anonymous", anonymous);
            put("contexts", contexts);
            put("media", media);
            put("needModeration", needModeration);

        }}, null, null);
        response.assertThat().statusCode(200);
        String status = response.extract().jsonPath().get("result.status");
        String createdReviewId = response.extract().jsonPath().get("result.reviewId");

        //This is the way to update a particular column value for the given testcaseNumber in CSV
        if (status.equalsIgnoreCase("Success") && createdReviewId != null)
            updateCSVColumnValueOfTest("ReviewsSample", "/resources/input-data/Users/", testcaseNumber, "reviewId", createdReviewId);

        //This shows how to fetch particular testcases data from csv
        int tokenNumber = 0;
        StringTokenizer st = getParticularTestDataFromCSV("ReviewsSample", "/resources/input-data/Users/", testcaseNumber);
        while (st.hasMoreTokens()) {
            tokenNumber++;
            String sd = st.nextToken();
            System.out.print(sd + " - ");
        }
    }


}

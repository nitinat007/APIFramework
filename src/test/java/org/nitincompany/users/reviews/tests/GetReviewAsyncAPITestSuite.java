package org.nitincompany.users.reviews.tests;

import io.restassured.response.ValidatableResponse;
import org.nitincompany.framework.readers.CSVParametersProvider;
import org.nitincompany.framework.readers.DataFileParameters;
import org.nitincompany.users.reviews.utils.ReviewSubmissionBean;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.nitincompany.users.reviews.utils.UsersReviewUtils.*;

/**
 * Author: nitinkumar
 * Created Date: 20/02/20
 * Info: Tests of getReviewAsync API. This suite should run after SubmitReviewAsyncAPITestSuite
 **/

public class GetReviewAsyncAPITestSuite extends UGCBase {

    @Test(description = "Test compares review data of csv with corresponding response of getReviewAsync API ", dataProvider = "csv", dataProviderClass = CSVParametersProvider.class, groups = {"regression", "ReviewWorkflow"}, priority = 1)
    @DataFileParameters(name = "Reviews.csv", path = "/resources/input-data/Users")
    public void testGetReviewAsync(String testcaseNumber, String testcaseInfo, String errorMessage, String objectId, String objectName, String objectNames, String contentTypes, String language, String reviewText, String ratings, String overallRating, String maximumRating, String tags, String reviewerId, String reviewerName, String anonymous, String contexts, String media, String needModeration, String reviewId, String createdTimestampStart) {

        logger.log(" -- Get Review for reviewId: " + reviewId + ", testcaseInfo: " + testcaseInfo + " -- ");

        ValidatableResponse response = usersReviewAPIFrameworkInstance.sendRequest("reviewAsyncServerService.getReviewAsync", null, new HashMap<String, String>() {{
            put("reviewId", reviewId);
        }}, null, null);
        response.assertThat().statusCode(200);

        if (errorMessage.isEmpty()) {
            String reviewFromCSVAsJsonString = usersReviewAPIFrameworkInstance.getRequestBody(new HashMap<String, String>() {{
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
            }}, "submitReviewAsync");

            softAssert.assertTrue(response.extract().jsonPath().get("result.review.reviewId").toString().equalsIgnoreCase(reviewId), "Result fetched is not of reviewId " + reviewId);
            if (testcaseInfo.contains("needModeration as true") || testcaseInfo.contains("needModeration not present")) {
                softAssert.assertTrue(response.extract().jsonPath().get("result.review.status").equals("SUBMITTED"), " Status should be SUBMITTED. Found:" + response.extract().jsonPath().get("result.review.status"));
            } else {
                softAssert.assertTrue(response.extract().jsonPath().get("result.review.status").equals("CURATED"), " Status should be CURATED. Found:" + response.extract().jsonPath().get("result.review.status"));
            }
            ArrayList<String> comparisionResult = compareTwoJsonStrings(response.extract().asString(), reviewFromCSVAsJsonString);
            softAssert.assertTrue(comparisionResult.isEmpty(), "Comparision of reviews b/w response and csv shows these differences: \n" + comparisionResult);
            softAssert.assertAll();

            String createdTimestamp = response.extract().jsonPath().get("result.review.createdTimestamp").toString();
            reviewSubmissionBean = new ReviewSubmissionBean(testcaseNumber, "createdTimestamp", createdTimestamp);

        }
    }

    @AfterMethod(description = "This updates the CSV with updatedTimestamp for each test. This value will further be used by tests (eg in getReviewsAsync)")
    public void postTestDataInsertion() {
        updateCSVColumnValueOfTest("Reviews", "/resources/input-data/Users/", reviewSubmissionBean.testcaseNumber, reviewSubmissionBean.columnToUpdate, reviewSubmissionBean.valueToUpdate);
    }
}

package com.progathon.users.reviews.tests;

import com.progathon.users.reviews.utils.UsersReviewUtils;
import io.restassured.response.ValidatableResponse;
import com.progathon.framework.readers.CSVParametersProvider;
import com.progathon.framework.readers.DataFileParameters;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.*;

/**
 * Author: nitinkumar
 * Created Date: 23/04/20
 * Info: Tests of updateReviewAsync API
 **/

public class UpdateReviewAsyncAPITestSuite extends UGCBase {
    String reviewId;

    @BeforeTest(description = "This method submits a review for any review data (say 4th row) read from Reviews.csv . Finally populates reviewId which is needed by Test")
    public void getTimestampForTest() {

        ValidatableResponse response = usersReviewAPIFrameworkInstance.sendRequest("reviewAsyncServerService.submitReviewAsync", null, new HashMap<String, String>() {{
            put("objectId", UsersReviewUtils.getParticularColumnValueOfATestFromCSV("Reviews", "/resources/input-data/Users", 4, "objectId"));
            put("objectName", UsersReviewUtils.getParticularColumnValueOfATestFromCSV("Reviews", "/resources/input-data/Users", 4, "objectName"));
            put("objectNames", UsersReviewUtils.getParticularColumnValueOfATestFromCSV("Reviews", "/resources/input-data/Users", 4, "objectNames"));
            put("contentTypes", UsersReviewUtils.getParticularColumnValueOfATestFromCSV("Reviews", "/resources/input-data/Users", 4, "contentTypes"));
            put("reviewText", UsersReviewUtils.getParticularColumnValueOfATestFromCSV("Reviews", "/resources/input-data/Users", 4, "reviewText"));
            put("language", UsersReviewUtils.getParticularColumnValueOfATestFromCSV("Reviews", "/resources/input-data/Users", 4, "language"));
            put("overallRating", UsersReviewUtils.getParticularColumnValueOfATestFromCSV("Reviews", "/resources/input-data/Users", 4, "overallRating"));
            put("maximumRating", UsersReviewUtils.getParticularColumnValueOfATestFromCSV("Reviews", "/resources/input-data/Users", 4, "maximumRating"));
            put("ratings", UsersReviewUtils.getParticularColumnValueOfATestFromCSV("Reviews", "/resources/input-data/Users", 4, "ratings"));
            put("tagId", UsersReviewUtils.getParticularColumnValueOfATestFromCSV("Reviews", "/resources/input-data/Users", 4, "tags"));
            put("reviewerId", UsersReviewUtils.getParticularColumnValueOfATestFromCSV("Reviews", "/resources/input-data/Users", 4, "reviewerId"));
            put("reviewerName", UsersReviewUtils.getParticularColumnValueOfATestFromCSV("Reviews", "/resources/input-data/Users", 4, "reviewerName"));
            put("anonymous", UsersReviewUtils.getParticularColumnValueOfATestFromCSV("Reviews", "/resources/input-data/Users", 4, "anonymous"));
            put("contexts", UsersReviewUtils.getParticularColumnValueOfATestFromCSV("Reviews", "/resources/input-data/Users", 4, "contexts"));
            put("media", UsersReviewUtils.getParticularColumnValueOfATestFromCSV("Reviews", "/resources/input-data/Users", 4, "media"));
            put("needModeration", UsersReviewUtils.getParticularColumnValueOfATestFromCSV("Reviews", "/resources/input-data/Users", 4, "needModeration"));
        }}, null, null);
        reviewId = response.extract().jsonPath().get("result.reviewId");
    }

    @Test(description = "Test reads test data one by one from ReviewsUpdate.csv file and calls updateReviewAsync API for a particular reviewId generated in pre-test", dataProvider = "csv", dataProviderClass = CSVParametersProvider.class, groups = {"regression", "ReviewWorkflow"}, priority = 1)
    @DataFileParameters(name = "ReviewsUpdate.csv", path = "/resources/input-data/Users")
    public void testGetReviewAsync(String testcaseNumber, String testcaseInfo, String status, String reviewerId, String anonymous, String dirtyFields) {

        logger.log(" -- " + testcaseInfo + " --");
        ValidatableResponse response = usersReviewAPIFrameworkInstance.sendRequest("reviewAsyncServerService.updateReviewAsync", null, new HashMap<String, String>() {{
            put("reviewId", reviewId);
            put("status", status);
            put("reviewerId", reviewerId);
            put("anonymous", anonymous);
            put("dirtyFields", dirtyFields);
        }}, null, null);
        response.assertThat().statusCode(200);

        HashMap review = UsersReviewUtils.getReviewOfGivenReviewId(reviewId);
        String statusInReview = String.valueOf(review.get("status"));
        String reviewerIdInReview = String.valueOf(review.get("reviewerId"));
        boolean anonymousInReview = Boolean.getBoolean(review.get("anonymous").toString());
        List<String> listOfFieldsChanges = Arrays.asList(dirtyFields.replace("[", "").replace("]", "").replace("\"","").split(","));

        if (listOfFieldsChanges.contains("STATUS")) {
            if (status.equalsIgnoreCase("")) {
                softAssert.assertTrue(statusInReview.equalsIgnoreCase("null"), "Status:  expected = " + status + " , received = " + statusInReview);
            } else {
                softAssert.assertTrue(statusInReview.equalsIgnoreCase(status), "Status: expected = " + status + " , received = " + statusInReview);
            }
        }
        if (listOfFieldsChanges.contains("REVIEWER_ID")) {
            if (reviewerId.equalsIgnoreCase("")) {
                softAssert.assertTrue(reviewerIdInReview.equalsIgnoreCase( "null"), "Status:  expected = " + reviewerId + " , received = " + reviewerIdInReview);
            } else {
                softAssert.assertTrue(reviewerIdInReview.equalsIgnoreCase(reviewerId), "Status: expected = " + reviewerId + " , received = " + reviewerIdInReview);
            }
        }
        if (listOfFieldsChanges.contains("ANONYMOUS")) {
            if (status.equalsIgnoreCase("")) {
                softAssert.assertTrue(anonymousInReview == false, "Status:  expected = " + anonymous + " , received = " + anonymousInReview);
            } else {
                softAssert.assertTrue(anonymousInReview == Boolean.getBoolean(anonymous), "Status: expected = " + anonymous + " , received = " + anonymousInReview);
            }
        }
        softAssert.assertAll();

    }
}

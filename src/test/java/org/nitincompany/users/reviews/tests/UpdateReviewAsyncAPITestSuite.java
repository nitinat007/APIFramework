package org.nitincompany.users.reviews.tests;

import io.restassured.response.ValidatableResponse;
import org.nitincompany.framework.readers.CSVParametersProvider;
import org.nitincompany.framework.readers.DataFileParameters;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.*;

import static org.nitincompany.users.reviews.utils.UsersReviewUtils.getParticularColumnValueOfATestFromCSV;
import static org.nitincompany.users.reviews.utils.UsersReviewUtils.getReviewOfGivenReviewId;

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
            put("objectId", getParticularColumnValueOfATestFromCSV("Reviews", "/resources/input-data/Users", 4, "objectId"));
            put("objectName", getParticularColumnValueOfATestFromCSV("Reviews", "/resources/input-data/Users", 4, "objectName"));
            put("objectNames", getParticularColumnValueOfATestFromCSV("Reviews", "/resources/input-data/Users", 4, "objectNames"));
            put("contentTypes", getParticularColumnValueOfATestFromCSV("Reviews", "/resources/input-data/Users", 4, "contentTypes"));
            put("reviewText", getParticularColumnValueOfATestFromCSV("Reviews", "/resources/input-data/Users", 4, "reviewText"));
            put("language", getParticularColumnValueOfATestFromCSV("Reviews", "/resources/input-data/Users", 4, "language"));
            put("overallRating", getParticularColumnValueOfATestFromCSV("Reviews", "/resources/input-data/Users", 4, "overallRating"));
            put("maximumRating", getParticularColumnValueOfATestFromCSV("Reviews", "/resources/input-data/Users", 4, "maximumRating"));
            put("ratings", getParticularColumnValueOfATestFromCSV("Reviews", "/resources/input-data/Users", 4, "ratings"));
            put("tagId", getParticularColumnValueOfATestFromCSV("Reviews", "/resources/input-data/Users", 4, "tags"));
            put("reviewerId", getParticularColumnValueOfATestFromCSV("Reviews", "/resources/input-data/Users", 4, "reviewerId"));
            put("reviewerName", getParticularColumnValueOfATestFromCSV("Reviews", "/resources/input-data/Users", 4, "reviewerName"));
            put("anonymous", getParticularColumnValueOfATestFromCSV("Reviews", "/resources/input-data/Users", 4, "anonymous"));
            put("contexts", getParticularColumnValueOfATestFromCSV("Reviews", "/resources/input-data/Users", 4, "contexts"));
            put("media", getParticularColumnValueOfATestFromCSV("Reviews", "/resources/input-data/Users", 4, "media"));
            put("needModeration", getParticularColumnValueOfATestFromCSV("Reviews", "/resources/input-data/Users", 4, "needModeration"));
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

        HashMap review = getReviewOfGivenReviewId(reviewId);
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

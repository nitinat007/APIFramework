package org.nitincompany.users.reviews.tests;

import io.restassured.response.ValidatableResponse;
import org.nitincompany.framework.readers.CSVParametersProvider;
import org.nitincompany.framework.readers.DataFileParameters;
import org.nitincompany.users.reviews.utils.ReviewSubmissionBean;
import org.testng.Assert;
import org.testng.annotations.*;

import java.util.HashMap;

import static org.nitincompany.users.reviews.utils.UsersReviewUtils.*;

/**
 * Author: nitinkumar
 * Created Date: 18/02/20
 * Info: Tests of submitReviewAsync API
 **/

public class SubmitReviewAsyncAPITestSuite extends UGCBase {

    @Test(description = "Test reads data from csv and sends submitReviewAsync request for each of the test data. ", dataProvider = "csv", dataProviderClass = CSVParametersProvider.class, groups = {"regression", "testSubmitReviewAsync"}, priority = 1)
    @DataFileParameters(name = "Reviews.csv", path = "/resources/input-data/Users")
    public void testSubmitReviewAsync(String testcaseNumber, String testcaseInfo, String errorMessage, String objectId, String objectName, String objectNames, String contentTypes, String language, String reviewText, String ratings, String overallRating, String maximumRating, String tags, String reviewerId, String reviewerName, String anonymous, String contexts, String media, String needModeration, String reviewId, String createdTimestampStart) {

        logger.log(" -- " + testcaseInfo + " --");
        ValidatableResponse response = usersReviewAPIFrameworkInstance.sendRequest("reviewAsyncServerService.submitReviewAsync", null, new HashMap<String, String>() {{
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
        if (!errorMessage.isEmpty()) { //For -ve TC, comparing errorMessage
            String errorMessageReceived = response.extract().jsonPath().get("error.message");
            Assert.assertTrue(errorMessageReceived.contains(errorMessage), "Response error message received does not contain expected error message :" + errorMessage);
        } else {
            String status = response.extract().jsonPath().get("result.status");
            Assert.assertTrue(status.equalsIgnoreCase("SUCCESS"), "Actual status in response is not SUCCESS. Status:" + status);
            String createdReviewId = response.extract().jsonPath().get("result.reviewId");
            Assert.assertTrue(isStringNotNullOrEmpty(createdReviewId), " reviewId is either empty or null. reviewId:" + createdReviewId);
            reviewSubmissionBean = new ReviewSubmissionBean(testcaseNumber, "reviewId", createdReviewId);
        }
    }

    @AfterMethod(description = "This updates the CSV with reviewId generated by each test. This value will further be used by tests (eg in getReviewAsync)")
    public void postTestDataInsertion() {
        updateCSVColumnValueOfTest("Reviews", "/resources/input-data/Users/", reviewSubmissionBean.testcaseNumber, reviewSubmissionBean.columnToUpdate, reviewSubmissionBean.valueToUpdate);
    }

}

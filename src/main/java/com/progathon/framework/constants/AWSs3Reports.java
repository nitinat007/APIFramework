package com.progathon.framework.constants;

import org.testng.Reporter;

import java.util.HashMap;

/**
 * Author: nitinkumar
 * Created Date: 31/03/20
 * Info: Contains info on automation report location in AWS S3
 * Sample Report URL: https://ap-southeast-1.console.aws.amazon.com/s3/object/qa-bucket-7351b3a2fcf2f428/qa-automation-reports/reports/runUsersReviewAPIAutomation/Result.html?region=ap-southeast-1&tab=overview
 **/

public class AWSs3Reports {
    String s3BucketName = "qa-bucket-7351b3a2fcf2f428";
    String regionID = "ap-southeast-1";
    HashMap<String, String> projectNameToAWSReportMapping = new HashMap<String, String>() {{
        put("Cinema", "runCinemaAPIAutomation");
        put("UsersReview", "runUsersReviewAPIAutomation");
        put("UsersComments", "runTestUserCommentsAPIAutomation");
        put("UsersBookmarks", "runTestUsersBookmarksAPIAutomation");
    }}; //currently 2nd argument is same as project's taskName

    public String getReportURLInAWS(String projectName) {
        String reportFolderNameInAws = projectNameToAWSReportMapping.get(projectName);
        if (reportFolderNameInAws == null) {
            Reporter.log("Please add project to AWS Report mapping. Report URL generated is incorrect", true);
        }
        String awsReportURL = "https://" + regionID + ".console.aws.amazon.com/s3/object/" + s3BucketName + "/qa-automation-reports/reports/" + reportFolderNameInAws + "/Result.html?region=" + regionID + "&tab=overview";
        return awsReportURL;
    }
}

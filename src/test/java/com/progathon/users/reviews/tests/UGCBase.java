package com.progathon.users.reviews.tests;

import com.progathon.framework.reporters.ExtentManager;
import com.progathon.users.reviews.utils.ReviewSubmissionBean;
import com.progathon.framework.communicators.Slack;
import com.progathon.framework.initializers.APIFramework;
import com.progathon.framework.reporters.Logger;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.asserts.SoftAssert;


import java.lang.reflect.Method;

/**
 * Author: nitinkumar
 * Created Date: 18/02/20
 * Info: Default behaviors of Users testcases. All Users testSuite Class should extend this class
 **/

public class UGCBase {

    APIFramework usersReviewAPIFrameworkInstance = new APIFramework("users");
    Logger logger = APIFramework.logger;
    ReviewSubmissionBean reviewSubmissionBean;
    SoftAssert softAssert;

    @BeforeMethod
    public void beforeMethod(Method method) {
        softAssert = new SoftAssert();
    }

    @Parameters("projectName")
    @AfterSuite(alwaysRun = true)
    public void afterSuite(@Optional String projectName) {
        ExtentManager.getInstance().flush();
        if (System.getProperty("Slack") != null && Boolean.parseBoolean(System.getProperty("Slack"))) {
            Slack slackUtils = new Slack();
            slackUtils.sendMessageToSlack(projectName);
        }
    }
}

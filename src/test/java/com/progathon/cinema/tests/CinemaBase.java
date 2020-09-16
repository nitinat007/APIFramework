package com.progathon.cinema.tests;

import com.progathon.framework.reporters.ExtentManager;
import com.progathon.framework.communicators.Slack;
import com.progathon.framework.initializers.APIFramework;
import com.progathon.framework.reporters.ExtentManager;
import com.progathon.framework.reporters.Logger;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.asserts.SoftAssert;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * Author: nitinkumar
 * Created Date: 10/12/19
 * Info: This is base class for cinema feature. This class needs to be extended by any test class of cinema
 **/

public class CinemaBase {
    Logger logger = APIFramework.logger;
    SoftAssert softAssert;

    @BeforeMethod
    public void beforeMethod(Method method) {
        softAssert = new SoftAssert();

        //logging description of test by reading annotation of test method
        Annotation[] annotations = method.getDeclaredAnnotations();
        String annotation = annotations[0].toString(); //annotation is present at 0th index
        int lengthOfDescription = "description".length();
        int lengthOfAnnotationString = annotation.length();
        for (int startingIndex = 0; startingIndex < lengthOfAnnotationString - lengthOfDescription; startingIndex++) {
            if (annotation.substring(startingIndex, startingIndex + lengthOfDescription).equals("description")) {
                String descriptionValue = annotation.substring(startingIndex).split("=")[1]; //fetching description value
                descriptionValue = descriptionValue.substring(0, descriptionValue.lastIndexOf(",")); //removed next key from end
                logger.log("---- <b>Test Description</b>: " + descriptionValue + " ----");
                break;
            }
        }
    }

    @Parameters("projectName")
    @AfterSuite(alwaysRun = true)
    public void afterSuite(@Optional String projectName) {
        ExtentManager.getInstance().flush();
        if (Boolean.parseBoolean(System.getProperty("Slack"))) {
            Slack slackUtils = new Slack();
            slackUtils.sendMessageToSlack(projectName);
        }

    }

}

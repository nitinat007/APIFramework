package com.progathon.framework.listeners;

import com.progathon.framework.reporters.ExtentTestManager;
import com.progathon.framework.reporters.Logger;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

/**
 * Author: nitinkumar
 * Created Date: 30/04/20
 * Info: Implementation of ITestListener
 **/

public class TestListener implements ITestListener{
    Logger logger = new Logger();
    public static int passed=0,failed=0,skipped=0;

    @Override
    public synchronized void onTestStart(ITestResult result) {
        ExtentTestManager.startTest(result.getName());
        logger.log("<b> ----- Starting test execution of : " + result.getTestClass().getName() + "."+ result.getName() + " ----- </b>");
    }

    @Override
    public synchronized void onTestSuccess(ITestResult result) {
        ExtentTestManager.getTest().pass("<font size=\"7\" color=\"#00ff00\">"+ result.getName() + " is passed"+"</font>");
        logger.log(result.getName()+" test :  PASS");
        passed++;
    }

    @Override
    public synchronized void onTestFailure(ITestResult result) {
        ExtentTestManager.getTest().fail(result.getThrowable());
        logger.log(result.getName()+" test : FAIL");
        failed++;
    }

    @Override
    public synchronized void onTestSkipped(ITestResult result) {
        ExtentTestManager.getTest().skip(result.getThrowable());
        logger.log(result.getName()+" test : SKIP");
        skipped++;
    }

    @Override
    public synchronized void onTestFailedButWithinSuccessPercentage(ITestResult result) {

    }

    @Override
    public synchronized void onStart(ITestContext context) {

    }

    @Override
    public synchronized void onFinish(ITestContext context) {

    }
}

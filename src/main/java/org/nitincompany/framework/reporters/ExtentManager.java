package org.nitincompany.framework.reporters;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;

import java.text.SimpleDateFormat;
import java.util.Date;


public class ExtentManager {

    private static ExtentReports extent;
    static String filepath = (System.getProperty("Jobname")!=null) ?
            System.getProperty("user.dir")+"/reports/"+System.getProperty("Jobname")+"/Result.html" :
            System.getProperty("user.dir")+"/reports/"+System.getProperty("Environment")+
                    new SimpleDateFormat("_yyyy-MM-dd_HH-mm_").format(new Date())+".html";
//    static String filepath =System.getProperty("user.dir")+"/reports/Result.html";

    static ExtentHtmlReporter htmlReporter = new ExtentHtmlReporter(filepath);

    public synchronized static ExtentReports getInstance() {
        if (extent == null) {
            extent = new ExtentReports();
           extent.attachReporter(htmlReporter);
        }
        return extent;
    }
}

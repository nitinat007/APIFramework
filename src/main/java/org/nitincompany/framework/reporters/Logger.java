package org.nitincompany.framework.reporters;

import com.aventstack.extentreports.Status;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import io.restassured.path.json.JsonPath;
import org.testng.Reporter;

/**
 * Author: nitinkumar
 * Created Date: 30/04/20
 * Info: Methods to log a message in Report
 **/

public class Logger {
    public synchronized void log(String message)
    {
        if(ExtentTestManager.getTest()==null)
        {
            ExtentTestManager.startTest("RunningSingleClass");
        }
        ExtentTestManager.getTest().log(Status.INFO,message);
        Reporter.log(message,1, true);

    }

    public void logError(String message,Throwable throwable)
    {

        ExtentTestManager.getTest().log(Status.ERROR,message);
        Reporter.log(message,1, true);
    }

    public void logRequest(String request)
    {
        ExtentTestManager.getTest().log(Status.INFO,"Here is the final request created : ");
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        ExtentTestManager.getTest().log(Status.INFO,"<pre>" + gson.toJson(request) + "</pre>");
        Reporter.log("Request Body: " + gson.toJson(request),1, true);
    }

    public synchronized void logRequest(JsonObject request)
    {
        ExtentTestManager.getTest().log(Status.INFO,"Here is the final request created : ");
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        ExtentTestManager.getTest().log(Status.INFO,"<pre>" + gson.toJson(request) + "</pre>");
        Reporter.log("Request Body: " + gson.toJson(request),1, true);
    }



    public  synchronized void logResponse(String response)
    {
        ExtentTestManager.getTest().log(Status.INFO,"Here is the Response received from API : ");
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        ExtentTestManager.getTest().log(Status.INFO,"<pre style= white-space: pre-wrap; word-break: keep-all;>" + gson.toJson(response) + "</pre>");
        Reporter.log("Response recieved: "+ gson.toJson(response),1, true);

    }

    public synchronized void logResponse(JsonPath response)
    {
        ExtentTestManager.getTest().log(Status.INFO,"Here is the Response received from API : ");
        //ExtentTestManager.getTest().log(Status.INFO,"<pre style= white-space: pre-wrap; word-break: keep-all;>"+response.prettyPrint()+"</pre>");
        ExtentTestManager.getTest().log(Status.INFO, getMarkup(response.prettyPrint()));
        Reporter.log("Response recieved: "+ response.prettyPrint(),1, true);

    }

    public String getMarkup(String content) {

        String markup =  "<ul class='collapsible' data-collapsible = 'expandable'> <li class='active'> <div class='collapsible-header active'><i class='material-icons'>zoom_in</i>Response from API</div> <div class = 'collapsible-body' style = 'display: block;'> <pre> %s </pre></div> </li> </ul>";

        //String button = "<div class= 'container'> <button type='button' class='btn btn-info' data-toggle='collapse' data-target='#demo'> Response </button> <div id='demo' class='collapse'> %s </div> </div>";

        String verySimple = "<div class= 'container'> <button type=\"button\" class=\"collapsible\">Open Collapsible</button> <div class=\"content\"> <pre>%s </pre> </div> </div>";
        return String.format(markup,content);
    }
}

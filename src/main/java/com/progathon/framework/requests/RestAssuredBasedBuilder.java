package com.progathon.framework.requests;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.progathon.apiRepository.APITemplate;
import com.progathon.framework.readers.JSONFileReader;
import com.progathon.framework.readers.PropertyReader;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import com.progathon.framework.reporters.Logger;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static io.restassured.RestAssured.given;

/**
 * Author: nitinkumar
 * Created Date: 30/04/20
 * Info: Utils to invoke APIs using RestAssured
 **/

public class RestAssuredBasedBuilder {

    public RequestSpecification request;
    public PropertyReader pReader;
    String baseUrl;
    public static ValidatableResponse response;
    Logger logger = new Logger();

    public RestAssuredBasedBuilder() {
    }

    public RestAssuredBasedBuilder(String filePath) {
        pReader = new PropertyReader(filePath);
        String server = pReader.get("url");
        baseUrl = "https://" + server;
        String origin = (String) pReader.get("origin");
        //String baseUrl= "http://"+server;
        request = new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .addHeader("Origin", origin)
                .setBaseUri(baseUrl)
                .build();
    }

    public ValidatableResponse sendPostRequest(APITemplate apiInfo, String body, PropertyReader featureProperties, String locale) {
        String origin = (String) featureProperties.get("origin");
        System.out.println("origin:" + origin + " baseURL:" + apiInfo.baseURL + " locale:" + locale);
        RequestSpecBuilder requestBuilder = new RequestSpecBuilder();
        requestBuilder.setContentType(ContentType.JSON);
        requestBuilder.setBaseUri(apiInfo.baseURL);
        requestBuilder.addHeader("Origin", origin);
        if (apiInfo.additionalHeaders != null) {
            requestBuilder.addHeaders(apiInfo.additionalHeaders);
        }
        if (apiInfo.parameterType != null && apiInfo.parameterType != "" && apiInfo.parameterType.equalsIgnoreCase("Query")) {
            requestBuilder.addQueryParams(apiInfo.parameters);
        }
        if (apiInfo.parameterType != null && apiInfo.parameterType != "" && apiInfo.parameterType.equalsIgnoreCase("Path")) {
            requestBuilder.addPathParams(apiInfo.parameters);
        }
        logger.log("URI: " + apiInfo.baseURL + (locale != null ? locale + "/" : "") + apiInfo.relativePath);
        request = given()
                .spec(requestBuilder.build())
                .body(body).log().all()
                .when();
        response = request
                .post(apiInfo.baseURL + (locale != null ? locale + "/" : "") + apiInfo.relativePath)
                .then()
                .log().everything();
        return response;
    }

    public ValidatableResponse sendGetRequest(APITemplate apiInfo, PropertyReader featureProperties, String locale) {
        String origin = (String) featureProperties.get("origin");
        System.out.println("origin:" + origin + " baseURL:" + apiInfo.baseURL);
        request = new RequestSpecBuilder()
                .addHeader("Origin", origin)
                .addHeaders(apiInfo.additionalHeaders)
                .setBaseUri("https://" + apiInfo.baseURL)
                .build().log().headers();

        response =
                given()
                        .spec(request)
                        .when()
                        .post(apiInfo.baseURL + (locale != null ? locale + "/" : "") + apiInfo.relativePath)
                        .then().log().all();
        return response;
    }

    /**
     * @param relativePathToRequestJsonFile Path to request json file
     * @param additionalBody                This HashMap is sent by test/testUtil containing entries to be replaced by this method
     * @param featureProperties             Hint: PropertyReader featureProperties = new PropertyReader(propertyFileRelativePath);
     * @return
     * @info Method creates Request body by reading json and replacing dynamic values (starting with $) with one provided by test/testUtils
     */
    public String createRequestBody(String relativePathToRequestJsonFile, HashMap<String, String> additionalBody, PropertyReader featureProperties) {
        JsonObject requestBody = null;
        String bodyOfRequest = "";
        try {
            JSONFileReader jsonFileReader = new JSONFileReader();
            requestBody = jsonFileReader.readJSONFiles(relativePathToRequestJsonFile);
            bodyOfRequest = requestBody.toString();

            if (additionalBody != null) {
                for (String key : additionalBody.keySet()) {
                    if (additionalBody.get(key) != null && !additionalBody.get(key).trim().contentEquals("")) {
                        bodyOfRequest = bodyOfRequest.replaceFirst("[$]" + key, additionalBody.get(key)); //replacing dynamic values with one supplied by test/testUtil
                    }
                }
            }

            //handling dynamic tvLifetime,nonce,tvSession,clientInterface of json request body.
            if (bodyOfRequest.contains("$tvLifetime"))
                bodyOfRequest = bodyOfRequest.replaceFirst("[$]tvLifetime", featureProperties.get("tvLifetime"));
            if (bodyOfRequest.contains("$nonce"))
                bodyOfRequest = bodyOfRequest.replaceFirst("[$]nonce", featureProperties.get("nonce"));
            if (bodyOfRequest.contains("$tvSession"))
                bodyOfRequest = bodyOfRequest.replaceFirst("[$]tvSession", featureProperties.get("tvSession"));
            if (bodyOfRequest.contains("$clientInterface"))
                bodyOfRequest = bodyOfRequest.replaceFirst("[$]clientInterface", featureProperties.get("clientInterface"));

            //Logic: checking if still some dynamic values (starting with $) left in requestBody and removing that property as it was not provided by test
            Matcher matcherOfPattern = Pattern.compile("\"[$][A-Za-z0-9]{1,}").matcher(bodyOfRequest);
            ArrayList<String> dynamicValuesInRequestBody = new ArrayList<String>();
            while (matcherOfPattern.find()) {
                String match = matcherOfPattern.group();
                dynamicValuesInRequestBody.add(match.substring(2, match.length())); //removing starting $ from matched string
            }
            bodyOfRequest = bodyOfRequest.
                    replace("\"[", "").           // After replacing dynamic values following changes are done
                    replace("]\"", "").           // * removing "[  and ]" characters
                    replace("\"{", "{").          // * removing double quotation mark (") before and after curly bracket   (eg: "contexts": "$contexts" to "contexts":"{"key1":"val1","key2":"val2"}")
                    replace("}\"", "}").
                    replace("[\"\"]", "[]").       // * removing double quotation marks from empty array
                    replace("[\"\"", "[\"").       // * removing one double quotation mark from square bracket followed by consecutive double quotation mark ("")  (eg in ["$val"] to [""val1","val2""] , extra " needs to be removed)
                    replace("\"\"]", "\"]").
                    replace("\"true\"", "true").   // * changes "true" to true and "false" to false
                    replace("\"false\"", "false");
            Gson gson = new Gson();
            JsonObject jsonObject = gson.fromJson(bodyOfRequest, JsonObject.class);
            if (dynamicValuesInRequestBody.size() != 0) {
                for (String keyToRemove : dynamicValuesInRequestBody) {
                    if (featureProperties.get("feature").equalsIgnoreCase("Users")) {
                        //in UGC, requests have 'params' Array of size 2. 0th position has client info and 1st position has filters.
                        if (jsonObject.getAsJsonArray("params").get(1).getAsJsonObject().get(keyToRemove) != null) {
                            if (!jsonObject.getAsJsonArray("params").get(1).getAsJsonObject().get(keyToRemove).isJsonArray()) {
                                jsonObject.getAsJsonArray("params").get(1).getAsJsonObject().remove(keyToRemove);
                            } else {
                                jsonObject.getAsJsonArray("params").get(1).getAsJsonObject().remove(keyToRemove);
                            }
                        } else {
                            for (String key : jsonObject.getAsJsonArray("params").get(1).getAsJsonObject().keySet()) {

                                if (jsonObject.getAsJsonArray("params").get(1).getAsJsonObject().get(key).isJsonArray()) {
                                    JsonArray jsonArrayInsideParams = jsonObject.getAsJsonArray("params").get(1).getAsJsonObject().get(key).getAsJsonArray();
                                    for (int currentPosition = 0; currentPosition < jsonArrayInsideParams.size(); currentPosition++) {
                                        if (jsonObject.getAsJsonArray("params").get(1).getAsJsonObject().get(key).getAsJsonArray().get(currentPosition).isJsonObject() && jsonObject.getAsJsonArray("params").get(1).getAsJsonObject().get(key).getAsJsonArray().get(currentPosition).getAsJsonObject().has(keyToRemove)) {
                                            jsonObject.getAsJsonArray("params").get(1).getAsJsonObject().get(key).getAsJsonArray().get(currentPosition).getAsJsonObject().remove(keyToRemove);
                                        }
                                    }

                                } else if (jsonObject.getAsJsonArray("params").get(1).getAsJsonObject().get(key).isJsonObject()) {
                                    jsonObject.getAsJsonArray("params").get(1).getAsJsonObject().get(key).getAsJsonObject().remove(keyToRemove);
                                }
                            }
                        }
                    } else {
                        if (jsonObject.getAsJsonObject("data").get(keyToRemove) != null) {
                            if (!jsonObject.getAsJsonObject("data").get(keyToRemove).isJsonArray()) {
                                jsonObject.getAsJsonObject("data").remove(keyToRemove);
                            } else { //if Json Array is empty putting: key=null instead of key=[]
                                jsonObject.getAsJsonObject("data").remove(keyToRemove);
                                jsonObject.getAsJsonObject("data").add(keyToRemove, null);
                            }
                        }

                        //Incase keyToRemove property is not directly under 'data', It needs to be removed from JsonObject within 'data'
                        for (String dataSubKey : jsonObject.getAsJsonObject("data").keySet()) {
                            if (jsonObject.get("data").getAsJsonObject().get(dataSubKey).isJsonObject() == true) {
                                jsonObject.get("data").getAsJsonObject().get(dataSubKey).getAsJsonObject().remove(keyToRemove);

                                if (jsonObject.getAsJsonObject("data").get(dataSubKey).getAsJsonObject().size() == 0) { // If no value for dataSubKey exists, remove the dataSubKey
                                    jsonObject.get("data").getAsJsonObject().remove(dataSubKey);
                                }
                            }
                        }
                    }
                }
            }
            bodyOfRequest = jsonObject.toString();

            /** System.out.println(" <*** \n request body below\n" + bodyOfRequest + " \n***>");**/
        } catch (FileNotFoundException e) {
            System.out.println("API call needs request body. Exception caught while reading " + relativePathToRequestJsonFile + ". Exception caught :" + e);
        }

        return bodyOfRequest;
    }
}

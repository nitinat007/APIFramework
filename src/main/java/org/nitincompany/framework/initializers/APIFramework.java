package org.nitincompany.framework.initializers;


import org.nitincompany.apiRepository.APITemplate;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import io.restassured.response.ValidatableResponse;
import org.nitincompany.framework.requests.RestAssuredBasedBuilder;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;


/**
 * Author: nitinkumar
 * Created Date: 08/11/19
 * Info: This initializes framework for a given feature/module
 **/

public class APIFramework extends BaseFrameworkInitializer {

    String featureName;
    String locale;
    APITemplate api = null; //api contains all info of the API
    Gson gson = new Gson();

    public APIFramework(String featureName) {
        super(featureName);
        this.featureName = featureName;
        // BasicConfigurator.configure();
    }

    public void setLocale(String locale) { //should be called by test if it needs to be run in different locale
        this.locale = locale;
    }

    public ValidatableResponse sendRequest(String apiName, HashMap<String, String> additionalHeaders, HashMap<String, String> additionalBody, String parameterType, HashMap<String, String> parameters) {
        ValidatableResponse validatableResponse = null;

        //At times apiName passed to this method is of type <APIName.methodName>, if so break that
        String[] apiNameAndMethod = apiName.split("\\.");
        apiName = apiNameAndMethod.length > 1 ? apiNameAndMethod[0] : apiName;
        String methodName = apiNameAndMethod.length > 1 ? apiNameAndMethod[1] : "";

        if (Thread.currentThread().getStackTrace()[2].getClassName().contains("Utils")) {
            logger.log(" <b>Pre-test:</b> Fetching test data from " + apiName + " API ..");
        }

        try {
            if (methodName.equals("")) {
                api = this.getAPIInfo(apiName, additionalHeaders, additionalBody, parameterType, parameters);
            } else {
                api = this.getAPIInfo(methodName, additionalHeaders, additionalBody, parameterType, parameters);
            }
            /* for debugging System.out.println("<**** \n Info of API Object below \n"+api+"\n****>");  */
            RestAssuredBasedBuilder requestBuilder = new RestAssuredBasedBuilder();
            switch (api.httpVerb.toUpperCase()) {
                case "POST":
                    String requestJsonPath = "/resources/request-json/" + featureName + "/" + (methodName.equals("") ? api.getClass().getSimpleName() : methodName) + ".json";
                    String requestBody = requestBuilder.createRequestBody(requestJsonPath, additionalBody, featureProperties);
                    logger.log("");
                    logger.logRequest(gson.fromJson(requestBody, JsonObject.class));
                    validatableResponse = requestBuilder.sendPostRequest(api, requestBody, featureProperties, locale);
                    break;
                case "GET":
                    validatableResponse = requestBuilder.sendGetRequest(api, featureProperties, locale);
                    break;
                default:
                    logger.log(api.httpVerb + " request yet to be handled in the framework");
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        logger.logResponse(validatableResponse.extract().body().jsonPath());
        setLocale(null); // reset locale after each call.
        return validatableResponse;
    }

    public String getRequestBody(HashMap<String, String> additionalBody, String RequestBodyJsonNameWithoutExtension) {
        RestAssuredBasedBuilder requestBuilder = new RestAssuredBasedBuilder();
        String requestJsonPath = "/resources/request-json/" + featureName + "/" + RequestBodyJsonNameWithoutExtension + ".json";
        String requestBody = requestBuilder.createRequestBody(requestJsonPath, additionalBody, featureProperties);
        return requestBody;
    }

    //method returns an instance of APITemplate containing all the info of given API (String apiName)
    public APITemplate getAPIInfo(String apiName, HashMap<String, String> additionalHeaders, HashMap<String, String> additionalBody, String parameterType, HashMap<String, String> parameters) throws ClassNotFoundException, NoSuchMethodException {
        Constructor APIClassConstructor = null;
        APITemplate APIInfo = null;
        try {
            Class APIClass = Class.forName("org.nitincompany.apiRepository." + featureName + "." + apiName);
            APIClassConstructor = APIClass.getConstructor(String.class, HashMap.class, HashMap.class, String.class, HashMap.class);
            APIInfo = (APITemplate) APIClassConstructor.newInstance(featureProperties.get("url"), additionalHeaders, additionalBody, parameterType, parameters);

        } catch (InstantiationException e) {
            e.printStackTrace();
            System.exit(1);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            System.exit(1);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            System.exit(1);
        }
        return APIInfo;
    }
/*
    public boolean validateSchema(ValidatableResponse res) {
        String schemaPath = "/resources/Schema/" + featureName + "/" + api.getClass().getSimpleName() + "Schema.json";
        try {
            JSONObject response = new JSONObject(res.extract().asString());
            String filePath = System.getProperty("user.dir") + schemaPath;
            ObjectMapper objectMapper = new ObjectMapper();
            InputStream inputStream = new FileInputStream(filePath);
            JsonNode rs = objectMapper.readTree(inputStream);
            JSONObject rawSchema = new JSONObject(rs.toString());
            Schema schema = SchemaLoader.load(rawSchema);
            schema.validate(response);
            Reporter.log("\nResponse Schema is as expected..\n ", 1, true);
            logger.log("Json schema Validated");
            return true;
        } catch (IOException e) {
            logger.log(" Exception in reading schema at " + schemaPath + ". Exception caught :" + e);
        } catch (Exception e) {
            logger.log(" Exception caught in schema validation: " + e);
        }
        return false;
    }

 */

}

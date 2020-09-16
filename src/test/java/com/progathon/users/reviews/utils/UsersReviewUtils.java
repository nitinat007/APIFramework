package com.progathon.users.reviews.utils;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;
import com.google.gson.*;
import com.progathon.users.reviews.utils.comparators.ReviewComparator;
import com.progathon.users.reviews.utils.filters.UserReviewFilters;
import io.restassured.response.ValidatableResponse;
import com.progathon.framework.initializers.APIFramework;

import java.io.*;
import java.util.*;


/**
 * Author: nitinkumar
 * Created Date: 17/02/20
 * Info: Utilities of Users TestCases
 **/

public class UsersReviewUtils {

    static ArrayList<String> reviewsListFromCSV = new ArrayList<>();
    static Gson gson = new Gson();

    public static String removeFirstAndLastChar(String str) { //Typically used to remove trailing and leading " character
        return str.trim().substring(1, str.length() - 1);
    }

    public static Map<String, String> stringToMap(String stringAsStructure) {

        stringAsStructure = stringAsStructure.replace("{", "").replace("}", "").replace("\"\"", "\"");
        Map<String, String> myMap = new HashMap<String, String>();
        String[] pairs = stringAsStructure.split(",");
        for (int i = 0; i < pairs.length; i++) {
            String pair = pairs[i];
            String[] keyValue = pair.split(":");
            myMap.put(keyValue[0], keyValue[1]);
        }
        return myMap;
    }

    public static void updateCSVColumnValueOfTest(String fileNameWithOutExtension, String filePath, String TestCaseNumber, String columnName, String newColumnValue) {

        String pathOfCSV = System.getProperty("user.dir") + filePath + fileNameWithOutExtension + ".csv";
        File csvFile = new File(pathOfCSV);
        BufferedReader br = null;
        int colNumToUpdate = -1;
        try {
            br = new BufferedReader(new FileReader(csvFile));
            String line = "";
            StringTokenizer st = null;
            int lineNumber = 0;
            int tokenNumber = 0;
            //Reading HeadRow and finding columnNumber for given columnName which needs to be updated
            if ((line = br.readLine()) != null) {
                lineNumber++;
                st = new StringTokenizer(line, ",");
                while (st.hasMoreTokens()) {
                    tokenNumber++;
                    String sd = st.nextToken();
                    if (sd.equalsIgnoreCase(columnName) || sd.substring(1, sd.length() - 1).equalsIgnoreCase(columnName)) {
                        colNumToUpdate = tokenNumber;
                    }
                }
            }

            // Read entire CSV , updating particular value and then replacing old with new
            CSVReader reader = new CSVReader(new FileReader(csvFile), ',');
            List<String[]> csvBody = reader.readAll();

            csvBody.get(Integer.parseInt(TestCaseNumber))[colNumToUpdate - 1] = newColumnValue;
            reader.close();

            CSVWriter writer = new CSVWriter(new FileWriter(csvFile), ',');
            writer.writeAll(csvBody);
            writer.flush();
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static StringTokenizer getParticularTestDataFromCSV(String fileNameWithOutExtension, String filePath, String TestCaseNumber) {

        String pathOfCSV = System.getProperty("user.dir") + filePath + fileNameWithOutExtension + ".csv";
        File csvFile = new File(pathOfCSV);
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(csvFile));
            String line = "";
            StringTokenizer st = null;
            int lineNumber = 0;
            //Reading line by line and returning the line if row number is equal to testcase number
            while ((line = br.readLine()) != null) {
                st = new StringTokenizer(line, ",");
                if (lineNumber == Integer.parseInt(TestCaseNumber)) {
                    return st;
                }
                lineNumber++;
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getParticularColumnValueOfATestFromCSV(String fileNameWithOutExtension, String filePath, int TestCaseNumber, String columnName) {
        if (filePath.charAt(filePath.length() - 1) != '/') {
            filePath = filePath + "/"; //append a slash in the end of filePath if it is missing
        }
        String pathOfCSV = System.getProperty("user.dir") + filePath + fileNameWithOutExtension + ".csv";
        File csvFile = new File(pathOfCSV);
        BufferedReader br = null;
        String columnValue = null;
        int colNumToFind = -1;
        try {
            br = new BufferedReader(new FileReader(csvFile));
            String line = "";
            StringTokenizer st = null;
            int lineNumber = 0;
            int tokenNumber = 0;
            //Reading HeadRow and finding columnNumber for given columnName
            if ((line = br.readLine()) != null) {
                lineNumber++;
                st = new StringTokenizer(line, ",");
                while (st.hasMoreTokens()) {
                    tokenNumber++;
                    String sd = st.nextToken();
                    if (sd.equalsIgnoreCase(columnName) || sd.substring(1, sd.length() - 1).equalsIgnoreCase(columnName)) {
                        colNumToFind = tokenNumber;
                    }
                }
            }

            // Read entire CSV , finding columnValue for the TestCaseNumber
            CSVReader reader = new CSVReader(new FileReader(csvFile), ',');
            List<String[]> csvBody = reader.readAll();

            columnValue = csvBody.get(TestCaseNumber)[colNumToFind - 1];
            reader.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return columnValue;
    }

    public static int getTotalTestDataFromCSV(String fileNameWithOutExtension, String filePath) {

        String pathOfCSV = System.getProperty("user.dir") + filePath + fileNameWithOutExtension + ".csv";
        File csvFile = new File(pathOfCSV);
        int size = 0;
        try {
            // Read entire CSV, find size
            CSVReader reader = new CSVReader(new FileReader(csvFile), ',');
            List<String[]> csvBody = reader.readAll();
            size = csvBody.size();
            reader.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return size;
    }

    public static boolean isStringNotNullOrEmpty(String str) {
        if (str == null || str.isEmpty())
            return false;
        return true;
    }

    public static ArrayList<String> compareTwoJsonStrings(String responseJson, String csvJson) {
        ArrayList<String> comparisionResult;
        ReviewComparator reviewComparator = new ReviewComparator();
        comparisionResult = reviewComparator.compareTwoReviews(responseJson, csvJson);
        return comparisionResult;
    }

    public static HashMap getReviewOfGivenReviewId(String reviewId) {
        APIFramework usersReviewAPIFrameworkInstance = new APIFramework("users");
        ValidatableResponse response = usersReviewAPIFrameworkInstance.sendRequest("reviewAsyncServerService.getReviewAsync", null, new HashMap<String, String>() {{
            put("reviewId", reviewId);
        }}, null, null);
        return response.extract().jsonPath().get("result.review");
    }

    public static ArrayList<String> getColumnValuesOfTestsAsList(String fileNameWithOutExtension, String filePath, String columnToFetch) {
        ArrayList<String> columnValuesAsListFromCSV = new ArrayList<>();
        int totalTestData = getTotalTestDataFromCSV(fileNameWithOutExtension, filePath);
        for (int i = 1; i < totalTestData; i++) {
            String columnValue = getParticularColumnValueOfATestFromCSV(fileNameWithOutExtension, filePath, i, columnToFetch);
            if (columnValue.length() > 0) {
                columnValuesAsListFromCSV.add(columnValue);
            }
        }
        return columnValuesAsListFromCSV;
    }

    public static JsonArray getReviewsFromCSVAfterApplyingFilters(String fileNameWithOutExtension, String filePath, List reviewsListFromResponse, String contentTypes, String context, String sortBy, String sortOrder, String skip, String limit, String reviewStatusSet, LimitsOnTimeStamp createdTimestampLimits) {
        int totalTestData = getTotalTestDataFromCSV(fileNameWithOutExtension, filePath);

        if (reviewsListFromCSV.size() == 0) { //means reviewsListFromCSV is not already read by any previous test
            for (int i = 1; i < totalTestData; i++) {
                String reviewId = getParticularColumnValueOfATestFromCSV(fileNameWithOutExtension, filePath, i, "reviewId");
                if (reviewId.length() > 0) {
                    HashMap review = getReviewOfGivenReviewId(reviewId);
                    String jsonSerializedReviewString = gson.toJson(review);
                    reviewsListFromCSV.add(jsonSerializedReviewString);
                }
            }
        }

        JsonArray reviewsArrayFromCSV = gson.fromJson(reviewsListFromCSV.toString(), JsonArray.class);

//        System.out.println("reviewsArrayFromCSV: " + reviewsArrayFromCSV);
//        System.out.print("ReviewIds in CSV : ");
//        for (int i = 0; i < reviewsArrayFromCSV.size(); i++) {
//            System.out.print(reviewsArrayFromCSV.get(i).getAsJsonObject().get("reviewId") + ",");
//        }

        //applying various filters - using Builder Pattern
//        System.out.println("\n ************* \n Applying contentTypes filters: " + contentTypes);
        reviewsArrayFromCSV = UserReviewFilters.applyContentTypesFilter(reviewsArrayFromCSV, contentTypes);

//        System.out.println(" ************* \n ReviewIds in CSV (after applyContentTypesFilter) : ");
//        for (int i = 0; i < reviewsArrayFromCSV.size(); i++) {
//            System.out.print(reviewsArrayFromCSV.get(i).getAsJsonObject().get("reviewId") + ",");
//        }
//
//        System.out.println("\n ************* \n Applying context filters: " + context);
        reviewsArrayFromCSV = UserReviewFilters.applyContextFilter(reviewsArrayFromCSV, context);
//
//        System.out.println(" ************* \n ReviewIds in CSV (after applyContextFilter) : ");
//        for (int i = 0; i < reviewsArrayFromCSV.size(); i++) {
//            System.out.print(reviewsArrayFromCSV.get(i).getAsJsonObject().get("reviewId") + ",");
//        }
//
//        System.out.println("\n ************* \n Applying sorting : sortBy=" + sortBy + " sortOrder=" + sortOrder);
        reviewsArrayFromCSV = UserReviewFilters.applySortingFilter(reviewsArrayFromCSV, sortBy, sortOrder);
//
//        System.out.println(" ************* \n ReviewIds in CSV (after applySortingFilter) : ");
//        for (int i = 0; i < reviewsArrayFromCSV.size(); i++) {
//            System.out.print(reviewsArrayFromCSV.get(i).getAsJsonObject().get("reviewId") + ",");
//        }
//
//        System.out.println("\n ************* \n Applying reviewStatusFilter : reviewStatusSet=" + reviewStatusSet );
        reviewsArrayFromCSV = UserReviewFilters.applyReviewStatusFilter(reviewsArrayFromCSV, reviewStatusSet);

//        System.out.println(" ************* \n ReviewIds in CSV (after applyReviewStatusFilter) : ");
//        for (int i = 0; i < reviewsArrayFromCSV.size(); i++) {
//            System.out.print(reviewsArrayFromCSV.get(i).getAsJsonObject().get("reviewId") + ",");
//        }

//        System.out.println("\n ************* \n Applying createdTimestampLimits ");
        reviewsArrayFromCSV = UserReviewFilters.applyTimeStampLimitFilter(reviewsArrayFromCSV, createdTimestampLimits);

//        System.out.println(" ************* \n ReviewIds in CSV (after createdTimestampLimits) : ");
//        for (int i = 0; i < reviewsArrayFromCSV.size(); i++) {
//            System.out.print(reviewsArrayFromCSV.get(i).getAsJsonObject().get("reviewId") + ",");
//        }

//        System.out.println("\n ************* \n Applying applySkip_LimitFilter : skip=" + skip + " limit=" + limit);
        reviewsArrayFromCSV = UserReviewFilters.applySkip_LimitFilter(reviewsArrayFromCSV, skip, limit);
//
//        System.out.println(" ************* \n ReviewIds in CSV (after applySkip_LimitFilter) : ");
//        for (int i = 0; i < reviewsArrayFromCSV.size(); i++) {
//            System.out.print(reviewsArrayFromCSV.get(i).getAsJsonObject().get("reviewId") + ",");
//        }
//        System.out.println("\n******** All filters applied *******");

        return reviewsArrayFromCSV;
    }

    public static class LimitsOnTimeStamp {
        public enum timeStampType {
            createdTimestamp,
            updatedTimestamp
        }

        public String start = "0";
        public String end = "0";
        public timeStampType type;

        public LimitsOnTimeStamp(timeStampType type) {
            this.type = type;
        }
    }
}




package com.progathon.users.reviews.utils.comparators;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.*;

/**
 * Author: nitinkumar
 * Created Date: 20/02/20
 * Info: Comparator of reviews
 **/

public class ReviewComparator {

    public ArrayList<String> compareTwoReviews(String responseNode, String csvBodyNode) {
        ArrayList<String> comparisionResult = new ArrayList<String>();

        Gson gson = new Gson();
        JsonObject responseNodeJsonObject = gson.fromJson(responseNode, JsonObject.class);
        JsonObject csvBodyNodeJsonObject = gson.fromJson(csvBodyNode, JsonObject.class);

        JsonObject csvReviewValue = csvBodyNodeJsonObject.getAsJsonArray("params").get(1).getAsJsonObject(); // will compare all values of csvReviewValue & responseReviewValue
        JsonObject responseReviewValue = responseNodeJsonObject.getAsJsonObject("result").getAsJsonObject("review");
        //Comparing objectType
        String objectTypeInResponse = responseReviewValue.get("objectType").getAsString();
        String objectTypeInCsv = csvBodyNodeJsonObject.getAsJsonArray("params").get(0).getAsJsonObject().get("objectType").getAsString();
        if (!objectTypeInResponse.equalsIgnoreCase(objectTypeInCsv)) {
            comparisionResult.add("objectType is unequal: Response has " + objectTypeInResponse + ", CSV has " + objectTypeInCsv);
        }
        //Comparing objectId
        String objectIdInResponse = responseReviewValue.get("objectId").getAsString();
        String objectIdInCsv = csvReviewValue.get("objectId").getAsString();
        if (!objectIdInResponse.equalsIgnoreCase(objectIdInCsv)) {
            comparisionResult.add("objectId is unequal: Response has " + objectIdInResponse + ", CSV has " + objectIdInCsv);
        }
        //Comparing objectNames
        JsonObject objectNamesInResponse = responseReviewValue.getAsJsonObject("objectNames");
        JsonObject objectNamesInCsv = csvReviewValue.getAsJsonObject("objectNames");
        Set<String> csvObjectNamesKeys = objectNamesInCsv.keySet();
        for (String objectName : csvObjectNamesKeys) {
            if (!objectNamesInCsv.get(objectName).getAsString().equalsIgnoreCase(objectNamesInResponse.get(objectName).getAsString())) {
                comparisionResult.add("objectNames is unequal: Response has " + objectNamesInResponse + ", CSV has " + objectNamesInCsv);
                break;
            }
        }
        //Comparing contentTypes Array
        JsonArray contentTypesInResponse = responseReviewValue.getAsJsonArray("contentTypes");
        JsonArray contentTypesInCsv = csvReviewValue.getAsJsonArray("contentTypes");
        for (JsonElement element : contentTypesInResponse) {
            if (contentTypesInCsv.contains(element)) {
                contentTypesInCsv.remove(element);
            } else {
                break;
            }
        }
        if (contentTypesInCsv.size() > 0) {
            comparisionResult.add("contentTypes is unequal: Response has " + contentTypesInResponse + ", CSV has " + contentTypesInCsv);
        }
        //Comparing language
        String languageInResponse = responseReviewValue.get("language").getAsString();
        String languageInCsv = csvReviewValue.get("language").getAsString();
        if (!languageInResponse.equalsIgnoreCase(languageInCsv)) {
            comparisionResult.add("language is unequal: Response has " + languageInResponse + ", CSV has " + languageInCsv);
        }
        //Comparing reviewText
        String reviewTextInResponse = responseReviewValue.get("reviewText").getAsString();
        String reviewTextInCsv = csvReviewValue.get("reviewText").getAsString();
        if (!reviewTextInResponse.equalsIgnoreCase(reviewTextInCsv)) {
            comparisionResult.add("reviewText is unequal: Response has " + reviewTextInResponse + ", CSV has " + reviewTextInCsv);
        }
        //Comparing overallRating
        String overallRatingInResponse = responseReviewValue.get("overallRating").getAsString();
        String overallRatingInCsv = csvReviewValue.get("overallRating").getAsString();
        if (Double.parseDouble(overallRatingInResponse) != Double.parseDouble(overallRatingInCsv)) {
            comparisionResult.add("overallRating is unequal: Response has " + overallRatingInResponse + ", CSV has " + overallRatingInCsv);
        }
        //Comparing maximumRating
        double maximumRatingInResponse = responseReviewValue.get("maximumRating").getAsDouble();
        double maximumRatingInCsv = csvReviewValue.get("maximumRating").getAsDouble();
        if (maximumRatingInResponse != maximumRatingInCsv) {
            comparisionResult.add("maximumRating is unequal: Response has " + maximumRatingInResponse + ", CSV has " + maximumRatingInCsv);
        }
        //Comparing ratings
        JsonElement ratingsIdInCsv = csvReviewValue.getAsJsonArray("ratings") != null ? csvReviewValue.getAsJsonArray("ratings").get(0).getAsJsonObject().get("ratingId") : null;
        if (ratingsIdInCsv != null) { //Checked only if ratings were present in Submit review request
            JsonObject ratingsInResponse = responseReviewValue.getAsJsonObject("ratings") != null ? responseReviewValue.getAsJsonObject("ratings").getAsJsonObject(ratingsIdInCsv.getAsString()) : null;
            JsonObject ratingsInCsv = csvReviewValue.getAsJsonArray("ratings").get(0).getAsJsonObject();
            if (!ratingsInResponse.get("ratingId").equals(ratingsInCsv.get("ratingId"))) {
                comparisionResult.add("ratingId in ratings is unequal: Response has " + ratingsInResponse.get("ratingId") + ", CSV has " + ratingsInCsv.get("ratingId"));
            }
            if (ratingsInResponse.get("ratingScore").getAsDouble() != ratingsInCsv.get("ratingScore").getAsDouble()) {
                comparisionResult.add("ratingScore in ratings is unequal: Response has " + ratingsInResponse.get("ratingScore").getAsDouble() + ", CSV has " + ratingsInCsv.get("ratingScore").getAsDouble());
            }
            if (!ratingsInResponse.get("ratingReview").equals(ratingsInCsv.get("ratingReview"))) {
                comparisionResult.add("ratingReview in ratings is unequal: Response has " + ratingsInResponse.get("ratingReview") + ", CSV has " + ratingsInCsv.get("ratingReview"));
            }
            if (!ratingsInResponse.get("ratingReviewTextOriginal").equals(ratingsInCsv.get("ratingReviewTextOriginal"))) {
                comparisionResult.add("ratingReviewTextOriginal in ratings is unequal: Response has " + ratingsInResponse.get("ratingReviewTextOriginal") + ", CSV has " + ratingsInCsv.get("ratingReviewTextOriginal"));
            }
            if (!ratingsInResponse.get("ratingOptionId").equals(ratingsInCsv.get("ratingOptionId"))) {
                comparisionResult.add("ratingOptionId in ratings is unequal: Response has " + ratingsInResponse.get("ratingOptionId") + ", CSV has " + ratingsInCsv.get("ratingOptionId"));
            }
            if (!ratingsInResponse.get("ratingTags").equals(ratingsInCsv.get("ratingTags"))) {
                comparisionResult.add("ratingTags in ratings is unequal: Response has " + ratingsInResponse.get("ratingTags") + ", CSV has " + ratingsInCsv.get("ratingTags"));
            }
        }
        //Comparing tags
        String tagsIdInCsv = csvReviewValue.getAsJsonArray("tags").get(0).getAsJsonObject().get("tagId").getAsString();
        JsonObject tagsInResponse = responseReviewValue.getAsJsonObject("tags").getAsJsonObject(tagsIdInCsv);
        JsonObject tagsInCsv = csvReviewValue.getAsJsonArray("tags").get(0).getAsJsonObject();
        if (!tagsInResponse.get("tagId").equals(tagsInCsv.get("tagId"))) {
            comparisionResult.add("tagId in tags is unequal: Response has " + tagsInResponse.get("tagId") + ", CSV has " + tagsInCsv.get("tagId"));
        }
        //Comparing reviewerId
        String reviewerIdInResponse = responseReviewValue.get("reviewerId").getAsString();
        String reviewerIdInCsv = csvReviewValue.get("reviewerId").getAsString();
        if (!reviewerIdInResponse.equalsIgnoreCase(reviewerIdInCsv)) {
            comparisionResult.add("reviewerId is unequal: Response has " + reviewerIdInResponse + ", CSV has " + reviewerIdInCsv);
        }
        //Comparing reviewerName
        String reviewerNameInResponse = responseReviewValue.get("reviewerName").getAsString();
        String reviewerNameInCsv = csvReviewValue.get("reviewerName").getAsString();
        if (!reviewerNameInResponse.equalsIgnoreCase(reviewerNameInCsv)) {
            comparisionResult.add("reviewerName is unequal: Response has " + reviewerNameInResponse + ", CSV has " + reviewerNameInCsv);
        }
        //Comparing anonymous
        boolean anonymousInResponse = responseReviewValue.get("anonymous").getAsBoolean();
        boolean anonymousInCsv = csvReviewValue.get("anonymous").getAsBoolean();
        if (anonymousInResponse != anonymousInCsv) {
            comparisionResult.add("anonymous is unequal: Response has " + anonymousInResponse + ", CSV has " + anonymousInCsv);
        }
        //Comparing context
        JsonObject contextInResponse = responseReviewValue.getAsJsonObject("context");
        JsonObject contextInCsv = csvReviewValue.getAsJsonObject("contexts");
        if (contextInCsv != null) { //check only if context is present in csv
            if (!contextInResponse.get("bookingId").getAsString().equalsIgnoreCase(contextInCsv.get("bookingId").getAsString())) {
                comparisionResult.add("bookingId in context is unequal: Response has " + contextInResponse.get("bookingId").getAsString() + ", CSV has " + contextInCsv.get("bookingId").getAsString());
            }
            if (!contextInResponse.get("country").getAsString().equalsIgnoreCase(contextInCsv.get("country").getAsString())) {
                comparisionResult.add("country in context is unequal: Response has " + contextInResponse.get("country").getAsString() + ", CSV has " + contextInCsv.get("country").getAsString());
            }
            if (!contextInResponse.get("city").getAsString().equalsIgnoreCase(contextInCsv.get("city").getAsString())) {
                comparisionResult.add("city in context is unequal: Response has " + contextInResponse.get("city").getAsString() + ", CSV has " + contextInCsv.get("city").getAsString());
            }
            if (!contextInResponse.get("geoid").getAsString().equalsIgnoreCase(contextInCsv.get("geoid").getAsString())) {
                comparisionResult.add("geoid in context is unequal: Response has " + contextInResponse.get("geoid").getAsString() + ", CSV has " + contextInCsv.get("geoid").getAsString());
            }
            if (!contextInResponse.get("BookingTimeStamp").getAsString().equalsIgnoreCase(contextInCsv.get("BookingTimeStamp").getAsString())) {
                comparisionResult.add("BookingTimeStamp in context is unequal: Response has " + contextInResponse.get("BookingTimeStamp").getAsString() + ", CSV has " + contextInCsv.get("BookingTimeStamp").getAsString());
            }
            if (!contextInResponse.get("quality").getAsString().equalsIgnoreCase(contextInCsv.get("quality").getAsString())) {
                comparisionResult.add("quality in context is unequal: Response has " + contextInResponse.get("quality").getAsString() + ", CSV has " + contextInCsv.get("quality").getAsString());
            }
        }

        //comparing media
        JsonObject mediaInResponse = responseReviewValue.getAsJsonArray("media").get(0).getAsJsonObject(); //media info at 0th position
        JsonObject mediaInCsv = csvReviewValue.getAsJsonArray("media").get(0).getAsJsonObject(); //media info at 0th position
        if (mediaInCsv != null) { //Check only if media were present in csv
            for (String mediaFieldInCsv : mediaInCsv.keySet()) { //compare media fields b/w csv & response
                if (mediaInResponse.get(mediaFieldInCsv) != null) {
                    if (!mediaInResponse.get(mediaFieldInCsv).isJsonObject() && !mediaInResponse.get(mediaFieldInCsv).getAsString().equalsIgnoreCase(mediaInCsv.get(mediaFieldInCsv).getAsString())) {
                        comparisionResult.add("media field '" + mediaFieldInCsv + "' is unequal: Response has " + mediaInResponse.get(mediaFieldInCsv).getAsString() + ", CSV has " + mediaInCsv.get(mediaFieldInCsv).getAsString());
                    } else if (mediaInResponse.get(mediaFieldInCsv).isJsonObject()) {
                        JsonObject contextObjectInResponse = mediaInResponse.get(mediaFieldInCsv).getAsJsonObject();
                        JsonObject contextObjectInCSV = mediaInCsv.get(mediaFieldInCsv).getAsJsonObject();
                        for (String contextObjectFieldCSV : contextObjectInCSV.keySet()) {
                            if (!contextObjectInCSV.get(contextObjectFieldCSV).getAsString().equalsIgnoreCase(contextObjectInResponse.get(contextObjectFieldCSV).getAsString())) {
                                comparisionResult.add("media field '" + contextObjectFieldCSV + "' is unequal: Response has " + contextObjectInResponse.get(contextObjectFieldCSV).getAsString() + ", CSV has " + contextObjectInCSV.get(contextObjectFieldCSV).getAsString());
                            }
                        }
                    }
                } else {
                    comparisionResult.add("media field '" + mediaFieldInCsv + "' is unequal: Response has " + mediaInResponse.get(mediaFieldInCsv) + ", CSV has " + mediaInCsv.get(mediaFieldInCsv));
                }
            }
        }
        return comparisionResult;
    }
}

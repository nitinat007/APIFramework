package org.nitincompany.users.reviews.utils.filters;

import com.google.gson.*;
import org.nitincompany.users.reviews.utils.UsersReviewUtils;
import org.nitincompany.users.reviews.utils.comparators.UserComparators;

import java.util.*;

/**
 * Author: nitinkumar
 * Created Date: 13/03/20
 * Info: Class contains filters to sort reviews. Each filter returns Reviews as Array based on filtering condition
 **/

public class UserReviewFilters {

    static Gson gson = new Gson();

    public static JsonArray applyContentTypesFilter(JsonArray reviewsArrayToFilter, String contentTypesInRequestFilter) {

        String[] contentTypesInRequestFilterAsArray = contentTypesInRequestFilter.trim().split(",");
        JsonArray reviewsArrayToReturn = reviewsArrayToFilter.deepCopy();
//        System.out.println("Initial size of reviewsArrayToReturn is " + reviewsArrayToReturn.size());
        if (contentTypesInRequestFilter.length() > 0) {
            for (JsonElement review : reviewsArrayToFilter) {
                JsonArray contentTypesInReview = review.getAsJsonObject().getAsJsonArray("contentTypes");
                String reviewId = review.getAsJsonObject().get("reviewId").getAsString();
//                System.out.println("\n Evaluating for reviewId " + reviewId);
                for (String aContentTypeValueFromFilter : contentTypesInRequestFilterAsArray) {

//                    System.out.println(" aContentTypeValueFromFilter: " + aContentTypeValueFromFilter);
                    JsonElement aContentTypeValueFromFilterAsJsonElement = new JsonParser().parse(aContentTypeValueFromFilter);
                    if (contentTypesInReview.contains(aContentTypeValueFromFilterAsJsonElement)) {
//                        System.out.println("contentTypesInReview : " + contentTypesInReview + " contains aContentTypeValueFromFilterAsJsonElement :" + aContentTypeValueFromFilterAsJsonElement + " . Retaining");
                    } else {
//                        System.out.println("Removing reviewId :" + reviewId + " as contentTypesInReview : " + contentTypesInReview + " doesn't contains contentTypesInRequestFilterAsArray : " + Arrays.toString(contentTypesInRequestFilterAsArray));
                        reviewsArrayToReturn.remove(review);
//                        System.out.println("Now count of reviewsArrayToReturn is " + reviewsArrayToReturn.size());
                        break;
                    }
                }
            }
        }
        return reviewsArrayToReturn;
    }

    public static JsonArray applyContextFilter(JsonArray reviewsArrayToFilter, String context) {
        if (context.length() > 0) {
            HashMap<String, Object> contextInFilterAsMap = new Gson().fromJson(context, HashMap.class);
            JsonArray reviewsArrayToReturn = reviewsArrayToFilter.deepCopy();
//            System.out.println("Initial size of reviewsArrayToReturn is " + reviewsArrayToReturn.size());

            for (JsonElement review : reviewsArrayToFilter) {
                JsonObject contextInReview = review.getAsJsonObject().getAsJsonObject("context");
                HashMap<String, Object> contextInReviewAsMap = new Gson().fromJson(contextInReview.toString(), HashMap.class);
                String reviewId = review.getAsJsonObject().get("reviewId").getAsString();
//                System.out.println("\n Evaluating for reviewId " + reviewId);
                boolean contextComparisionValue = true;
                for (String keyInContext : contextInFilterAsMap.keySet()) {
                    if (!contextInFilterAsMap.get(keyInContext).equals(contextInReviewAsMap.get(keyInContext))) {
                        contextComparisionValue = false;
                    }
                }
                if (contextComparisionValue == true) {
//                    System.out.println("Retaining reviewId " + reviewId + " as context match");
                } else {
                    reviewsArrayToReturn.remove(review);
//                    System.out.println("Removing reviewId " + reviewId + " as context do not match.\n contextInFilterAsMap: " + contextInFilterAsMap + "\n contextInReviewAsMap: " + contextInReviewAsMap);
//                    System.out.println("Now count of reviewsArrayToReturn is " + reviewsArrayToReturn.size());
                }
            }
            return reviewsArrayToReturn;
        } else {
            return reviewsArrayToFilter; //context filter does not apply
        }
    }

    public static JsonArray applyReviewStatusFilter(JsonArray reviewsArrayToFilter, String reviewStatusSet) {
        if (reviewStatusSet.length() > 0) {
            ArrayList<String> contextInFilterAsList = new Gson().fromJson("[" + reviewStatusSet + "]", ArrayList.class);
            JsonArray reviewsArrayToReturn = reviewsArrayToFilter.deepCopy();
//            System.out.println("Initial size of reviewsArrayToReturn is " + reviewsArrayToReturn.size());

            for (JsonElement review : reviewsArrayToFilter) {
                String reviewId = review.getAsJsonObject().get("reviewId").getAsString();
                String statusInReview = review.getAsJsonObject().get("status").getAsString();
//                System.out.println("\n Evaluating for reviewId " + reviewId);
                if (contextInFilterAsList.contains(statusInReview)) {
//                    System.out.println("Retaining reviewId " + reviewId + " as reviewStatus match");
                } else {
                    reviewsArrayToReturn.remove(review);
//                    System.out.println("Removing reviewId " + reviewId + " as status do not match.\n contextInFilterAsList: " + contextInFilterAsList + "\n statusInReview: " + statusInReview);
//                    System.out.println("Now count of reviewsArrayToReturn is " + reviewsArrayToReturn.size());
                }
            }
            return reviewsArrayToReturn;
        } else {
            return reviewsArrayToFilter; //context filter does not apply
        }
    }

    public static JsonArray applyTimeStampLimitFilter(JsonArray reviewsArrayToFilter, UsersReviewUtils.LimitsOnTimeStamp createdTimestampLimits) {
//        System.out.println("start :" + createdTimestampLimits.start + " end:" + createdTimestampLimits.end);
        JsonArray reviewsArrayToReturn = reviewsArrayToFilter.deepCopy();
        if (Double.parseDouble(createdTimestampLimits.start) > 0) {
            for (JsonElement review : reviewsArrayToFilter) {
                String reviewId = review.getAsJsonObject().get("reviewId").getAsString();
                String createdTimestamp = review.getAsJsonObject().get("createdTimestamp").getAsString();
//                System.out.println("\n Evaluating for reviewId " + reviewId);
                if (Double.parseDouble(createdTimestampLimits.start) > Double.parseDouble(createdTimestamp)) {
                    reviewsArrayToReturn.remove(review);
//                    System.out.println("Removing reviewId " + reviewId );
//                    System.out.println("Now count of reviewsArrayToReturn is " + reviewsArrayToReturn.size());
                }
            }
            return reviewsArrayToReturn;
        } else if (Double.parseDouble(createdTimestampLimits.end) > 0) {
            for (JsonElement review : reviewsArrayToFilter) {
                String reviewId = review.getAsJsonObject().get("reviewId").getAsString();
                String createdTimestamp = review.getAsJsonObject().get("createdTimestamp").getAsString();
//                System.out.println("\n Evaluating for reviewId " + reviewId);
                if (Double.parseDouble(createdTimestampLimits.end) < Double.parseDouble(createdTimestamp)) {
                    reviewsArrayToReturn.remove(review);
//                    System.out.println("Removing reviewId " + reviewId );
//                    System.out.println("Now count of reviewsArrayToReturn is " + reviewsArrayToReturn.size());
                }
            }
            return reviewsArrayToReturn;
        }
        return reviewsArrayToFilter; // filter does not apply
    }

    public static JsonArray applySortingFilter(JsonArray reviewsArrayToFilter, String sortByInRequestFilter, String sortOrderInRequestFilter) {

        JsonArray reviewsArrayToReturn = reviewsArrayToFilter.deepCopy();
//        System.out.println("Initial size of reviewsArrayToReturn is " + reviewsArrayToReturn.size());
        List<JsonElement> listOfReviews = new ArrayList<JsonElement>();
        for (int i = 0; i < reviewsArrayToFilter.size(); i++) {
            listOfReviews.add(reviewsArrayToFilter.get(i));
        }
        if (sortByInRequestFilter.equalsIgnoreCase("SUBMISSION_TIMESTAMP")) {
            if (sortOrderInRequestFilter.equalsIgnoreCase("ASCENDING")) {
                Collections.sort(listOfReviews, Collections.reverseOrder(new UserComparators.CreatedTimeStampComparator()));
            } else {
                Collections.sort(listOfReviews, new UserComparators.CreatedTimeStampComparator());
            }
        } else if (sortByInRequestFilter.equalsIgnoreCase("UPDATED_TIMESTAMP")) {
            if (sortOrderInRequestFilter.equalsIgnoreCase("ASCENDING")) {
                Collections.sort(listOfReviews, Collections.reverseOrder(new UserComparators.UpdatedTimeStampComparator()));
            } else {
                Collections.sort(listOfReviews, new UserComparators.UpdatedTimeStampComparator());
            }
        } else if (sortByInRequestFilter.equalsIgnoreCase("OVERALL_RATING")) {
            if (sortOrderInRequestFilter.equalsIgnoreCase("ASCENDING")) {
                Collections.sort(listOfReviews, Collections.reverseOrder(new UserComparators.OverallRatingComparator()));
            } else {
                Collections.sort(listOfReviews, new UserComparators.OverallRatingComparator());
            }
        } else {
            Collections.sort(listOfReviews, new UserComparators.CreatedTimeStampComparator());
        }

        reviewsArrayToReturn = gson.toJsonTree(listOfReviews).getAsJsonArray();
        return reviewsArrayToReturn;
    }

    public static JsonArray applySkip_LimitFilter(JsonArray reviewsArrayToFilter, String skip, String limit) {

        JsonArray reviewsArrayToReturn = reviewsArrayToFilter.deepCopy();
//        System.out.println("Initial size of reviewsArrayToReturn is " + reviewsArrayToReturn.size());
        //setting default skip & limit if it is not already set
        if (skip.length() == 0 || Integer.parseInt(skip) < 0) {
            skip = "0";
        }
        if (limit.length() == 0 || Integer.parseInt(limit) == 0) {
            limit = "10";
        }
        if (skip.length() > 0 && limit.length() > 0) {
            int numOfRemoval = 0;
            for (int countOfReviewsArrayToFilter = 0; countOfReviewsArrayToFilter < reviewsArrayToFilter.size(); countOfReviewsArrayToFilter++) {
                if (countOfReviewsArrayToFilter < Integer.parseInt(skip) || countOfReviewsArrayToFilter >= (Integer.parseInt(limit) + Integer.parseInt(skip))) {
//                    System.out.println("Removing review at index " + countOfReviewsArrayToFilter);
                    reviewsArrayToReturn.remove(countOfReviewsArrayToFilter - numOfRemoval);
                    numOfRemoval++;
                }
            }
        }
        return reviewsArrayToReturn;
    }

}

package org.nitincompany.users.reviews.utils.comparators;

import com.google.gson.JsonElement;

import java.util.Comparator;

/**
 * Author: nitinkumar
 * Created Date: 13/03/20
 * Info: Contains comparators used to sort Reviews
 **/

public class UserComparators {

    public static class CreatedTimeStampComparator implements Comparator<JsonElement> {
        @Override
        public int compare(JsonElement o1, JsonElement o2) {
            double createdTimestamp1 = o1.getAsJsonObject().get("createdTimestamp").getAsDouble();
            double createdTimestamp2 = o2.getAsJsonObject().get("createdTimestamp").getAsDouble();
            if (createdTimestamp1 > createdTimestamp2) {
                return -1;
            } else {
                return 1;
            }
        }
    }

    public static class UpdatedTimeStampComparator implements Comparator<JsonElement> {
        @Override
        public int compare(JsonElement o1, JsonElement o2) {
            double updatedTimestamp1 = o1.getAsJsonObject().get("updatedTimestamp").getAsDouble();
            double updatedTimestamp2 = o2.getAsJsonObject().get("updatedTimestamp").getAsDouble();
            if (updatedTimestamp1 > updatedTimestamp2) {
                return -1;
            } else {
                return 1;
            }
        }
    }

    public static class OverallRatingComparator implements Comparator<JsonElement> {

        @Override
        public int compare(JsonElement o1, JsonElement o2) {
            double overallRating1 = o1.getAsJsonObject().get("overallRating").getAsDouble();
            double overallRating2 = o2.getAsJsonObject().get("overallRating").getAsDouble();
            if (overallRating1 > overallRating2) {
                return -1;
            } else {
                return 1;
            }
        }
    }
}





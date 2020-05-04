package org.nitincompany.cinema.utils;

import io.restassured.response.ValidatableResponse;
import org.nitincompany.cinema.constants.CinemaConstants;
import org.nitincompany.cinema.utils.requestFields.CinemaBookSeatRequestFields;
import org.nitincompany.cinema.utils.requestFields.CinemaGetScheduleSummaryRequestFields;
import org.nitincompany.framework.initializers.APIFramework;
import org.nitincompany.framework.reporters.Logger;


import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Author: nitinkumar
 * Created Date: 28/11/19
 * Info: Contains utility methods of cinema
 **/

public class CinemaUtils {

    static APIFramework cinemaAPIFrameworkInstance = new APIFramework("cinema");
    static ValidatableResponse responseOflandingPageQuickBuy;
    static String theatreIdOfPreviouslyTrackedLandingPageQuickBuy = null; //Introduced to reduce API calls.
    static LinkedHashMap<String, CinemaGetScheduleSummaryRequestFields> countryAndOneBookableMovieMap = new LinkedHashMap<>();
    static Logger logger = new Logger();

    //utility to check if a List is sorted
    public static <T extends List> boolean isSorted(T collectionOfObject) {
        T clonedCollectionOfObject = (T) collectionOfObject.stream().collect(Collectors.toList());
        Collections.sort(clonedCollectionOfObject, new Comparator<Object>() {
            @Override
            public int compare(Object o1, Object o2) {
                return o1.toString().toLowerCase().compareTo(o2.toString().toLowerCase());
            }
        });
        //System.out.println(" Order in Response:"+ collectionOfObject+" \n Expected order   :"+clonedCollectionOfObject+"\n");
        return clonedCollectionOfObject.equals(collectionOfObject);
    }

    public static ArrayList<String> getMovieIdsOfTheatre(String theatreId) {
        ArrayList<String> movieIds = new ArrayList<String>();
        if (responseOflandingPageQuickBuy == null || theatreIdOfPreviouslyTrackedLandingPageQuickBuy != theatreId) {
            ValidatableResponse response = cinemaAPIFrameworkInstance.sendRequest("landingPageQuickBuy", null, new HashMap<String, String>() {{
                put("theatreId", theatreId);
            }}, null, null);
            response.assertThat().statusCode(200);
            responseOflandingPageQuickBuy = response;
        }
        List<Object> listOfMovies = responseOflandingPageQuickBuy.extract().jsonPath().getList("data.movies");
        for (int movieCount = 0; movieCount < listOfMovies.size(); movieCount++) {
            movieIds.add(responseOflandingPageQuickBuy.extract().jsonPath().get("data.movies[" + movieCount + "].movieInfo.id"));
        }
        theatreIdOfPreviouslyTrackedLandingPageQuickBuy = theatreId;
        return movieIds;
    }

    public static String getAValidMovieId() {
        ArrayList<String> movieIdsOfValidTheatre = new ArrayList<>();//
        for (String country : CinemaConstants.validCountryTheatresMap.keySet()) {
            if (movieIdsOfValidTheatre.size() > 0) { //no need to continue looping if we have atleast a valid movieId
                break;
            }
            for (String theatreId : CinemaConstants.validCountryTheatresMap.get(country).keySet()) {
                movieIdsOfValidTheatre.addAll(CinemaUtils.getMovieIdsOfTheatre(theatreId));
                if (movieIdsOfValidTheatre.size() > 0) { //no need to continue looping if we have atleast a valid movieId
                    break;
                } else {
                    logger.log("Note: No movie listed in theatre ID " + theatreId + " of " + country);
                }
            }
        }
        return movieIdsOfValidTheatre.get(0);
    }

    public static LinkedHashMap getTheatreIdAndMovieIdsMap() {
        LinkedHashMap<String, ArrayList> theatreIdMovieIdsMap = new LinkedHashMap<>();
        for (String country : CinemaConstants.validCountryTheatresMap.keySet()) {
            for (String theatreId : CinemaConstants.validCountryTheatresMap.get(country).keySet()) {
                if (CinemaUtils.getMovieIdsOfTheatre(theatreId).size() > 0) {
                    theatreIdMovieIdsMap.put(theatreId, CinemaUtils.getMovieIdsOfTheatre(theatreId));
                }
            }
        }
        return theatreIdMovieIdsMap;
    }

    /*
    returns map (of country and movie schedule information) of one bookable movie from each country
     */
    public static LinkedHashMap<String, CinemaGetScheduleSummaryRequestFields> getCountryAndOneBookableMovieMap() {

        if (countryAndOneBookableMovieMap.size() > 0) { //avoiding api calls if countryAndOneBookableMovieMap already calculated
            return countryAndOneBookableMovieMap;
        }
        for (String country : CinemaConstants.validCountryTheatresMap.keySet()) {
            for (String theatreId : CinemaConstants.validCountryTheatresMap.get(country).keySet()) {

                if (countryAndOneBookableMovieMap.get(country) != null) {
                    break; //breaking as we already have one bookableMovie for this country
                }
                if (CinemaUtils.getMovieIdsOfTheatre(theatreId).size() > 0) {
                    for (String movieId : CinemaUtils.getMovieIdsOfTheatre(theatreId)) {
                        String valueOfTheatreFromValidCountryTheatreMap = CinemaConstants.validCountryTheatresMap.get(country).get(theatreId);
                        String localeValue = valueOfTheatreFromValidCountryTheatreMap.split("_")[2];
                        ArrayList<CinemaGetScheduleSummaryRequestFields> movieScheduleList = getSchedulesOfMovie(theatreId, movieId, localeValue);
                        if (movieScheduleList.size() > 0) {
                            countryAndOneBookableMovieMap.put(country, movieScheduleList.get(0));
                            break; //breaking as we have put one BookableMovie for this country in countryMovieIdMap
                        }
                    }
                }
            }
            if (countryAndOneBookableMovieMap.get(country) == null) {
                logger.log("Note: No bookable movie for country " + country);
                countryAndOneBookableMovieMap.put(country, new CinemaGetScheduleSummaryRequestFields(CinemaConstants.INVALID_RANDOM_VALUE_WHEN_PRE_TEST_FAILS + country, CinemaConstants.INVALID_RANDOM_VALUE_WHEN_PRE_TEST_FAILS + country, new HashMap<String, String>() {{
                    put("day", "");
                    put("month", "");
                    put("year", "");
                }}, CinemaConstants.INVALID_RANDOM_VALUE_WHEN_PRE_TEST_FAILS + country, CinemaConstants.INVALID_RANDOM_VALUE_WHEN_PRE_TEST_FAILS + country));
            }
        }
        //System.out.println("countryAndOneBookableMovieMap:"+countryAndOneBookableMovieMap);
        return countryAndOneBookableMovieMap;
    }

    public static ArrayList<CinemaGetScheduleSummaryRequestFields> getSchedulesOfMovie(String theatreId, String movieId, String locale) {
        ArrayList<CinemaGetScheduleSummaryRequestFields> schedulesOfMovie = new ArrayList<>();
        cinemaAPIFrameworkInstance.setLocale(locale);
        ValidatableResponse response = cinemaAPIFrameworkInstance.sendRequest("searchMovieSchedule", null, new HashMap<String, String>() {{
            put("theatreId", theatreId);
            put("movieId", movieId);
        }}, null, null);

        if (response.extract().statusCode() == 200) {
            Map<String, String> date = response.extract().jsonPath().getMap("data.todayDate");
            List<Object> listOfMovieSchedules = response.extract().jsonPath().getList("data.movieSchedules");
            for (int movieSchedulesCount = 0; movieSchedulesCount < listOfMovieSchedules.size(); movieSchedulesCount++) {
                String auditoriumTypeId = response.extract().jsonPath().get("data.movieSchedules[" + movieSchedulesCount + "].id");
                List<Object> listOfShowTimes = response.extract().jsonPath().get("data.movieSchedules[" + movieSchedulesCount + "].showTimes");
                for (int showTimeCount = 0; showTimeCount < listOfShowTimes.size(); showTimeCount++) {
                    String showTimeId = response.extract().jsonPath().get("data.movieSchedules[" + movieSchedulesCount + "].showTimes[" + showTimeCount + "].id");
                    //if movie schedule is not available for 'date' (i.e todayDate), updating 'date' to next available date by parsing showTimeId
                    if (!showTimeId.startsWith(date.get("year") + "-" + date.get("month") + "-" + date.get("day"))) {
                        String showAvailableDate[] = showTimeId.split("\\.")[0].split("-");
                        date.put("year", showAvailableDate[0]);
                        date.put("month", showAvailableDate[1]);
                        date.put("day", showAvailableDate[2]);
                    }
                    boolean isMovieBookable = response.extract().jsonPath().get("data.movieSchedules[" + movieSchedulesCount + "].showTimes[" + showTimeCount + "].isAvailable");
                    CinemaGetScheduleSummaryRequestFields cinemaGetScheduleSummaryRequestFields = new CinemaGetScheduleSummaryRequestFields(movieId, theatreId, date, auditoriumTypeId, showTimeId);
                    if (cinemaGetScheduleSummaryRequestFields.isAllNotNullableFieldsNotNull() && isMovieBookable) {
                        schedulesOfMovie.add(cinemaGetScheduleSummaryRequestFields);
                    }
                }
            }
        } else {
            logger.log("Note: Response code is " + response.extract().statusCode() + " while searching schedule for theatreId " + theatreId + " movieId " + movieId);
        }
        return schedulesOfMovie;
    }

    /*
    returns all request data required to book a seat (for a given movieSchedule)
     */
    public static CinemaBookSeatRequestFields getSeatsAndAddOnsInfoOfaMovieSchedule(CinemaGetScheduleSummaryRequestFields cinemaSchedule, int numberOfSeats, boolean withAddOn, int numberOfAddOns, String country) {
        CinemaBookSeatRequestFields cinemaBookSeatRequestFields = new CinemaBookSeatRequestFields();
        boolean addOnsSaleAvailable;

        ValidatableResponse searchSeatLayoutResponse = cinemaAPIFrameworkInstance.sendRequest("searchSeatLayout", null, new HashMap<String, String>() {{
            put("movieId", cinemaSchedule.movieId);
            put("theatreId", cinemaSchedule.theatreId);
            putAll(cinemaSchedule.date);
            put("auditoriumTypeId", cinemaSchedule.auditoriumTypeId);
            put("showTimeId", cinemaSchedule.showTimeId);
        }}, null, null);

        if (searchSeatLayoutResponse.extract().statusCode() == 200) {
            String auditoriumNumber = searchSeatLayoutResponse.extract().jsonPath().get("data.auditoriumNumber");
            addOnsSaleAvailable = searchSeatLayoutResponse.extract().jsonPath().get("data.addOnsSaleAvailable");
            int seatCount = 0;
            ArrayList<CinemaBookSeatRequestFields.Seat> seats = new ArrayList<>();
            ArrayList<CinemaBookSeatRequestFields.AddOn> addOns = new ArrayList();
            String typeOfSeat = null; //All seats must be of same type

            while (seatCount < numberOfSeats) {
                ArrayList seatLayoutList = searchSeatLayoutResponse.extract().jsonPath().get("data.seatLayout");
                int randomRowNumberOfSeatLayout = new Random().nextInt(seatLayoutList.size());
                ArrayList randomRowOfSeatLayout = searchSeatLayoutResponse.extract().jsonPath().get("data.seatLayout[" + randomRowNumberOfSeatLayout + "]"); //data.seatLayout is in 2D array
                if (country.equalsIgnoreCase("THAILAND")) {
                    /*
                    Note: Thailand has only one provider named MajorTH. This does not allow to book addOns via us. There are lot of constraint in choosing seats.
                    1. Can not leave one empty seat in the corner.
                    2. Can not leave one seat between two seats.
                    3. All seat must be of same type
                    4. You can pick seats with 2+ gap in between.

                    Here we start with a random row of seat layout and find seat at 1/3 position of row. From here keep adding one position to get the next seat. Keep adding this seat in 'seats' ArrayList till we have required numberOfSeats.
                     */
                    int anySeatPositionOfTheRandomRow = randomRowOfSeatLayout.size() / 3;
                    Object seat = searchSeatLayoutResponse.extract().jsonPath().get("data.seatLayout[" + randomRowNumberOfSeatLayout + "][" + anySeatPositionOfTheRandomRow + "]");
                    while (seat != null && seatCount < numberOfSeats) {
                        boolean isSeatTaken = searchSeatLayoutResponse.extract().jsonPath().get("data.seatLayout[" + randomRowNumberOfSeatLayout + "][" + anySeatPositionOfTheRandomRow + "].isTaken");
                        if (!isSeatTaken) {
                            String providerCinemaSeatTypeId = searchSeatLayoutResponse.extract().jsonPath().get("data.seatLayout[" + randomRowNumberOfSeatLayout + "][" + anySeatPositionOfTheRandomRow + "].type.typeId");
                            String seatId = searchSeatLayoutResponse.extract().jsonPath().get("data.seatLayout[" + randomRowNumberOfSeatLayout + "][" + anySeatPositionOfTheRandomRow + "].id");
                            if (typeOfSeat == null && providerCinemaSeatTypeId != null) {
                                typeOfSeat = providerCinemaSeatTypeId;
                            }
                            if (providerCinemaSeatTypeId.equals(typeOfSeat)) {
                                seats.add(cinemaBookSeatRequestFields.new Seat(seatId, providerCinemaSeatTypeId));
                                seatCount++;
                            }
                        }
                        seat = searchSeatLayoutResponse.extract().jsonPath().get("data.seatLayout[" + randomRowNumberOfSeatLayout + "][" + ++anySeatPositionOfTheRandomRow + "]");
                    }
                } else {

                    int randomSeatOfThisRow = new Random().nextInt(randomRowOfSeatLayout.size());
                    Object seat = searchSeatLayoutResponse.extract().jsonPath().get("data.seatLayout[" + randomRowNumberOfSeatLayout + "][" + randomSeatOfThisRow + "]");

                    if (seat != null) {
                        boolean isSeatTaken = searchSeatLayoutResponse.extract().jsonPath().get("data.seatLayout[" + randomRowNumberOfSeatLayout + "][" + randomSeatOfThisRow + "].isTaken");
                        if (!isSeatTaken) {
                            String providerCinemaSeatTypeId = searchSeatLayoutResponse.extract().jsonPath().get("data.seatLayout[" + randomRowNumberOfSeatLayout + "][" + randomSeatOfThisRow + "].type.typeId");
                            String seatId = searchSeatLayoutResponse.extract().jsonPath().get("data.seatLayout[" + randomRowNumberOfSeatLayout + "][" + randomSeatOfThisRow + "].id");
                            if (typeOfSeat == null && providerCinemaSeatTypeId != null) {
                                typeOfSeat = providerCinemaSeatTypeId;
                            }
                            if (providerCinemaSeatTypeId.equals(typeOfSeat)) {
                                seats.add(cinemaBookSeatRequestFields.new Seat(seatId, providerCinemaSeatTypeId));
                                seatCount++;
                            }
                        }
                    }
                }
            }
            if (withAddOn == true) {
                if (addOnsSaleAvailable == false) {
                    logger.log("AddOns are not available for cinemaSchedule " + cinemaSchedule + ". Continuing without AddOns");
                } else {
                    ValidatableResponse searchAddonsResponse = cinemaAPIFrameworkInstance.sendRequest("searchAddons", null, new HashMap<String, String>() {{
                                put("movieId", cinemaSchedule.movieId);
                                put("theatreId", cinemaSchedule.theatreId);
                                putAll(cinemaSchedule.date);
                                put("showTimeId", cinemaSchedule.showTimeId);
                            }},
                            null,
                            null);
                    if (searchAddonsResponse.extract().statusCode() == 200) {
                        HashMap<String, Object> addOnsMenuMap = searchAddonsResponse.extract().jsonPath().get("data.addOnsMenuMap");
                        int addOnCount = 0;
                        for (String addOnsMenuMapKey : addOnsMenuMap.keySet()) {
                            String addOnsId = searchAddonsResponse.extract().jsonPath().get("data.addOnsMenuMap." + addOnsMenuMapKey + ".addOnsId");
                            addOns.add(cinemaBookSeatRequestFields.new AddOn(addOnsId, Integer.toString(new Random().nextInt(5) + 1))); //Adding random quantity of addOns. 1 <= quantity <= 5
                            addOnCount++;
                            if (addOnCount >= numberOfAddOns)
                                break;
                        }

                    } else {
                        logger.log("Failed to fetch Add-ons of cinemaSchedule " + cinemaSchedule + " as response code of searchSeatLayout api is " + searchAddonsResponse.extract().statusCode());
                    }
                }
            }
            cinemaBookSeatRequestFields.setValues(cinemaSchedule, addOns, auditoriumNumber, "", seats); //currency is set by calling DP

        } else {
            cinemaBookSeatRequestFields.setValues(cinemaSchedule, new ArrayList<CinemaBookSeatRequestFields.AddOn>() {{
                add(cinemaBookSeatRequestFields.new AddOn(CinemaConstants.INVALID_RANDOM_VALUE_WHEN_PRE_TEST_FAILS, CinemaConstants.INVALID_RANDOM_VALUE_WHEN_PRE_TEST_FAILS));
            }}, CinemaConstants.INVALID_RANDOM_VALUE_WHEN_PRE_TEST_FAILS, "", new ArrayList<CinemaBookSeatRequestFields.Seat>() {{
                add(cinemaBookSeatRequestFields.new Seat(CinemaConstants.INVALID_RANDOM_VALUE_WHEN_PRE_TEST_FAILS, CinemaConstants.INVALID_RANDOM_VALUE_WHEN_PRE_TEST_FAILS));
            }});
            logger.log("Failed to fetch seats of cinemaSchedule " + cinemaSchedule + " as response code of searchSeatLayout api is " + searchSeatLayoutResponse.extract().statusCode());
        }
        return cinemaBookSeatRequestFields;
    }

    public static HashMap<String, String> getASuccessfulBookingInformation(String country, int numberOfSeats, boolean withAddOn, int numberOfAddOns) {
        HashMap<String, String> successfulBookingInformation = new HashMap<>();
        LinkedHashMap<String, CinemaGetScheduleSummaryRequestFields> validCountryAndBookableMovieMap = getCountryAndOneBookableMovieMap();
        CinemaGetScheduleSummaryRequestFields schedule = validCountryAndBookableMovieMap.get(country);

        CinemaBookSeatRequestFields cinemaBookSeatRequestFieldsSA = CinemaUtils.getSeatsAndAddOnsInfoOfaMovieSchedule(schedule, numberOfSeats, withAddOn, numberOfAddOns, country);
        ValidatableResponse bookSeatResponse = cinemaAPIFrameworkInstance.sendRequest("bookSeat", null, new HashMap<String, String>() {{
            if (cinemaBookSeatRequestFieldsSA.bookedSeatList.size() > 0) {
                put("bookedSeats", String.valueOf(cinemaBookSeatRequestFieldsSA.bookedSeatList));
            }
            if (cinemaBookSeatRequestFieldsSA.addOnsBookingList.size() > 0) {
                put("addOnsBookingList", String.valueOf(cinemaBookSeatRequestFieldsSA.addOnsBookingList));
            }
            put("movieId", cinemaBookSeatRequestFieldsSA.movieId);
            put("theatreId", cinemaBookSeatRequestFieldsSA.theatreId);
            putAll(cinemaBookSeatRequestFieldsSA.date);
            put("showTimeId", cinemaBookSeatRequestFieldsSA.showTimeId);
            put("currency", cinemaBookSeatRequestFieldsSA.currency);
            put("showTime", cinemaBookSeatRequestFieldsSA.showTime);
            put("auditoriumNumber", cinemaBookSeatRequestFieldsSA.auditoriumNumber);
            put("auditoriumId", cinemaBookSeatRequestFieldsSA.auditoriumId);
        }}, null, null);

        if (bookSeatResponse.extract().statusCode() == 200) {
            String invoiceId = bookSeatResponse.extract().jsonPath().get("data.bookingInfo.invoiceId");
            String bookingId = bookSeatResponse.extract().jsonPath().get("data.bookingInfo.bookingId");
            String auth = bookSeatResponse.extract().jsonPath().get("data.bookingInfo.auth");
            successfulBookingInformation.put("invoiceId", invoiceId);
            successfulBookingInformation.put("bookingId", bookingId);
            successfulBookingInformation.put("auth", auth);
            System.out.println("successfulBookingInformation: " + successfulBookingInformation);
        } else {
            successfulBookingInformation.put("invoiceId", CinemaConstants.INVALID_RANDOM_VALUE_WHEN_PRE_TEST_FAILS);
            successfulBookingInformation.put("bookingId", CinemaConstants.INVALID_RANDOM_VALUE_WHEN_PRE_TEST_FAILS);
            successfulBookingInformation.put("auth", CinemaConstants.INVALID_RANDOM_VALUE_WHEN_PRE_TEST_FAILS);
            logger.log("Failed to fetch response of bookedSeats api. Response is " + bookSeatResponse.extract().statusCode());
        }
        return successfulBookingInformation;
    }

    public static void cancelBooking(String[] bookingInfo) {
        if (bookingInfo.length == 3) {
            ValidatableResponse cancelBookingResponse = cinemaAPIFrameworkInstance.sendRequest("cancelBooking", null, new HashMap<String, String>() {{
                put("bookingId", bookingInfo[0]);
                put("invoiceId", bookingInfo[1]);
                put("auth", bookingInfo[2]);
            }}, null, null);
        }
    }

    public static String getFavouriteTheatreID() {

        ValidatableResponse getAllTheatresResponse = cinemaAPIFrameworkInstance.sendRequest("getAllTheatres", null, null, null, null);
        List<Object> listOfCity = getAllTheatresResponse.extract().jsonPath().getList("data.cinemaCities.name");
        for (int cinemaCitiesCount = 0; cinemaCitiesCount < listOfCity.size(); cinemaCitiesCount++) {
            List<String> listOfTheatre = getAllTheatresResponse.extract().jsonPath().getList("data.cinemaCities[" + cinemaCitiesCount + "].theatreList.id");
            List<Boolean> listOfIsFavourite = getAllTheatresResponse.extract().jsonPath().getList("data.cinemaCities[" + cinemaCitiesCount + "].theatreList.isFavourite");
            if (listOfTheatre.size() == listOfIsFavourite.size() && listOfIsFavourite.contains(true)) {
                return listOfTheatre.get(listOfIsFavourite.indexOf(true));
            }
        }
        return null;
    }

    //returns list of nowPlaying, comingSoon, preSale movies
    public static HashMap<String, List<String>> getListOfMoviesInACity(String cityId) {
        HashMap<String, List<String>> mapOfMovieTypeAndMovieList = new HashMap<String, List<String>>();
        ValidatableResponse responseOfLandingPageDiscoverAPI = cinemaAPIFrameworkInstance.sendRequest("landingPageDiscover", null, new HashMap<String, String>() {{
            put("cityId", cityId);
        }}, null, null);
        mapOfMovieTypeAndMovieList.put("nowPlaying", responseOfLandingPageDiscoverAPI.extract().jsonPath().getList("data.nowPlaying.movieInfo.id"));
        mapOfMovieTypeAndMovieList.put("comingSoon", responseOfLandingPageDiscoverAPI.extract().jsonPath().getList("data.comingSoon.movieInfo.id"));
        mapOfMovieTypeAndMovieList.put("preSale", responseOfLandingPageDiscoverAPI.extract().jsonPath().getList("data.presale.movieInfo.id"));
        return mapOfMovieTypeAndMovieList;
    }

    public static HashMap<String, String> getDateMap(int offset) {
        HashMap<String, String> today = new HashMap<>();
        LocalDate todayDate = LocalDate.now(ZoneId.of("Asia/Jakarta"));
        todayDate = todayDate.plusDays(offset);
        System.out.println(" Local date : " + todayDate);
        today.put("month", Integer.toString(todayDate.getMonthValue()));
        today.put("day", Integer.toString(todayDate.getDayOfMonth()));
        today.put("year", Integer.toString(todayDate.getYear()));
        return today;
    }

    public static boolean isListNotNullOrEmpty(ArrayList<Object> arrayList) {
        if (arrayList == null || arrayList.isEmpty())
            return false;
        return true;
    }

    public static boolean isStringNotNullOrEmpty(String str) {
        if (str == null || str.isEmpty())
            return false;
        return true;
    }

    public static boolean isDateMapCorrect(Map<String, String> date) {
        if (date.containsKey("month") && date.get("month").length() > 0 && date.containsKey("day") && date.get("day").length() > 0 && date.containsKey("year") && date.get("year").length() > 0)
            return true;
        return false;
    }

    public static boolean stringHasIntegerValue(String str) {
        if (str == null || str.isEmpty())
            return false;
        try {
            Integer.parseInt(str);
        } catch (NumberFormatException ex) {
            return false;
        }
        return true;
    }

    public static boolean isBoolean(boolean booleanValue) {
        return booleanValue == true || booleanValue == false;
    }

    public static String getShowTimeFromShowTimeId(String showTimeId) {
        //eg of showTimeId 2020-01-07.15:45.252062.7
        return showTimeId.split("\\.").length > 1 ? showTimeId.split("\\.")[1] : "noShowTimeAvailable";
    }
}

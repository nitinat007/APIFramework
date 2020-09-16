package com.progathon.cinema.dp;

import com.progathon.cinema.constants.CinemaConstants;
import com.progathon.cinema.utils.CinemaUtils;
import com.progathon.cinema.utils.requestFields.CinemaBookSeatRequestFields;
import com.progathon.cinema.utils.requestFields.CinemaGetScheduleSummaryRequestFields;
import org.testng.Reporter;
import org.testng.annotations.DataProvider;


import java.util.*;

import static com.progathon.cinema.utils.CinemaUtils.getCountryAndOneBookableMovieMap;

/**
 * Author: nitinkumar
 * Created Date: 11/11/19
 * Info: Contains data providers for theater related testcases in Cinema
 **/

public class CinemaDP {

    @DataProvider(name = "getLocalesDP")
    public static Iterator<Object[]> locales() {
        String[] locs = CinemaConstants.getSupportedCinemaLocales();
        List<Object[]> data = new ArrayList<>();
        for (String loc : locs) {
            data.add(new String[]{loc});
        }
        return data.iterator();
    }

    @DataProvider(name = "getTheatreIDCurrencyAndTrackingRequest")
    public Object[][] getTheatreIDCurrencyAndTrackingRequest() {
        int countOfTestData = 0;
        Object[][] object = new Object[6 * CinemaConstants.getAllSupportedCinemaCurrencies().size()][]; // 6 signifies total number of unique testData (below)
        for (String currency : CinemaConstants.getAllSupportedCinemaCurrencies()) {
            object[countOfTestData++] = new Object[]{CinemaConstants.VALID_THEATRE_ID_INDONESIA, currency, CinemaConstants.getTrackingRequestMap()};
            object[countOfTestData++] = new Object[]{CinemaConstants.VALID_THEATRE_ID_INDONESIA, currency, null};
            object[countOfTestData++] = new Object[]{CinemaConstants.VALID_THEATRE_ID_INDONESIA, null, null};
            object[countOfTestData++] = new Object[]{CinemaConstants.INVALID_THEATRE_ID, currency, CinemaConstants.getTrackingRequestMap()};
            object[countOfTestData++] = new Object[]{CinemaConstants.INVALID_THEATRE_ID, currency, null};
            object[countOfTestData++] = new Object[]{CinemaConstants.INVALID_THEATRE_ID, null, null};
        }
        return object;
    }

    @DataProvider(name = "getLocaleAndTheatreId")
    public static Iterator<Object[]> getLocaleAndTheatreId() {
        String[] locs = CinemaConstants.getSupportedCinemaLocales();
        String[] validTheatreIds = CinemaConstants.getTheatreIdsForLocales();
        List<Object[]> data = new ArrayList<Object[]>();
        for (int countLocs = 0; countLocs < locs.length; countLocs++) {
            data.add(new String[]{locs[countLocs], validTheatreIds[countLocs]});
        }
        return data.iterator();
    }

    @DataProvider(name = "getValidTheatreId")
    public static Object[] getValidTheatreId() {
        return new Object[]{CinemaConstants.VALID_THEATRE_ID_INDONESIA};
    }

    @DataProvider(name = "getValidAndInvalidTheatreId")
    public static Object[][] getTheatreId() {
        return new Object[][]{new Object[]{CinemaConstants.VALID_THEATRE_ID_INDONESIA, "valid"}, new Object[]{CinemaConstants.INVALID_THEATRE_ID, "invalid"}};
    }

    @DataProvider(name = "getValidAndInvalidMovieId")
    public static Object[] getMovieId() {
        return new Object[][]{new Object[]{CinemaUtils.getAValidMovieId(), "valid"}, new Object[]{"123456789", "invalid"}};
    }

    @DataProvider(name = "getLocaleAndMovieId")
    public static Iterator<Object[]> getLocaleAndMovieId() {
        String[] locs = CinemaConstants.getSupportedCinemaLocales();
        String movieId = CinemaUtils.getAValidMovieId();
        List<Object[]> data = new ArrayList<Object[]>();
        for (int countLocs = 0; countLocs < locs.length; countLocs++) {
            data.add(new String[]{locs[countLocs], movieId});
        }
        return data.iterator();
    }

    @DataProvider(name = "getValidMovieId")
    public static Object[] getValidMovieId() {
        return new Object[]{CinemaUtils.getAValidMovieId()};
    }

    @DataProvider(name = "getValidAndInvalidTheatreIdMovieId")
    public static Object[][] getValidAndInvalidTheatreIdMovieId() {
        LinkedHashMap<String, ArrayList> theatreIdMovieIdsMap = CinemaUtils.getTheatreIdAndMovieIdsMap();
        return new Object[][]{new Object[]{theatreIdMovieIdsMap.keySet().stream().findFirst().get(), theatreIdMovieIdsMap.values().stream().findFirst().get().get(0).toString(), "valid"}, new Object[]{CinemaConstants.INVALID_THEATRE_ID, "123456789", "invalid"}};
    }

    @DataProvider(name = "getTheatreIdMovieIdCurrencyDayMonthYear")
    public Object[][] getTheatreIdMovieIdCurrencyDayMonthYear() {
        int countOfTestData = 0;
        LinkedHashMap<String, ArrayList> theatreIdMovieIdsMap = CinemaUtils.getTheatreIdAndMovieIdsMap();
        String theatreId = theatreIdMovieIdsMap.keySet().stream().findFirst().get();
        String movieId = theatreIdMovieIdsMap.values().stream().findFirst().get().get(0).toString(); //fetching any one valid (say first) entry from List
        Object[][] object = new Object[4 * CinemaConstants.getAllSupportedCinemaCurrencies().size()][]; // 4 signifies total number of unique testData (below)
        HashMap<String, String> date = CinemaUtils.getDateMap(+1);
        for (String currency : CinemaConstants.getAllSupportedCinemaCurrencies()) {
            object[countOfTestData++] = new Object[]{theatreId, movieId, currency, date};
            object[countOfTestData++] = new Object[]{theatreId, movieId, currency, null};
            object[countOfTestData++] = new Object[]{theatreId, movieId, null, date};
            object[countOfTestData++] = new Object[]{theatreId, movieId, null, null};
        }
        return object;
    }

    @DataProvider(name = "getLocaleTheatreIdMovieId")
    public static Iterator<Object[]> getLocaleTheatreIdMovieId() {
        String[] locs = CinemaConstants.getSupportedCinemaLocales();
        String[] validTheatreIds = CinemaConstants.getTheatreIdsForLocales();
        List<Object[]> data = new ArrayList<Object[]>();
        for (int countLocs = 0; countLocs < locs.length; countLocs++) {
            String validMovieId;
            try {
                validMovieId = CinemaUtils.getMovieIdsOfTheatre(validTheatreIds[countLocs]).stream().findFirst().get();
            } catch (Exception e) {
                validMovieId = CinemaConstants.INVALID_RANDOM_VALUE_WHEN_PRE_TEST_FAILS;
                Reporter.log("Note: Failed to fetch movieId. Initialized movieId to a random value." + e.getMessage());
            }
            data.add(new String[]{locs[countLocs], validTheatreIds[countLocs], validMovieId});
        }
        return data.iterator();
    }

    @DataProvider(name = "getValidTheatreIdMovieId")
    public static Object[][] getValidTheatreIdMovieId() {
        LinkedHashMap<String, ArrayList> theatreIdMovieIdsMap = CinemaUtils.getTheatreIdAndMovieIdsMap();
        String theatreId = theatreIdMovieIdsMap.keySet().stream().findFirst().get();
        String movieId = theatreIdMovieIdsMap.values().stream().findFirst().get().get(0).toString();
        return new Object[][]{new Object[]{theatreId, movieId}};
    }

    @DataProvider(name = "getValidTheatreIdMovieIdDate")
    public static Object[][] getValidTheatreIdMovieIdDate() {
        LinkedHashMap<String, ArrayList> theatreIdMovieIdsMap = CinemaUtils.getTheatreIdAndMovieIdsMap();
        String theatreId = theatreIdMovieIdsMap.keySet().stream().findFirst().get();
        String movieId = theatreIdMovieIdsMap.values().stream().findFirst().get().get(0).toString();
        ArrayList<CinemaGetScheduleSummaryRequestFields> schedulesOfMovie = CinemaUtils.getSchedulesOfMovie(theatreId, movieId, null);
        HashMap<String, String> firstAvailableShow = new HashMap<>();
        firstAvailableShow.putAll(schedulesOfMovie.get(0).date);
        HashMap<String, String> yesterday = CinemaUtils.getDateMap(-1);
        return new Object[][]{new Object[]{theatreId, movieId, firstAvailableShow, true}, new Object[]{theatreId, movieId, yesterday, false}};
    }

    @DataProvider(name = "getValidAndInvalidMovieSchedulesForEachCountry")
    public static Iterator<Object[]> getValidAndInvalidMovieSchedulesForEachCountry() { //total testData count = countries * 2
        List<Object[]> data = new ArrayList<Object[]>();
        LinkedHashMap<String, CinemaGetScheduleSummaryRequestFields> validCountryAndBookableMovieMap = getCountryAndOneBookableMovieMap();
        Set<String> countries = validCountryAndBookableMovieMap.keySet();
        for (String country : countries) {
            CinemaGetScheduleSummaryRequestFields schedule = validCountryAndBookableMovieMap.get(country);
            data.add(new Object[]{schedule.movieId, schedule.theatreId, schedule.date, schedule.auditoriumTypeId, schedule.showTimeId, "valid"});
            data.add(new Object[]{schedule.movieId, schedule.theatreId, schedule.date, "callIsIndependentOfThisID", "someRandomValue", "invalid"});
        }
        return data.iterator();
    }

    @DataProvider(name = "getLocaleAndValidMovieSchedule")
    public static Iterator<Object[]> getLocaleAndValidMovieSchedule() { //total testData count = total locales
        List<Object[]> data = new ArrayList<Object[]>();
        LinkedHashMap<String, CinemaGetScheduleSummaryRequestFields> validCountryAndBookableMovieMap = getCountryAndOneBookableMovieMap();
        Set<String> countries = validCountryAndBookableMovieMap.keySet();
        for (String country : countries) {
            CinemaGetScheduleSummaryRequestFields schedule = validCountryAndBookableMovieMap.get(country);
            ArrayList<String> locales = CinemaConstants.countryLocalesMap.get(country);
            for (String locale : locales) {
                data.add(new Object[]{locale, schedule.movieId, schedule.theatreId, schedule.date, schedule.auditoriumTypeId, schedule.showTimeId});
            }
        }
        return data.iterator();
    }

    @DataProvider(name = "getAValidMovieSchedule")
    public static Iterator<Object[]> getAValidMovieSchedule() { // total testData count = 1
        List<Object[]> data = new ArrayList<Object[]>();
        LinkedHashMap<String, CinemaGetScheduleSummaryRequestFields> validCountryAndBookableMovieMap = getCountryAndOneBookableMovieMap();
        Set<String> countries = validCountryAndBookableMovieMap.keySet();
        for (String country : countries) {
            CinemaGetScheduleSummaryRequestFields schedule = validCountryAndBookableMovieMap.get(country);
            data.add(new Object[]{schedule.movieId, schedule.theatreId, schedule.date, schedule.auditoriumTypeId, schedule.showTimeId});
            return data.iterator();
        }
        return data.iterator();
    }

    //DPs of SearchSeatLayoutAPITestSuite below
    @DataProvider(name = "getValidAndInvalidMovieSchedulesWithCurrencyForEachCountry")
    public static Iterator<Object[]> getValidAndInvalidMovieSchedulesWithCurrencyForEachCountry() { //total testData count = 2* Summation of( country * supportedCurrency)
        List<Object[]> data = new ArrayList<Object[]>();
        LinkedHashMap<String, CinemaGetScheduleSummaryRequestFields> validCountryAndBookableMovieMap = getCountryAndOneBookableMovieMap();
        Set<String> countries = validCountryAndBookableMovieMap.keySet();
        for (String country : countries) {
            CinemaGetScheduleSummaryRequestFields schedule = validCountryAndBookableMovieMap.get(country);
            ArrayList<String> currenciesSupportedByThisCountry = CinemaConstants.countryCurrencyMap.get(country);
            for (String currency : currenciesSupportedByThisCountry) {
                data.add(new Object[]{schedule.movieId, schedule.theatreId, schedule.date, schedule.auditoriumTypeId, schedule.showTimeId, currency, "valid"});
                data.add(new Object[]{schedule.movieId, schedule.theatreId, schedule.date, "callIsIndependentOfThisID", "someRandomValue", currency, "invalid"});
            }
        }
        return data.iterator();
    }

    @DataProvider(name = "getMovieScheduleAndCurrency")
    public static Iterator<Object[]> getMovieScheduleAndCurrency() { // total testData count = 2
        List<Object[]> data = new ArrayList<Object[]>();
        LinkedHashMap<String, CinemaGetScheduleSummaryRequestFields> validCountryAndBookableMovieMap = getCountryAndOneBookableMovieMap();
        Set<String> countries = validCountryAndBookableMovieMap.keySet();
        for (String country : countries) {
            CinemaGetScheduleSummaryRequestFields schedule = validCountryAndBookableMovieMap.get(country);
            ArrayList<String> currenciesSupportedByThisCountry = CinemaConstants.countryCurrencyMap.get(country);
            for (String currency : currenciesSupportedByThisCountry) {
                data.add(new Object[]{schedule.movieId, schedule.theatreId, schedule.date, schedule.auditoriumTypeId, schedule.showTimeId, currency});
                data.add(new Object[]{schedule.movieId, schedule.theatreId, schedule.date, schedule.auditoriumTypeId, schedule.showTimeId, null});
                return data.iterator();
            }
        }
        return data.iterator();
    }

    @DataProvider(name = "getLocaleValidMovieScheduleAndCurrency")
    public static Iterator<Object[]> getLocaleValidMovieScheduleAndCurrency() { //total testData count = Summation of ( no. of locales of a country * supportedCurrencies)
        List<Object[]> data = new ArrayList<Object[]>();
        LinkedHashMap<String, CinemaGetScheduleSummaryRequestFields> validCountryAndBookableMovieMap = getCountryAndOneBookableMovieMap();
        Set<String> countries = validCountryAndBookableMovieMap.keySet();
        for (String country : countries) {
            CinemaGetScheduleSummaryRequestFields schedule = validCountryAndBookableMovieMap.get(country);
            ArrayList<String> locales = CinemaConstants.countryLocalesMap.get(country);
            ArrayList<String> currenciesSupportedByThisCountry = CinemaConstants.countryCurrencyMap.get(country);
            for (String locale : locales) {
                for (String currency : currenciesSupportedByThisCountry) {
                    data.add(new Object[]{locale, schedule.movieId, schedule.theatreId, schedule.date, schedule.auditoriumTypeId, schedule.showTimeId, currency});
                }
            }
        }
        return data.iterator();
    }

    @DataProvider(name = "getAValidMovieScheduleAndCurrency")
    public static Iterator<Object[]> getAValidMovieScheduleAndCurrency() { // total testData count = 1
        List<Object[]> data = new ArrayList<Object[]>();
        LinkedHashMap<String, CinemaGetScheduleSummaryRequestFields> validCountryAndBookableMovieMap = getCountryAndOneBookableMovieMap();
        Set<String> countries = validCountryAndBookableMovieMap.keySet();
        for (String country : countries) {
            CinemaGetScheduleSummaryRequestFields schedule = validCountryAndBookableMovieMap.get(country);
            ArrayList<String> locales = CinemaConstants.countryLocalesMap.get(country);
            ArrayList<String> currenciesSupportedByThisCountry = CinemaConstants.countryCurrencyMap.get(country);
            for (String currency : currenciesSupportedByThisCountry) {
                data.add(new Object[]{schedule.movieId, schedule.theatreId, schedule.date, schedule.auditoriumTypeId, schedule.showTimeId, currency});
                return data.iterator();
            }

        }
        return data.iterator();
    }

    //DPs of SearchAddonsTestSuite below
    @DataProvider(name = "getValidAndInvalidMovieSchedulesWithCurrencyForEachCountryForAddOns")
    public static Iterator<Object[]> getValidAndInvalidMovieSchedulesWithCurrencyForEachCountryForAddOns() { //total testData count = 2* Summation of ( countryServingAddon * supportedCurrency)
        List<Object[]> data = new ArrayList<Object[]>();
        LinkedHashMap<String, CinemaGetScheduleSummaryRequestFields> validCountryAndBookableMovieMap = getCountryAndOneBookableMovieMap(); //assumption is that bookable movie will always have addons

        //Removing THAILAND from map as there are no theatre serving Addon in thailand. Remove below line once theatres start serving addons
        validCountryAndBookableMovieMap.remove("THAILAND");

        Set<String> countries = validCountryAndBookableMovieMap.keySet();
        for (String country : countries) {
            CinemaGetScheduleSummaryRequestFields schedule = validCountryAndBookableMovieMap.get(country);
            ArrayList<String> currenciesSupportedByThisCountry = CinemaConstants.countryCurrencyMap.get(country);
            for (String currency : currenciesSupportedByThisCountry) {
                data.add(new Object[]{schedule.movieId, schedule.theatreId, schedule.date, schedule.showTimeId, currency, "valid"});
                data.add(new Object[]{schedule.movieId, CinemaConstants.INVALID_THEATRE_ID, schedule.date, "callIsIndependentOfShowTimeId", currency, "invalid"});
            }
        }
        return data.iterator();
    }

    @DataProvider(name = "getMovieScheduleAndCurrencyForAddons")
    public static Iterator<Object[]> getMovieScheduleAndCurrencyForAddons() { // total testData count = 2
        List<Object[]> data = new ArrayList<Object[]>();
        LinkedHashMap<String, CinemaGetScheduleSummaryRequestFields> validCountryAndBookableMovieMap = getCountryAndOneBookableMovieMap();

        //Removing THAILAND from map as there are no theatre serving Addon in thailand. Remove below line once theatres start serving addons
        validCountryAndBookableMovieMap.remove("THAILAND");

        Set<String> countries = validCountryAndBookableMovieMap.keySet();
        for (String country : countries) {
            CinemaGetScheduleSummaryRequestFields schedule = validCountryAndBookableMovieMap.get(country);
            ArrayList<String> currenciesSupportedByThisCountry = CinemaConstants.countryCurrencyMap.get(country);
            for (String currency : currenciesSupportedByThisCountry) {
                data.add(new Object[]{schedule.movieId, schedule.theatreId, schedule.date, schedule.showTimeId, currency});
                data.add(new Object[]{schedule.movieId, schedule.theatreId, schedule.date, schedule.showTimeId, null});
                return data.iterator();
            }
        }
        return data.iterator();
    }

    @DataProvider(name = "getLocaleValidMovieScheduleAndCurrencyForAddons")
    public static Iterator<Object[]> getLocaleValidMovieScheduleAndCurrencyForAddons() { //total testData count = Summation of (no. of locales of a country * supportedCurrencies)
        List<Object[]> data = new ArrayList<Object[]>();
        LinkedHashMap<String, CinemaGetScheduleSummaryRequestFields> validCountryAndBookableMovieMap = getCountryAndOneBookableMovieMap();

        //Removing THAILAND from map as there are no theatre serving Addon in thailand. Remove below line once theatres start serving addons
        validCountryAndBookableMovieMap.remove("THAILAND");

        Set<String> countries = validCountryAndBookableMovieMap.keySet();
        for (String country : countries) {
            CinemaGetScheduleSummaryRequestFields schedule = validCountryAndBookableMovieMap.get(country);
            ArrayList<String> locales = CinemaConstants.countryLocalesMap.get(country);
            ArrayList<String> currenciesSupportedByThisCountry = CinemaConstants.countryCurrencyMap.get(country);
            for (String locale : locales) {
                for (String currency : currenciesSupportedByThisCountry) {
                    data.add(new Object[]{locale, schedule.movieId, schedule.theatreId, schedule.date, schedule.showTimeId, currency});
                }
            }
        }
        return data.iterator();
    }

    @DataProvider(name = "getAValidMovieScheduleAndCurrencyForAddons")
    public static Iterator<Object[]> getAValidMovieScheduleAndCurrencyForAddons() { // total testData count = 1
        List<Object[]> data = new ArrayList<Object[]>();
        LinkedHashMap<String, CinemaGetScheduleSummaryRequestFields> validCountryAndBookableMovieMap = getCountryAndOneBookableMovieMap();
        Set<String> countries = validCountryAndBookableMovieMap.keySet();
        for (String country : countries) {
            CinemaGetScheduleSummaryRequestFields schedule = validCountryAndBookableMovieMap.get(country);
            ArrayList<String> currenciesSupportedByThisCountry = CinemaConstants.countryCurrencyMap.get(country);
            for (String currency : currenciesSupportedByThisCountry) {
                data.add(new Object[]{schedule.movieId, schedule.theatreId, schedule.date, schedule.showTimeId, currency});
                return data.iterator();
            }

        }
        return data.iterator();
    }

    //DPs of bookSeat below
    @DataProvider(name = "getValidAndInvalidBookingRequestFieldsWithCurrencyForEachCountry")
    public static Iterator<Object[]> getValidAndInvalidSeatRequestFieldsWithCurrencyForEachCountry() { //total testData count = 10
        List<Object[]> data = new ArrayList<Object[]>();
        LinkedHashMap<String, CinemaGetScheduleSummaryRequestFields> validCountryAndBookableMovieMap = getCountryAndOneBookableMovieMap(); //assumption is that bookable movie will always have addons

        Set<String> countries = validCountryAndBookableMovieMap.keySet();
        for (String country : countries) {

            CinemaGetScheduleSummaryRequestFields schedule = validCountryAndBookableMovieMap.get(country);

            //Fetching request fields for 0 seat & 0 Add-on
            CinemaBookSeatRequestFields cinemaBookSeatRequestFields00 = CinemaUtils.getSeatsAndAddOnsInfoOfaMovieSchedule(schedule, 0, false, 0, country);
            //Fetching request fields for 1 seat & no Add-on
            CinemaBookSeatRequestFields cinemaBookSeatRequestFields10 = CinemaUtils.getSeatsAndAddOnsInfoOfaMovieSchedule(schedule, 1, false, 0, country);
            //Fetching request fields for 3 seat & no Add-on
            CinemaBookSeatRequestFields cinemaBookSeatRequestFields30 = CinemaUtils.getSeatsAndAddOnsInfoOfaMovieSchedule(schedule, 3, false, 0, country);
            //Fetching request fields for 1 seat & 1 Add-on
            CinemaBookSeatRequestFields cinemaBookSeatRequestFields11 = CinemaUtils.getSeatsAndAddOnsInfoOfaMovieSchedule(schedule, 1, true, 1, country);
            //Fetching request fields for 3 seat & 3 Add-on
            CinemaBookSeatRequestFields cinemaBookSeatRequestFields33 = CinemaUtils.getSeatsAndAddOnsInfoOfaMovieSchedule(schedule, 3, true, 3, country);
            //Fetching request fields for 0 seat & 1 Add-on
            CinemaBookSeatRequestFields cinemaBookSeatRequestFields01 = CinemaUtils.getSeatsAndAddOnsInfoOfaMovieSchedule(schedule, 0, true, 1, country);
            //Fetching request fields for 1 seat & 1 Add-on and making addon quantity 0
            CinemaBookSeatRequestFields cinemaBookSeatRequestFields110 = CinemaUtils.getSeatsAndAddOnsInfoOfaMovieSchedule(schedule, 1, true, 1, country);
            if (cinemaBookSeatRequestFields110.addOnsBookingList.size() > 0) {
                cinemaBookSeatRequestFields110.addOnsBookingList.get(0).quantity = "0";
            }

            ArrayList<String> currenciesSupportedByThisCountry = CinemaConstants.countryCurrencyMap.get(country);
            for (String currency : currenciesSupportedByThisCountry) {
                data.add(new Object[]{cinemaBookSeatRequestFields00.bookedSeatList, cinemaBookSeatRequestFields00.addOnsBookingList, cinemaBookSeatRequestFields00.movieId, cinemaBookSeatRequestFields00.theatreId, cinemaBookSeatRequestFields00.date, cinemaBookSeatRequestFields00.showTimeId, currency, cinemaBookSeatRequestFields00.showTime, cinemaBookSeatRequestFields00.auditoriumNumber, cinemaBookSeatRequestFields00.auditoriumId, "invalid"});
                data.add(new Object[]{cinemaBookSeatRequestFields10.bookedSeatList, cinemaBookSeatRequestFields10.addOnsBookingList, cinemaBookSeatRequestFields10.movieId, cinemaBookSeatRequestFields10.theatreId, cinemaBookSeatRequestFields10.date, cinemaBookSeatRequestFields10.showTimeId, currency, cinemaBookSeatRequestFields10.showTime, cinemaBookSeatRequestFields10.auditoriumNumber, cinemaBookSeatRequestFields10.auditoriumId, "valid"});
                data.add(new Object[]{cinemaBookSeatRequestFields30.bookedSeatList, cinemaBookSeatRequestFields30.addOnsBookingList, cinemaBookSeatRequestFields30.movieId, cinemaBookSeatRequestFields30.theatreId, cinemaBookSeatRequestFields30.date, cinemaBookSeatRequestFields30.showTimeId, currency, cinemaBookSeatRequestFields30.showTime, cinemaBookSeatRequestFields30.auditoriumNumber, cinemaBookSeatRequestFields30.auditoriumId, "valid"});
                if (!country.equalsIgnoreCase("THAILAND")) { // (MajorTH) thailand provider does not support addOn. Remove 'if' condition when provider starts supporting addOn
                    data.add(new Object[]{cinemaBookSeatRequestFields11.bookedSeatList, cinemaBookSeatRequestFields11.addOnsBookingList, cinemaBookSeatRequestFields11.movieId, cinemaBookSeatRequestFields11.theatreId, cinemaBookSeatRequestFields11.date, cinemaBookSeatRequestFields11.showTimeId, currency, cinemaBookSeatRequestFields11.showTime, cinemaBookSeatRequestFields11.auditoriumNumber, cinemaBookSeatRequestFields11.auditoriumId, "valid"});
                    data.add(new Object[]{cinemaBookSeatRequestFields33.bookedSeatList, cinemaBookSeatRequestFields33.addOnsBookingList, cinemaBookSeatRequestFields33.movieId, cinemaBookSeatRequestFields33.theatreId, cinemaBookSeatRequestFields33.date, cinemaBookSeatRequestFields33.showTimeId, currency, cinemaBookSeatRequestFields33.showTime, cinemaBookSeatRequestFields33.auditoriumNumber, cinemaBookSeatRequestFields33.auditoriumId, "valid"});
                    data.add(new Object[]{cinemaBookSeatRequestFields01.bookedSeatList, cinemaBookSeatRequestFields01.addOnsBookingList, cinemaBookSeatRequestFields01.movieId, cinemaBookSeatRequestFields01.theatreId, cinemaBookSeatRequestFields01.date, cinemaBookSeatRequestFields01.showTimeId, currency, cinemaBookSeatRequestFields01.showTime, cinemaBookSeatRequestFields01.auditoriumNumber, cinemaBookSeatRequestFields01.auditoriumId, "invalid"});
                    data.add(new Object[]{cinemaBookSeatRequestFields110.bookedSeatList, cinemaBookSeatRequestFields110.addOnsBookingList, cinemaBookSeatRequestFields110.movieId, cinemaBookSeatRequestFields110.theatreId, cinemaBookSeatRequestFields110.date, cinemaBookSeatRequestFields110.showTimeId, currency, cinemaBookSeatRequestFields110.showTime, cinemaBookSeatRequestFields110.auditoriumNumber, cinemaBookSeatRequestFields110.auditoriumId, "validSeatInvalidAddon"});
                }
            }
        }
        return data.iterator();
    }

    @DataProvider(name = "getAValidBookingRequestFieldsWithCurrency")
    public static Iterator<Object[]> getAValidSeatRequestFieldsWithCurrency() { //total testData count = 1
        List<Object[]> data = new ArrayList<Object[]>();
        LinkedHashMap<String, CinemaGetScheduleSummaryRequestFields> validCountryAndBookableMovieMap = getCountryAndOneBookableMovieMap(); //assumption is that bookable movie will always have addons

        Set<String> countries = validCountryAndBookableMovieMap.keySet();
        for (String country : countries) {
            CinemaGetScheduleSummaryRequestFields schedule = validCountryAndBookableMovieMap.get(country);

            //Fetching request fields for 1 seat & no Add-on
            CinemaBookSeatRequestFields cinemaBookSeatRequestFields10 = CinemaUtils.getSeatsAndAddOnsInfoOfaMovieSchedule(schedule, 1, false, 0, country);

            ArrayList<String> currenciesSupportedByThisCountry = CinemaConstants.countryCurrencyMap.get(country);
            for (String currency : currenciesSupportedByThisCountry) {
                data.add(new Object[]{cinemaBookSeatRequestFields10.bookedSeatList, cinemaBookSeatRequestFields10.addOnsBookingList, cinemaBookSeatRequestFields10.movieId, cinemaBookSeatRequestFields10.theatreId, cinemaBookSeatRequestFields10.date, cinemaBookSeatRequestFields10.showTimeId, currency, cinemaBookSeatRequestFields10.showTime, cinemaBookSeatRequestFields10.auditoriumNumber, cinemaBookSeatRequestFields10.auditoriumId});
            }
        }
        return data.iterator();
    }

    @DataProvider(name = "getValidBookingRequestFieldsWithAndWithoutCurrency")
    public static Iterator<Object[]> getValidBookingRequestFieldsWithAndWithoutCurrency() { //total testData count = 2
        List<Object[]> data = new ArrayList<Object[]>();
        LinkedHashMap<String, CinemaGetScheduleSummaryRequestFields> validCountryAndBookableMovieMap = getCountryAndOneBookableMovieMap(); //assumption is that bookable movie will always have addons

        Set<String> countries = validCountryAndBookableMovieMap.keySet();
        for (String country : countries) {
            CinemaGetScheduleSummaryRequestFields schedule = validCountryAndBookableMovieMap.get(country);

            //Fetching request fields for 1 seat & no Add-on
            CinemaBookSeatRequestFields cinemaBookSeatRequestFields10 = CinemaUtils.getSeatsAndAddOnsInfoOfaMovieSchedule(schedule, 1, false, 0, country);

            ArrayList<String> currenciesSupportedByThisCountry = CinemaConstants.countryCurrencyMap.get(country);
            for (String currency : currenciesSupportedByThisCountry) {
                data.add(new Object[]{cinemaBookSeatRequestFields10.bookedSeatList, cinemaBookSeatRequestFields10.addOnsBookingList, cinemaBookSeatRequestFields10.movieId, cinemaBookSeatRequestFields10.theatreId, cinemaBookSeatRequestFields10.date, cinemaBookSeatRequestFields10.showTimeId, currency, cinemaBookSeatRequestFields10.showTime, cinemaBookSeatRequestFields10.auditoriumNumber, cinemaBookSeatRequestFields10.auditoriumId});
            }
            //Again fetching request fields for 1 seat & no Add-on
            CinemaBookSeatRequestFields anotherCinemaBookSeatRequestFields10 = CinemaUtils.getSeatsAndAddOnsInfoOfaMovieSchedule(schedule, 1, false, 0, country);
            data.add(new Object[]{anotherCinemaBookSeatRequestFields10.bookedSeatList, anotherCinemaBookSeatRequestFields10.addOnsBookingList, anotherCinemaBookSeatRequestFields10.movieId, anotherCinemaBookSeatRequestFields10.theatreId, anotherCinemaBookSeatRequestFields10.date, anotherCinemaBookSeatRequestFields10.showTimeId, null, anotherCinemaBookSeatRequestFields10.showTime, anotherCinemaBookSeatRequestFields10.auditoriumNumber, anotherCinemaBookSeatRequestFields10.auditoriumId});
        }
        return data.iterator();
    }

    @DataProvider(name = "getLocaleValidBookingRequestFields")
    public static Iterator<Object[]> getLocaleValidBookingRequestFields() { //total testData count = 2
        List<Object[]> data = new ArrayList<Object[]>();
        LinkedHashMap<String, CinemaGetScheduleSummaryRequestFields> validCountryAndBookableMovieMap = getCountryAndOneBookableMovieMap(); //assumption is that bookable movie will always have addons

        Set<String> countries = validCountryAndBookableMovieMap.keySet();
        for (String country : countries) {
            CinemaGetScheduleSummaryRequestFields schedule = validCountryAndBookableMovieMap.get(country);

            ArrayList<String> currenciesSupportedByThisCountry = CinemaConstants.countryCurrencyMap.get(country);
            for (String currency : currenciesSupportedByThisCountry) {
                ArrayList<String> locales = CinemaConstants.countryLocalesMap.get(country);
                for (String locale : locales) {
                    //Fetching request fields for 1 seat & no Add-on
                    CinemaBookSeatRequestFields cinemaBookSeatRequestFields10 = CinemaUtils.getSeatsAndAddOnsInfoOfaMovieSchedule(schedule, 1, false, 0, country);
                    data.add(new Object[]{locale, cinemaBookSeatRequestFields10.bookedSeatList, cinemaBookSeatRequestFields10.addOnsBookingList, cinemaBookSeatRequestFields10.movieId, cinemaBookSeatRequestFields10.theatreId, cinemaBookSeatRequestFields10.date, cinemaBookSeatRequestFields10.showTimeId, currency, cinemaBookSeatRequestFields10.showTime, cinemaBookSeatRequestFields10.auditoriumNumber, cinemaBookSeatRequestFields10.auditoriumId});
                }
            }
        }
        return data.iterator();
    }

    @DataProvider(name = "getAValidBookingRequestFieldsWithoutCurrencyOfIndonesia")
    public static Iterator<Object[]> getAValidBookingRequestFieldsWithoutCurrencyOfIndonesia() { //total testData count = 1
        List<Object[]> data = new ArrayList<Object[]>();
        LinkedHashMap<String, CinemaGetScheduleSummaryRequestFields> validCountryAndBookableMovieMap = getCountryAndOneBookableMovieMap();
        CinemaGetScheduleSummaryRequestFields schedule = validCountryAndBookableMovieMap.get("INDONESIA");

        //Fetching request fields for 1 seat & no Add-on
        CinemaBookSeatRequestFields cinemaBookSeatRequestFields10 = CinemaUtils.getSeatsAndAddOnsInfoOfaMovieSchedule(schedule, 1, false, 0, "INDONESIA");
        data.add(new Object[]{cinemaBookSeatRequestFields10.bookedSeatList, cinemaBookSeatRequestFields10.addOnsBookingList, cinemaBookSeatRequestFields10.movieId, cinemaBookSeatRequestFields10.theatreId, cinemaBookSeatRequestFields10.date, cinemaBookSeatRequestFields10.showTimeId, cinemaBookSeatRequestFields10.showTime, cinemaBookSeatRequestFields10.auditoriumNumber, cinemaBookSeatRequestFields10.auditoriumId});
        return data.iterator();
    }

    //DP of getBookingReview & cancelBooking APIs below
    @DataProvider(name = "getValidAndInvalidBookingInformation")
    public static Iterator<Object[]> getValidAndInvalidBookingInformation() { //total testData count = 2
        List<Object[]> data = new ArrayList<Object[]>();
        HashMap<String, String> successfulBookingInformation = CinemaUtils.getASuccessfulBookingInformation("INDONESIA", 1, true, 1);
        data.add(new Object[]{successfulBookingInformation.get("invoiceId"), successfulBookingInformation.get("bookingId"), successfulBookingInformation.get("auth"), CinemaConstants.getTrackingRequestMap(), "valid"});
        data.add(new Object[]{CinemaConstants.INVALID_VALUE, CinemaConstants.INVALID_VALUE, successfulBookingInformation.get("auth"), CinemaConstants.getTrackingRequestMap(), "invalid"});
        return data.iterator();
    }

    @DataProvider(name = "getValidBookingInformationWithAndWithoutTrackingRequest")
    public static Iterator<Object[]> getValidBookingInformationWithAndWithoutTrackingRequest() { //total testData count = 2
        List<Object[]> data = new ArrayList<Object[]>();
        HashMap<String, String> successfulBookingInformation = CinemaUtils.getASuccessfulBookingInformation("INDONESIA", 1, true, 1);
        data.add(new Object[]{successfulBookingInformation.get("invoiceId"), successfulBookingInformation.get("bookingId"), successfulBookingInformation.get("auth"), CinemaConstants.getTrackingRequestMap()});
        HashMap<String, String> successfulBookingInformation2 = CinemaUtils.getASuccessfulBookingInformation("INDONESIA", 1, true, 1);
        data.add(new Object[]{successfulBookingInformation2.get("invoiceId"), successfulBookingInformation2.get("bookingId"), successfulBookingInformation2.get("auth"), null});
        return data.iterator();
    }

    @DataProvider(name = "getLocaleValidBookingInfo")
    public static Iterator<Object[]> getLocaleValidBookingInfo() { //total testData count is 5 ( Summation of no. of locales of a country )
        List<Object[]> data = new ArrayList<Object[]>();
        LinkedHashMap<String, CinemaGetScheduleSummaryRequestFields> validCountryAndBookableMovieMap = getCountryAndOneBookableMovieMap();
        Set<String> countries = validCountryAndBookableMovieMap.keySet();
        for (String country : countries) {
            ArrayList<String> locales = CinemaConstants.countryLocalesMap.get(country);
            for (String locale : locales) {
                HashMap<String, String> successfulBookingInformation = CinemaUtils.getASuccessfulBookingInformation(country, 1, true, 1);
                data.add(new Object[]{locale, successfulBookingInformation.get("invoiceId"), successfulBookingInformation.get("bookingId"), successfulBookingInformation.get("auth"), CinemaConstants.getTrackingRequestMap()});
            }
        }
        return data.iterator();
    }

    @DataProvider(name = "getAValidBookingInformation")
    public static Object[][] getAValidBookingInformation() { //total testData count = 1
        Object[][] data = new Object[1][];
        HashMap<String, String> successfulBookingInformation = CinemaUtils.getASuccessfulBookingInformation("INDONESIA", 1, true, 1);
        data[0] = new Object[]{successfulBookingInformation.get("invoiceId"), successfulBookingInformation.get("bookingId"), successfulBookingInformation.get("auth"), CinemaConstants.getTrackingRequestMap()};
        return data;
    }

    //DPs of saveToFavourite below
    //Note: getTheatreIDCurrencyAndTrackingRequest ,getLocaleAndTheatreId, getValidTheatreId methods reused
    @DataProvider(name = "getTheatreIDAndTrackingRequest")
    public Object[][] getTheatreIDAndTrackingRequest() {
        int countOfTestData = 0;
        Object[][] object = new Object[4][]; // 4 signifies total number of unique testData (below)
        object[countOfTestData++] = new Object[]{CinemaConstants.VALID_THEATRE_ID_INDONESIA, CinemaConstants.getTrackingRequestMap()};
        object[countOfTestData++] = new Object[]{CinemaConstants.VALID_THEATRE_ID_INDONESIA, null};
        object[countOfTestData++] = new Object[]{CinemaConstants.INVALID_THEATRE_ID, CinemaConstants.getTrackingRequestMap()};
        object[countOfTestData++] = new Object[]{CinemaConstants.INVALID_THEATRE_ID, null};
        return object;
    }

    //DPs of checkInventoryByLocation API below
    @DataProvider(name = "getValidAndInvalidLatLong")
    public Object[][] getValidAndInvalidLatLong() {
        ArrayList<String[]> listOfValidAndInvalidLatLng = CinemaConstants.validAndInvalidSupportedLatLng;
        Object[][] object = new Object[listOfValidAndInvalidLatLng.size()][];
        for (int countOfTestData = 0; countOfTestData < listOfValidAndInvalidLatLng.size(); countOfTestData++) {
            object[countOfTestData] = new Object[]{listOfValidAndInvalidLatLng.get(countOfTestData)[0], listOfValidAndInvalidLatLng.get(countOfTestData)[1], listOfValidAndInvalidLatLng.get(countOfTestData)[2]}; //latitude, longitude and type of test result added
        }
        return object;
    }

    @DataProvider(name = "getLocalesAndAValidLatLonDP")
    public static Iterator<Object[]> getLocalesAndAValidLatLonDP() {
        String[] locs = CinemaConstants.getSupportedCinemaLocales();
        String[] validLatLng = CinemaConstants.validAndInvalidSupportedLatLng.get(0);
        String lat = validLatLng[0];
        String lon = validLatLng[1];
        List<Object[]> data = new ArrayList<>();
        for (String loc : locs) {
            data.add(new String[]{loc, lat, lon});
        }
        return data.iterator();
    }

    @DataProvider(name = "getAValidLatLonDP")
    public static Object[] getAValidLatLonDP() {
        String[] validLatLng = CinemaConstants.validAndInvalidSupportedLatLng.get(0);
        String lat = validLatLng[0];
        String lon = validLatLng[1];
        return new Object[][]{new Object[]{lat, lon}};
    }

    //DPs of landingPageDiscover API below
    @DataProvider(name = "getValidAndInvalidCityIdDP")
    public Iterator<Object[]> getValidAndInvalidCityIdDP() {        //total testData count is 3 ( 1+ no. of supported countries )
        List<Object[]> data = new ArrayList<Object[]>();
        HashMap<String, HashMap<String, ArrayList<String>>> countryAndDefaultCityDetailsMap = CinemaConstants.countryAndDefaultCityDetailsMap;
        Set<String> countries = countryAndDefaultCityDetailsMap.keySet();
        for (String country : countries) {
            HashMap<String, ArrayList<String>> defaultCityInfoMapOfCountry = countryAndDefaultCityDetailsMap.get(country);
            Set<String> cityIds = defaultCityInfoMapOfCountry.keySet();
            for (String cityId : cityIds) {
                ArrayList<String> cityInfo = defaultCityInfoMapOfCountry.get(cityId);
                data.add(new Object[]{cityId, cityInfo.get(1), "valid"});
            }
        }
        data.add(new Object[]{CinemaConstants.INVALID_VALUE, null, "invalid"});
        return data.iterator();
    }

    @DataProvider(name = "getCityIDCurrencyAndTrackingRequest")
    public Iterator<Object[]> getCityIDCurrencyAndTrackingRequest() { //total testData count is 3 * ( no. of supported currency for Indonesia )
        List<Object[]> data = new ArrayList<Object[]>();
        HashMap<String, ArrayList<String>> defaultCityInfoMapOfCountry = CinemaConstants.countryAndDefaultCityDetailsMap.get("INDONESIA");
        ArrayList<String> currencies = CinemaConstants.countryCurrencyMap.get("INDONESIA");
        Set<String> cityIds = defaultCityInfoMapOfCountry.keySet();
        for (String currency : currencies) {
            data.add(new Object[]{cityIds.stream().findFirst().get(), currency, CinemaConstants.getTrackingRequestMap()});
            data.add(new Object[]{cityIds.stream().findFirst().get(), null, CinemaConstants.getTrackingRequestMap()});
            data.add(new Object[]{cityIds.stream().findFirst().get(), null, null});
        }
        return data.iterator();
    }

    @DataProvider(name = "getLocaleAndCityIDofEachCountry")
    public Iterator<Object[]> getLocaleAndCityID() { //total testData count is 5 ( Summation of  no. of supported locales of country )
        List<Object[]> data = new ArrayList<Object[]>();
        Set<String> countries = CinemaConstants.countryAndDefaultCityDetailsMap.keySet();
        for (String country : countries) {
            ArrayList<String> locales = CinemaConstants.countryLocalesMap.get(country);
            HashMap<String, ArrayList<String>> defaultCityInfoMapOfCountry = CinemaConstants.countryAndDefaultCityDetailsMap.get(country);
            Set<String> cityIds = defaultCityInfoMapOfCountry.keySet();
            for (String locale : locales) {
                data.add(new Object[]{locale, cityIds.stream().findFirst().get()});
            }
        }
        return data.iterator();
    }

    @DataProvider(name = "getAValidCityIDofIndonesia")
    public Iterator<Object[]> getAValidCityIDofIndonesia() { //total testData count is 1
        List<Object[]> data = new ArrayList<Object[]>();
        HashMap<String, ArrayList<String>> defaultCityInfoMapOfCountry = CinemaConstants.countryAndDefaultCityDetailsMap.get("INDONESIA");
        data.add(new Object[]{defaultCityInfoMapOfCountry.keySet().stream().findFirst().get()});
        return data.iterator();
    }

    //DPs of searchTheatreDetail API below
    @DataProvider(name = "getValidAndInvalidTheatreIdCurrencyDateDP")
    public Iterator<Object[]> getValidAndInvalidTheatreIdCurrencyDateDP() {        //total testData count is 3 ( 1+ no. of supported countries )
        List<Object[]> data = new ArrayList<Object[]>();
        LinkedHashMap<String, LinkedHashMap<String, String>> validCountryTheatresMap = CinemaConstants.validCountryTheatresMap;
        Set<String> countries = validCountryTheatresMap.keySet();
        HashMap<String, String> date = CinemaUtils.getDateMap(+1);
        for (String country : countries) {
            HashMap<String, String> theatresMapOfCountry = validCountryTheatresMap.get(country);
            String theatreId = theatresMapOfCountry.keySet().stream().findFirst().get();
            ArrayList<String> currenciesSupportedByThisCountry = CinemaConstants.countryCurrencyMap.get(country);
            for (String currency : currenciesSupportedByThisCountry) {
                data.add(new Object[]{currency, theatreId, date, "valid"});
            }
        }
        data.add(new Object[]{null, CinemaConstants.INVALID_THEATRE_ID, date, "invalid"});
        return data.iterator();
    }

    @DataProvider(name = "getAllCombinationOfTheatreIdCurrencyDateDP")
    public Iterator<Object[]> getAllCombinationOfTheatreIdCurrencyDateDP() {        //total testData count is 8 (4 * no. of supported countries )
        List<Object[]> data = new ArrayList<Object[]>();
        LinkedHashMap<String, LinkedHashMap<String, String>> validCountryTheatresMap = CinemaConstants.validCountryTheatresMap;
        Set<String> countries = validCountryTheatresMap.keySet();
        HashMap<String, String> date = CinemaUtils.getDateMap(+1);
        for (String country : countries) {
            HashMap<String, String> theatresMapOfCountry = validCountryTheatresMap.get(country);
            String theatreId = theatresMapOfCountry.keySet().stream().findFirst().get();
            ArrayList<String> currenciesSupportedByThisCountry = CinemaConstants.countryCurrencyMap.get(country);
            for (String currency : currenciesSupportedByThisCountry) {
                data.add(new Object[]{currency, theatreId, date});
                data.add(new Object[]{null, theatreId, date});
                data.add(new Object[]{currency, theatreId, null});
                data.add(new Object[]{null, theatreId, null});
            }
        }
        return data.iterator();
    }

    @DataProvider(name = "getAValidTheatreIdCurrencyDateOfEachCountryDP")
    public Iterator<Object[]> getAValidTheatreIdCurrencyDateOfEachCountryDP() {        //total testData count is 5 ( no. of locales of supported countries )
        List<Object[]> data = new ArrayList<Object[]>();
        LinkedHashMap<String, LinkedHashMap<String, String>> validCountryTheatresMap = CinemaConstants.validCountryTheatresMap;
        Set<String> countries = validCountryTheatresMap.keySet();
        HashMap<String, String> date = CinemaUtils.getDateMap(+1);
        for (String country : countries) {
            ArrayList<String> locales = CinemaConstants.countryLocalesMap.get(country);
            HashMap<String, String> theatresMapOfCountry = validCountryTheatresMap.get(country);
            String theatreId = theatresMapOfCountry.keySet().stream().findFirst().get();
            ArrayList<String> currenciesSupportedByThisCountry = CinemaConstants.countryCurrencyMap.get(country);
            for (String locale : locales) {
                for (String currency : currenciesSupportedByThisCountry) {
                    data.add(new Object[]{locale, currency, theatreId, date});
                }
            }
        }
        return data.iterator();
    }

    @DataProvider(name = "getAValidTheatreIdCurrencyDateDP")
    public Iterator<Object[]> getAValidTheatreIdCurrencyDateDP() {        //total testData count is 1
        List<Object[]> data = new ArrayList<Object[]>();
        LinkedHashMap<String, LinkedHashMap<String, String>> validCountryTheatresMap = CinemaConstants.validCountryTheatresMap;
        Set<String> countries = validCountryTheatresMap.keySet();
        HashMap<String, String> date = CinemaUtils.getDateMap(+2);
        for (String country : countries) {
            ArrayList<String> locales = CinemaConstants.countryLocalesMap.get(country);
            HashMap<String, String> theatresMapOfCountry = validCountryTheatresMap.get(country);
            String theatreId = theatresMapOfCountry.keySet().stream().findFirst().get();
            ArrayList<String> currenciesSupportedByThisCountry = CinemaConstants.countryCurrencyMap.get(country);
            for (String locale : locales) {
                for (String currency : currenciesSupportedByThisCountry) {
                    data.add(new Object[]{locale, currency, theatreId, date});
                    return data.iterator();
                }
            }
        }
        return data.iterator();
    }

    //DPs of searchTheatre API below
    @DataProvider(name = "getValidAndInvalidCityIdMovieIdDP")
    public Iterator<Object[]> getValidAndInvalidCityIdMovieIdDP() {        //total testData count is 3 ( 1+ no. of supported countries )
        List<Object[]> data = new ArrayList<Object[]>();
        HashMap<String, HashMap<String, ArrayList<String>>> countryAndDefaultCityDetailsMap = CinemaConstants.countryAndDefaultCityDetailsMap;
        Set<String> countries = countryAndDefaultCityDetailsMap.keySet();
        for (String country : countries) {
            HashMap<String, ArrayList<String>> defaultCityInfoMapOfCountry = countryAndDefaultCityDetailsMap.get(country);
            Set<String> cityIds = defaultCityInfoMapOfCountry.keySet();
            for (String cityId : cityIds) {
                HashMap<String, List<String>> mapOfMovieTypeAndMovieList = CinemaUtils.getListOfMoviesInACity(cityId);
                try {
                    String aMovieIdOfTypeNowPlaying = mapOfMovieTypeAndMovieList.get("nowPlaying").stream().findFirst().get();
                    data.add(new Object[]{cityId, aMovieIdOfTypeNowPlaying, "nowPlaying"});
                    String aMovieIdOfTypeComingSoon = mapOfMovieTypeAndMovieList.get("comingSoon").stream().findFirst().get();
                    data.add(new Object[]{cityId, aMovieIdOfTypeComingSoon, "comingSoon"});
                    //data for preSale might be needed for production
                } catch (Exception ex) {
                    ArrayList<String> cityInfo = defaultCityInfoMapOfCountry.get(cityId);
                    Reporter.log("Note: Failed to fetch movies from landingPageDiscover API for city " + cityInfo.get(0));
                    data.add(new Object[]{cityId, CinemaConstants.INVALID_RANDOM_VALUE_WHEN_PRE_TEST_FAILS + "_" + cityInfo.get(0), "nowPlaying"});
                    data.add(new Object[]{cityId, CinemaConstants.INVALID_RANDOM_VALUE_WHEN_PRE_TEST_FAILS + "_" + cityInfo.get(0), "comingSoon"});
                }
            }
        }
        data.add(new Object[]{CinemaConstants.INVALID_VALUE, CinemaConstants.INVALID_VALUE, "invalid"});
        return data.iterator();
    }

    @DataProvider(name = "getValidCombinationOfCityIdMovieIdTrackingRequestDP")
    public Iterator<Object[]> getValidCombinationOfCityIdMovieIdTrackingRequestDP() {        //total testData count is 2^2
        List<Object[]> data = new ArrayList<Object[]>();
        HashMap<String, HashMap<String, ArrayList<String>>> countryAndDefaultCityDetailsMap = CinemaConstants.countryAndDefaultCityDetailsMap;
        HashMap<String, ArrayList<String>> defaultCityInfoMapOfCountry = countryAndDefaultCityDetailsMap.get("INDONESIA");
        String cityId = defaultCityInfoMapOfCountry.keySet().stream().findFirst().get(); //getting one of the cityId for Indonesia
        HashMap<String, List<String>> mapOfMovieTypeAndMovieList = CinemaUtils.getListOfMoviesInACity(cityId);
        try {
            String aMovieIdOfTypeNowPlaying = mapOfMovieTypeAndMovieList.get("nowPlaying").stream().findFirst().get();
            data.add(new Object[]{cityId, aMovieIdOfTypeNowPlaying, CinemaConstants.getTrackingRequestMap()});
            data.add(new Object[]{cityId, aMovieIdOfTypeNowPlaying, null});
            data.add(new Object[]{cityId, null, CinemaConstants.getTrackingRequestMap()});
            data.add(new Object[]{cityId, null, null});

        } catch (Exception ex) {
            ArrayList<String> cityInfo = defaultCityInfoMapOfCountry.get(cityId);
            Reporter.log("Note: Failed to fetch movies from landingPageDiscover API for city " + cityInfo.get(0));
            data.add(new Object[]{cityId, CinemaConstants.INVALID_RANDOM_VALUE_WHEN_PRE_TEST_FAILS + "_" + cityInfo.get(0), CinemaConstants.getTrackingRequestMap()});
        }
        return data.iterator();
    }

    @DataProvider(name = "getLocaleAndAllRequestParameterOfEachCountryDP")
    public Iterator<Object[]> getLocaleAndAllRequestParameterOfEachCountryDP() {        //total testData count is 3 ( 1+ no. of supported countries )
        List<Object[]> data = new ArrayList<Object[]>();
        HashMap<String, HashMap<String, ArrayList<String>>> countryAndDefaultCityDetailsMap = CinemaConstants.countryAndDefaultCityDetailsMap;
        Set<String> countries = countryAndDefaultCityDetailsMap.keySet();
        for (String country : countries) {
            HashMap<String, ArrayList<String>> defaultCityInfoMapOfCountry = countryAndDefaultCityDetailsMap.get(country);
            String cityId = defaultCityInfoMapOfCountry.keySet().stream().findFirst().get();
            HashMap<String, List<String>> mapOfMovieTypeAndMovieList = CinemaUtils.getListOfMoviesInACity(cityId);
            ArrayList<String> locales = CinemaConstants.countryLocalesMap.get(country);
            try {
                String aMovieIdOfTypeNowPlaying = mapOfMovieTypeAndMovieList.get("nowPlaying").stream().findFirst().get();
                for (String locale : locales) {
                    data.add(new Object[]{locale, cityId, aMovieIdOfTypeNowPlaying, CinemaConstants.getTrackingRequestMap()});
                }
            } catch (Exception ex) {
                ArrayList<String> cityInfo = defaultCityInfoMapOfCountry.get(cityId);
                Reporter.log("Note: Failed to fetch movies from landingPageDiscover API for city " + cityInfo.get(0)); //cityInfo.get(0) is name of city
                for (String locale : locales) {
                    data.add(new Object[]{locale, cityId, CinemaConstants.INVALID_RANDOM_VALUE_WHEN_PRE_TEST_FAILS + "_" + cityInfo.get(0), CinemaConstants.getTrackingRequestMap()});
                }
            }
        }
        return data.iterator();
    }

    @DataProvider(name = "getAValidCityIdMovieIdTrackingRequestDP")
    public Iterator<Object[]> getAValidCityIdMovieIdTrackingRequestDP() {        //total testData count is 1
        List<Object[]> data = new ArrayList<Object[]>();
        HashMap<String, HashMap<String, ArrayList<String>>> countryAndDefaultCityDetailsMap = CinemaConstants.countryAndDefaultCityDetailsMap;
        HashMap<String, ArrayList<String>> defaultCityInfoMapOfCountry = countryAndDefaultCityDetailsMap.get("INDONESIA");
        String cityId = defaultCityInfoMapOfCountry.keySet().stream().findFirst().get();
        HashMap<String, List<String>> mapOfMovieTypeAndMovieList = CinemaUtils.getListOfMoviesInACity(cityId);
        try {
            String aMovieIdOfTypeNowPlaying = mapOfMovieTypeAndMovieList.get("nowPlaying").stream().findFirst().get();
            data.add(new Object[]{cityId, aMovieIdOfTypeNowPlaying, CinemaConstants.getTrackingRequestMap()});

        } catch (Exception ex) {
            ArrayList<String> cityInfo = defaultCityInfoMapOfCountry.get(cityId);
            Reporter.log("Note: Failed to fetch movies from landingPageDiscover API for city " + cityInfo.get(0));
            data.add(new Object[]{cityId, CinemaConstants.INVALID_RANDOM_VALUE_WHEN_PRE_TEST_FAILS + "_" + cityInfo.get(0), CinemaConstants.getTrackingRequestMap()});
        }
        return data.iterator();
    }

    //DPs of searchTheatresInfo API below
    @DataProvider(name = "getValidAndInvalidListsOfTheatreIdDP")
    public Iterator<Object[]> getValidAndInvalidListsOfTheatreIdDP() {        //total testData count is 5 ( no. of supported countries * 2 +1)
        List<Object[]> data = new ArrayList<Object[]>();
        LinkedHashMap<String, LinkedHashMap<String, String>> validCountryTheatresMap = CinemaConstants.validCountryTheatresMap;
        Set<String> countries = validCountryTheatresMap.keySet();
        for (String country : countries) {
            LinkedHashMap<String, String> theatresMapOfCountry = validCountryTheatresMap.get(country);
            Set<String> theatreIds = theatresMapOfCountry.keySet();
            data.add(new Object[]{new ArrayList<String>() {{
                addAll(theatreIds);
            }}, "allTheatreIdsValidInList"});
            data.add(new Object[]{new ArrayList<String>() {{
                addAll(theatreIds);
                add(CinemaConstants.INVALID_THEATRE_ID);
            }}, "oneTheatreIdInvalidInList"});
        }
        data.add(new Object[]{new ArrayList<String>() {{
            add(CinemaConstants.INVALID_THEATRE_ID);
        }}, "valueInvalidInList"});
        return data.iterator();
    }

    @DataProvider(name = "getValidListsOfTheatreIdAndLocaleDP")
    public Iterator<Object[]> getValidListsOfTheatreIdAndLocaleDP() {        //total testData count is 5 ( no. of supported countries * 2 +1)
        List<Object[]> data = new ArrayList<Object[]>();
        LinkedHashMap<String, LinkedHashMap<String, String>> validCountryTheatresMap = CinemaConstants.validCountryTheatresMap;
        Set<String> countries = validCountryTheatresMap.keySet();
        for (String country : countries) {
            LinkedHashMap<String, String> theatresMapOfCountry = validCountryTheatresMap.get(country);
            Set<String> theatreIds = theatresMapOfCountry.keySet();
            ArrayList<String> locales = CinemaConstants.countryLocalesMap.get(country);
            for (String locale : locales) {
                data.add(new Object[]{new ArrayList<String>() {{
                    addAll(theatreIds);
                }}, locale});
            }
        }
        return data.iterator();
    }

    @DataProvider(name = "getAValidListsOfTheatreIdDP")
    public Iterator<Object[]> getAValidListsOfTheatreIdDP() {        //total testData count is 1
        List<Object[]> data = new ArrayList<Object[]>();
        LinkedHashMap<String, LinkedHashMap<String, String>> validCountryTheatresMap = CinemaConstants.validCountryTheatresMap;
        Set<String> countries = validCountryTheatresMap.keySet();
        for (String country : countries) {
            LinkedHashMap<String, String> theatresMapOfCountry = validCountryTheatresMap.get(country);
            Set<String> theatreIds = theatresMapOfCountry.keySet();
            data.add(new Object[]{new ArrayList<String>() {{
                addAll(theatreIds);
            }}});
            return data.iterator();
        }
        return data.iterator();
    }
}

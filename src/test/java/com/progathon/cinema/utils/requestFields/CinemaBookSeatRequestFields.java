package com.progathon.cinema.utils.requestFields;

import com.progathon.cinema.utils.CinemaUtils;

import java.util.ArrayList;
import java.util.Map;

/**
 * Author: nitinkumar
 * Created Date: 06/01/20
 * Info: All the request fields of bookSeat API
 **/

public class CinemaBookSeatRequestFields {

    public String showTimeId;
    public String showTime;
    public ArrayList<AddOn> addOnsBookingList = new ArrayList<>();
    public String movieId;
    public String theatreId;
    public Map<String, String> date;
    public String auditoriumNumber;
    public String auditoriumId;
    public String currency;
    public ArrayList<Seat> bookedSeatList = new ArrayList<>();

    public CinemaBookSeatRequestFields() {
    }

    public CinemaBookSeatRequestFields(CinemaGetScheduleSummaryRequestFields cinemaGetScheduleSummaryRequestFields, String showTimeId, String showTime, ArrayList<AddOn> addOnsBookingList, String movieId, String theatreId, Map<String, String> date, String auditoriumTypeId, String auditoriumNumber, String auditoriumId, String currency, ArrayList<Seat> bookedSeatList) {
        this.showTimeId = showTimeId;
        this.showTime = showTime;
        this.addOnsBookingList = addOnsBookingList;
        this.movieId = movieId;
        this.theatreId = theatreId;
        this.date = date;
        //this.auditoriumTypeId = auditoriumTypeId;
        this.auditoriumNumber = auditoriumNumber;
        this.auditoriumId = auditoriumTypeId;
        this.currency = currency;
        this.bookedSeatList = bookedSeatList;
    }

    public void setValues(CinemaGetScheduleSummaryRequestFields scheduleSummaryRequestFields, ArrayList<AddOn> addOnsBookingList, String auditoriumNumber, String currency, ArrayList<Seat> bookedSeats) {
        this.movieId = scheduleSummaryRequestFields.movieId;
        this.theatreId = scheduleSummaryRequestFields.theatreId;
        this.date = scheduleSummaryRequestFields.date;
        this.auditoriumId = scheduleSummaryRequestFields.auditoriumTypeId;
        this.showTimeId = scheduleSummaryRequestFields.showTimeId;
        this.showTime = CinemaUtils.getShowTimeFromShowTimeId(scheduleSummaryRequestFields.showTimeId);
        this.addOnsBookingList = addOnsBookingList;
        this.auditoriumNumber = auditoriumNumber;
        this.currency = currency;
        if(bookedSeats!=null)
            this.bookedSeatList.addAll(bookedSeats);
    }

    public class AddOn {
        String addonsId;
        public String quantity;

        public AddOn(String addonsId, String quantity) {
            this.addonsId = addonsId;
            this.quantity = quantity;
        }

        @Override
        public String toString() {
            return "{" +
                    "addonsId=\"" + addonsId + "\"" +
                    ", quantity=\"" + quantity + "\"" +
                    "}";
        }
    }

    public class Seat {
        String seatId;
        String providerCinemaSeatTypeId;

        public Seat(String seatId, String providerCinemaSeatTypeId) {
            this.seatId = seatId;
            this.providerCinemaSeatTypeId = providerCinemaSeatTypeId;
        }

        @Override
        public String toString() {
            return "{" +
                    "seatId=\"" + seatId + "\"" +
                    ", providerCinemaSeatTypeId=\"" + providerCinemaSeatTypeId + "\"" +
                    "}";
        }
    }

    @Override
    public String toString() {
        return "{" +
                " showTimeId='" + showTimeId + '\'' +
                ", showTime='" + showTime + '\'' +
                ", addOnsBookingList=" + addOnsBookingList +
                ", movieId='" + movieId + '\'' +
                ", theatreId='" + theatreId + '\'' +
                ", date=" + date +
                ", auditoriumNumber='" + auditoriumNumber + '\'' +
                ", auditoriumId='" + auditoriumId + '\'' +
                ", currency='" + currency + '\'' +
                ", bookedSeats=" + bookedSeatList +
                '}';
    }
}



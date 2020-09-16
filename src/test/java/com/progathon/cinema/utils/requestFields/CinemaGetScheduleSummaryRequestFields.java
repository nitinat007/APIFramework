package com.progathon.cinema.utils.requestFields;

import java.util.Map;

/**
 * Author: nitinkumar
 * Created Date: 12/12/19
 * Info: All the request fields of getScheduleSummary API
 **/

public class CinemaGetScheduleSummaryRequestFields {
    public String movieId;
    public String theatreId;
    public Map<String,String> date;
    public String auditoriumTypeId;
    public String showTimeId;

    public CinemaGetScheduleSummaryRequestFields(String movieId, String theatreId, Map<String, String> date, String auditoriumTypeId, String showTimeId) {
        this.movieId = movieId;
        this.theatreId = theatreId;
        this.date = date;
        this.auditoriumTypeId = auditoriumTypeId;
        this.showTimeId = showTimeId;
    }

    public boolean isAllNotNullableFieldsNotNull(){
        return movieId!=null && theatreId!=null && auditoriumTypeId!=null && showTimeId!=null && date.get("day")!=null && date.get("month")!=null && date.get("year")!=null;
    }

    @Override
    public String toString() {
        return "{" +
                "movieId='" + movieId + '\'' +
                ", theatreId='" + theatreId + '\'' +
                ", date=" + date +
                ", auditoriumTypeId='" + auditoriumTypeId + '\'' +
                ", showTimeId='" + showTimeId + '\'' +
                '}';
    }
}

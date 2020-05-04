package org.nitincompany.users.reviews.utils;

/**
 * Author: nitinkumar
 * Created Date: 18/02/20
 * Info: This class contains parameters used in updating CSV
 **/

public class ReviewSubmissionBean {

    public String valueToUpdate;
    public String columnToUpdate;
    public String createdTimestampStart;
    public String createdTimestampEnd;
    public String updatedTimestampStart;
    public String updatedTimestampEnd;
    public String testcaseNumber;

    public ReviewSubmissionBean(String testcaseNumber, String columnToUpdate, String valueToUpdate) {
        this.flushAllData();
        this.valueToUpdate = valueToUpdate;
        this.testcaseNumber = testcaseNumber;
        this.columnToUpdate = columnToUpdate;
    }

    public void flushAllData() {
        this.valueToUpdate = "";
        this.columnToUpdate = "";
        this.createdTimestampStart = "";
        this.createdTimestampEnd = "";
        this.updatedTimestampStart = "";
        this.updatedTimestampEnd = "";
        this.testcaseNumber = "";
    }
}

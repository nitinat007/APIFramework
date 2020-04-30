package org.nitincompany.framework.communicators;

import io.restassured.RestAssured;
import org.nitincompany.framework.constants.AWSs3Reports;
import org.nitincompany.framework.constants.SlackConstants;
import org.nitincompany.framework.listeners.TestListener;

import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Author: nitinkumar
 * Created Date: 30/04/20
 * Info: utilities to send slack notification
 **/

public class Slack {
    public void sendMessageToSlack(String projectName) {
        try {

            FileReader reader = new FileReader(getSlackConfigFilePath(projectName));
            Properties p = new Properties();
            p.load(reader);

            String channelName = p.getProperty("channel.name"), botName = p.getProperty("bot.name");
            String channel[] = channelName.split(",");
            String reportURL = System.getProperty("reportURL");
            if (reportURL == null) { //Fallback: if reportURL is not passed (by AWS/jenkins/local) then assuming that slack notification is triggered from AWS
                reportURL = new AWSs3Reports().getReportURLInAWS(projectName);
            }
            String pic = p.getProperty("pic");
            int total = TestListener.passed + TestListener.failed + TestListener.skipped;

            Map map = new HashMap();
            map.put("Content-Type", "application/json");
            map.put("Accept", "*/*");

            for (int i = 0; i < channel.length; i++) {
                RestAssured.given().headers(map).body("{\"channel\": \"" + channel[i] + "\", \"username\": \"" + botName + "\", \"text\": \"" + "*Project :* " + projectName + "\n *PIC:* " + pic + "\n *Report:* <" + reportURL + "| click>\", \"icon_emoji\": \":robot_face:\",   \"attachments\":[\n" +
                        "      {\n" +
                        "         \"fields\":[\n" +
                        "            {\n" +
                        "               \"title\":\"Total no. of Test Cases\",\n" +
                        "               \"value\":\"" + total + "\",\n" +
                        "               \"short\":false\n" +
                        "            }\n" +
                        "         ]\n" +
                        "      },\n" +
                        "      {\n" +
                        "         \"color\":\"#00ff00\",\n" +
                        "         \"fields\":[\n" +
                        "            {\n" +
                        "               \"title\":\"Passed :check_green:\",\n" +
                        "               \"value\":\"" + TestListener.passed + "\",\n" +
                        "               \"short\":false\n" +
                        "            }\n" +
                        "         ]\n" +
                        "      },\n" +
                        "      {\n" +
                        "         \"color\":\"#ff0000\",\n" +
                        "         \"fields\":[\n" +
                        "            {\n" +
                        "               \"title\":\"Failed :elstat_failed:\",\n" +
                        "               \"value\":\"" + TestListener.failed + "\",\n" +
                        "               \"short\":false\n" +
                        "            }\n" +
                        "         ]\n" +
                        "      },\n" +
                        "      {\n" +
                        "         \"color\":\"#FFFF00\",\n" +
                        "         \"fields\":[\n" +
                        "            {\n" +
                        "               \"title\":\"Skipped :warning:\",\n" +
                        "               \"value\":\"" + TestListener.skipped + "\",\n" +
                        "               \"short\":false\n" +
                        "            }\n" +
                        "         ]\n" +
                        "      }\n" +
                        "\n" +
                        "\n" +
                        "   ]\n}").when().post(SlackConstants.WEBHOOK_URL);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static public String getSlackConfigFilePath(String project) {
        switch (project) {
            case "Experience upper funnel":
                return System.getProperty("user.dir") + "/resources/config-files/slack/slack-upfun.properties";

            case "Experience lower funnel topproduct":
                return System.getProperty("user.dir") + "/resources/config-files/slack/slack-lowfun.properties";
            case "Experience lower funnel regression":
                return System.getProperty("user.dir") + "/resources/config-files/slack/slack-lowfun.properties";
            case "Cinema":
                return System.getProperty("user.dir") + "/resources/config-files/slack/slack-cinema.properties";
            case "UsersReview":
                return System.getProperty("user.dir") + "/resources/config-files/slack/slack-UGC.properties";
            case "UsersComments":
                return System.getProperty("user.dir") + "/resources/config-files/slack/slack-UGC.properties";
            case "UsersBookmarks":
                return System.getProperty("user.dir") + "/resources/config-files/slack/slack-UGC.properties";
            case "Merchandising":
                return System.getProperty("user.dir") + "/resources/config-files/slack/slack-merchandising.properties";
        }
        return null;
    }
}

package com.progathon.framework.readers;

/**
 * // TODO Comment
 */

import au.com.bytecode.opencsv.CSVReader;
import com.progathon.framework.utils.Utils;
import com.progathon.framework.utils.Utils;
import org.testng.ITestContext;
import org.testng.Reporter;
import org.testng.annotations.DataProvider;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class CSVParametersProvider {

  private static Scanner scanner = null;
  private static List<String[]> testData ;
  private static String[] data = null;
  private static String separator = "|";
  private static String filePath = null;
  private static String encoding = null;
  private static boolean debug = false;

  @DataProvider(parallel = false, name = "csv")
  public static Object[][] createData_from_csv(final ITestContext context,
                                               final Method method) {

    testData= new ArrayList<>();
    DataFileParameters parameters = method
            .getAnnotation(DataFileParameters.class);
    if (parameters != null) {

      Reporter.log("---->>>>>> Working Directory = " + System.getProperty("user.dir") + " <<<<<<----",1,true);
      filePath = System.getProperty("user.dir")+String.format("%s/%s",
              (parameters.path().isEmpty() || parameters.path().matches("^\\.$"))
                      ? System.getProperty("user.dir")
                      : Utils.resolveEnvVars(parameters.path()),
              parameters.name());
      encoding = parameters.encoding().isEmpty() ? "UTF-8"
              : parameters.encoding();
    } else {
      throw new RuntimeException(
              "Missing / invalid DataFileParameters annotation");
    }

    int linenum = 0;
    if (debug) {
      System.err
              .println(String.format("Reading configuration file: '%s'", filePath));
    }
    try {
      CSVReader reader = new CSVReader(new FileReader(filePath), ',');

      while ((data = reader.readNext()) != null) {
        // nextLine[] is an array of values from the line
        //String line = scanner.next();
        linenum++;
        if (linenum == 1)
          continue;
        testData.add(data);
      }
    } catch (FileNotFoundException e) {
      System.err.println(String.format("File was not found: '%s'", filePath));
      e.printStackTrace();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    Object[][] testDataArray = new String[testData.size()][];
    testData.toArray(testDataArray);
    return testDataArray;
  }

  private static String readFile(String path, Charset encoding)
          throws IOException {
    byte[] encoded = Files.readAllBytes(Paths.get(path));
    return new String(encoded, encoding);
  }

}

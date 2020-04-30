package org.nitincompany.framework.initializers;

import org.nitincompany.framework.exceptions.FolderDoesNotExistException;
import org.nitincompany.framework.readers.PropertyReader;
import org.nitincompany.framework.reporters.Logger;


import java.io.File;

/**
 * Author: nitinkumar
 * Created Date: 11/11/19
 * Info: This acts as base for framework initialization. Initializer of a particular type should extend this class.
 **/

public class BaseFrameworkInitializer {
    public String env;
    public PropertyReader featureProperties;
    public String propertyFileRelativePath;
    public static Logger logger = new Logger();

    public BaseFrameworkInitializer(String featureName) {

        env = System.getProperty("Environment");
        String pathToFeatureName = System.getProperty("user.dir") + "/src/test/java/org/nitincompany/" + featureName;
        try {
            if (!new File(pathToFeatureName).isDirectory()) {
                throw new FolderDoesNotExistException("Folder " + pathToFeatureName + " not found.");
            }

            propertyFileRelativePath = "/resources/config-files/" + env.toLowerCase() + "/" + featureName + ".properties";
            featureProperties = new PropertyReader(propertyFileRelativePath);
            //featureProperties.printThemAll();
        } catch (Exception ex) {
            logger.log("Exception: " + ex);
            return;
        }

    }
}

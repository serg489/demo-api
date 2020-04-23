package com.demo.shared.configuration;

import com.demo.shared.exceptions.CommonException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

public class Config {

    private static final Logger LOG = LoggerFactory.getLogger(Config.class);
    private static final String ENVIRONMENT = getEnvVariableOrElse("ENVIRONMENT", "config");
    private static Properties property = new Properties();

    static {
        String propertyFileName = String.format(".%s.properties", ENVIRONMENT);
        InputStream input;
        URL baseResource;

        try {
            baseResource = ClassLoader.getSystemResource(propertyFileName);
            LOG.info(String.format("Reading property file %s", baseResource.getPath()));

            input = baseResource.openStream();
            property.load(baseResource.openStream());
            property.forEach((key, value) -> LOG.debug(String.format("Read property [%s][%s]", key, value)));

            input.close();
        } catch (NullPointerException ex) {
            throw new CommonException(String.format("Can not find file %s. Please add '.{ENVIRONMENT}.property file' " +
                    "with list of variables to your project, where {ENVIRONMENT} is value of env variable." +
                    "For example add .qa.property file and set env variable ENVIRONMENT=qa. If nothing set, default " +
                    "filename is .config", propertyFileName), ex);
        } catch (IOException ex) {
            throw new CommonException("Problems with reading config file", ex);
        }
    }

    public static String getProperty(String key) {
        String value = readeFromPropFile(key);
        if (value == null) {
            throw new CommonException(String.format("Property value not found for key [%s]", key));
        } else {
            return value;
        }
    }

    public static String getPropertyOrElse(String key, String defaultValue) {
        String value = readeFromPropFile(key);
        if (value == null) {
            return defaultValue;
        } else {
            return value;
        }
    }

    public static String getEnvVariableOrElse(String variableName, String defaultValue) {
        // property has higher priority
        if (System.getProperty(variableName) != null) {
            return System.getProperty(variableName);
        }
        // env variable has second priority
        if (System.getenv(variableName) != null) {
            return System.getenv(variableName);
        }
        return defaultValue;
    }

    private static String readeFromPropFile(String key) {
        return property.getProperty(key);
    }
}

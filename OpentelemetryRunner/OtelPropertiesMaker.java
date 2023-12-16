package com.example.MercenarySys.OpentelemetryRunner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;


public class OtelPropertiesMaker {
    private static final Logger logger = LoggerFactory.getLogger(OtelPropertiesMaker.class);
    private static final Properties properties = new Properties();
    private final String SENDING_PROTOCOL;
    private final String SENDING_ENDPOINT;
    private final String FILE_PATH = "otel.properties";

    public OtelPropertiesMaker(String sending_protocol, String sending_endpoint) {
        File file = new File(FILE_PATH);
        this.SENDING_PROTOCOL = sending_protocol;
        this.SENDING_ENDPOINT = sending_endpoint;
        if (file.exists()) {
            if (file.delete()) {
                logger.info("Existing otel.properties file deleted.");
            } else {
                logger.info("Failed to delete existing otel.properties file.");
            }
        } else {
            logger.info("otel.properties file does not exist. create new one.");
        }
    }

    public void createAttributeAndProtocolProperty(String DEVELOPER_NAME, String SERVICE_NAME, String SERVICE_NAMESPACE, String SERVICE_VERSION) {
        try (FileInputStream fileInputStream = new FileInputStream(FILE_PATH)) {
            properties.load(fileInputStream);
        } catch (IOException e) {
            logger.error("An error occurred while loading .properties file: " + e.getMessage(), e);
        }
        properties.setProperty("otel.logs.exporter", SENDING_PROTOCOL);
        properties.setProperty("otel.metrics.exporter", SENDING_PROTOCOL);
        properties.setProperty("otel.traces.exporter", SENDING_PROTOCOL);
        properties.setProperty("otel.exporter.otlp.endpoint", SENDING_ENDPOINT);
        String attributemaker =
                "service.name=" + SERVICE_NAME + "," +
                        "service.namespace=" + SERVICE_NAMESPACE + "," +
                        "service.version=" + SERVICE_VERSION + "," +
                        "developer=" + DEVELOPER_NAME;
        properties.setProperty("otel.resource.attributes", attributemaker);
        properties.setProperty("otel.javaagent.debug", "false");
        try (FileOutputStream fileOutputStream = new FileOutputStream(FILE_PATH)) {
            properties.store(fileOutputStream, null);
        } catch (IOException e) {
            logger.error("An error occurred while saving .properties file: " + e.getMessage(), e);
        }
    }

    public void editTraceMethod(String filePath, OtelMethodExtractor extractor) { // otel.properties의 resource.attribute 부분 수정
        String finalValue = extractor.extractAndWriteMethods();
        try (FileInputStream fileInputStream = new FileInputStream(filePath)) {
            properties.load(fileInputStream);
        } catch (IOException ignored) {
        }
        properties.setProperty("otel.instrumentation.methods.include", finalValue);
        try (FileOutputStream fileOutputStream = new FileOutputStream(filePath)) {
            properties.store(fileOutputStream, null);
        } catch (IOException e) {
            logger.error("An error occurred while saving .properties file: " + e.getMessage(), e);
        }
    }

    public void fileRemoveOrCreate() throws IOException {
        File file = new File(FILE_PATH);
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
                logger.info("Existing otel.properties file deleted.");
                file.createNewFile();
            } else {
                try {
                    file.createNewFile();
                    logger.info("Success create new otel.properties file");
                } catch (IOException e) {
                    logger.info("Failed to create new otel.properties file.");
                    return;
                }
            }
        }
        logger.info("Created otel.properties file.");
    }
}
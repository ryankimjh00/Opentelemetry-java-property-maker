package com.example.MercenarySys.OpentelemetryRunner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class OtelObjectMaker {
    private static final Logger logger = LoggerFactory.getLogger(OtelObjectMaker.class);

    public void objectMaker() throws IOException {
        logger.info("\n" +
                " _____                      _          _                         _                 ______                                  _          ___  ___        _                \n" +
                "|  _  |                    | |        | |                       | |                | ___ \\                                | |         |  \\/  |       | |               \n" +
                "| | | | _ __    ___  _ __  | |_   ___ | |  ___  _ __ ___    ___ | |_  _ __  _   _  | |_/ / _ __   ___   _ __    ___  _ __ | |_  _   _ | .  . |  __ _ | | __  ___  _ __ \n" +
                "| | | || '_ \\  / _ \\| '_ \\ | __| / _ \\| | / _ \\| '_ ` _ \\  / _ \\| __|| '__|| | | | |  __/ | '__| / _ \\ | '_ \\  / _ \\| '__|| __|| | | || |\\/| | / _` || |/ / / _ \\| '__|\n" +
                "\\ \\_/ /| |_) ||  __/| | | || |_ |  __/| ||  __/| | | | | ||  __/| |_ | |   | |_| | | |    | |   | (_) || |_) ||  __/| |   | |_ | |_| || |  | || (_| ||   < |  __/| |   \n" +
                " \\___/ | .__/  \\___||_| |_| \\__| \\___||_| \\___||_| |_| |_| \\___| \\__||_|    \\__, | \\_|    |_|    \\___/ | .__/  \\___||_|    \\__| \\__, |\\_|  |_/ \\__,_||_|\\_\\ \\___||_|   \n" +
                "       | |                                                                   __/ |                     | |                       __/ |                                 \n" +
                "       |_|                                                                  |___/                      |_|                      |___/                                  \n");
        // 이 어플리케이션이 있는 패키지 경로를 가져옴
        String packageName = OtelApplication.class.getPackage().getName();
        // 패키지 경로에서 .OpentelemetryRunner을 제거함
        String modifiedPackage = packageName.replace(".OpentelemetryRunner", "");
        logger.info("Composing otel.properties file.");
        // otel.properties 파일을 생성하고, 추출된 메소드를 넣음
        OtelPropertiesMaker otelPropertiesMaker = new OtelPropertiesMaker("otlp", "http://host_server_ip:4317");
        OtelMethodExtractor extractor = new OtelMethodExtractor(modifiedPackage);
        otelPropertiesMaker.fileRemoveOrCreate();
        extractor.extractAndWriteMethods();
        otelPropertiesMaker.editTraceMethod("otel.properties", extractor);
        otelPropertiesMaker.createAttributeAndProtocolProperty("developer_name", "service_name", "service_namespace", "service_version");
        logger.info("Composed otel.properties file with no issue.");
        logger.info("\n\nNow finish configuring your application following to manual\n[Opentelemetry Agent Configuration.pdf]\n");
        logger.info("Contact : https://t.me/ryankimjh546");
    }
}

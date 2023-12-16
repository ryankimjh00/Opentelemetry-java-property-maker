package com.example.MercenarySys.OpentelemetryRunner;

import java.io.IOException;

public class OtelApplication {
    public static void main(String[] args) throws IOException {
        OtelObjectMaker otelObjectMaker = new OtelObjectMaker();
        otelObjectMaker.objectMaker();
    }
}

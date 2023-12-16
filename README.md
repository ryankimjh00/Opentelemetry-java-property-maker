# Opentelemetry-java-property-maker

- Run `OpentelemetryRunner` package where your main method place.

- `OpentelemetryRunner` package have to get your package name. So you have to set Opentelemetry-java-property-maker's package correctly.

- `Opentelemetry-java-property-maker` creates a file called `otel.properties`, including the required values for setting the **otel agent**. The main function is to extract data for monitoring **trace data** using `Tempo`, `Zipkin`, or `Jaeger`.
- To get a closer look at the trace data, the otel agent must recognize all classes and methods running in this application. `Opentelemetry-java-property-make`r helps you observe trace data by collecting classes and methods at high speed. **Like below**
 ```
...
otel.instrumentaion.methods.include=com.example.path.change.{ClassName1}[Method1 Method2,Method3];\
com.example.path.change.{ClassName2}[Method1 Method2,Method3];\
com.example.path.change.{ClassName2}[Method1 Method2,Method3];\
...
com.example.path.change.{ClassName10}[Method1 Method2,Method3];\
...
com.example.path.change.{ClassName20}[Method1 Method2,Method3];
```

# In a good condition, you can see this Logs
```
22:57:05.593 [main] INFO com.example.OtelApplication.OpentelemetryRunner.OtelObjectMaker -- 
 _____                      _          _                         _                 ______                                  _          ___  ___        _                
|  _  |                    | |        | |                       | |                | ___ \                                | |         |  \/  |       | |               
| | | | _ __    ___  _ __  | |_   ___ | |  ___  _ __ ___    ___ | |_  _ __  _   _  | |_/ / _ __   ___   _ __    ___  _ __ | |_  _   _ | .  . |  __ _ | | __  ___  _ __ 
| | | || '_ \  / _ \| '_ \ | __| / _ \| | / _ \| '_ ` _ \  / _ \| __|| '__|| | | | |  __/ | '__| / _ \ | '_ \  / _ \| '__|| __|| | | || |\/| | / _` || |/ / / _ \| '__|
\ \_/ /| |_) ||  __/| | | || |_ |  __/| ||  __/| | | | | ||  __/| |_ | |   | |_| | | |    | |   | (_) || |_) ||  __/| |   | |_ | |_| || |  | || (_| ||   < |  __/| |   
 \___/ | .__/  \___||_| |_| \__| \___||_| \___||_| |_| |_| \___| \__||_|    \__, | \_|    |_|    \___/ | .__/  \___||_|    \__| \__, |\_|  |_/ \__,_||_|\_\ \___||_|   
       | |                                                                   __/ |                     | |                       __/ |                                 
       |_|                                                                  |___/                      |_|                      |___/                                  

22:57:05.598 [main] INFO com.example.OtelApplication.OpentelemetryRunner.OtelObjectMaker -- Composing otel.properties file.
22:57:05.606 [main] INFO com.example.OtelApplication.OpentelemetryRunner.OtelPropertiesMaker -- Existing otel.properties file deleted.
22:57:05.612 [main] INFO com.example.OtelApplication.OpentelemetryRunner.OtelPropertiesMaker -- Created otel.properties file.
22:57:05.803 [main] INFO com.example.OtelApplication.OpentelemetryRunner.OtelObjectMaker -- Composed otel.properties file with no issue.
22:57:05.803 [main] INFO com.example.OtelApplication.OpentelemetryRunner.OtelObjectMaker -- Contact : https://t.me/ryankimjh546
```

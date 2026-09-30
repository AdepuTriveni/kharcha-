package com.kharcha.insights;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/** Forecast, risk scoring (ONNX), bandit, triggers (§14, §19, §22.1). */
@SpringBootApplication
public class InsightsApplication {

    public static void main(String[] args) {
        SpringApplication.run(InsightsApplication.class, args);
    }
}

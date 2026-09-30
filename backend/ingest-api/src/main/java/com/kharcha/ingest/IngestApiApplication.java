package com.kharcha.ingest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/** REST API: phone uploads, Ask, bill photos, settings (§9). */
@SpringBootApplication
public class IngestApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(IngestApiApplication.class, args);
    }
}

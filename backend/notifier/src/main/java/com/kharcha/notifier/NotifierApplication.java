package com.kharcha.notifier;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/** Policy gate, Telegram, FCM, feedback capture (§22.2, §25). */
@SpringBootApplication
public class NotifierApplication {

    public static void main(String[] args) {
        SpringApplication.run(NotifierApplication.class, args);
    }
}

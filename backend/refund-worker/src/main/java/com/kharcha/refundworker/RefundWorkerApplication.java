package com.kharcha.refundworker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/** Temporal refund workflows (§23). */
@SpringBootApplication
public class RefundWorkerApplication {

    public static void main(String[] args) {
        SpringApplication.run(RefundWorkerApplication.class, args);
    }
}

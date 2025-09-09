package io.github.g4lowy.phishingguard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PhishingGuardApplication {

    public static void main(String[] args) {
        SpringApplication.run(PhishingGuardApplication.class, args);
    }

    //todo: dodać komunikację między modułami + testy
}

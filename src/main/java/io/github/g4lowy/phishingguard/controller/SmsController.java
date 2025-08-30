package io.github.g4lowy.phishingguard.controller;


import io.github.g4lowy.phishingguard.domain.Decision;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/sms")
public class SmsController {


    @PostMapping
    private ResponseEntity<Mono<Decision>> handleSms(@RequestBody Decision decision) {


        return null;
    }
}

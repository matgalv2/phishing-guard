package io.github.g4lowy.phishingguard.common.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class PhoneNumberValidator implements ConstraintValidator<PhoneNumber, String> {

    private final String pattern;

    private PhoneNumberValidator(@Value("${app.validation.phone.pattern}") String pattern){

        this.pattern = pattern;
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        return Optional.of(value)
                .map(phoneNumber -> phoneNumber.matches(pattern))
                .orElse(false);
    }
}
package io.github.g4lowy.phishingguard.common.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PhoneNumberValidator.class)
@Documented
public @interface PhoneNumber {

    String message() default "{validation.phone.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

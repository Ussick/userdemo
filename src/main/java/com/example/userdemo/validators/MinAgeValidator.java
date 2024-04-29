package com.example.userdemo.validators;

import com.example.userdemo.annotations.MinAge;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDate;


@Component
public class MinAgeValidator implements ConstraintValidator<MinAge, LocalDate> {

    @Value("${minAge}")
    private long minAge;

    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
        LocalDate checkDate = value.plusYears(minAge);
        LocalDate now = LocalDate.now();
        return now.isAfter(checkDate)||now.isEqual(checkDate);
    }
}

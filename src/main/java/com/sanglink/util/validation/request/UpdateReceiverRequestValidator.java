package com.sanglink.util.validation.request;

import com.sanglink.dto.request.UpdateReceiverRequest;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

public class UpdateReceiverRequestValidator {
    public static List<String> validate(UpdateReceiverRequest req){
        List<String> errors = new ArrayList<>(CreateUserRequestValidator.validate(req));
        if (req.id() == null || req.id() <= 0) errors.add("Valid donor ID is required for update");

        if (req.birthday() != null) {
            if (req.birthday().isAfter(LocalDate.now())) errors.add("Birthday cannot be in the future");
            int age = Period.between(req.birthday(), LocalDate.now()).getYears();
            if (age <= 0 || age > 120) errors.add("Invalid age");
        }

        if (req.need() == null) errors.add("Need is required");

        return errors;
    }
}

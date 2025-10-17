package com.sanglink.util.validation.request;

import com.sanglink.dto.request.CreateDonorRequest;
import com.sanglink.dto.request.contract.CreateUserRequest;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CreateUserRequestValidator {
    public static List<String> validate(CreateUserRequest req) {
        List<String> errors = new ArrayList<>();
        if (req == null) {
            errors.add("Request is required");
            return errors;
        }
        if (Optional.ofNullable(req.cin()).orElse("").isBlank()) errors.add("CIN is required");
        if (Optional.ofNullable(req.nom()).orElse("").isBlank()) errors.add("Nom is required");
        if (Optional.ofNullable(req.prenom()).orElse("").isBlank()) errors.add("Prenom is required");
        if (Optional.ofNullable(req.phone()).orElse("").isBlank()) errors.add("Phone is required");
        if (req.birthday() == null) errors.add("Birthday is required");
        if (req.gender() == null) errors.add("Gender is required");
        if (req.bloodGroup() == null) errors.add("Blood group is required");

        return errors;
    }
}

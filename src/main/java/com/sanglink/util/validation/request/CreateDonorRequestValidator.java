package com.sanglink.util.validation.request;

import com.sanglink.dto.request.CreateDonorRequest;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CreateDonorRequestValidator {
    public static List<String> validate(CreateDonorRequest req) {
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
        if (req.weight() <= 0) errors.add("Weight must be positive");
        if (req.birthday() != null) {
            if (req.birthday().isAfter(LocalDate.now())) errors.add("Birthday cannot be in future");
            int age = Period.between(req.birthday(), LocalDate.now()).getYears();
            if (age < 18 || age > 65) errors.add("Age must be between 18 and 65");
        }
        if (req.weight() > 0 && req.weight() < 50) errors.add("Weight must be at least 50 kg");
        return errors;
    }
}

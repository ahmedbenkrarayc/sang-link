package com.sanglink.util.validation.request;

import com.sanglink.dto.request.CreateDonationRequest;

import java.util.ArrayList;
import java.util.List;

public class CreateDonationRequestValidator {
    public static List<String> validate(CreateDonationRequest req) {
        List<String> errors = new ArrayList<>();

        if (req == null) {
            errors.add("Request cannot be null");
            return errors;
        }

        if (req.donorId() == null || req.donorId() <= 0)
            errors.add("Donor ID is required and must be positive");

        if (req.receiverId() == null || req.receiverId() <= 0)
            errors.add("Receiver ID is required and must be positive");

        if (req.volume() <= 0)
            errors.add("Volume must be greater than zero");

        return errors;
    }
}

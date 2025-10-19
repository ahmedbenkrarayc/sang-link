package com.sanglink.dto.request;

import com.sanglink.dto.request.contract.CreateUserRequest;
import com.sanglink.entity.enums.BloodGroup;
import com.sanglink.entity.enums.Gender;

import java.time.LocalDate;

public record UpdateDonorRequest(
        Long id,
        String cin,
        String nom,
        String prenom,
        String phone,
        LocalDate birthday,
        Gender gender,
        BloodGroup bloodGroup,
        double weight
) implements CreateUserRequest{
}

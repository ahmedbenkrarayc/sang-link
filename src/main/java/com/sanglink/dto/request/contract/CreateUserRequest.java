package com.sanglink.dto.request.contract;

import com.sanglink.entity.enums.BloodGroup;
import com.sanglink.entity.enums.Gender;

import java.time.LocalDate;

public interface CreateUserRequest {
    String cin();
    String nom();
    String prenom();
    String phone();
    LocalDate birthday();
    Gender gender();
    BloodGroup bloodGroup();
}

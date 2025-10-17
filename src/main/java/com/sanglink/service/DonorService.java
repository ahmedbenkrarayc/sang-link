package com.sanglink.service;

import com.sanglink.dto.request.CreateDonorRequest;
import com.sanglink.entity.Donor;

import java.util.List;
import java.util.Optional;

public interface DonorService {
    List<String> createDonor(CreateDonorRequest req);
}

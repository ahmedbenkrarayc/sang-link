package com.sanglink.service;

import com.sanglink.dto.request.CreateDonorRequest;
import com.sanglink.dto.request.UpdateDonorRequest;
import com.sanglink.entity.Donor;
import com.sanglink.entity.enums.DonorStatus;

import java.util.List;
import java.util.Optional;

public interface DonorService {
    List<String> createDonor(CreateDonorRequest req);
    List<Donor> getDonors(int page, int pageSize, String search, DonorStatus status);
    long countDonors(String search, DonorStatus status);
    List<String> deleteDonor(Long id);
    List<Donor> getAvailableCompatibleDonors(Long receiverId);
    Optional<Donor> getDonorById(Long id);
    List<String> updateDonor(UpdateDonorRequest req);
}

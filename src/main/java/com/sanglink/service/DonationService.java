package com.sanglink.service;

import com.sanglink.dto.request.CreateDonationRequest;
import com.sanglink.entity.Donation;
import com.sanglink.entity.enums.BloodGroup;

public interface DonationService {
    Donation linkReceiverToDonor(CreateDonationRequest request);
}

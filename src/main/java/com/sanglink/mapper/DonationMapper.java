package com.sanglink.mapper;

import com.sanglink.dto.request.CreateDonationRequest;
import com.sanglink.entity.Donation;
import com.sanglink.entity.Donor;
import com.sanglink.entity.Receiver;
import com.sanglink.entity.enums.DonationStatus;

import java.time.LocalDate;

public class DonationMapper {
    public static Donation toEntity(CreateDonationRequest req, Donor donor, Receiver receiver) {
        Donation donation = new Donation();
        donation.setDateDon(LocalDate.now());
        donation.setVolume(req.volume());
        donation.setStatus(DonationStatus.PENDING);
        donation.setDonor(donor);
        donation.setReceiver(receiver);
        return donation;
    }
}

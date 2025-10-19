package com.sanglink.repository.impl;

import com.sanglink.dao.DonationDAO;
import com.sanglink.entity.Donation;
import com.sanglink.repository.DonationRepository;

public class DonationRepositoryImpl implements DonationRepository {
    private final DonationDAO donationDAO;

    public DonationRepositoryImpl(DonationDAO donationDAO) {
        this.donationDAO = donationDAO;
    }

    @Override
    public void save(Donation donation) {
        donationDAO.save(donation);
    }
}

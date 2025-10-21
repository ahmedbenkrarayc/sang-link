package com.sanglink.repository.impl;

import com.sanglink.dao.DonorDAO;
import com.sanglink.entity.Donor;
import com.sanglink.entity.enums.DonorStatus;
import com.sanglink.repository.DonorRepository;

import java.util.List;
import java.util.Optional;

public class DonorRepositoryImpl implements DonorRepository {
    private final DonorDAO donorDAO;

    public DonorRepositoryImpl(DonorDAO donorDAO) { this.donorDAO = donorDAO; }

    @Override
    public Donor save(Donor donor) { return donorDAO.save(donor); }

    @Override
    public Optional<Donor> findByCin(String cin) { return donorDAO.findByCin(cin); }

    @Override
    public Optional<Donor> findById(Long id) { return donorDAO.findById(id); }

    @Override
    public List<Donor> findAll(int page, int pageSize, String search, DonorStatus status) {
        return donorDAO.findAll(page, pageSize, search, status);
    }

    @Override
    public long countAll(String search, DonorStatus status) {
        return donorDAO.countAll(search, status);
    }

    @Override
    public List<Donor> findAvailableCompatibleDonors(Long receiverId) {
        return donorDAO.findAvailableCompatibleDonors(receiverId);
    }

    @Override
    public List<Donor> findAll(){
        return donorDAO.findAll();
    }
}
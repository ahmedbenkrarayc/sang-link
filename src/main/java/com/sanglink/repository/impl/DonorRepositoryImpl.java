package com.sanglink.repository.impl;

import com.sanglink.dao.DonorDAO;
import com.sanglink.entity.Donor;
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
    public List<Donor> findAll() { return donorDAO.findAll(); }
}
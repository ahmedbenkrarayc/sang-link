package com.sanglink.repository;

import com.sanglink.entity.Donor;

import java.util.List;
import java.util.Optional;

public interface DonorRepository {
    Donor save(Donor donor);
    Optional<Donor> findByCin(String cin);
    Optional<Donor> findById(Long id);
    List<Donor> findAll();
}

package com.sanglink.dao;

import com.sanglink.entity.Donor;
import com.sanglink.entity.MedicalAssessment;

import java.util.List;
import java.util.Optional;

public interface DonorDAO {
    Donor save(Donor donor);
    Optional<Donor> findById(Long id);
    Optional<Donor> findByCin(String cin);
    List<Donor> findAll();
}

package com.sanglink.repository.impl;

import com.sanglink.dao.MedicalAssessmentDAO;
import com.sanglink.entity.MedicalAssessment;
import com.sanglink.repository.MedicalAssessmentRepository;

import java.util.Optional;

public class MedicalAssessmentRepositoryImpl implements MedicalAssessmentRepository {
    private final MedicalAssessmentDAO dao;

    public MedicalAssessmentRepositoryImpl(MedicalAssessmentDAO dao) { this.dao = dao; }

    @Override
    public MedicalAssessment save(MedicalAssessment assessment) { return dao.save(assessment); }

    @Override
    public Optional<MedicalAssessment> findLatestByDonorId(Long donorId) { return dao.findLatestByDonorId(donorId); }
}

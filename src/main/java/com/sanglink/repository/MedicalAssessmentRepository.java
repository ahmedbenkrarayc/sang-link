package com.sanglink.repository;

import com.sanglink.entity.MedicalAssessment;

import java.util.Optional;

public interface MedicalAssessmentRepository {
    MedicalAssessment save(MedicalAssessment assessment);
    Optional<MedicalAssessment> findLatestByDonorId(Long donorId);
}

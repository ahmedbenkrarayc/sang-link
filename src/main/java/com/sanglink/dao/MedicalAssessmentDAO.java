package com.sanglink.dao;

import com.sanglink.entity.MedicalAssessment;

import java.util.Optional;

public interface MedicalAssessmentDAO {
    MedicalAssessment save(MedicalAssessment assessment);
    Optional<MedicalAssessment> findLatestByDonorId(Long donorId);
}

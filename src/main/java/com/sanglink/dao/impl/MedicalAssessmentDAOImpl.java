package com.sanglink.dao.impl;

import com.sanglink.dao.MedicalAssessmentDAO;
import com.sanglink.entity.MedicalAssessment;
import jakarta.persistence.EntityManager;

import java.util.Optional;

public class MedicalAssessmentDAOImpl implements MedicalAssessmentDAO {
    private final EntityManager em;

    public MedicalAssessmentDAOImpl(EntityManager em) { this.em = em; }

    @Override
    public MedicalAssessment save(MedicalAssessment assessment) {
        var tx = em.getTransaction();
        try {
            tx.begin();
            if (assessment.getId() == null) {
                em.persist(assessment);
            } else {
                assessment = em.merge(assessment);
            }
            tx.commit();
            return assessment;
        } catch (RuntimeException ex) {
            if (tx.isActive()) tx.rollback();
            throw ex;
        }
    }

    @Override
    public Optional<MedicalAssessment> findLatestByDonorId(Long donorId) {
        return em.createQuery(
                        "SELECT m FROM MedicalAssessment m WHERE m.donor.id = :did ORDER BY m.assessmentDate DESC",
                        MedicalAssessment.class)
                .setParameter("did", donorId)
                .setMaxResults(1)
                .getResultStream()
                .findFirst();
    }
}

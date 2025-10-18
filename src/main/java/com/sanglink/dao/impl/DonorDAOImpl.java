package com.sanglink.dao.impl;

import com.sanglink.dao.DonorDAO;
import com.sanglink.entity.Donor;
import com.sanglink.entity.MedicalAssessment;
import com.sanglink.entity.enums.DonorStatus;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;

import java.util.List;
import java.util.Optional;

public class DonorDAOImpl implements DonorDAO {
    private final EntityManager em;

    public DonorDAOImpl(EntityManager em) { this.em = em; }

    @Override
    public Donor save(Donor donor) {
        var tx = em.getTransaction();
        try {
            tx.begin();
            if (donor.getId() == null) {
                em.persist(donor);
            } else {
                donor = em.merge(donor);
            }
            tx.commit();
            return donor;
        } catch (RuntimeException ex) {
            if (tx.isActive()) tx.rollback();
            throw ex;
        }
    }

    @Override
    public Optional<Donor> findById(Long id) {
        return Optional.ofNullable(em.find(Donor.class, id));
    }

    @Override
    public Optional<Donor> findByCin(String cin) {
        List<Donor> result = em.createQuery("SELECT d FROM Donor d WHERE d.cin = :cin", Donor.class)
                .setParameter("cin", cin)
                .getResultList();
        return result.stream().findFirst();
    }

    @Override
    public List<Donor> findAll(int page, int pageSize, String search, DonorStatus status) {
        StringBuilder jpql = new StringBuilder("""
            SELECT DISTINCT d 
            FROM Donor d
            JOIN FETCH d.medicalAssessments a
            WHERE a.id = (
                SELECT MAX(a2.id)
                FROM MedicalAssessment a2
                WHERE a2.donor = d
            )
        """);

        if (search != null && !search.isBlank()) {
            jpql.append(" AND (LOWER(d.cin) LIKE LOWER(:search) OR LOWER(d.nom) LIKE LOWER(:search) OR LOWER(d.prenom) LIKE LOWER(:search))");
        }

        if (status != null) {
            jpql.append(" AND d.status = :status");
        }

        jpql.append(" ORDER BY d.id DESC");

        TypedQuery<Donor> query = em.createQuery(jpql.toString(), Donor.class);

        if (search != null && !search.isBlank()) {
            query.setParameter("search", "%" + search + "%");
        }

        if (status != null) {
            query.setParameter("status", status);
        }

        query.setFirstResult((page - 1) * pageSize);
        query.setMaxResults(pageSize);

        return query.getResultList();
    }

    @Override
    public long countAll(String search, DonorStatus status) {
        StringBuilder jpql = new StringBuilder("""
            SELECT COUNT(DISTINCT d) 
            FROM Donor d 
            JOIN d.medicalAssessments a
            WHERE a.id = (
                SELECT MAX(a2.id)
                FROM MedicalAssessment a2
                WHERE a2.donor = d
            )
        """);

        if (search != null && !search.isBlank()) {
            jpql.append(" AND (LOWER(d.cin) LIKE LOWER(:search) OR LOWER(d.nom) LIKE LOWER(:search) OR LOWER(d.prenom) LIKE LOWER(:search))");
        }

        if (status != null) {
            jpql.append(" AND d.status = :status");
        }

        var query = em.createQuery(jpql.toString(), Long.class);

        if (search != null && !search.isBlank()) {
            query.setParameter("search", "%" + search + "%");
        }

        if (status != null) {
            query.setParameter("status", status);
        }

        return query.getSingleResult();
    }
}

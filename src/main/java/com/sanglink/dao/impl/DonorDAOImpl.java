package com.sanglink.dao.impl;

import com.sanglink.dao.DonorDAO;
import com.sanglink.entity.Donor;
import com.sanglink.entity.MedicalAssessment;
import com.sanglink.entity.Receiver;
import com.sanglink.entity.enums.BloodGroup;
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

    @Override
    public List<Donor> findAvailableCompatibleDonors(Long receiverId) {
        Receiver receiver = em.find(Receiver.class, receiverId);
        if (receiver == null) return List.of();

        List<BloodGroup> compatibleGroups = getCompatibleBloodGroups(receiver.getBloodGroup());

        String jpql = """
            SELECT d FROM Donor d
            WHERE d.status = :status
              AND d.bloodGroup IN :compatibleGroups
        """;

        TypedQuery<Donor> query = em.createQuery(jpql, Donor.class);
        query.setParameter("status", DonorStatus.DISPONIBLE);
        query.setParameter("compatibleGroups", compatibleGroups);

        return query.getResultList();
    }

    private List<BloodGroup> getCompatibleBloodGroups(BloodGroup receiverGroup) {
        return switch (receiverGroup) {
            case A_POSITIVE -> List.of(
                    BloodGroup.A_POSITIVE, BloodGroup.A_NEGATIVE,
                    BloodGroup.O_POSITIVE, BloodGroup.O_NEGATIVE);
            case A_NEGATIVE -> List.of(BloodGroup.A_NEGATIVE, BloodGroup.O_NEGATIVE);
            case B_POSITIVE -> List.of(
                    BloodGroup.B_POSITIVE, BloodGroup.B_NEGATIVE,
                    BloodGroup.O_POSITIVE, BloodGroup.O_NEGATIVE);
            case B_NEGATIVE -> List.of(BloodGroup.B_NEGATIVE, BloodGroup.O_NEGATIVE);
            case AB_POSITIVE -> List.of(
                    BloodGroup.A_POSITIVE, BloodGroup.A_NEGATIVE,
                    BloodGroup.B_POSITIVE, BloodGroup.B_NEGATIVE,
                    BloodGroup.AB_POSITIVE, BloodGroup.AB_NEGATIVE,
                    BloodGroup.O_POSITIVE, BloodGroup.O_NEGATIVE);
            case AB_NEGATIVE -> List.of(
                    BloodGroup.A_NEGATIVE, BloodGroup.B_NEGATIVE,
                    BloodGroup.AB_NEGATIVE, BloodGroup.O_NEGATIVE);
            case O_POSITIVE -> List.of(BloodGroup.O_POSITIVE, BloodGroup.O_NEGATIVE);
            case O_NEGATIVE -> List.of(BloodGroup.O_NEGATIVE);
        };
    }
}

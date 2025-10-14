package com.sanglink.dao.impl;

import com.sanglink.dao.DonorDAO;
import com.sanglink.entity.Donor;
import com.sanglink.entity.MedicalAssessment;
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
    public List<Donor> findAll() {
        return em.createQuery("SELECT d FROM Donor d", Donor.class).getResultList();
    }
}

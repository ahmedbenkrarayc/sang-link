package com.sanglink.dao.impl;

import com.sanglink.dao.DonationDAO;
import com.sanglink.entity.Donation;
import jakarta.persistence.EntityManager;

public class DonationDAOImpl implements DonationDAO {
    private final EntityManager em;

    public DonationDAOImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public void save(Donation donation) {
        em.getTransaction().begin();
        if (donation.getId() == null) {
            em.persist(donation);
        } else {
            em.merge(donation);
        }
        em.getTransaction().commit();
    }

}

package com.sanglink.dao.impl;

import com.sanglink.dao.ReceiverDAO;
import com.sanglink.entity.Donor;
import com.sanglink.entity.Receiver;
import com.sanglink.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;

import java.util.List;
import java.util.Optional;

public class ReceiverDAOImpl implements ReceiverDAO {
    private final EntityManager em;

    public ReceiverDAOImpl(EntityManager em) { this.em = em; }

    @Override
    public void save(Receiver receiver) {
        var tx = em.getTransaction();
        try {
            tx.begin();
            if (receiver.getId() == null) {
                em.persist(receiver);
            } else {
                receiver = em.merge(receiver);
            }
            tx.commit();
        } catch (RuntimeException ex) {
            if (tx.isActive()) tx.rollback();
            throw ex;
        }
    }

    @Override
    public Optional<Receiver> findById(Long id) {
        return Optional.ofNullable(em.find(Receiver.class, id));
    }

    @Override
    public List<Receiver> findAll() {
        return em.createQuery("SELECT r FROM Receiver r ORDER BY r.need DESC", Receiver.class)
                .getResultList();
    }
}

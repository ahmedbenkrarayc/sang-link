package com.sanglink.dao.impl;

import com.sanglink.dao.ReceiverDAO;
import com.sanglink.entity.Donor;
import com.sanglink.entity.Receiver;
import com.sanglink.entity.User;
import com.sanglink.entity.enums.Need;
import com.sanglink.entity.enums.ReceiverStatus;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;

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
    public List<Receiver> findAll(int page, int pageSize, String search, Need need) {
        StringBuilder jpql = new StringBuilder("""
            SELECT r FROM Receiver r
            LEFT JOIN FETCH r.donations d
            WHERE r.status = :waiting
        """);

        if (search != null && !search.isBlank()) {
            jpql.append(" AND (LOWER(r.cin) LIKE LOWER(:search) OR LOWER(r.nom) LIKE LOWER(:search) OR LOWER(r.prenom) LIKE LOWER(:search))");
        }

        if (need != null) {
            jpql.append(" AND r.need = :need");
        }

        //CRITICAL -> URGENT -> NORMAL
        jpql.append(" ORDER BY CASE r.need WHEN :critical THEN 1 WHEN :urgent THEN 2 WHEN :normal THEN 3 ELSE 4 END");

        TypedQuery<Receiver> query = em.createQuery(jpql.toString(), Receiver.class);

        query.setParameter("waiting", ReceiverStatus.WAITING);

        if (search != null && !search.isBlank()) {
            query.setParameter("search", "%" + search + "%");
        }
        if (need != null) {
            query.setParameter("need", need);
        }

        query.setParameter("critical", Need.CRITICAL);
        query.setParameter("urgent", Need.URGENT);
        query.setParameter("normal", Need.NORMAL);

        query.setFirstResult((page - 1) * pageSize);
        query.setMaxResults(pageSize);

        return query.getResultList();
    }

    @Override
    public long countAll(String search, Need needFilter) {
        StringBuilder jpql = new StringBuilder("SELECT COUNT(r) FROM Receiver r WHERE 1=1");

        if (search != null && !search.isBlank()) {
            jpql.append(" AND (LOWER(r.cin) LIKE LOWER(:search) OR LOWER(r.nom) LIKE LOWER(:search) OR LOWER(r.prenom) LIKE LOWER(:search))");
        }

        if (needFilter != null) {
            jpql.append(" AND r.need = :need");
        }

        TypedQuery<Long> query = em.createQuery(jpql.toString(), Long.class);

        if (search != null && !search.isBlank()) {
            query.setParameter("search", "%" + search + "%");
        }
        if (needFilter != null) {
            query.setParameter("need", needFilter);
        }

        return query.getSingleResult();
    }
}

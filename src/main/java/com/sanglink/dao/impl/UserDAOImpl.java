package com.sanglink.dao.impl;

import com.sanglink.dao.UserDAO;
import com.sanglink.entity.Donor;
import com.sanglink.entity.User;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Optional;

public class UserDAOImpl implements UserDAO {
    private final EntityManager em;

    public UserDAOImpl(EntityManager em) { this.em = em; }

    @Override
    public Optional<User> findByCin(String cin) {
        List<User> result = em.createQuery("SELECT u FROM User u WHERE u.cin = :cin", User.class)
                .setParameter("cin", cin)
                .getResultList();
        return result.stream().findFirst();
    }

    @Override
    public boolean deleteById(Long id) {
        var tx = em.getTransaction();
        try {
            tx.begin();
            User user = em.find(User.class, id);
            if (user != null) {
                em.remove(user);
                tx.commit();
                return true;
            }
            tx.rollback();
            return false;
        } catch (RuntimeException ex) {
            if (tx.isActive()) tx.rollback();
            throw ex;
        }
    }
}

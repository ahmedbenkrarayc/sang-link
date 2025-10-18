package com.sanglink.repository.impl;

import com.sanglink.dao.DonorDAO;
import com.sanglink.dao.UserDAO;
import com.sanglink.entity.User;
import com.sanglink.repository.UserRepository;

import java.util.Optional;

public class UserRepositoryImpl implements UserRepository {
    private final UserDAO userDAO;

    public UserRepositoryImpl(UserDAO userDAO) { this.userDAO = userDAO; }

    @Override
    public Optional<User> findByCin(String cin) {
        return userDAO.findByCin(cin);
    }

    @Override
    public boolean deleteById(Long id) {
        return userDAO.deleteById(id);
    }
}

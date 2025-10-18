package com.sanglink.dao;

import com.sanglink.entity.Donor;
import com.sanglink.entity.User;

import java.util.Optional;

public interface UserDAO {
    Optional<User> findByCin(String cin);
    boolean deleteById(Long id);
}

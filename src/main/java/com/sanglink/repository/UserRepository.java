package com.sanglink.repository;

import com.sanglink.entity.User;

import java.util.Optional;

public interface UserRepository {
    Optional<User> findByCin(String cin);
    boolean deleteById(Long id);
}

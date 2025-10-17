package com.sanglink.repository;

import com.sanglink.entity.Receiver;
import com.sanglink.entity.User;

import java.util.List;
import java.util.Optional;

public interface ReceiverRepository {
    void save(Receiver receiver);
    Optional<Receiver> findById(Long id);
    List<Receiver> findAll();
}

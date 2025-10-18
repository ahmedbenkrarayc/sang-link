package com.sanglink.repository.impl;

import com.sanglink.dao.ReceiverDAO;
import com.sanglink.entity.Receiver;
import com.sanglink.entity.User;
import com.sanglink.entity.enums.Need;
import com.sanglink.repository.ReceiverRepository;

import java.util.List;
import java.util.Optional;

public class ReceiverRepositoryImpl implements ReceiverRepository {
    private final ReceiverDAO dao;

    public ReceiverRepositoryImpl(ReceiverDAO dao) { this.dao = dao; }

    @Override
    public void save(Receiver receiver) { dao.save(receiver); }

    @Override
    public Optional<Receiver> findById(Long id) { return dao.findById(id); }

    @Override
    public List<Receiver> findAll(int page, int pageSize, String search, Need needFilter) {
        return dao.findAll(page, pageSize, search, needFilter);
    }

    @Override
    public long countAll(String search, Need needFilter) {
        return dao.countAll(search, needFilter);
    }
}

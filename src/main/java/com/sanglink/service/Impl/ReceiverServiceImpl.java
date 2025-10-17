package com.sanglink.service.Impl;

import com.sanglink.dto.request.CreateReceiverRequest;
import com.sanglink.entity.Donor;
import com.sanglink.entity.Receiver;
import com.sanglink.entity.User;
import com.sanglink.entity.enums.ReceiverStatus;
import com.sanglink.mapper.ReceiverMapper;
import com.sanglink.repository.ReceiverRepository;
import com.sanglink.repository.UserRepository;
import com.sanglink.service.ReceiverService;
import com.sanglink.util.validation.request.CreateReceiverRequestValidator;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ReceiverServiceImpl implements ReceiverService {
    private final UserRepository userRepository;
    private final ReceiverRepository receiverRepository;

    public ReceiverServiceImpl(UserRepository userRepository, ReceiverRepository receiverRepository) {
        this.userRepository = userRepository;
        this.receiverRepository = receiverRepository;
    }

    @Override
    public List<String> createReceiver(CreateReceiverRequest request) {
        List<String> errors = new ArrayList<>();

        //basic request validation
        List<String> validationErrors = CreateReceiverRequestValidator.validate(request);
        if (!validationErrors.isEmpty()) {
            errors.addAll(validationErrors);
            return errors;
        }

        //check duplicate CIN
        Optional<User> existing = userRepository.findByCin(request.cin());
        if (existing.isPresent()) {
            errors.add("A receiver with CIN '" + request.cin() + "' already exists.");
            return errors;
        }

        Receiver receiver = ReceiverMapper.toEntity(request);
        receiver.setStatus(ReceiverStatus.WAITING);

        receiverRepository.save(receiver);

        return List.of();
    }
}

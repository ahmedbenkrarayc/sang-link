package com.sanglink.service.Impl;

import com.sanglink.dto.request.CreateReceiverRequest;
import com.sanglink.dto.request.UpdateReceiverRequest;
import com.sanglink.entity.Donor;
import com.sanglink.entity.Receiver;
import com.sanglink.entity.User;
import com.sanglink.entity.enums.Need;
import com.sanglink.entity.enums.ReceiverStatus;
import com.sanglink.mapper.ReceiverMapper;
import com.sanglink.repository.ReceiverRepository;
import com.sanglink.repository.UserRepository;
import com.sanglink.service.ReceiverService;
import com.sanglink.util.validation.request.CreateReceiverRequestValidator;
import com.sanglink.util.validation.request.UpdateReceiverRequestValidator;

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

    @Override
    public List<Receiver> getReceivers(int page, int pageSize, String search, Need need) {
        return receiverRepository.findAll(page, pageSize, search, need);
    }

    @Override
    public long countReceivers(String search, Need need) {
        return receiverRepository.countAll(search, need);
    }

    @Override
    public List<String> deleteReceiver(Long id) {
        List<String> errors = new ArrayList<>();

        var receiver = receiverRepository.findById(id);
        if (receiver == null) {
            errors.add("Receiver not found");
            return errors;
        }

        boolean deleted = userRepository.deleteById(id);
        if (!deleted) {
            errors.add("Failed to delete receiver. Please try again.");
        }

        return errors;
    }

    @Override
    public Optional<Receiver> getReceiverById(Long id) {
        return receiverRepository.findById(id);
    }

    @Override
    public List<String> updateReceiver(UpdateReceiverRequest req) {
        List<String> errors = new ArrayList<>();

        errors.addAll(UpdateReceiverRequestValidator.validate(req));
        if (!errors.isEmpty()) return errors;

        Optional<Receiver> optionalReceiver = receiverRepository.findById(req.id());
        if (optionalReceiver.isEmpty()) {
            errors.add("Receiver not found with ID: " + req.id());
            return errors;
        }

        Receiver receiver = optionalReceiver.get();

        receiver.setCin(req.cin());
        receiver.setNom(req.nom());
        receiver.setPrenom(req.prenom());
        receiver.setPhone(req.phone());
        receiver.setBirthday(req.birthday());
        receiver.setGender(req.gender());
        receiver.setBloodGroup(req.bloodGroup());

        receiverRepository.save(receiver);

        return List.of();
    }

}

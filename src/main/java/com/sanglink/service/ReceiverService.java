package com.sanglink.service;

import com.sanglink.dto.request.CreateReceiverRequest;
import com.sanglink.entity.Donor;
import com.sanglink.entity.Receiver;
import com.sanglink.entity.enums.Need;

import java.util.List;
import java.util.Optional;

public interface ReceiverService {
    List<String> createReceiver(CreateReceiverRequest req);
    List<Receiver> getReceivers(int page, int pageSize, String search, Need need);
    long countReceivers(String search, Need need);
    List<String> deleteReceiver(Long id);
}

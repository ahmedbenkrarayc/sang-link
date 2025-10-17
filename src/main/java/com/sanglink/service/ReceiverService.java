package com.sanglink.service;

import com.sanglink.dto.request.CreateReceiverRequest;
import com.sanglink.entity.Donor;
import com.sanglink.entity.Receiver;

import java.util.List;
import java.util.Optional;

public interface ReceiverService {
    List<String> createReceiver(CreateReceiverRequest req);
}

package com.sanglink.mapper;

import com.sanglink.dto.request.CreateReceiverRequest;
import com.sanglink.entity.Receiver;
import com.sanglink.entity.enums.Need;
import com.sanglink.entity.enums.ReceiverStatus;

public class ReceiverMapper {
    public static Receiver toEntity(CreateReceiverRequest req) {
        Receiver receiver = new Receiver();
        receiver.setCin(req.cin());
        receiver.setNom(req.nom());
        receiver.setPrenom(req.prenom());
        receiver.setPhone(req.phone());
        receiver.setBirthday(req.birthday());
        receiver.setGender(req.gender());
        receiver.setBloodGroup(req.bloodGroup());
        receiver.setNeed(req.need());
        receiver.setStatus(ReceiverStatus.WAITING);
        return receiver;
    }
}

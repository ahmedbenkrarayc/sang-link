package com.sanglink.service.Impl;

import com.sanglink.dto.request.CreateDonationRequest;
import com.sanglink.entity.Donation;
import com.sanglink.entity.Donor;
import com.sanglink.entity.Receiver;
import com.sanglink.entity.enums.BloodGroup;
import com.sanglink.entity.enums.DonorStatus;
import com.sanglink.entity.enums.Need;
import com.sanglink.entity.enums.ReceiverStatus;
import com.sanglink.mapper.DonationMapper;
import com.sanglink.repository.DonationRepository;
import com.sanglink.repository.DonorRepository;
import com.sanglink.repository.ReceiverRepository;
import com.sanglink.service.DonationService;
import jakarta.persistence.EntityNotFoundException;

import java.util.Optional;

public class DonationServiceImpl implements DonationService {
    private final DonationRepository donationRepository;
    private final DonorRepository donorRepository;
    private final ReceiverRepository receiverRepository;

    public DonationServiceImpl(DonationRepository donationRepository, DonorRepository donorRepository, ReceiverRepository receiverRepository) {
        this.donationRepository = donationRepository;
        this.donorRepository = donorRepository;
        this.receiverRepository = receiverRepository;
    }

    @Override
    public Donation linkReceiverToDonor(CreateDonationRequest request) {
        Optional<Donor> donorOpt = donorRepository.findById(request.donorId());
        Optional<Receiver> receiverOpt = receiverRepository.findById(request.receiverId());

        Donor donor = donorOpt.orElseThrow(() ->
                new EntityNotFoundException("Donor not found with ID: " + request.donorId()));
        Receiver receiver = receiverOpt.orElseThrow(() ->
                new EntityNotFoundException("Receiver not found with ID: " + request.receiverId()));

        if (donor.getStatus() != DonorStatus.DISPONIBLE) {
            throw new IllegalStateException("Selected donor is not available for donation.");
        }

        if (!isBloodCompatible(donor.getBloodGroup(), receiver.getBloodGroup())) {
            throw new IllegalArgumentException("Incompatible blood groups between donor and receiver.");
        }

        Donation donation = DonationMapper.toEntity(request, donor, receiver);
        donationRepository.save(donation);

        donor.setStatus(DonorStatus.NOT_AVAILABLE);
        donorRepository.save(donor);

        long totalDonations = receiver.getDonations() == null ? 0 : receiver.getDonations().size() + 1;
        int required = getRequiredDonationCount(receiver.getNeed());

        if (totalDonations >= required) {
            receiver.setStatus(ReceiverStatus.SATISFIED);
        } else {
            receiver.setStatus(ReceiverStatus.WAITING);
        }

        receiverRepository.save(receiver);
        return donation;
    }

    private int getRequiredDonationCount(Need need) {
        return switch (need) {
            case CRITICAL -> 4;
            case URGENT -> 3;
            case NORMAL -> 1;
        };
    }

    private boolean isBloodCompatible(BloodGroup donor, BloodGroup receiver) {
        return switch (receiver) {
            case O_NEGATIVE -> donor == BloodGroup.O_NEGATIVE;
            case O_POSITIVE -> donor == BloodGroup.O_NEGATIVE || donor == BloodGroup.O_POSITIVE;
            case A_NEGATIVE -> donor == BloodGroup.O_NEGATIVE || donor == BloodGroup.A_NEGATIVE;
            case A_POSITIVE -> donor == BloodGroup.O_NEGATIVE || donor == BloodGroup.O_POSITIVE || donor == BloodGroup.A_NEGATIVE || donor == BloodGroup.A_POSITIVE;
            case B_NEGATIVE -> donor == BloodGroup.O_NEGATIVE || donor == BloodGroup.B_NEGATIVE;
            case B_POSITIVE -> donor == BloodGroup.O_NEGATIVE || donor == BloodGroup.O_POSITIVE || donor == BloodGroup.B_NEGATIVE || donor == BloodGroup.B_POSITIVE;
            case AB_NEGATIVE -> donor == BloodGroup.O_NEGATIVE || donor == BloodGroup.A_NEGATIVE || donor == BloodGroup.B_NEGATIVE || donor == BloodGroup.AB_NEGATIVE;
            case AB_POSITIVE -> true;
        };
    }
}

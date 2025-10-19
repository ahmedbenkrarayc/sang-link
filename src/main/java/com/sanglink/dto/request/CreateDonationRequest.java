package com.sanglink.dto.request;

public record CreateDonationRequest(
        Long donorId,
        Long receiverId,
        double volume
) {
}

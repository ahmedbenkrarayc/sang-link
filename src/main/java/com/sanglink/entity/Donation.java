package com.sanglink.entity;

import com.sanglink.entity.enums.DonationStatus;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "donations")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Donation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate dateDon;

    private double volume;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DonationStatus status;

    @ManyToOne
    @JoinColumn(name = "receiver_id", nullable = false)
    private Receiver receiver;

    @ManyToOne
    @JoinColumn(name = "donor_id", nullable = false)
    private Donor donor;
}
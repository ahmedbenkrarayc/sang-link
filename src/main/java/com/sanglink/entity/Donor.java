package com.sanglink.entity;

import com.sanglink.entity.enums.DonorStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "donors")
@DiscriminatorValue("DONOR")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Donor extends User {
    @Column(nullable = false)
    private double weight;

    private LocalDate lastDonation;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DonorStatus status;

    @OneToMany(mappedBy = "donor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Donation> donations;

    @OneToMany(mappedBy = "donor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MedicalAssessment> medicalAssessments;
}

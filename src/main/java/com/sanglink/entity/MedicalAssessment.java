package com.sanglink.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "medical_assessments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedicalAssessment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean hepatiteB;
    private boolean hepatiteC;
    private boolean vih;
    private boolean diabeteInsulin;
    private boolean grossesse;
    private boolean allaitement;

    private LocalDate assessmentDate;

    @ManyToOne
    @JoinColumn(name = "donor_id", nullable = false)
    private Donor donor;
}

package com.sanglink.service.Impl;

import com.sanglink.dto.request.CreateDonorRequest;
import com.sanglink.entity.Donor;
import com.sanglink.entity.MedicalAssessment;
import com.sanglink.entity.enums.BloodGroup;
import com.sanglink.entity.enums.DonorStatus;
import com.sanglink.entity.enums.Gender;
import com.sanglink.repository.DonorRepository;
import com.sanglink.repository.MedicalAssessmentRepository;
import com.sanglink.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class DonorServiceImplTest {

    private DonorRepository donorRepository;
    private MedicalAssessmentRepository assessmentRepository;
    private UserRepository userRepository;
    private DonorServiceImpl donorService;

    @BeforeEach
    void setUp() {
        donorRepository = mock(DonorRepository.class);
        assessmentRepository = mock(MedicalAssessmentRepository.class);
        userRepository = mock(UserRepository.class);

        donorService = new DonorServiceImpl(donorRepository, assessmentRepository, userRepository);
    }

    @Test
    void createDonor_withHepatitisB_shouldBeNonEligible() {
        // Arrange
        CreateDonorRequest request = new CreateDonorRequest(
                "CIN001",
                "John",
                "Doe",
                "123456789",
                LocalDate.of(1990, 1, 1),
                Gender.MALE,
                BloodGroup.O_POSITIVE,
                70.0,
                true,
                false,
                false,
                false,
                false,
                false
        );

        when(userRepository.findByCin(request.cin())).thenReturn(Optional.empty());
        when(donorRepository.save(any(Donor.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(assessmentRepository.save(any(MedicalAssessment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        donorService.createDonor(request);

        // Assert
        ArgumentCaptor<Donor> donorCaptor = ArgumentCaptor.forClass(Donor.class);
        verify(donorRepository, times(1)).save(donorCaptor.capture());
        Donor savedDonor = donorCaptor.getValue();

        assertEquals(DonorStatus.NON_ELIGIBLE, savedDonor.getStatus(), "Donor should be NON_ELIGIBLE due to Hepatitis B");
        verify(assessmentRepository, times(1)).save(any(MedicalAssessment.class));
    }

    @Test
    void createDonor_healthyAdult_shouldBeDisponible() {
        // Arrange
        CreateDonorRequest request = new CreateDonorRequest(
                "CIN002",
                "Alice",
                "Smith",
                "987654321",
                LocalDate.of(1995, 5, 15),
                Gender.FEMALE,
                BloodGroup.A_POSITIVE,
                65.0,
                false, // hepatiteB
                false, // hepatiteC
                false, // vih
                false, // diabeteInsulin
                false, // grossesse
                false  // allaitement
        );

        when(userRepository.findByCin(request.cin())).thenReturn(Optional.empty());
        when(donorRepository.save(any(Donor.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(assessmentRepository.save(any(MedicalAssessment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        donorService.createDonor(request);

        // Assert
        ArgumentCaptor<Donor> donorCaptor = ArgumentCaptor.forClass(Donor.class);
        verify(donorRepository, times(1)).save(donorCaptor.capture());
        Donor savedDonor = donorCaptor.getValue();

        assertEquals(DonorStatus.DISPONIBLE, savedDonor.getStatus(), "Healthy donor should be DISPONIBLE");
        verify(assessmentRepository, times(1)).save(any(MedicalAssessment.class));
    }
}

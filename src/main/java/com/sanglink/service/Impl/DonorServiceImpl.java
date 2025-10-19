package com.sanglink.service.Impl;

import com.sanglink.dto.request.CreateDonorRequest;
import com.sanglink.dto.request.UpdateDonorRequest;
import com.sanglink.entity.Donation;
import com.sanglink.entity.Donor;
import com.sanglink.entity.MedicalAssessment;
import com.sanglink.entity.Receiver;
import com.sanglink.entity.enums.DonorStatus;
import com.sanglink.mapper.DonorMapper;
import com.sanglink.repository.DonorRepository;
import com.sanglink.repository.MedicalAssessmentRepository;
import com.sanglink.repository.UserRepository;
import com.sanglink.service.DonorService;
import com.sanglink.util.validation.request.CreateDonorRequestValidator;
import com.sanglink.util.validation.request.UpdateDonorRequestValidator;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class DonorServiceImpl implements DonorService {

    private final DonorRepository donorRepository;
    private final MedicalAssessmentRepository assessmentRepository;
    private final UserRepository userRepository;

    public DonorServiceImpl(DonorRepository donorRepository, MedicalAssessmentRepository assessmentRepository, UserRepository userRepository) {
        this.donorRepository = donorRepository;
        this.assessmentRepository = assessmentRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<String> createDonor(CreateDonorRequest req) {
        //Validate request
        List<String> errors = CreateDonorRequestValidator.validate(req);
        if (!errors.isEmpty()) return errors;

        //Unique CIN
        if (userRepository.findByCin(req.cin()).isPresent()) {
            errors.add("Donor already exists with CIN: " + req.cin());
            return errors;
        }

        //Map to entities
        Donor donor = DonorMapper.toDonor(req);
        MedicalAssessment assessment = DonorMapper.toMedicalAssessment(req, donor);

        //Link: donor -> assessment
        donor.setMedicalAssessments(List.of(assessment));
        assessment.setDonor(donor);

        //Determine status using the newly created assessment
        DonorStatus status = determineStatus(donor, assessment);
        donor.setStatus(status);

        Donor saved = donorRepository.save(donor);
        if (assessment.getDonor() != null && saved.getId() != null) {
            assessment.setDonor(saved);
        }
        assessmentRepository.save(assessment);

        return List.of();
    }

    private DonorStatus determineStatus(Donor donor, MedicalAssessment assessment) {
        int age = Optional.ofNullable(donor.getBirthday())
                .map(b -> Period.between(b, LocalDate.now()).getYears())
                .orElse(0);

        boolean hasContra = assessment.isHepatiteB()
                || assessment.isHepatiteC()
                || assessment.isVih()
                || assessment.isDiabeteInsulin()
                || assessment.isGrossesse()
                || assessment.isAllaitement();

        if (age < 18 || age > 65 || donor.getWeight() < 50 || hasContra) {
            return DonorStatus.NON_ELIGIBLE;
        }

        // donation count
        long donations = Optional.ofNullable(donor.getDonations())
                .map(List::stream)
                .orElseGet(Stream::empty)
                .count();
        if (donations >= 1) return DonorStatus.NOT_AVAILABLE;

        //receiver association: distinct receiver ids count >=1
        boolean hasReceiver = Optional.ofNullable(donor.getDonations())
                .map(list -> list.stream()
                        .map(Donation::getReceiver)
                        .filter(r -> r != null)
                        .map(Receiver::getId)
                        .distinct()
                        .count() >= 1)
                .orElse(false);
        if (hasReceiver) return DonorStatus.NOT_AVAILABLE;

        return DonorStatus.DISPONIBLE;
    }

    @Override
    public List<Donor> getDonors(int page, int pageSize, String search, DonorStatus status) {
        return donorRepository.findAll(page, pageSize, search, status);
    }

    @Override
    public long countDonors(String search, DonorStatus status) {
        return donorRepository.countAll(search, status);
    }

    @Override
    public List<String> deleteDonor(Long id) {
        List<String> errors = new ArrayList<>();

        var donor = donorRepository.findById(id);
        if (donor == null) {
            errors.add("Donor not found");
            return errors;
        }

        boolean deleted = userRepository.deleteById(id);
        if (!deleted) {
            errors.add("Failed to delete donor. Please try again.");
        }

        return errors;
    }

    @Override
    public List<Donor> getAvailableCompatibleDonors(Long receiverId) {
        return donorRepository.findAvailableCompatibleDonors(receiverId);
    }

    @Override
    public Optional<Donor> getDonorById(Long id) {
        return donorRepository.findById(id);
    }

    @Override
    public List<String> updateDonor(UpdateDonorRequest req) {
        List<String> errors = new ArrayList<>(UpdateDonorRequestValidator.validate(req));

        if (!errors.isEmpty()) return errors;

        Optional<Donor> optionalDonor = donorRepository.findById(req.id());
        if (optionalDonor.isEmpty()) {
            errors.add("Donor not found with ID: " + req.id());
            return errors;
        }

        Donor donor = optionalDonor.get();

        donor.setCin(req.cin());
        donor.setNom(req.nom());
        donor.setPrenom(req.prenom());
        donor.setPhone(req.phone());
        donor.setBirthday(req.birthday());
        donor.setGender(req.gender());
        donor.setBloodGroup(req.bloodGroup());
        donor.setWeight(req.weight());

        donorRepository.save(donor);

        return List.of();
    }
}

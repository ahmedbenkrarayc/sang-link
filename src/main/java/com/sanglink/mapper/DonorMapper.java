package com.sanglink.mapper;

import com.sanglink.dto.request.CreateDonorRequest;
import com.sanglink.entity.Donor;
import com.sanglink.entity.MedicalAssessment;

import java.time.LocalDate;
import java.util.Optional;

public class DonorMapper {
    public static Donor toDonor(CreateDonorRequest req) {
        Donor d = new Donor();
        Optional.ofNullable(req.cin()).ifPresent(d::setCin);
        Optional.ofNullable(req.nom()).ifPresent(d::setNom);
        Optional.ofNullable(req.prenom()).ifPresent(d::setPrenom);
        Optional.ofNullable(req.phone()).ifPresent(d::setPhone);
        Optional.ofNullable(req.birthday()).ifPresent(d::setBirthday);
        Optional.ofNullable(req.gender()).ifPresent(d::setGender);
        Optional.ofNullable(req.bloodGroup()).ifPresent(d::setBloodGroup);
        d.setWeight(req.weight());
        return d;
    }

    public static MedicalAssessment toMedicalAssessment(CreateDonorRequest req, Donor donor) {
        MedicalAssessment m = new MedicalAssessment();
        m.setDonor(donor);
        m.setHepatiteB(req.hepatiteB());
        m.setHepatiteC(req.hepatiteC());
        m.setVih(req.vih());
        m.setDiabeteInsulin(req.diabeteInsulin());
        m.setGrossesse(req.grossesse());
        m.setAllaitement(req.allaitement());
        m.setAssessmentDate(LocalDate.now());
        return m;
    }
}

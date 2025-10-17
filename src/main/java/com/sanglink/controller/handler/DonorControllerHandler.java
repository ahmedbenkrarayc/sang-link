package com.sanglink.controller.handler;

import com.sanglink.dto.request.CreateDonorRequest;
import com.sanglink.entity.enums.BloodGroup;
import com.sanglink.entity.enums.Gender;
import com.sanglink.service.DonorService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class DonorControllerHandler {
    public void create(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setAttribute("genders", Gender.values());
        req.setAttribute("bloodGroups", BloodGroup.values());

        req.getRequestDispatcher("/view/donors/create.jsp").forward(req, resp);
    }

    public void store(HttpServletRequest req, HttpServletResponse resp, DonorService donorService)
            throws ServletException, IOException {

        try {
            CreateDonorRequest request = new CreateDonorRequest(
                    req.getParameter("cin"),
                    req.getParameter("nom"),
                    req.getParameter("prenom"),
                    req.getParameter("phone"),
                    LocalDate.parse(req.getParameter("birthday")),
                    Gender.valueOf(req.getParameter("gender")),
                    BloodGroup.valueOf(req.getParameter("bloodGroup")),
                    Double.parseDouble(req.getParameter("weight")),
                    Boolean.parseBoolean(req.getParameter("hepatiteB")),
                    Boolean.parseBoolean(req.getParameter("hepatiteC")),
                    Boolean.parseBoolean(req.getParameter("vih")),
                    Boolean.parseBoolean(req.getParameter("diabeteInsulin")),
                    Boolean.parseBoolean(req.getParameter("grossesse")),
                    Boolean.parseBoolean(req.getParameter("allaitement"))
            );

            List<String> errors = donorService.createDonor(request);

            req.setAttribute("genders", Gender.values());
            req.setAttribute("bloodGroups", BloodGroup.values());

            if (!errors.isEmpty()) {
                req.setAttribute("errors", errors);
            } else {
                req.setAttribute("success", "Donor created successfully!");
            }

            req.getRequestDispatcher("/view/donors/create.jsp").forward(req, resp);

        } catch (IllegalArgumentException ex) {
            req.setAttribute("errors", List.of("Invalid input: " + ex.getMessage()));
            req.getRequestDispatcher("/view/donors/create.jsp").forward(req, resp);
        } catch (Exception ex) {
            req.setAttribute("errors", List.of("Something went wrong please try again later !"));
            req.getRequestDispatcher("/view/donors/create.jsp").forward(req, resp);
        }
    }
}

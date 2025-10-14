package com.sanglink.controller;

import com.sanglink.dao.DonorDAO;
import com.sanglink.dao.MedicalAssessmentDAO;
import com.sanglink.dao.impl.DonorDAOImpl;
import com.sanglink.dao.impl.MedicalAssessmentDAOImpl;
import com.sanglink.dto.request.CreateDonorRequest;
import com.sanglink.entity.enums.BloodGroup;
import com.sanglink.entity.enums.Gender;
import com.sanglink.repository.impl.DonorRepositoryImpl;
import com.sanglink.repository.impl.MedicalAssessmentRepositoryImpl;
import com.sanglink.service.DonorService;
import com.sanglink.service.Impl.DonorServiceImpl;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class DonorServlet extends HttpServlet {

    private DonorService donorService;
    private EntityManager em;

    @Override
    public void init() throws ServletException {
        super.init();

        EntityManagerFactory emf = (EntityManagerFactory) getServletContext().getAttribute("emf");
        if (emf == null) {
            throw new ServletException("EntityManagerFactory not found in servlet context");
        }

        em = emf.createEntityManager();

        DonorDAO donorDAO = new DonorDAOImpl(em);
        MedicalAssessmentDAO assessmentDAO = new MedicalAssessmentDAOImpl(em);

        var donorRepo = new DonorRepositoryImpl(donorDAO);
        var assessmentRepo = new MedicalAssessmentRepositoryImpl(assessmentDAO);

        this.donorService = new DonorServiceImpl(donorRepo, assessmentRepo);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setAttribute("genders", Gender.values());
        req.setAttribute("bloodGroups", BloodGroup.values());

        req.getRequestDispatcher("/view/donors/create.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
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

    @Override
    public void destroy() {
        super.destroy();
        if (em != null && em.isOpen()) em.close();
    }
}

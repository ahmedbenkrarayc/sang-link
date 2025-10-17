package com.sanglink.controller;

import com.sanglink.controller.handler.DonorControllerHandler;
import com.sanglink.dao.DonorDAO;
import com.sanglink.dao.MedicalAssessmentDAO;
import com.sanglink.dao.UserDAO;
import com.sanglink.dao.impl.DonorDAOImpl;
import com.sanglink.dao.impl.MedicalAssessmentDAOImpl;
import com.sanglink.dao.impl.UserDAOImpl;
import com.sanglink.dto.request.CreateDonorRequest;
import com.sanglink.entity.enums.BloodGroup;
import com.sanglink.entity.enums.Gender;
import com.sanglink.repository.impl.DonorRepositoryImpl;
import com.sanglink.repository.impl.MedicalAssessmentRepositoryImpl;
import com.sanglink.repository.impl.UserRepositoryImpl;
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
    private final DonorControllerHandler donorHandler = new DonorControllerHandler();

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
        UserDAO userDAO = new UserDAOImpl(em);

        var donorRepo = new DonorRepositoryImpl(donorDAO);
        var assessmentRepo = new MedicalAssessmentRepositoryImpl(assessmentDAO);
        var userRepo = new UserRepositoryImpl(userDAO);

        this.donorService = new DonorServiceImpl(donorRepo, assessmentRepo, userRepo);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String path = req.getPathInfo();

        if (path == null || "/".equals(path)) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }else if (path.equals("/create")) {
            donorHandler.create(req, resp);
        }else {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String path = req.getPathInfo();

        if (path.equals("/create")) {
            donorHandler.store(req, resp, donorService);
        }else {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    public void destroy() {
        super.destroy();
        if (em != null && em.isOpen()) em.close();
    }
}

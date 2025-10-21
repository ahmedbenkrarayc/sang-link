package com.sanglink.controller;

import com.sanglink.controller.handler.DonationControllerHandler;
import com.sanglink.controller.handler.DonorControllerHandler;
import com.sanglink.dao.DonorDAO;
import com.sanglink.dao.MedicalAssessmentDAO;
import com.sanglink.dao.UserDAO;
import com.sanglink.dao.impl.DonorDAOImpl;
import com.sanglink.dao.impl.MedicalAssessmentDAOImpl;
import com.sanglink.dao.impl.UserDAOImpl;
import com.sanglink.repository.impl.DonorRepositoryImpl;
import com.sanglink.repository.impl.MedicalAssessmentRepositoryImpl;
import com.sanglink.repository.impl.UserRepositoryImpl;
import com.sanglink.service.DonorService;
import com.sanglink.service.Impl.DonorServiceImpl;
import com.sanglink.config.JpaBootstrapListener;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class DonorServlet extends HttpServlet {

    private final DonorControllerHandler donorHandler = new DonorControllerHandler();
    private final DonationControllerHandler donationHandler = new DonationControllerHandler();

    private DonorService createDonorService(EntityManager em) {
        DonorDAO donorDAO = new DonorDAOImpl(em);
        MedicalAssessmentDAO assessmentDAO = new MedicalAssessmentDAOImpl(em);
        UserDAO userDAO = new UserDAOImpl(em);

        var donorRepo = new DonorRepositoryImpl(donorDAO);
        var assessmentRepo = new MedicalAssessmentRepositoryImpl(assessmentDAO);
        var userRepo = new UserRepositoryImpl(userDAO);

        return new DonorServiceImpl(donorRepo, assessmentRepo, userRepo);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String path = req.getPathInfo();
        EntityManager em = JpaBootstrapListener.getEntityManagerFactory().createEntityManager();
        try {
            DonorService donorService = createDonorService(em);

            if (path == null || "/".equals(path)) {
                donorHandler.index(req, resp, donorService);
            } else if (path.equals("/create")) {
                donorHandler.create(req, resp);
            } else if (path.equals("/compatible")) {
                donationHandler.index(req, resp, donorService);
            } else if (path.equals("/edit")) {
                donorHandler.edit(req, resp, donorService);
            } else {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } finally {
            if (em.isOpen()) em.close();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String path = req.getPathInfo();
        EntityManager em = JpaBootstrapListener.getEntityManagerFactory().createEntityManager();
        try {
            DonorService donorService = createDonorService(em);

            if ("/create".equals(path)) {
                donorHandler.store(req, resp, donorService);
            } else if ("/edit".equals(path)) {
                donorHandler.update(req, resp, donorService);
            } else {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } finally {
            if (em.isOpen()) em.close();
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        EntityManager em = JpaBootstrapListener.getEntityManagerFactory().createEntityManager();
        try {
            DonorService donorService = createDonorService(em);
            donorHandler.drop(req, resp, donorService);
        } finally {
            if (em.isOpen()) em.close();
        }
    }
}

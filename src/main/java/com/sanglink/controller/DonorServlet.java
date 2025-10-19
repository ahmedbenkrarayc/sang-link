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
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class DonorServlet extends HttpServlet {

    private DonorService donorService;
    private EntityManager em;
    private final DonorControllerHandler donorHandler = new DonorControllerHandler();
    private final DonationControllerHandler donationHandler = new DonationControllerHandler();

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
            donorHandler.index(req, resp, donorService);
        }else if (path.equals("/create")) {
            donorHandler.create(req, resp);
        }else if (path.equals("/compatible")) {
            donationHandler.index(req, resp, donorService);
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
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        donorHandler.drop(req, resp, donorService);
    }

    @Override
    public void destroy() {
        super.destroy();
        if (em != null && em.isOpen()) em.close();
    }
}

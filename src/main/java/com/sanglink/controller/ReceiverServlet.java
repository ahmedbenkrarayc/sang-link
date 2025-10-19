package com.sanglink.controller;

import com.sanglink.controller.handler.ReceiverControllerHandler;
import com.sanglink.dao.DonorDAO;
import com.sanglink.dao.MedicalAssessmentDAO;
import com.sanglink.dao.ReceiverDAO;
import com.sanglink.dao.UserDAO;
import com.sanglink.dao.impl.DonorDAOImpl;
import com.sanglink.dao.impl.MedicalAssessmentDAOImpl;
import com.sanglink.dao.impl.ReceiverDAOImpl;
import com.sanglink.dao.impl.UserDAOImpl;
import com.sanglink.dto.request.CreateReceiverRequest;
import com.sanglink.entity.enums.BloodGroup;
import com.sanglink.entity.enums.Gender;
import com.sanglink.entity.enums.Need;
import com.sanglink.repository.impl.DonorRepositoryImpl;
import com.sanglink.repository.impl.MedicalAssessmentRepositoryImpl;
import com.sanglink.repository.impl.ReceiverRepositoryImpl;
import com.sanglink.repository.impl.UserRepositoryImpl;
import com.sanglink.service.Impl.DonorServiceImpl;
import com.sanglink.service.Impl.ReceiverServiceImpl;
import com.sanglink.service.ReceiverService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class ReceiverServlet extends HttpServlet {
    private ReceiverService receiverService;
    private EntityManager em;
    private final ReceiverControllerHandler receiverHandler = new ReceiverControllerHandler();

    @Override
    public void init() throws ServletException {
        super.init();

        EntityManagerFactory emf = (EntityManagerFactory) getServletContext().getAttribute("emf");
        if (emf == null) {
            throw new ServletException("EntityManagerFactory not found in servlet context");
        }

        em = emf.createEntityManager();

        ReceiverDAO receiverDAO = new ReceiverDAOImpl(em);
        UserDAO userDAO = new UserDAOImpl(em);
        var receiverRepo = new ReceiverRepositoryImpl(receiverDAO);
        var userRepo = new UserRepositoryImpl(userDAO);

        this.receiverService = new ReceiverServiceImpl(userRepo, receiverRepo);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String path = req.getPathInfo();

        if (path == null || "/".equals(path)) {
            receiverHandler.index(req, resp, receiverService);
        }else if (path.equals("/create")) {
            receiverHandler.create(req, resp);
        }else if (path.equals("/edit")) {
            receiverHandler.edit(req, resp, receiverService);
        }else {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String path = req.getPathInfo();

        if (path.equals("/create")) {
            receiverHandler.store(req, resp, receiverService);
        }if (path.equals("/edit")) {
            receiverHandler.update(req, resp, receiverService);
        }else {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        receiverHandler.drop(req, resp, receiverService);
    }

    @Override
    public void destroy() { if (em != null && em.isOpen()) em.close(); }
}

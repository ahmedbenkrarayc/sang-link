package com.sanglink.controller;

import com.sanglink.controller.handler.DonationControllerHandler;
import com.sanglink.dao.*;
import com.sanglink.dao.impl.*;
import com.sanglink.repository.impl.*;
import com.sanglink.service.DonationService;
import com.sanglink.service.Impl.DonationServiceImpl;
import com.sanglink.service.Impl.DonorServiceImpl;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class DonationServlet extends HttpServlet {
    private EntityManager em;
    private DonationService donationService;
    private final DonationControllerHandler donationHandler = new DonationControllerHandler();

    @Override
    public void init() throws ServletException {
        super.init();

        EntityManagerFactory emf = (EntityManagerFactory) getServletContext().getAttribute("emf");
        if (emf == null) {
            throw new ServletException("EntityManagerFactory not found in servlet context");
        }

        em = emf.createEntityManager();

        DonationDAO donationDAO = new DonationDAOImpl(em);
        DonorDAO donorDAO = new DonorDAOImpl(em);
        ReceiverDAO receiverDAO = new ReceiverDAOImpl(em);

        var donationRepo = new DonationRepositoryImpl(donationDAO);
        var donorRepo = new DonorRepositoryImpl(donorDAO);
        var receiverRepo = new ReceiverRepositoryImpl(receiverDAO);

        this.donationService = new DonationServiceImpl(donationRepo, donorRepo, receiverRepo);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        donationHandler.store(req, resp, donationService);
    }

    @Override
    public void destroy() {
        super.destroy();
        if (em != null && em.isOpen()) em.close();
    }
}

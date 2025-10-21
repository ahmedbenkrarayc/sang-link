package com.sanglink.controller;

import com.sanglink.controller.handler.DonationControllerHandler;
import com.sanglink.dao.DonationDAO;
import com.sanglink.dao.DonorDAO;
import com.sanglink.dao.ReceiverDAO;
import com.sanglink.dao.impl.DonationDAOImpl;
import com.sanglink.dao.impl.DonorDAOImpl;
import com.sanglink.dao.impl.ReceiverDAOImpl;
import com.sanglink.repository.impl.DonationRepositoryImpl;
import com.sanglink.repository.impl.DonorRepositoryImpl;
import com.sanglink.repository.impl.ReceiverRepositoryImpl;
import com.sanglink.service.DonationService;
import com.sanglink.service.Impl.DonationServiceImpl;
import com.sanglink.config.JpaBootstrapListener;
import jakarta.persistence.EntityManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class DonationServlet extends HttpServlet {

    private final DonationControllerHandler donationHandler = new DonationControllerHandler();

    private DonationService createDonationService(EntityManager em) {
        DonationDAO donationDAO = new DonationDAOImpl(em);
        DonorDAO donorDAO = new DonorDAOImpl(em);
        ReceiverDAO receiverDAO = new ReceiverDAOImpl(em);

        var donationRepo = new DonationRepositoryImpl(donationDAO);
        var donorRepo = new DonorRepositoryImpl(donorDAO);
        var receiverRepo = new ReceiverRepositoryImpl(receiverDAO);

        return new DonationServiceImpl(donationRepo, donorRepo, receiverRepo);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        EntityManager em = JpaBootstrapListener.getEntityManagerFactory().createEntityManager();
        try {
            DonationService donationService = createDonationService(em);
            donationHandler.store(req, resp, donationService);
        } finally {
            if (em.isOpen()) em.close();
        }
    }
}

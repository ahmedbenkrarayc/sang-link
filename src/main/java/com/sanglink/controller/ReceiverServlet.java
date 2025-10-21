package com.sanglink.controller;

import com.sanglink.controller.handler.ReceiverControllerHandler;
import com.sanglink.dao.ReceiverDAO;
import com.sanglink.dao.UserDAO;
import com.sanglink.dao.impl.ReceiverDAOImpl;
import com.sanglink.dao.impl.UserDAOImpl;
import com.sanglink.repository.impl.ReceiverRepositoryImpl;
import com.sanglink.repository.impl.UserRepositoryImpl;
import com.sanglink.service.Impl.ReceiverServiceImpl;
import com.sanglink.service.ReceiverService;
import com.sanglink.config.JpaBootstrapListener;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class ReceiverServlet extends HttpServlet {
    private final ReceiverControllerHandler receiverHandler = new ReceiverControllerHandler();

    @Override
    public void init() throws ServletException {
        super.init();
        EntityManagerFactory emf = (EntityManagerFactory) getServletContext().getAttribute("emf");
        if (emf == null) {
            throw new ServletException("EntityManagerFactory not found in servlet context");
        }
    }

    private ReceiverService createReceiverService(EntityManager em) {
        ReceiverDAO receiverDAO = new ReceiverDAOImpl(em);
        UserDAO userDAO = new UserDAOImpl(em);
        var receiverRepo = new ReceiverRepositoryImpl(receiverDAO);
        var userRepo = new UserRepositoryImpl(userDAO);
        return new ReceiverServiceImpl(userRepo, receiverRepo);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String path = req.getPathInfo();
        EntityManager em = JpaBootstrapListener.getEntityManagerFactory().createEntityManager();
        try {
            ReceiverService receiverService = createReceiverService(em);

            if (path == null || "/".equals(path)) {
                receiverHandler.index(req, resp, receiverService);
            } else if (path.equals("/create")) {
                receiverHandler.create(req, resp);
            } else if (path.equals("/edit")) {
                receiverHandler.edit(req, resp, receiverService);
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
            ReceiverService receiverService = createReceiverService(em);

            if ("/create".equals(path)) {
                receiverHandler.store(req, resp, receiverService);
            } else if ("/edit".equals(path)) {
                receiverHandler.update(req, resp, receiverService);
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
            ReceiverService receiverService = createReceiverService(em);
            receiverHandler.drop(req, resp, receiverService);
        } finally {
            if (em.isOpen()) em.close();
        }
    }
}

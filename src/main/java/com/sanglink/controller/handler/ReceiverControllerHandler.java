package com.sanglink.controller.handler;

import com.sanglink.dto.request.CreateReceiverRequest;
import com.sanglink.dto.request.UpdateReceiverRequest;
import com.sanglink.entity.Receiver;
import com.sanglink.entity.enums.BloodGroup;
import com.sanglink.entity.enums.Gender;
import com.sanglink.entity.enums.Need;
import com.sanglink.service.ReceiverService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class ReceiverControllerHandler {
    private static final Logger logger = LoggerFactory.getLogger(ReceiverControllerHandler.class);

    public void index(HttpServletRequest req, HttpServletResponse resp, ReceiverService receiverService)
            throws ServletException, IOException {

        String search = req.getParameter("search");
        String needParam = req.getParameter("need");
        int page = parseIntOrDefault(req.getParameter("page"), 1);
        int pageSize = parseIntOrDefault(req.getParameter("pageSize"), 10);

        Need need = null;
        if (needParam != null && !needParam.isBlank()) {
            try {
                need = Need.valueOf(needParam.toUpperCase());
            } catch (IllegalArgumentException ignored) {}
        }

        List<Receiver> receivers = receiverService.getReceivers(page, pageSize, search, need);
        long total = receiverService.countReceivers(search, need);
        long totalPages = (long) Math.ceil((double) total / pageSize);

        req.setAttribute("receivers", receivers);
        req.setAttribute("currentPage", page);
        req.setAttribute("totalPages", totalPages);
        req.setAttribute("search", search);
        req.setAttribute("selectedNeed", need);
        req.setAttribute("needs", Need.values());

        logger.info("Listed receivers: page={}, pageSize={}, search={}, need={}", page, pageSize, search, need);
        req.getRequestDispatcher("/view/receivers/list.jsp").forward(req, resp);
    }

    public void create(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setAttribute("genders", Gender.values());
        req.setAttribute("bloodGroups", BloodGroup.values());
        req.setAttribute("needs", Need.values());
        req.getRequestDispatcher("/view/receivers/create.jsp").forward(req, resp);
    }

    public void store(HttpServletRequest req, HttpServletResponse resp, ReceiverService receiverService)
            throws ServletException, IOException {

        try {
            CreateReceiverRequest request = new CreateReceiverRequest(
                    req.getParameter("cin"),
                    req.getParameter("nom"),
                    req.getParameter("prenom"),
                    req.getParameter("phone"),
                    LocalDate.parse(req.getParameter("birthday")),
                    Gender.valueOf(req.getParameter("gender")),
                    BloodGroup.valueOf(req.getParameter("bloodGroup")),
                    Need.valueOf(req.getParameter("need"))
            );

            List<String> errors = receiverService.createReceiver(request);
            req.setAttribute("genders", Gender.values());
            req.setAttribute("bloodGroups", BloodGroup.values());
            req.setAttribute("needs", Need.values());

            if (!errors.isEmpty()) {
                req.setAttribute("errors", errors);
                logger.warn("Receiver create validation errors: {}", errors);
            } else {
                req.setAttribute("success", "Receiver created successfully!");
                logger.info("Created receiver with CIN: {}", request.cin());
            }

            req.getRequestDispatcher("/view/receivers/create.jsp").forward(req, resp);
        } catch (IllegalArgumentException ex) {
            req.setAttribute("errors", List.of("Invalid input: " + ex.getMessage()));
            logger.error("Failed to create receiver due to invalid input", ex);
            req.getRequestDispatcher("/view/receivers/create.jsp").forward(req, resp);
        } catch (Exception ex) {
            req.setAttribute("errors", List.of("Something went wrong please try again later!"));
            logger.error("Failed to create receiver", ex);
            req.getRequestDispatcher("/view/receivers/create.jsp").forward(req, resp);
        }
    }

    public void drop(HttpServletRequest req, HttpServletResponse resp, ReceiverService receiverService)
            throws ServletException, IOException {
        try {
            Long id = Long.parseLong(req.getParameter("id"));
            List<String> errors = receiverService.deleteReceiver(id);

            resp.setContentType("application/json");

            if (!errors.isEmpty()) {
                logger.error("Delete receiver failed: {}", errors);
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("{\"status\":\"error\",\"errors\":" + errors + "}");
                return;
            }

            logger.info("Deleted receiver with ID: {}", id);
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write("{\"status\":\"success\",\"message\":\"Receiver deleted successfully.\"}");
        } catch (NumberFormatException e) {
            logger.error("Delete receiver failed: invalid ID format", e);
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"status\":\"error\",\"message\":\"Invalid receiver ID.\"}");
        } catch (Exception e) {
            logger.error("Unexpected error deleting receiver", e);
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"status\":\"error\",\"message\":\"" + e.getMessage() + "\"}");
        }
    }

    public void edit(HttpServletRequest req, HttpServletResponse resp, ReceiverService receiverService)
            throws ServletException, IOException {
        try {
            Long id = Long.parseLong(req.getParameter("id"));

            var receiverOpt = receiverService.getReceiverById(id);
            if (receiverOpt.isEmpty()) {
                logger.error("Receiver not found with ID: {}", id);
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Receiver not found");
                return;
            }

            req.setAttribute("receiver", receiverOpt.get());
            req.setAttribute("genders", Gender.values());
            req.setAttribute("bloodGroups", BloodGroup.values());
            req.setAttribute("needs", Need.values());

            req.getRequestDispatcher("/view/receivers/edit.jsp").forward(req, resp);
        } catch (NumberFormatException e) {
            logger.error("Invalid receiver ID format for edit", e);
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid receiver ID");
        } catch (Exception e) {
            logger.error("Unexpected error requesting receiver edit page", e);
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Unexpected error: " + e.getMessage());
        }
    }

    public void update(HttpServletRequest req, HttpServletResponse resp, ReceiverService receiverService)
            throws ServletException, IOException {
        try {
            Long id = Long.parseLong(req.getParameter("id"));

            var receiverOpt = receiverService.getReceiverById(id);
            if (receiverOpt.isEmpty()) {
                logger.error("Receiver not found with ID: {}", id);
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Receiver not found");
                return;
            }

            UpdateReceiverRequest request = new UpdateReceiverRequest(
                    id,
                    req.getParameter("cin"),
                    req.getParameter("nom"),
                    req.getParameter("prenom"),
                    req.getParameter("phone"),
                    LocalDate.parse(req.getParameter("birthday")),
                    Gender.valueOf(req.getParameter("gender")),
                    BloodGroup.valueOf(req.getParameter("bloodGroup")),
                    Need.valueOf(req.getParameter("need"))
            );

            List<String> errors = receiverService.updateReceiver(request);

            req.setAttribute("receiver", receiverOpt.get());
            req.setAttribute("genders", Gender.values());
            req.setAttribute("bloodGroups", BloodGroup.values());
            req.setAttribute("needs", Need.values());

            if (!errors.isEmpty()) {
                req.setAttribute("errors", errors);
                logger.warn("Failed to update receiver: {}", errors);
            } else {
                req.setAttribute("success", "Receiver updated successfully!");
                receiverService.getReceiverById(id).ifPresent(r -> req.setAttribute("receiver", r));
                logger.info("Receiver updated successfully with ID: {}", id);
            }

            req.getRequestDispatcher("/view/receivers/edit.jsp").forward(req, resp);
        } catch (NumberFormatException e) {
            logger.error("Invalid receiver ID format for update", e);
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid receiver ID");
        } catch (IllegalArgumentException e) {
            req.setAttribute("errors", List.of("Invalid input: " + e.getMessage()));
            logger.error("Failed to update receiver due to invalid input", e);
            req.getRequestDispatcher("/view/receivers/edit.jsp").forward(req, resp);
        } catch (Exception e) {
            req.setAttribute("errors", List.of("Something went wrong, please try again later!"));
            logger.error("Unexpected error updating receiver", e);
            req.getRequestDispatcher("/view/receivers/edit.jsp").forward(req, resp);
        }
    }

    private int parseIntOrDefault(String value, int defaultValue) {
        try {
            return value != null ? Integer.parseInt(value) : defaultValue;
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
}

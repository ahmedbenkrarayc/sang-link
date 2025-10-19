package com.sanglink.controller.handler;

import com.sanglink.dto.request.CreateReceiverRequest;
import com.sanglink.dto.request.UpdateReceiverRequest;
import com.sanglink.entity.Receiver;
import com.sanglink.entity.enums.BloodGroup;
import com.sanglink.entity.enums.Gender;
import com.sanglink.entity.enums.Need;
import com.sanglink.service.DonorService;
import com.sanglink.service.ReceiverService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class ReceiverControllerHandler {
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
            if (!errors.isEmpty()) req.setAttribute("errors", errors);
            else req.setAttribute("success", "Receiver created successfully!");

            req.getRequestDispatcher("/view/receivers/create.jsp").forward(req, resp);
        } catch (IllegalArgumentException ex) {
            req.setAttribute("errors", List.of("Invalid input: " + ex.getMessage()));
            req.getRequestDispatcher("/view/receivers/create.jsp").forward(req, resp);
        } catch (Exception ex) {
            req.setAttribute("errors", List.of("Something went wrong please try again later !"));
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
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("{\"status\":\"error\",\"errors\":" + errors + "}");
                return;
            }

            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write("{\"status\":\"success\",\"message\":\"Receiver deleted successfully.\"}");
        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"status\":\"error\",\"message\":\"Invalid receiver ID.\"}");
        } catch (Exception e) {
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
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Receiver not found");
                return;
            }

            req.setAttribute("receiver", receiverOpt.get());
            req.setAttribute("genders", Gender.values());
            req.setAttribute("bloodGroups", BloodGroup.values());
            req.setAttribute("needs", Need.values());

            req.getRequestDispatcher("/view/receivers/edit.jsp").forward(req, resp);

        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid receiver ID");
        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Unexpected error: " + e.getMessage());
        }
    }

    public void update(HttpServletRequest req, HttpServletResponse resp, ReceiverService receiverService)
            throws ServletException, IOException {
        try {
            Long id = Long.parseLong(req.getParameter("id"));

            var receiverOpt = receiverService.getReceiverById(id);
            if (receiverOpt.isEmpty()) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Receiver not found");
                return;
            }

            System.out.println("id=" + id);
            System.out.println("cin=" + req.getParameter("cin"));
            System.out.println("nom=" + req.getParameter("nom"));
            System.out.println("prenom=" + req.getParameter("prenom"));
            System.out.println("phone=" + req.getParameter("phone"));
            System.out.println("birthday=" + req.getParameter("birthday"));
            System.out.println("gender=" + req.getParameter("gender"));
            System.out.println("bloodGroup=" + req.getParameter("bloodGroup"));
            System.out.println("need=" + req.getParameter("need"));

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
            } else {
                req.setAttribute("success", "Receiver updated successfully!");
                receiverService.getReceiverById(id).ifPresent(r -> req.setAttribute("receiver", r));
            }

            req.getRequestDispatcher("/view/receivers/edit.jsp").forward(req, resp);

        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid receiver ID");
        } catch (IllegalArgumentException e) {
            req.setAttribute("errors", List.of("Invalid input: " + e.getMessage()));
            req.getRequestDispatcher("/view/receivers/edit.jsp").forward(req, resp);
        } catch (Exception e) {
            System.out.println("==================>"+e.getMessage());
            req.setAttribute("errors", List.of("Something went wrong, please try again later!"));
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

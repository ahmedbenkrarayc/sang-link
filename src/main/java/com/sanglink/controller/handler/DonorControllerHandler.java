package com.sanglink.controller.handler;

import com.sanglink.dto.request.CreateDonorRequest;
import com.sanglink.dto.request.UpdateDonorRequest;
import com.sanglink.entity.Donor;
import com.sanglink.entity.enums.BloodGroup;
import com.sanglink.entity.enums.DonorStatus;
import com.sanglink.entity.enums.Gender;
import com.sanglink.service.DonorService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class DonorControllerHandler {
    public void index(HttpServletRequest req, HttpServletResponse resp, DonorService donorService) throws ServletException, IOException {
        String search = req.getParameter("search");
        String statusParam = req.getParameter("status");
        int page = parseIntOrDefault(req.getParameter("page"), 1);
        int pageSize = parseIntOrDefault(req.getParameter("pageSize"), 10);

        DonorStatus status = null;
        if (statusParam != null && !statusParam.isBlank()) {
            try {
                status = DonorStatus.valueOf(statusParam);
            } catch (IllegalArgumentException ignored) {}
        }

        List<Donor> donors = donorService.getDonors(page, pageSize, search, status);
        long total = donorService.countDonors(search, status);
        long totalPages = (long) Math.ceil((double) total / pageSize);

        req.setAttribute("donors", donors);
        req.setAttribute("currentPage", page);
        req.setAttribute("totalPages", totalPages);
        req.setAttribute("search", search);
        req.setAttribute("selectedStatus", status);
        req.setAttribute("statuses", DonorStatus.values());

        req.getRequestDispatcher("/view/donors/list.jsp").forward(req, resp);
    }

    public void create(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setAttribute("genders", Gender.values());
        req.setAttribute("bloodGroups", BloodGroup.values());

        req.getRequestDispatcher("/view/donors/create.jsp").forward(req, resp);
    }

    public void store(HttpServletRequest req, HttpServletResponse resp, DonorService donorService)
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

    public void drop(HttpServletRequest req, HttpServletResponse resp, DonorService donorService)
        throws ServletException, IOException {
        try {
            Long id = Long.parseLong(req.getParameter("id"));
            List<String> errors = donorService.deleteDonor(id);

            resp.setContentType("application/json");

            if (!errors.isEmpty()) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("{\"status\":\"error\",\"errors\":" + errors + "}");
                return;
            }

            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write("{\"status\":\"success\",\"message\":\"Donor deleted successfully.\"}");
        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"status\":\"error\",\"message\":\"Invalid donor ID.\"}");
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"status\":\"error\",\"message\":\"" + e.getMessage() + "\"}");
        }
    }

    public void edit(HttpServletRequest req, HttpServletResponse resp, DonorService donorService)
            throws ServletException, IOException {
        try {
            Long id = Long.parseLong(req.getParameter("id"));

            Donor donor = donorService.getDonorById(id).orElse(null);

            if (donor == null) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Donor not found");
                return;
            }

            req.setAttribute("donor", donor);
            req.setAttribute("genders", Gender.values());
            req.setAttribute("bloodGroups", BloodGroup.values());

            req.getRequestDispatcher("/view/donors/edit.jsp").forward(req, resp);

        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid donor ID");
        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Unexpected error: " + e.getMessage());
        }
    }

    public void update(HttpServletRequest req, HttpServletResponse resp, DonorService donorService)
            throws ServletException, IOException {
        try {
            Long id = Long.parseLong(req.getParameter("id"));

            Donor existingDonor = donorService.getDonorById(id).orElse(null);
            if (existingDonor == null) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Donor not found");
                return;
            }

            UpdateDonorRequest request = new UpdateDonorRequest(
                    id,
                    req.getParameter("cin"),
                    req.getParameter("nom"),
                    req.getParameter("prenom"),
                    req.getParameter("phone"),
                    LocalDate.parse(req.getParameter("birthday")),
                    Gender.valueOf(req.getParameter("gender")),
                    BloodGroup.valueOf(req.getParameter("bloodGroup")),
                    Double.parseDouble(req.getParameter("weight"))
            );

            List<String> errors = donorService.updateDonor(request);

            req.setAttribute("genders", Gender.values());
            req.setAttribute("bloodGroups", BloodGroup.values());
            req.setAttribute("donor", existingDonor);

            if (!errors.isEmpty()) {
                req.setAttribute("errors", errors);
            } else {
                req.setAttribute("success", "Donor updated successfully!");
                donorService.getDonorById(id).ifPresent(d -> req.setAttribute("donor", d));
            }

            req.getRequestDispatcher("/view/donors/edit.jsp").forward(req, resp);

        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid donor ID");
        } catch (IllegalArgumentException e) {
            req.setAttribute("errors", List.of("Invalid input: " + e.getMessage()));
            req.getRequestDispatcher("/view/donors/edit.jsp").forward(req, resp);
        } catch (Exception e) {
            req.setAttribute("errors", List.of("Something went wrong, please try again later!"));
            req.getRequestDispatcher("/view/donors/edit.jsp").forward(req, resp);
        }
    }

    private int parseIntOrDefault(String param, int def) {
        try {
            return (param == null || param.isBlank()) ? def : Integer.parseInt(param);
        } catch (NumberFormatException e) {
            return def;
        }
    }
}

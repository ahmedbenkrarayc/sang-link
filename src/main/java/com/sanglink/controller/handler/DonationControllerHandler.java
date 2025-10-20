package com.sanglink.controller.handler;

import com.sanglink.dto.request.CreateDonationRequest;
import com.sanglink.entity.Donor;
import com.sanglink.service.DonationService;
import com.sanglink.service.DonorService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

public class DonationControllerHandler {
    private static final Logger logger = LoggerFactory.getLogger(DonationControllerHandler.class);

    public void index(HttpServletRequest req, HttpServletResponse resp, DonorService donorService)  throws IOException {
        resp.setContentType("application/json;charset=UTF-8");

        String receiverIdParam = req.getParameter("receiverId");
        if (receiverIdParam == null || receiverIdParam.isBlank()) {
            logger.warn("Missing receiverId parameter for donor listing");
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\":\"Receiver ID is required.\"}");
            return;
        }

        try {
            Long receiverId = Long.parseLong(receiverIdParam);
            List<Donor> donors = donorService.getAvailableCompatibleDonors(receiverId);

            if (donors.isEmpty()) {
                logger.info("No compatible donors found for receiverId={}", receiverId);
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.getWriter().write("{\"message\":\"No compatible donors found.\"}");
                return;
            }

            logger.info("Found {} compatible donors for receiverId={}", donors.size(), receiverId);
            StringBuilder json = new StringBuilder("[");
            for (int i = 0; i < donors.size(); i++) {
                Donor d = donors.get(i);
                json.append("{")
                        .append("\"id\":").append(d.getId()).append(",")
                        .append("\"cin\":\"").append(escapeJson(d.getCin())).append("\",")
                        .append("\"nom\":\"").append(escapeJson(d.getNom())).append("\",")
                        .append("\"prenom\":\"").append(escapeJson(d.getPrenom())).append("\",")
                        .append("\"bloodGroup\":\"").append(escapeJson(d.getBloodGroup().name())).append("\",")
                        .append("\"status\":\"").append(escapeJson(d.getStatus().name())).append("\"")
                        .append("}");
                if (i < donors.size() - 1) json.append(",");
            }
            json.append("]");

            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write(json.toString());

        } catch (NumberFormatException e) {
            logger.error("Invalid receiverId format: {}", receiverIdParam, e);
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\":\"Invalid receiver ID format.\"}");
        } catch (IllegalArgumentException e) {
            logger.error("Error processing donor listing for receiverId={}: {}", receiverIdParam, e.getMessage(), e);
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\":\"" + escapeJson(e.getMessage()) + "\"}");
        } catch (Exception e) {
            logger.error("Unexpected error listing donors for receiverId={}", receiverIdParam, e);
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"error\":\"Unexpected error: " + escapeJson(e.getMessage()) + "\"}");
        }
    }

    public void store(HttpServletRequest req, HttpServletResponse resp, DonationService donationService)
            throws IOException {

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try {
            Long receiverId = Long.parseLong(req.getParameter("receiverId"));
            Long donorId = Long.parseLong(req.getParameter("donorId"));
            double volume = Double.parseDouble(req.getParameter("volume"));

            CreateDonationRequest request = new CreateDonationRequest(donorId, receiverId, volume);
            donationService.linkReceiverToDonor(request);

            logger.info("Created donation: donorId={}, receiverId={}, volume={}", donorId, receiverId, volume);

            String json = """
            {
                "success": true,
                "message": "Donation created and donor linked successfully!"
            }
            """;
            resp.getWriter().write(json);

        } catch (EntityNotFoundException | IllegalStateException | IllegalArgumentException e) {
            logger.warn("Failed to create donation: {}", e.getMessage(), e);
            String json = String.format("""
            {
                "success": false,
                "errors": ["%s"]
            }
            """, e.getMessage().replace("\"", "\\\""));
            resp.getWriter().write(json);
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } catch (Exception e) {
            logger.error("Unexpected error creating donation", e);
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"success\":false,\"errors\":[\"Unexpected error: " + escapeJson(e.getMessage()) + "\"]}");
        }
    }

    private String escapeJson(String value) {
        if (value == null) return "";
        return value
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }
}

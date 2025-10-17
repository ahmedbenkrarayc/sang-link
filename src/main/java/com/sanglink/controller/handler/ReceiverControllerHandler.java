package com.sanglink.controller.handler;

import com.sanglink.dto.request.CreateReceiverRequest;
import com.sanglink.entity.enums.BloodGroup;
import com.sanglink.entity.enums.Gender;
import com.sanglink.entity.enums.Need;
import com.sanglink.service.ReceiverService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class ReceiverControllerHandler {
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
}

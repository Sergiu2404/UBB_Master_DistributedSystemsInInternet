package com.example.weather_preferences_app.servlets.country;

import com.example.weather_preferences_app.entities.Country;
import com.example.weather_preferences_app.services.Service;
import com.google.gson.Gson;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBTransactionRolledbackException;
import jakarta.persistence.PersistenceException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;


@WebServlet("/countries")
public class CountriesServlet extends HttpServlet {
    @EJB
    private Service service;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println("GET /countries called");
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        String countryIdParam = request.getParameter("countryId");

        if (countryIdParam != null) {
            try {
                Long id = Long.parseLong(countryIdParam);
                Country country = service.getCountryById(id);

                if (country != null) {
                    out.print(new Gson().toJson(country));
                } else {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "Country not found");
                }
            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid country ID");
            }
        } else {
            List<Country> countries = service.getAllCountries();
            out.print(new Gson().toJson(countries));
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        String name = request.getParameter("name");
        String region = request.getParameter("region");

        if (name == null || name.isBlank() || region == null || region.isBlank()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print(new Gson().toJson(Map.of("error", "Name and region are required")));
            return;
        }

        Country country = new Country(name, region);

        try {
            boolean success = service.saveCountry(country);

            if (success) {
                response.setStatus(HttpServletResponse.SC_CREATED);
                out.print(new Gson().toJson(Map.of(
                        "message", "Country created successfully",
                        "country", country
                )));
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print(new Gson().toJson(Map.of(
                        "error", "Country already exists or invalid data"
                )));
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print(new Gson().toJson(Map.of(
                    "error", "Unexpected server error: " + e.getMessage()
            )));
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        String idParam = request.getParameter("countryId");
        String name = request.getParameter("name");
        String region = request.getParameter("region");

        if (idParam == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print(new Gson().toJson(Map.of("error", "Missing country ID")));
            return;
        }

        try {
            Long id = Long.parseLong(idParam);
            Country existing = service.getCountryById(id);

            if (existing == null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                out.print(new Gson().toJson(Map.of("error", "Country not found")));
                return;
            }

            boolean found = service.existsCountryByName(name);

            if (!found) {
                this.service.updateCountry(existing, name, region);
                response.setStatus(HttpServletResponse.SC_OK);
                out.print(new Gson().toJson(Map.of(
                        "message", "Country updated successfully",
                        "country", existing
                )));
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print(new Gson().toJson(Map.of("error", "Update failed, possibly duplicate name or invalid data")));
            }
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print(new Gson().toJson(Map.of("error", "Invalid country ID")));
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print(new Gson().toJson(Map.of("error", "Unexpected server error: " + e.getMessage())));
        }

    }


    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String idParam = request.getParameter("countryId");
        System.out.println("DELETE /countries" + idParam);


        if (idParam == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing country ID");
            return;
        }

        try {
            Long id = Long.parseLong(idParam);
            Country existing = service.getCountryById(id);

            if (existing == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Country not found");
                return;
            }

            boolean success = service.removeCountry(existing.getId());

            if (success) {
                response.setStatus(HttpServletResponse.SC_NO_CONTENT); // 204 — no content
            } else {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to delete country");
            }

        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid country ID");
        }
    }
}

package com.example.weather_preferences_app.servlets.preference;

import com.example.weather_preferences_app.entities.Preference;
import com.example.weather_preferences_app.services.Service;
import com.google.gson.Gson;
import jakarta.ejb.EJB;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

@WebServlet(name="PreferencesServlet", urlPatterns = {"/preferences"})
public class PreferencesServlet extends HttpServlet {
    @EJB
    private Service service;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        String preferenceIdParam = request.getParameter("preferenceId");
        String locationIdParam = request.getParameter("locationId");

        try{
            if(preferenceIdParam != null){
                Long preferenceId = Long.parseLong(preferenceIdParam);
                Preference preference = this.service.getPreferenceById(preferenceId);
                if(preference != null){
                    out.print(new Gson().toJson(preference));
                } else {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "Preference not found with id: " + preferenceIdParam);
                }
            }
            else if(locationIdParam != null){
                Long locationId = Long.parseLong(locationIdParam);
                List<Preference> preferences = this.service.getPreferencesByLocationId(locationId);
                out.print(new Gson().toJson(preferences));

            } else {
                List<Preference> preferences = this.service.getAllPreferences();
                out.print(new Gson().toJson(preferences));
            }
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid ID parameter: " + locationIdParam + ", prefId: " + preferenceIdParam);
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error fetching preferences");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        String description = request.getParameter("description");
        String minTempParam = request.getParameter("minTemp");
        String maxTempParam = request.getParameter("maxTemp");
        String locationIdParam = request.getParameter("locationId");

        if (description == null || description.isBlank() || locationIdParam == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print(new Gson().toJson(Map.of("error", "Missing required parameters")));
            return;
        }

        try {
            double minTemp = Double.parseDouble(minTempParam);
            double maxTemp = Double.parseDouble(maxTempParam);
            if(minTemp < -90 || minTemp > 90 || maxTemp < -90 || maxTemp > 90 || minTemp > maxTemp){
                throw new IllegalArgumentException("Invalid temperature values");
            }

            Long locationId = Long.parseLong(locationIdParam);

            Preference preference = new Preference(
                    description, minTemp, maxTemp, locationId
            );

            boolean success = this.service.savePreference(preference);

            if (success) {
                response.setStatus(HttpServletResponse.SC_CREATED);
                out.print(new Gson().toJson(preference));
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print(new Gson().toJson(Map.of("error", "Failed to save preference")));
            }
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print(new Gson().toJson(Map.of("error", "Invalid location ID")));
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print(new Gson().toJson(Map.of("error", "Error creating peference: " + e.getMessage())));
        }
    }


    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        String preferenceIdParam = request.getParameter("preferenceId");
        String description = request.getParameter("description");
        String minTempParam = request.getParameter("minTemp");
        String maxTempParam = request.getParameter("maxTemp");

        if (preferenceIdParam == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print(new Gson().toJson(Map.of("error", "Preference ID required")));
            return;
        }

        try {
            double minTemp = Double.parseDouble(minTempParam);
            double maxTemp = Double.parseDouble(maxTempParam);
            if(minTemp < -90 || minTemp > 90 || maxTemp < -90 || maxTemp > 90 || minTemp > maxTemp){
                throw new IllegalArgumentException("Invalid temperature values");
            }

            Long prefernceId = Long.parseLong(preferenceIdParam);
            Preference preference = service.getPreferenceById(prefernceId);
            if (preference == null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                out.print(new Gson().toJson(Map.of("error", "Preference not found with id: " + preferenceIdParam)));
                return;
            }

            if (description != null && !description.isBlank()) {
                preference.setDescription(description);
                preference.setMinTemperature(minTemp);
                preference.setMaxTemperature(maxTemp);
            }

            boolean success = service.updatePreference(preference);
            if (success) {
                out.print(new Gson().toJson(preference));
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print(new Gson().toJson(Map.of("error", "Failed to update preference")));
            }
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print(new Gson().toJson(Map.of("error", "Invalid preference ID: " + preferenceIdParam)));
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print(new Gson().toJson(Map.of("error", "Error updating preference")));
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");

        String preferenceIdParam = request.getParameter("preferenceId");
        if (preferenceIdParam == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Preference ID required");
            return;
        }

        try {
            Long preferenceId = Long.parseLong(preferenceIdParam);
            Preference preference = service.getPreferenceById(preferenceId);
            if (preference == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Preference not found");
                return;
            }

            boolean success = service.removePreference(preference.getId());
            if (success) {
                response.setStatus(HttpServletResponse.SC_NO_CONTENT);
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Failed to delete preference");
            }
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid preference ID: " + preferenceIdParam);
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error deleting preference");
        }
    }
}

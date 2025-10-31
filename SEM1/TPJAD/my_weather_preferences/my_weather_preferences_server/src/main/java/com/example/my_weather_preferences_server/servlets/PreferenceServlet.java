package com.example.my_weather_preferences_server.servlets;

import com.example.my_weather_preferences_server.entities.Preference;
import com.example.my_weather_preferences_server.services.PostgresService;
import jakarta.ejb.EJB;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/preferences")
public class PreferenceServlet extends HttpServlet {
    @EJB
    private PostgresService preferenceService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Preference s = new Preference();
        s.setMinTemperature(5);
        s.setMaxTemperature(20);
        preferenceService.create(s);
        resp.getWriter().println("Preference saved in Postgres!");
    }
}

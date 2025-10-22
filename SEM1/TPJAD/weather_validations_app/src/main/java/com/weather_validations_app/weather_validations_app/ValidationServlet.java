package com.weather_validations_app.weather_validations_app;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name="validation", urlPatterns = {"/validation"})
public class ValidationServlet extends HttpServlet {
    private String location;
    private int maxTemp;
    private int minTemp;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        location = request.getParameter("location");
        maxTemp = Integer.parseInt(request.getParameter("max_temp"));
        minTemp = Integer.parseInt(request.getParameter("max_temp"));
        //response.getWriter().println("validation Servlet");
    }
}

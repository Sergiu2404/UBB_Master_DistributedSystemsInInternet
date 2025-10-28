package com.example.weather_app;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "forecasts", urlPatterns = {"/forecasts"})
public class ForecastsServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String location = request.getParameter("location");
            String minParam = request.getParameter("min_temp");
            String maxParam = request.getParameter("max_temp");
            Double minTemp = null;
            Double maxTemp = null;

            if (location == null || location.isEmpty()) {
                request.setAttribute("error", "Please enter a location.");
                RequestDispatcher dispatcher = request.getRequestDispatcher("index.jsp");
                dispatcher.forward(request, response);
                return;
            }
            if (minParam == null || minParam.trim().isEmpty()) {
                minTemp = -90.0;
            } else {
                minTemp = Double.parseDouble(minParam);
            }
            if (maxParam == null || maxParam.trim().isEmpty()) {
                maxTemp = 90.0;
            } else {
                maxTemp = Double.parseDouble(maxParam);
            }
            if (minTemp > maxTemp) {
                request.setAttribute("error", "Max temp must be greater or equal to min temp.");
                RequestDispatcher dispatcher = request.getRequestDispatcher("index.jsp");
                dispatcher.forward(request, response);
                return;
            }
            System.out.println("Forecasts servlet: " + location + ": " + minParam + ", " + maxParam);


            request.setAttribute("location", location);
            request.setAttribute("min_temp", minTemp);
            request.setAttribute("max_temp", maxTemp);
            request.getRequestDispatcher("/weather_service").forward(request, response);
        } catch (Exception e){
            request.setAttribute("error", e.getMessage());
            RequestDispatcher dispatcher = request.getRequestDispatcher("index.jsp");
            dispatcher.forward(request, response);
            return;
        }
    }
}

package com.example.weather_preferences_app.servlets.country;

import com.example.weather_preferences_app.entities.Country;
import com.example.weather_preferences_app.services.Service;
import com.google.gson.Gson;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;


@WebServlet("/countries")
public class CountriesServlet extends HttpServlet {
    @EJB
    private Service service;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        System.out.println("entered get countries\n\n\n");
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        String countryIdParam = request.getParameter("countryId");

        if(countryIdParam != null){
            Long id = Long.parseLong(countryIdParam);
            Country country = service.getCountryById(id);
            out.print(new Gson().toJson(country));
        } else {
            List<Country> countries = service.getAllCountries();
            System.out.println(new Gson().toJson(countries));
            out.print(new Gson().toJson(countries));
        }
//        if(countryIdParam != null){
//            Long countryId = Long.parseLong(countryIdParam);
//            Country country = this.service.getCountryById(countryId);
//            request.setAttribute("country", country);
//            request.getRequestDispatcher("countries.jsp").forward(request, response);
//        } else {
//            List<Country> countries = this.service.getAllCountries();
//            request.setAttribute("countries", countries);
//            request.getRequestDispatcher("/country/countries.jsp").forward(request, response);
//        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println("entered post countries");
        String name = request.getParameter("name");
        String region = request.getParameter("region");

        if (name != null && region != null && !name.isEmpty() && !region.isEmpty()) {
            Country country = new Country(name, region);
            service.saveCountry(country);
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Name and Region are required");
        }
    }
}

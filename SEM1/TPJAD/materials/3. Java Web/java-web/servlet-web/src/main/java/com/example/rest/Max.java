package com.example.rest;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/Servlet")
public class Max extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int nr1 = 0, nr2 = 0;
        try {
            nr1 = Integer.parseInt(request.getParameter("nr1"));
        } catch (Exception ignored) {
        }
        try {
            nr2 = Integer.parseInt(request.getParameter("nr2"));
        } catch (Exception ignored) {
        }
        PrintWriter out = response.getWriter();
        response.setContentType("text/plain");
        out.println("Maxim(" + nr1 + "," + nr2 + ") = " + Math.max(nr1, nr2));
        out.close();

    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}

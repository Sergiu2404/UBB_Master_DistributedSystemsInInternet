package com.example.rest;

import java.io.*;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/Add")
public class Aduna extends HttpServlet {
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response) throws ServletException, IOException {
        int unu = 0, doi = 0;
        try {
            unu = Integer.parseInt(request.getParameter("unu"));
        } catch (Exception e) {
            unu = 0;
        }
        try {
            doi = Integer.parseInt(request.getParameter("doi"));
        } catch (Exception e) {
            doi = 0;
        }
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<html><head><title>Suma</title></head><body>");
        out.println("<form method=\"POST\" action=\"Add\">");
        out.println("Nr1:<input type=\"text\" name=\"unu\" value=\"" + unu + "\">");
        out.println("Nr2:<input type=\"text\" name=\"doi\" value=\"" + doi + "\">");
        out.println("Suma:<input type=\"text\" disabled=\"disabled\" value=\"" + (unu + doi) + "\">");
        out.println("<input type=\"submit\" value=\"Aduna\">");
        out.println("</form></body></html>");
        out.close();
    }

    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}

package com.example.rest;

import java.io.*;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/LM")
public class LitereMari extends HttpServlet {
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response) throws ServletException, IOException {
        String sir = request.getParameter("sir");
        if (sir == null) sir = "";
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<HTML><HEAD><TITLE>Litere mari</TITLE></HEAD>");
        out.println("<BODY><H3>" + sir.toUpperCase() + "</h3></BODY></HTML>");
        out.close();
    }
}

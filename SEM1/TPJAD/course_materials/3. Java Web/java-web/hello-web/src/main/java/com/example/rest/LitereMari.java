package com.example.rest;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.*;

public class LitereMari extends HttpServlet {
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response) throws ServletException, IOException {
        String sir = request.getParameter("sir");
        if (sir == null) {
            sir = "";
        }
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<HTML><HEAD><TITLE>Litere mari</TITLE></HEAD>");
        out.println("<BODY><H3>" + sir.toUpperCase() + sir.toLowerCase() + "</h3></BODY></HTML>");
        out.close();
    }
}

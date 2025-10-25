package com.example.rest.embedded;

import java.io.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ClientHtml extends HttpServlet {
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Suma sau conversie</title>");
        out.println("</head>");
        out.println("<body>");
        out.println("<form method=\"GET\" action=\"Add\">");
        out.println("Nr1:<input type=\"text\" name=\"unu\">");
        out.println("Nr2:<input type=\"text\" name=\"doi\">");
        out.println("<input type=\"submit\" value=\"Aduna GET\" />");
        out.println("</form>");
        out.println("<form method=\"POST\" action=\"Add\">");
        out.println("Nr1:<input type=\"text\" name=\"unu\">");
        out.println("Nr2:<input type=\"text\" name=\"doi\">");
        out.println("<input type=\"submit\" value=\"Aduna POST\" />");
        out.println("</form>");
        out.println("<form method=\"POST\" action=\"LM\">");
        out.println("Sirul de convertit: <input type=\"text\" name=\"sir\">");
        out.println("<input type=\"submit\" value=\"Conversie POST\">");
        out.println("</form>");
        out.println("</body>");
        out.println("</html>");
        out.close();
    }
}

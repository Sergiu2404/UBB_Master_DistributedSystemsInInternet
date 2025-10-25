package com.example.maventraining.hello.web;

import com.example.maventraining.hello.util.HelloWorld;
import jakarta.servlet.*;
import jakarta.servlet.http.*;

import java.io.IOException;


public class HelloServlet extends HttpServlet {
    private HelloWorld helloWorld = new HelloWorld();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        String name = pathInfo.substring(1, pathInfo.length());
        res.getWriter().println(helloWorld.hello(name));
    }
}

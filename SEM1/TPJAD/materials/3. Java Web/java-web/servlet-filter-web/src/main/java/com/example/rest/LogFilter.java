package com.example.rest;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;

import java.io.*;


@WebFilter("/*")
public class LogFilter implements Filter {
    private FilterConfig fConfig = null;

    public LogFilter() {
    }

    public void init(FilterConfig fConfig) throws ServletException {
        this.fConfig = fConfig;
        this.fConfig.getServletContext().setAttribute("numar", 0);
    }

    public void destroy() {
        this.fConfig = null;
    }

    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        if (fConfig == null) {
            return;
        }
        Long t1 = System.currentTimeMillis();
        Integer counter = (Integer) fConfig.getServletContext().getAttribute("numar");
        counter++;
        String ipAddress = request.getRemoteAddr();
        System.out.println("LogFilter#doFilter: " + ipAddress + " counter: " + counter);
        this.fConfig.getServletContext().setAttribute("numar", counter);
        chain.doFilter(request, response);
        Long t2 = System.currentTimeMillis();
        System.out.println("LogFilter#doFilter: " + (t2 - t1) + " millis.");
    }
}

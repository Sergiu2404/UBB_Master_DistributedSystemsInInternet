package com.stock_recommendation.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;

@WebServlet(name = "SearchServlet", urlPatterns = {"/api/search"})
public class SearchServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/search.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String q = req.getParameter("q"); // single ticker only

        JSONObject payload = new JSONObject();
        payload.put("q", q == null ? "" : q.trim());

        String scheme = req.getScheme();
        String serverName = req.getServerName();
        int serverPort = req.getServerPort();
        String contextPath = req.getContextPath();

        String baseUrl = scheme + "://" + serverName + ":" + serverPort + contextPath;
        URL url = new URL(baseUrl + "/api/filter");

        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setConnectTimeout(5000);
        con.setReadTimeout(10000);
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        con.setDoOutput(true);

        try (OutputStream os = con.getOutputStream()) {
            os.write(payload.toString().getBytes("UTF-8"));
        }

        try (InputStream is = (con.getResponseCode() / 100 == 2)
                ? con.getInputStream() : con.getErrorStream()) {
            String json = (is == null) ? "[]" : new String(is.readAllBytes(), "UTF-8");
            req.setAttribute("resultsJson", json);
        }

        req.getRequestDispatcher("/WEB-INF/views/search.jsp").forward(req, resp);
    }
}

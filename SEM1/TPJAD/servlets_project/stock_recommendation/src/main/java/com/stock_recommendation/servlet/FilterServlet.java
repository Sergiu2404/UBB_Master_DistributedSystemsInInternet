package com.stock_recommendation.servlet;

import com.stock_recommendation.entity.StockInfo;
import com.stock_recommendation.service.SearchService;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

@WebServlet(name = "FilterServlet", urlPatterns = {"/api/filter"})
public class FilterServlet extends HttpServlet {
    private SearchService searchService;

    @Override
    public void init() throws ServletException {
        try {
            log("FilterServlet initializing...");
            this.searchService = new SearchService(getServletContext());
            log("FilterServlet initialized successfully");
        } catch (Exception e) {
            log("ERROR: Failed to initialize FilterServlet: " + e.getMessage());
            e.printStackTrace();
            throw new ServletException("Failed to initialize SearchService", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        log("FilterServlet doPost called at " + new Date());

        resp.setContentType("application/json;charset=UTF-8");
        resp.setHeader("Access-Control-Allow-Origin", "*");
        resp.setHeader("Access-Control-Allow-Methods", "POST");
        resp.setHeader("Access-Control-Allow-Headers", "Content-Type");

        try {
            String body = new String(req.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
            log("Received request body: " + body);

            if (body.trim().isEmpty()) {
                log("ERROR: Empty request body");
                sendErrorResponse(resp, "Empty request body");
                return;
            }

            JSONObject in;
            try {
                in = new JSONObject(body);
            } catch (Exception e) {
                log("ERROR: Invalid JSON in request body: " + e.getMessage());
                sendErrorResponse(resp, "Invalid JSON format");
                return;
            }

            // Extract search query
            String q = in.optString("q", "").trim();
            log("Extracted query: '" + q + "'");

            if (q.isEmpty()) {
                log("ERROR: Empty query parameter");
                sendErrorResponse(resp, "Query parameter 'q' is required");
                return;
            }

            // Perform search
            log("Calling searchService.search with query: " + q);
            List<StockInfo> results = searchService.search(q);
            log("Search completed. Results count: " + results.size());

            // Convert to JSON
            JSONArray out = new JSONArray();
            for (StockInfo si : results) {
                log("Processing result: " + si.getSymbol());

                JSONObject o = new JSONObject();
                o.put("symbol", si.getSymbol());
                o.put("name", si.getName());

                if (si.getPrice() != null) {
                    o.put("price", si.getPrice());
                }
                if (si.getPreviousClose() != null) {
                    o.put("previousClose", si.getPreviousClose());
                }
                if (si.getChange() != null) {
                    o.put("change", si.getChange());
                }
                if (si.getChangePercent() != null) {
                    o.put("changePercent", si.getChangePercent());
                }
                if (si.getVolume() != null) {
                    o.put("volume", si.getVolume());
                }
                if (si.getMarketCap() != null) {
                    o.put("marketCap", si.getMarketCap());
                }
                if (si.getCurrency() != null) {
                    o.put("currency", si.getCurrency());
                }
                if (si.getAsOf() != null) {
                    o.put("asOf", si.getAsOf().getTime());
                }

                out.put(o);
            }

            // Send response
            String jsonResponse = out.toString();
            log("Sending response: " + jsonResponse.substring(0, Math.min(200, jsonResponse.length())));

            try (PrintWriter w = resp.getWriter()) {
                w.write(jsonResponse);
            }

            log("Response sent successfully");

        } catch (Exception e) {
            log("ERROR in FilterServlet: " + e.getClass().getSimpleName() + " - " + e.getMessage());
            e.printStackTrace();
            sendErrorResponse(resp, "Internal server error: " + e.getMessage());
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json;charset=UTF-8");

        JSONObject status = new JSONObject();
        status.put("status", "OK");
        status.put("service", "FilterServlet");
        status.put("timestamp", new Date().getTime());
        status.put("message", "Service is running");

        if (searchService != null) {
            status.put("cacheSize", searchService.getCacheSize());
        }

        try (PrintWriter w = resp.getWriter()) {
            w.write(status.toString());
        }
    }

    private void sendErrorResponse(HttpServletResponse resp, String message) throws IOException {
        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);

        JSONObject error = new JSONObject();
        error.put("error", message);
        error.put("timestamp", new Date().getTime());

        try (PrintWriter w = resp.getWriter()) {
            w.write(error.toString());
        }
    }

    @Override
    public void destroy() {
        log("FilterServlet is being destroyed");
        super.destroy();
    }
}
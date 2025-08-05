package com.stock_recommendation.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stock_recommendation.entity.PortfolioStock;
import com.stock_recommendation.entity.Stock;
import com.stock_recommendation.repository.PortfolioRepository;
import com.stock_recommendation.repository.StockRepository;
import com.stock_recommendation.service.PortfolioAnalyzerService;
import com.stock_recommendation.service.PortfolioService;
import com.stock_recommendation.service.StockService;
import com.stock_recommendation.utils.CSVLoader;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@WebServlet("/recommendation")
public class RecommendationServlet extends HttpServlet {
    private PortfolioAnalyzerService portfolioAnalyzerService;

    @Override
    public void init(){
        CSVLoader csvLoader = new CSVLoader();
        StockRepository stockRepository = new StockRepository(csvLoader);
        PortfolioRepository portfolioRepository = new PortfolioRepository();

        StockService stockService = new StockService(stockRepository);
        PortfolioService portfolioService = new PortfolioService(portfolioRepository, stockService);

        portfolioAnalyzerService = new PortfolioAnalyzerService(stockService, portfolioService);
    }

    private List<PortfolioStock> fetchPortfolioFromOtherServlet() throws IOException {
        URL url = new URL("http://localhost:8080/portfolio?action=api");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");

        if (conn.getResponseCode() != 200) {
            throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
        }

        try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
            StringBuilder jsonBuilder = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                jsonBuilder.append(line);
            }
            conn.disconnect();

            ObjectMapper mapper = new ObjectMapper();
            return Arrays.asList(mapper.readValue(jsonBuilder.toString(), PortfolioStock[].class));
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<PortfolioStock> externalPortfolio = fetchPortfolioFromOtherServlet();
        portfolioAnalyzerService.setExternalPortfolio(externalPortfolio); // 👈 inject external portfolio

        String mode = request.getParameter("mode");

        switch (mode) {
            case "missingIndustries":
                Map<String, List<Stock>> missingIndustries = portfolioAnalyzerService.recommendMissingIndustriesStocks();
                request.setAttribute("recommendations", missingIndustries);
                break;

            case "balancedQuantity":
                double maxPercentage = Double.parseDouble(request.getParameter("maxIndustryPercentage"));
                Map<String, List<Stock>> balance = portfolioAnalyzerService.recommendToBalanceByQuantity(maxPercentage);
                request.setAttribute("recommendations", balance);
                break;

            case "overconcentratedStocks":
                int maxQuantity = Integer.parseInt(request.getParameter("maxQuantity"));
                List<PortfolioStock> overconcentratedStocks = portfolioAnalyzerService.getOverconcentratedStocks(maxQuantity);
                request.setAttribute("overconcentratedStocks", overconcentratedStocks);
                break;

            case "balancedPortfolio":
                double maxIndustryPercentage = Double.parseDouble(request.getParameter("maxIndustryPercentage"));
                int maxStockQuantity = Integer.parseInt(request.getParameter("maxStockQuantity"));
                int suggestedQuantity = Integer.parseInt(request.getParameter("suggestedStockQuantity"));

                List<PortfolioStock> balanced = portfolioAnalyzerService.getRecommendedBalancedPortfolio(maxIndustryPercentage, maxStockQuantity, suggestedQuantity);
                request.setAttribute("balancedPortfolio", balanced);
                break;

            case "autoBalanced":
                int totalQuantity = Integer.parseInt(request.getParameter("totalTargetQuantity"));
                int perIndustry = Integer.parseInt(request.getParameter("stocksPerIndustry"));

                List<PortfolioStock> autoBalanced = portfolioAnalyzerService.buildBalancedPortfolioEvenly(totalQuantity, perIndustry);
                request.setAttribute("autoBalanced", autoBalanced);
                break;

            default:
                request.setAttribute("error", "Unknown recommendation mode");
        }

        request.getRequestDispatcher("/WEB-INF/views/recommendation.jsp").forward(request, response);
    }
}

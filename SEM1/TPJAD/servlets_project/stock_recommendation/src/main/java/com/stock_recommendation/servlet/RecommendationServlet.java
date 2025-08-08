//package com.stock_recommendation.servlet;
//
//import com.stock_recommendation.entity.PortfolioStock;
//import com.stock_recommendation.entity.Stock;
//import com.stock_recommendation.repository.PortfolioRepository;
//import com.stock_recommendation.service.PortfolioAnalyzerService;
//import com.stock_recommendation.service.PortfolioService;
//import com.stock_recommendation.utils.CSVLoader;
//
//import javax.servlet.ServletException;
//import javax.servlet.annotation.WebServlet;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.servlet.http.HttpSession;
//import java.io.IOException;
//import java.util.List;
//import java.util.Map;
////
////@WebServlet("/recommendation")
////public class RecommendationServlet extends HttpServlet {
////    private PortfolioAnalyzerService portfolioAnalyzerService;
////
////    @Override
////    public void init(){
////        CSVLoader csvLoader = new CSVLoader();
////        StockRepository stockRepository = new StockRepository(csvLoader);
////        PortfolioRepository portfolioRepository = new PortfolioRepository();
////
////        StockService stockService = new StockService(stockRepository);
////        PortfolioService portfolioService = new PortfolioService(portfolioRepository, stockService);
////
////        portfolioAnalyzerService = new PortfolioAnalyzerService(stockService, portfolioService);
////    }
////
////    private List<PortfolioStock> fetchPortfolioFromOtherServlet() throws IOException {
////        URL url = new URL("http://localhost:8080/portfolio?action=api");
////        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
////        conn.setRequestMethod("GET");
////        conn.setRequestProperty("Accept", "application/json");
////
////        if (conn.getResponseCode() != 200) {
////            throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
////        }
////
////        try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
////            StringBuilder jsonBuilder = new StringBuilder();
////            String line;
////            while ((line = br.readLine()) != null) {
////                jsonBuilder.append(line);
////            }
////            conn.disconnect();
////
////            ObjectMapper mapper = new ObjectMapper();
////            return Arrays.asList(mapper.readValue(jsonBuilder.toString(), PortfolioStock[].class));
////        }
////    }
////
////    @Override
////    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
////        List<PortfolioStock> externalPortfolio = fetchPortfolioFromOtherServlet();
////        portfolioAnalyzerService.setExternalPortfolio(externalPortfolio); // 👈 inject external portfolio
////
////        String mode = request.getParameter("mode");
////
////        switch (mode) {
////            case "missingIndustries":
////                Map<String, List<Stock>> missingIndustries = portfolioAnalyzerService.recommendMissingIndustriesStocks();
////                request.setAttribute("recommendations", missingIndustries);
////                break;
////
////            case "balancedQuantity":
////                double maxPercentage = Double.parseDouble(request.getParameter("maxIndustryPercentage"));
////                Map<String, List<Stock>> balance = portfolioAnalyzerService.recommendToBalanceByQuantity(maxPercentage);
////                request.setAttribute("recommendations", balance);
////                break;
////
////            case "overconcentratedStocks":
////                int maxQuantity = Integer.parseInt(request.getParameter("maxQuantity"));
////                List<PortfolioStock> overconcentratedStocks = portfolioAnalyzerService.getOverconcentratedStocks(maxQuantity);
////                request.setAttribute("overconcentratedStocks", overconcentratedStocks);
////                break;
////
////            case "balancedPortfolio":
////                double maxIndustryPercentage = Double.parseDouble(request.getParameter("maxIndustryPercentage"));
////                int maxStockQuantity = Integer.parseInt(request.getParameter("maxStockQuantity"));
////                int suggestedQuantity = Integer.parseInt(request.getParameter("suggestedStockQuantity"));
////
////                List<PortfolioStock> balanced = portfolioAnalyzerService.getRecommendedBalancedPortfolio(maxIndustryPercentage, maxStockQuantity, suggestedQuantity);
////                request.setAttribute("balancedPortfolio", balanced);
////                break;
////
////            case "autoBalanced":
////                int totalQuantity = Integer.parseInt(request.getParameter("totalTargetQuantity"));
////                int perIndustry = Integer.parseInt(request.getParameter("stocksPerIndustry"));
////
////                List<PortfolioStock> autoBalanced = portfolioAnalyzerService.buildBalancedPortfolioEvenly(totalQuantity, perIndustry);
////                request.setAttribute("autoBalanced", autoBalanced);
////                break;
////
////            default:
////                request.setAttribute("error", "Unknown recommendation mode");
////        }
////
////        request.getRequestDispatcher("/WEB-INF/views/recommendation.jsp").forward(request, response);
////    }
////}
//
//@WebServlet("/recommendation")
//public class RecommendationServlet extends HttpServlet {
//    private StockService stockService;
//
//    @Override
//    public void init(){
//        CSVLoader csvLoader = new CSVLoader();
//        StockRepository stockRepository = new StockRepository(csvLoader);
//        stockService = new StockService(stockRepository);
//    }
//
//    @Override
//    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        HttpSession session = request.getSession();
//
//        // Get the same portfolio repository that PortfolioServlet uses
//        PortfolioRepository portfolioRepository = (PortfolioRepository) session.getAttribute("portfolioRepository");
//        if (portfolioRepository == null) {
//            request.setAttribute("error", "No portfolio found. Please add some stocks first.");
//            request.getRequestDispatcher("/WEB-INF/views/recommendation.jsp").forward(request, response);
//            return;
//        }
//
//        // Create services using session portfolio repository
//        PortfolioService portfolioService = new PortfolioService(portfolioRepository, stockService);
//        PortfolioAnalyzerService portfolioAnalyzerService = new PortfolioAnalyzerService(stockService, portfolioService);
//
//        String mode = request.getParameter("mode");
//        if (mode == null) {
//            mode = "balancedPortfolio"; // Default to the main feature
//        }
//
//        try {
//            // Check if portfolio has stocks
//            List<PortfolioStock> currentPortfolio = portfolioService.getPortfolio();
//            if (currentPortfolio == null || currentPortfolio.isEmpty()) {
//                request.setAttribute("error", "Your portfolio is empty. Please add some stocks first.");
//                request.getRequestDispatcher("/WEB-INF/views/recommendation.jsp").forward(request, response);
//                return;
//            }
//
//            // Set current portfolio for debugging
//            request.setAttribute("currentPortfolio", currentPortfolio);
//
//            if ("balancedPortfolio".equals(mode)) {
//                // Main feature: balanced portfolio considering both quantity and industry
//                double maxIndustryPercentage = 30.0; // Default
//                int maxStockQuantity = 100; // Default
//                int suggestedQuantity = 20; // Default
//
//                // Parse parameters if provided
//                String maxIndustryParam = request.getParameter("maxIndustryPercentage");
//                if (maxIndustryParam != null && !maxIndustryParam.isEmpty()) {
//                    maxIndustryPercentage = Double.parseDouble(maxIndustryParam);
//                }
//
//                String maxStockParam = request.getParameter("maxStockQuantity");
//                if (maxStockParam != null && !maxStockParam.isEmpty()) {
//                    maxStockQuantity = Integer.parseInt(maxStockParam);
//                }
//
//                String suggestedParam = request.getParameter("suggestedStockQuantity");
//                if (suggestedParam != null && !suggestedParam.isEmpty()) {
//                    suggestedQuantity = Integer.parseInt(suggestedParam);
//                }
//
//                List<PortfolioStock> balanced = portfolioAnalyzerService.getRecommendedBalancedPortfolio(
//                        maxIndustryPercentage, maxStockQuantity, suggestedQuantity);
//
//                request.setAttribute("balancedPortfolio", balanced);
//                request.setAttribute("analysisType", "Balanced Portfolio Analysis");
//
//            } else if ("missingIndustries".equals(mode)) {
//                Map<String, List<Stock>> missingIndustries = portfolioAnalyzerService.recommendMissingIndustriesStocks();
//                request.setAttribute("recommendations", missingIndustries);
//                request.setAttribute("analysisType", "Missing Industries Analysis");
//
//            } else if ("overconcentratedStocks".equals(mode)) {
//                int maxQuantity = 50; // Default
//                String maxQuantityParam = request.getParameter("maxQuantity");
//                if (maxQuantityParam != null && !maxQuantityParam.isEmpty()) {
//                    maxQuantity = Integer.parseInt(maxQuantityParam);
//                }
//
//                List<PortfolioStock> overconcentratedStocks = portfolioAnalyzerService.getOverconcentratedStocks(maxQuantity);
//                request.setAttribute("overconcentratedStocks", overconcentratedStocks);
//                request.setAttribute("analysisType", "Overconcentration Analysis");
//
//            } else {
//                request.setAttribute("error", "Unknown recommendation mode: " + mode);
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace(); // For debugging
//            request.setAttribute("error", "Error generating recommendations: " + e.getMessage());
//        }
//
//        request.getRequestDispatcher("/WEB-INF/views/recommendation.jsp").forward(request, response);
//    }
//}
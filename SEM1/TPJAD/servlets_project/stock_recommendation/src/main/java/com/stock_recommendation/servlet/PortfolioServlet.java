//package com.stock_recommendation.servlet;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.stock_recommendation.entity.PortfolioStock;
//import com.stock_recommendation.entity.Stock;
//import com.stock_recommendation.repository.PortfolioRepository;
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
//import java.util.NoSuchElementException;
//
//@WebServlet("/portfolio")
//public class PortfolioServlet extends HttpServlet {
//    private PortfolioService portfolioService;
//    private StockService stockService;
//
//    @Override
//    public void init(){
////        CSVLoader csvLoader = new CSVLoader();
////        StockRepository stockRepository = new StockRepository(csvLoader);
////        PortfolioRepository portfolioRepository = new PortfolioRepository();
////        stockService = new StockService(stockRepository);
////        portfolioService = new PortfolioService(portfolioRepository, stockService);
//    }
//
//    @Override
//    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        String action = request.getParameter("action");
//        HttpSession httpSession = request.getSession();
//
//        PortfolioRepository portfolioRepository = (PortfolioRepository) httpSession.getAttribute("portfolioRepository");
//        if (portfolioRepository == null) {
//            portfolioRepository = new PortfolioRepository();
//            httpSession.setAttribute("portfolioRepository", portfolioRepository);
//        }
//
//        if (stockService == null) {
//            CSVLoader csvLoader = new CSVLoader();
//            StockRepository stockRepository = new StockRepository(csvLoader);
//            stockService = new StockService(stockRepository);
//        }
//        portfolioService = new PortfolioService(portfolioRepository, stockService);
//
//        request.setAttribute("searched", false);
//
//        if ("api".equalsIgnoreCase(action)) {
//            response.setContentType("application/json");
//            List<PortfolioStock> portfolio = portfolioService.getPortfolio();
//            new ObjectMapper().writeValue(response.getWriter(), portfolio);
//            return;
//        }
//
//        if ("getStock".equalsIgnoreCase(action)) {
//            request.setAttribute("searched", true);
//
//            String ticker = request.getParameter("ticker");
//            if (ticker != null && !ticker.isEmpty()) {
//                try {
//                    Stock stock = stockService.getStockByTicker(ticker);
//                    request.setAttribute("stock", stock);
//                } catch (NoSuchElementException e) {
//                    request.setAttribute("stockNotFound", true);
//                }
//            } else {
//                request.setAttribute("stockNotFound", true);
//            }
//        }
//
//
//        List<PortfolioStock> portfolio = portfolioService.getPortfolio();
//        List<Stock> availableStocks = portfolioService.getAvailableStocksNotInPortfolio();
//
//        request.setAttribute("portfolio", portfolio);
//        request.setAttribute("availableStocks", availableStocks);
//
//        request.getRequestDispatcher("/WEB-INF/views/portfolio.jsp").forward(request, response);
//    }
//
//
//    @Override
//    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
//        HttpSession session = request.getSession();
//
//        PortfolioRepository portfolioRepository = (PortfolioRepository) session.getAttribute("portfolioRepository");
//        if (portfolioRepository == null) {
//            portfolioRepository = new PortfolioRepository();
//            session.setAttribute("portfolioRepository", portfolioRepository);
//        }
//
//        if (stockService == null) {
//            CSVLoader csvLoader = new CSVLoader();
//            StockRepository stockRepository = new StockRepository(csvLoader);
//            stockService = new StockService(stockRepository);
//        }
//
//        portfolioService = new PortfolioService(portfolioRepository, stockService);
//
//
//        String action = request.getParameter("action");
//        String ticker = request.getParameter("ticker");
//        int quantity = Integer.parseInt(request.getParameter("quantity"));
//
//        switch(action){
//            case "add":
//                portfolioService.addStock(ticker, quantity);
//                break;
//            case "update":
//                portfolioService.updateStock(ticker, quantity);
//                break;
//            case "delete":
//                portfolioService.removeStock(ticker);
//                break;
//        }
//
//        response.sendRedirect(request.getContextPath() + "/portfolio");
//    }
//}

package com.stock_recommendation.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stock_recommendation.entity.PortfolioStock;
import com.stock_recommendation.entity.Stock;
import com.stock_recommendation.repository.PortfolioRepository;
import com.stock_recommendation.repository.StockRepository;
import com.stock_recommendation.service.PortfolioService;
import com.stock_recommendation.service.StockService;
import com.stock_recommendation.utils.CSVLoader;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/portfolio")
public class PortfolioServlet extends HttpServlet {
    private PortfolioService portfolioService;
    private StockService stockService;

    @Override
    public void init(){
        CSVLoader csvLoader = new CSVLoader();
        StockRepository stockRepository = new StockRepository(csvLoader);
        PortfolioRepository portfolioRepository = new PortfolioRepository();
        stockService = new StockService(stockRepository);
        portfolioService = new PortfolioService(portfolioRepository, stockService);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("api".equalsIgnoreCase(action)) {
            response.setContentType("application/json");
            List<PortfolioStock> portfolio = portfolioService.getPortfolio();

            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(response.getWriter(), portfolio);
            return;
        }
        if ("getStock".equalsIgnoreCase(action)) {
            String ticker = request.getParameter("ticker");
            if (ticker != null && !ticker.isEmpty()) {
                Stock stock = stockService.getStockByTicker(ticker);
                if (stock != null) {
                    request.setAttribute("stock", stock);
                } else {
                    request.setAttribute("stockNotFound", true);
                }
            }
        }

        List<PortfolioStock> portfolio = portfolioService.getPortfolio();
        List<Stock> availableStocks = portfolioService.getAvailableStocksNotInPortfolio();

        request.setAttribute("portfolio", portfolio);
        request.setAttribute("availableStocks", availableStocks);
        request.getRequestDispatcher("/WEB-INF/views/portfolio.jsp").forward(request, response);
    }
//@Override
//protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
//    response.setContentType("text/plain");
//    response.getWriter().println("Servlet is working!");
//}


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String action = request.getParameter("action");
        String ticker = request.getParameter("ticker");
        int quantity = Integer.parseInt(request.getParameter("quantity"));

        switch(action){
            case "add":
                portfolioService.addStock(ticker, quantity);
                break;
            case "update":
                portfolioService.updateStock(ticker, quantity);
                break;
            case "delete":
                portfolioService.removeStock(ticker);
                break;
        }

        response.sendRedirect(request.getContextPath() + "/portfolio");
    }
}

package com.stock_recommendation.servlet;


import com.stock_recommendation.entity.Stock;
import com.stock_recommendation.repository.StockRepository;
import com.stock_recommendation.service.StockService;
import com.stock_recommendation.utils.CSVLoader;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/stock")
public class StockServlet extends HttpServlet {
    private StockService stockService;

    @Override
    public void init() {
        stockService = new StockService(new StockRepository(new CSVLoader()));
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.getWriter().write("StockServlet is working");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String ticker = request.getParameter("ticker");
        System.out.println("Received ticker: " + ticker);

        if (ticker != null && !ticker.trim().isEmpty()) {
            Stock stock = stockService.getStockByTicker(ticker);
//            if (stock != null) {
//                System.out.println("Found stock: " + stock.getTicker());
//                request.getSession().setAttribute("searchedStock", stock);
//            } else {
//                System.out.println("Stock NOT found for ticker: " + ticker);
//                request.getSession().setAttribute("searchError", "Stock not found");
//            }
            if (stock != null) {
                System.out.println("Found stock: " + stock.getTicker());
                request.getSession().setAttribute("searchedStock", stock);
                request.getSession().removeAttribute("searchError"); // ✅ clear old error
            } else {
                System.out.println("Stock NOT found for ticker: " + ticker);
                request.getSession().removeAttribute("searchedStock"); // ✅ clear old result
                request.getSession().setAttribute("searchError", "Stock not found");
            }
        } else {
            System.out.println("Empty ticker received");
            request.getSession().setAttribute("searchError", "Ticker cannot be empty");
            request.getSession().setAttribute("searchError", "Ticker cannot be empty");
        }

        response.sendRedirect(request.getContextPath() + "/portfolio");
    }

}

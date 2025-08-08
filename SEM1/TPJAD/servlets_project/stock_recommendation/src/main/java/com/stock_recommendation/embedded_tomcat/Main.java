package com.stock_recommendation.embedded_tomcat;
import com.stock_recommendation.servlet.FilterServlet;
import com.stock_recommendation.servlet.SearchServlet;
import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;
import org.apache.tomcat.util.scan.StandardJarScanner;

import java.io.File;

public class Main {
    public static void main(String[] args) throws Exception {
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(8080);
        tomcat.setBaseDir("temp-tomcat");

        File webContentFolder = new File("src/main/webapp");
        Context context = tomcat.addWebapp("", webContentFolder.getAbsolutePath());

        ((StandardJarScanner) context.getJarScanner()).setScanManifest(false);

//        Tomcat.addServlet(context, "PortfolioServlet", new PortfolioServlet());
//        context.addServletMappingDecoded("/portfolio", "PortfolioServlet");
//
//        Tomcat.addServlet(context, "RecommendationServlet", new RecommendationServlet());
//        context.addServletMappingDecoded("/recommendation", "RecommendationServlet");
//
//        context.addWelcomeFile("portfolio.jsp");
        Tomcat.addServlet(context, "SearchServlet", new SearchServlet());
        context.addServletMappingDecoded("/api/search", "SearchServlet");
        context.addServletMappingDecoded("/search", "SearchServlet");
        Tomcat.addServlet(context, "FilterServlet", new FilterServlet());
        context.addServletMappingDecoded("/api/filter", "FilterServlet");

        context.addWelcomeFile("index.jsp");
        System.out.println("Starting embedded Tomcat at http://localhost:8080");

        tomcat.getConnector();
        tomcat.start();
        tomcat.getServer().await();
    }
}

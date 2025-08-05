package com.stock_recommendation.embedded_tomcat;
import com.stock_recommendation.servlet.HomeServlet;
import com.stock_recommendation.servlet.PortfolioServlet;
import com.stock_recommendation.servlet.RecommendationServlet;
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

        Tomcat.addServlet(context, "HomeServlet", new HomeServlet());
        context.addServletMappingDecoded("", "HomeServlet");

        Tomcat.addServlet(context, "PortfolioServlet", new PortfolioServlet());
        context.addServletMappingDecoded("/portfolio", "PortfolioServlet");

        Tomcat.addServlet(context, "RecommendationServlet", new RecommendationServlet());
        context.addServletMappingDecoded("/recommendation", "RecommendationServlet");

        context.addWelcomeFile("portfolio.jsp");
        System.out.println("Starting embedded Tomcat at http://localhost:8080");

        tomcat.getConnector();
        tomcat.start();
        tomcat.getServer().await();

    }
}

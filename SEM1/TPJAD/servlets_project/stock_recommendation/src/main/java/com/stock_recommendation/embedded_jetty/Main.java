//package com.stock_recommendation.embedded_jetty;
//
//import com.stock_recommendation.servlet.FilterServlet;
//import com.stock_recommendation.servlet.IndexServlet;
//import com.stock_recommendation.servlet.SearchServlet;
//import org.eclipse.jetty.server.Server;
//import org.eclipse.jetty.ee10.webapp.WebAppContext;
//
//import java.io.File;
//
//public class Main {
//    private static int PORT = 8081;
//    public static void main(String[] args) throws Exception {
//        Server server = new Server(PORT);
//
//        WebAppContext context = new WebAppContext();
//        context.setContextPath("/");
//        File webAppDir = new File("src/main/webapp");
//
//        context.addServlet(new SearchServlet(), "/api/search");
//        context.addServlet(new SearchServlet(), "/search");
//        context.addServlet(new FilterServlet(), "/api/filter");
//        context.addServlet(new IndexServlet(), "/");
//
//        context.setWar(webAppDir.getAbsolutePath());
//        context.setParentLoaderPriority(true);
//        context.setWelcomeFiles(new String[]{"index.jsp"});
//        context.setAttribute(
//                "org.eclipse.jetty.server.webapp.ContainerIncludeJarPattern",
//                ".*jakarta.servlet.jsp.jstl.*\\.jar$|.*taglibs.*\\.jar$"
//        );
//
//        server.setHandler(context);
//        System.out.println("Jetty at http://localhost:" + PORT + "/");
//        server.start();
//        server.join();
//    }
//}
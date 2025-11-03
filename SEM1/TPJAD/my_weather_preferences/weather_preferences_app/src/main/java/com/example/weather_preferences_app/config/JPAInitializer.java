package com.example.weather_preferences_app.config;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
//
//@WebListener
//public class JPAInitializer implements ServletContextListener {
//
//    private static EntityManagerFactory emfPostgres;
//    private static EntityManagerFactory emfSQLServer;
//    private static EntityManagerFactory emfOracle; // <-- Add this
//
//    @Override
//    public void contextInitialized(ServletContextEvent sce) {
//        try {
//            // postgres
//            emfPostgres = Persistence.createEntityManagerFactory("PU_Postgres");
//            sce.getServletContext().setAttribute("emfPostgres", emfPostgres);
//            System.out.println("Postgres EntityManagerFactory created successfully!");
//
//            // sql server
//            emfSQLServer = Persistence.createEntityManagerFactory("PU_SQLServer");
//            sce.getServletContext().setAttribute("emfSQLServer", emfSQLServer);
//            System.out.println("SQL Server EntityManagerFactory created successfully!");
//
//            // oracle
//            emfOracle = Persistence.createEntityManagerFactory("PU_Oracle");
//            sce.getServletContext().setAttribute("emfOracle", emfOracle);
//            System.out.println("Oracle EntityManagerFactory created successfully!");
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new RuntimeException("Failed to initialize JPA", e);
//        }
//    }
//
//    @Override
//    public void contextDestroyed(ServletContextEvent sce) {
//        if (emfPostgres != null && emfPostgres.isOpen()) {
//            emfPostgres.close();
//        }
//        if (emfSQLServer != null && emfSQLServer.isOpen()) {
//            emfSQLServer.close();
//        }
//        if (emfOracle != null && emfOracle.isOpen()) {
//            emfOracle.close();
//        }
//    }
//
//    public static EntityManagerFactory getEmfPostgres() {
//        return emfPostgres;
//    }
//
//    public static EntityManagerFactory getEmfSQLServer() {
//        return emfSQLServer;
//    }
//
//    public static EntityManagerFactory getEmfOracle() {
//        return emfOracle;
//    }
//}

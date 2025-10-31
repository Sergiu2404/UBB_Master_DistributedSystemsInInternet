package com.example.server.config;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
//
//@WebListener
//public class JPAInitializer implements ServletContextListener {
//    private static EntityManagerFactory emf;
//
//    // automatically called by tomcat at startup
//    @Override
//    public void contextInitialized(ServletContextEvent sce) {
//        try {
//            emf = Persistence.createEntityManagerFactory("PU_Postgres");
//            sce.getServletContext().setAttribute("emf", emf);
//            System.out.println("EntityManagerFactory created successfully!");
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new RuntimeException("Failed to initialize JPA", e);
//        }
//    }
//
//    @Override
//    public void contextDestroyed(ServletContextEvent sce) {
//        if (emf != null && emf.isOpen()) {
//            emf.close();
//            System.out.println("EntityManagerFactory closed.");
//        }
//    }
//
//    public static EntityManagerFactory getEmf() {
//        return emf;
//    }
//}



@WebListener
public class JPAInitializer implements ServletContextListener {

    private static EntityManagerFactory emfPostgres;
    private static EntityManagerFactory emfSQLServer;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            emfPostgres = Persistence.createEntityManagerFactory("PU_Postgres");
            sce.getServletContext().setAttribute("emfPostgres", emfPostgres);
            System.out.println("Postgres EntityManagerFactory created successfully!");

            emfSQLServer = Persistence.createEntityManagerFactory("PU_SQLServer");
            sce.getServletContext().setAttribute("emfSQLServer", emfSQLServer);
            System.out.println("SQL Server EntityManagerFactory created successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to initialize JPA", e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        if (emfPostgres != null && emfPostgres.isOpen()) {
            emfPostgres.close();
        }
        if (emfSQLServer != null && emfSQLServer.isOpen()) {
            emfSQLServer.close();
        }
    }

    public static EntityManagerFactory getEmfPostgres() {
        return emfPostgres;
    }

    public static EntityManagerFactory getEmfSQLServer() {
        return emfSQLServer;
    }
}
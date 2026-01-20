package com.example.rest;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebListener;
import jakarta.servlet.http.*;

/**
 * Application Lifecycle Listener implementation class UniversalListener
 */
@WebListener
public class UniversalListener implements ServletContextListener,
        ServletContextAttributeListener, HttpSessionListener,
        HttpSessionAttributeListener, HttpSessionActivationListener,
        HttpSessionBindingListener, ServletRequestListener,
        ServletRequestAttributeListener {

    /**
     * Default constructor.
     */
    public UniversalListener() {
        // TODO Auto-generated constructor stub
        System.out.println("Universal Listener Default constructor");
    }

    /**
     * @see ServletRequestListener#requestDestroyed(ServletRequestEvent)
     */
    public void requestDestroyed(ServletRequestEvent arg0) {
        // TODO Auto-generated method stub
        System.out.println("ServletRequestListener#requestDestroyed: " + arg0);
    }

    /**
     * @see HttpSessionAttributeListener#attributeAdded(HttpSessionBindingEvent)
     */
    public void attributeAdded(HttpSessionBindingEvent arg0) {
        // TODO Auto-generated method stub
        System.out.println("HttpSessionAttributeListener#attributeAdded: " + arg0);
    }

    /**
     * @see ServletContextListener#contextInitialized(ServletContextEvent)
     */
    public void contextInitialized(ServletContextEvent arg0) {
        // TODO Auto-generated method stub
        System.out.println("ServletContextListener#contextInitialized: " + arg0);
    }

    /**
     * @see HttpSessionActivationListener#sessionDidActivate(HttpSessionEvent)
     */
    public void sessionDidActivate(HttpSessionEvent arg0) {
        // TODO Auto-generated method stub
        System.out.println("HttpSessionActivationListener#sessionDidActivate: " + arg0);
    }

    /**
     * @see HttpSessionBindingListener#valueBound(HttpSessionBindingEvent)
     */
    public void valueBound(HttpSessionBindingEvent arg0) {
        // TODO Auto-generated method stub
        System.out.println("HttpSessionBindingListener#valueBound: " + arg0);
    }

    /**
     * @see ServletContextAttributeListener#attributeAdded(ServletContextAttributeEvent)
     */
    public void attributeAdded(ServletContextAttributeEvent arg0) {
        // TODO Auto-generated method stub
        System.out.println("ServletContextAttributeListener#attributeAdded: " + arg0);
    }

    /**
     * @see ServletContextAttributeListener#attributeRemoved(ServletContextAttributeEvent)
     */
    public void attributeRemoved(ServletContextAttributeEvent arg0) {
        // TODO Auto-generated method stub
        System.out.println("ServletContextAttributeListener#attributeRemoved: " + arg0);
    }

    /**
     * @see HttpSessionListener#sessionDestroyed(HttpSessionEvent)
     */
    public void sessionDestroyed(HttpSessionEvent arg0) {
        // TODO Auto-generated method stub
        System.out.println("HttpSessionListener#sessionDestroyed: " + arg0);
    }

    /**
     * @see HttpSessionAttributeListener#attributeRemoved(HttpSessionBindingEvent)
     */
    public void attributeRemoved(HttpSessionBindingEvent arg0) {
        // TODO Auto-generated method stub
        System.out.println("HttpSessionAttributeListener#attributeRemoved: " + arg0);
    }

    /**
     * @see ServletRequestAttributeListener#attributeAdded(ServletRequestAttributeEvent)
     */
    public void attributeAdded(ServletRequestAttributeEvent arg0) {
        // TODO Auto-generated method stub
        System.out.println("ServletRequestAttributeListener#attributeAdded: " + arg0);
    }

    /**
     * @see HttpSessionBindingListener#valueUnbound(HttpSessionBindingEvent)
     */
    public void valueUnbound(HttpSessionBindingEvent arg0) {
        // TODO Auto-generated method stub
        System.out.println("HttpSessionBindingListener#valueUnbound: " + arg0);
    }

    /**
     * @see HttpSessionActivationListener#sessionWillPassivate(HttpSessionEvent)
     */
    public void sessionWillPassivate(HttpSessionEvent arg0) {
        // TODO Auto-generated method stub
        System.out.println("HttpSessionActivationListener#sessionWillPassivate: " + arg0);
    }

    /**
     * @see HttpSessionListener#sessionCreated(HttpSessionEvent)
     */
    public void sessionCreated(HttpSessionEvent arg0) {
        // TODO Auto-generated method stub
        System.out.println("HttpSessionListener#sessionCreated: " + arg0);
    }

    /**
     * @see HttpSessionAttributeListener#attributeReplaced(HttpSessionBindingEvent)
     */
    public void attributeReplaced(HttpSessionBindingEvent arg0) {
        // TODO Auto-generated method stub
        System.out.println("HttpSessionAttributeListener#attributeReplaced: " + arg0);
    }

    /**
     * @see ServletContextAttributeListener#attributeReplaced(ServletContextAttributeEvent)
     */
    public void attributeReplaced(ServletContextAttributeEvent arg0) {
        // TODO Auto-generated method stub
        System.out.println("ServletContextAttributeListener#attributeReplaced: " + arg0);
    }

    /**
     * @see ServletRequestAttributeListener#attributeRemoved(ServletRequestAttributeEvent)
     */
    public void attributeRemoved(ServletRequestAttributeEvent arg0) {
        // TODO Auto-generated method stub
        System.out.println("ServletRequestAttributeListener#attributeRemoved: " + arg0);
    }

    /**
     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent arg0) {
        // TODO Auto-generated method stub
        System.out.println("ServletContextListener#contextDestroyed: " + arg0);
    }

    /**
     * @see ServletRequestAttributeListener#attributeReplaced(ServletRequestAttributeEvent)
     */
    public void attributeReplaced(ServletRequestAttributeEvent arg0) {
        // TODO Auto-generated method stub
        System.out.println("ServletRequestAttributeListener#attributeReplaced: " + arg0);
    }

    /**
     * @see ServletRequestListener#requestInitialized(ServletRequestEvent)
     */
    public void requestInitialized(ServletRequestEvent arg0) {
        // TODO Auto-generated method stub
        System.out.println("ServletRequestListener#requestInitialized: " + arg0);
    }

}

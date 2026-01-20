package com.example.rest.embedded;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.apache.tomcat.util.descriptor.web.FilterDef;
import org.apache.tomcat.util.descriptor.web.FilterMap;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.Random;

public class ProgrammaticTomcat {

    private static boolean isFree(int port) {
        try {
            new ServerSocket(port).close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    private Tomcat tomcat = null;

    private int randomPort;

    public ProgrammaticTomcat() {
        // Get a random port number in range 6000 (inclusive) - 9000 (exclusive)
        this.randomPort = new Random().ints(6000, 9000).filter(ProgrammaticTomcat::isFree).findFirst().orElse(8080);
    }


    public int getPort() {
        return randomPort;
    }

    public void startTomcat() throws LifecycleException {
        tomcat = new Tomcat();
        tomcat.setPort(8080);
        tomcat.setHostname("localhost");
        String appBase = ".";
        tomcat.getHost().setAppBase(appBase);

        File docBase = new File(System.getProperty("java.io.tmpdir"));
        Context context = tomcat.addContext("", docBase.getAbsolutePath());

        addServlet(context, "", ClientHtml.class);
        addServlet(context, "/my-servlet/", MyServlet.class);
        addServlet(context, "/Add", Aduna.class);
        addServlet(context, "/LM", LitereMari.class);

        addFilterAndMapping(context, "/my-servlet/*", MyFilter.class);
        addFilterAndMapping(context, "/*", LogFilter.class);

        tomcat.start();
        tomcat.getServer().await();
    }

    private static void addFilterAndMapping(Context context, String urlPattern, Class filterClass) {
        // add a filter and filterMapping
        FilterDef myFilterDef = new FilterDef();
        myFilterDef.setFilterClass(filterClass.getName());
        myFilterDef.setFilterName(filterClass.getSimpleName());
        context.addFilterDef(myFilterDef);

        FilterMap myFilterMap = new FilterMap();
        myFilterMap.setFilterName(filterClass.getSimpleName());
        myFilterMap.addURLPattern(urlPattern);
        context.addFilterMap(myFilterMap);
    }

    private static void addServlet(Context context, String mappingURL, Class servletClass) {
        // add a servlet
        Tomcat.addServlet(context, servletClass.getSimpleName(), servletClass.getName());
        context.addServletMappingDecoded(mappingURL, servletClass.getSimpleName());
    }

    public void stopTomcat() throws LifecycleException {
        tomcat.stop();
        tomcat.destroy();
    }
}

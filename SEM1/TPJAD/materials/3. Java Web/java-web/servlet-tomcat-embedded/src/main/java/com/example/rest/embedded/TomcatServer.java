package com.example.rest.embedded;



public class TomcatServer {
    public static void main(String[] args) throws Exception {

        ProgrammaticTomcat tomcat = new ProgrammaticTomcat();
        System.out.println(tomcat.getPort());
        tomcat.startTomcat();

    }
}

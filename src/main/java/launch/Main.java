/* Copyright 2015 Oracle and/or its affiliates. All rights reserved. */
package launch;

import org.apache.catalina.startup.Tomcat;

public class Main {
    
    public static String PORT = System.getenv("PORT");
    public static String HOSTNAME = System.getenv("HOSTNAME");
    
    public static void main(String[] args) throws Exception {
        if (PORT==null || PORT.length()==0) PORT="8080";
        if (HOSTNAME==null || HOSTNAME.length()==0) HOSTNAME="8080";

        String contextPath = "/" ;
        String appBase = ".";
        Tomcat tomcat = new Tomcat();   
        tomcat.setPort(Integer.valueOf(PORT ));
        tomcat.setHostname(HOSTNAME);
        tomcat.getHost().setAppBase(appBase);
        tomcat.addWebapp(contextPath, appBase);
//        tomcat.addWebapp(host, url, name, path)
        
        tomcat.start();
        tomcat.getServer().await();
    }
}
/* Copyright © 2015 Oracle and/or its affiliates. All rights reserved. */
package launch;

import java.util.Optional;

import org.apache.catalina.startup.Tomcat;

public class Main {
    
    public static final Optional<String> PORT = Optional.ofNullable(System.getenv("PORT"));
    public static final Optional<String> HOSTNAME = Optional.ofNullable(System.getenv("HOSTNAME"));
    
    public static void main(String[] args) throws Exception {
        String contextPath = "/" ;
        String appBase = ".";
        Tomcat tomcat = new Tomcat();   
        tomcat.setPort(Integer.valueOf(PORT.orElse("8080") ));
        tomcat.setHostname(HOSTNAME.orElse("localhost"));
        tomcat.getHost().setAppBase(appBase);
        tomcat.addWebapp(contextPath, appBase);
//        tomcat.addWebapp(host, url, name, path)
        
        tomcat.start();
        tomcat.getServer().await();
    }
}
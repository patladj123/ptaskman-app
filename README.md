## PTaskman
PTaskman is web based cross-platform task manager which allows you to monitor the resources like memory and CPU usage on the machine that it is running, also allows you to monitor and kill the processes.
Soon there will be a stage demo url.

Version 1.0 released.
It should be working on Windows, Linux and OS X, but I haven't had the chance to test it on OS X yet, so please stick to Windows and Linux for now.
For feedback for OS X please write on patladj / gmail
It is developed using IntelliJ IDEA ULTIMATE 2017.1.4

This software uses the following 3rd party libraries:
 - JavaSysMon by jezhumble (https://github.com/jezhumble/javasysmon)
 - SmoothieCharts (http://smoothiecharts.org)
 - DataTables (https://datatables.net/)

Requirements for stand alone build & run (uses embedded Tomcat 7):
 - Java 8
 - Maven 3
 - available network port 8080
 
Build & run HOWTO. Stand alone. (No need of app server. Uses embedded Tomcat 7):
 - mvn clean compile
 - mvn package
 - cd target
 - java -jar ptaskman-app-1.0-SNAPSHOT-jar-with-dependencies.jar
 - Navigate your browser to http://localhost:8080/jsp/ptaskman.jsp


If you want to run it IDE I will publish instructions soon.
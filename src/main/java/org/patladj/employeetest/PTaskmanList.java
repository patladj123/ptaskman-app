/* Copyright © 2015 Oracle and/or its affiliates. All rights reserved. */
package org.patladj.employeetest;

import java.util.ArrayList;
import java.util.List;


public class PTaskmanList {
    private static final List<PTaskman> employeeList = new ArrayList();
    
    private PTaskmanList(){
    }
    
    static{
        employeeList.add(new PTaskman("John","Smith","12-12-1980","Manager","Sales","john.smith@abc.com"));
        employeeList.add(new PTaskman("Laura","Adams","02-11-1979","Manager","IT","laura.adams@abc.com"));
        employeeList.add(new PTaskman("Peter","Williams","22-10-1966","Coordinator","HR","peter.williams@abc.com"));
        employeeList.add(new PTaskman("Joana","Sanders","11-11-1976","Manager","Marketing","joana.sanders@abc.com"));
        employeeList.add(new PTaskman("John","Drake","18-08-1988","Coordinator","Finance","john.drake@abc.com"));
        employeeList.add(new PTaskman("Samuel","Williams","22-03-1985","Coordinator","Finance","samuel.williams@abc.com"));
    }
    
    public static List <PTaskman> getInstance(){
        return employeeList;
    }
}

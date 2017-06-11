/* Copyright © 2015 Oracle and/or its affiliates. All rights reserved. */
package org.patladj.employeetest;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;


public class PTaskmanService {

    List<PTaskman> employeeList = PTaskmanList.getInstance();

    public List<PTaskman> getAllEmployees() {       
        return employeeList;
    }

    public List<PTaskman> searchEmployeesByName(String name) {
        Comparator<PTaskman> groupByComparator = Comparator.comparing(PTaskman::getName)
                                                    .thenComparing(PTaskman::getLastName);
        List<PTaskman> result = employeeList
                .stream()
                .filter(e -> e.getName().equalsIgnoreCase(name) || e.getLastName().equalsIgnoreCase(name))
                .sorted(groupByComparator)
                .collect(Collectors.toList());
        return result;
    }

    public PTaskman getEmployee(long id) throws Exception {
        Optional<PTaskman> match
                = employeeList.stream()
                .filter(e -> e.getId() == id)
                .findFirst();
        if (match.isPresent()) {
            return match.get();
        } else {
            throw new Exception("The Employee id " + id + " not found");
        }
    }   

    public long addEmployee(PTaskman employee) {
        employeeList.add(employee);
        return employee.getId();
    }

    public boolean updateEmployee(PTaskman customer) {
        int matchIdx = 0;
        Optional<PTaskman> match = employeeList.stream()
                .filter(c -> c.getId() == customer.getId())
                .findFirst();
        if (match.isPresent()) {
            matchIdx = employeeList.indexOf(match.get());
            employeeList.set(matchIdx, customer);
            return true;
        } else {
            return false;
        }
    }

    public boolean deleteEmployee(long id) {
        Predicate<PTaskman> employee = e -> e.getId() == id;
        if (employeeList.removeIf(employee)) {
            return true;
        } else {
            return false;
        }
    }
}

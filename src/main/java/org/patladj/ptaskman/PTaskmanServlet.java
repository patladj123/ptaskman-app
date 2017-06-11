/* Copyright © 2015 Oracle and/or its affiliates. All rights reserved. */
package org.patladj.ptaskman;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.RequestDispatcher;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This is a TEST Servlet. It will be teleted soon.
 * @author PatlaDJ
 *
 */
@WebServlet(
        name = "PTaskmanServlet",
        urlPatterns = {"/ptaskman"}
)
public class PTaskmanServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	String nextJSP = "jsp/ptaskman.jsp";
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(nextJSP);
        dispatcher.forward(req, resp);
    }
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String action = req.getParameter("action");
        switch (action) {
            case "add":
//                addEmployeeAction(req, resp);
                break;
            case "edit":
//                editEmployeeAction(req, resp);
                break;            
            case "remove":
//                removeEmployeeByName(req, resp);
                break;            
        }

    }

}

package main.controller;

import main.util.Constants;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class ApplicationController extends HttpServlet {

    @Override
    public void init() throws ServletException {
        ServletContext context = getServletContext();
        context.setAttribute("queryNames", Constants.QUERIES.keySet());
        //set trigger names as well

        super.init();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.sendRedirect("/table");
    }
}

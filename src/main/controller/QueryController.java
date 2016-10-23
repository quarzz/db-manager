package main.controller;


import static main.util.QueryManipulator.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

public class QueryController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String queryName = getNameFromQuery(req);


        Set<String> queryParameterNames = getQueryParameterNames(queryName);

        System.out.println("QUERY NAME: " + queryName);
        System.out.println("Parameters: ");
        queryParameterNames.forEach(System.out::println);

        req.setAttribute("queryParameterNames", queryParameterNames);
        req.getRequestDispatcher("query.jsp").forward(req, resp);
    }
}

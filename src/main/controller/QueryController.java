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

        String queryName = getNameFromRequest(req);
        Set<String> queryParameterNames = getQueryParameterNames(queryName);

        req.setAttribute("queryName", queryName);
        req.setAttribute("queryParameterNames", queryParameterNames);
        req.getRequestDispatcher("query.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

//        String sqlQueryString =
    }
}

package main.controller;


import main.dao.TableDAO;
import main.dao.TableDaoImpl;
import main.util.Collection;
import main.util.Constants;

import static main.util.QueryManipulator.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class QueryController extends HttpServlet {
    private TableDAO tableDAO;

    @Override
    public void init() throws ServletException {
        tableDAO =
                new TableDaoImpl(Constants.DATABASE_URL, Constants.USERNAME, Constants.PASSWORD);
        super.init();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String queryName = getNameFromRequest(req);
        Set<String> queryParameterNames = getQueryParameterNames(queryName);

        req.setAttribute("queryName", queryName);
        req.setAttribute("queryParameterNames", queryParameterNames);

        String url = req.getRequestURI();
        if (req.getQueryString() != null && req.getQueryString().length() >0)
            url += '?' + req.getQueryString();
        req.getSession().setAttribute("prevUrl", url);

        req.getRequestDispatcher("query.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        String queryName = getNameFromRequest(req);

        Map<String, String> queryParameters = getParametersFromRequest(req);

        String sqlQueryString =
                Collection.gsub(Constants.QUERIES.get(queryName), "&([a-zA-Z0-9_]+)", queryParameters);

        ArrayList<ArrayList<String>> data;

        try {
            data = tableDAO.getQueryData(sqlQueryString);
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        req.setAttribute("action", "query");
        req.setAttribute("tableName", queryName);
        req.setAttribute("tableData", data);

        String url = req.getRequestURI();
        if (req.getQueryString() != null && req.getQueryString().length() >0)
            url += '?' + req.getQueryString();
        req.getSession().setAttribute("prevUrl", url);

        req.getRequestDispatcher("/show.jsp").forward(req, resp);
    }
}

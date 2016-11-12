package main.controller;

import main.dao.TableDAO;
import main.dao.TableDaoImpl;
import main.util.Constants;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class TableController extends HttpServlet {

    private TableDAO tableDAO;

    @Override
    public void init() throws ServletException {
        tableDAO =
                new TableDaoImpl(Constants.DATABASE_URL, Constants.USERNAME, Constants.PASSWORD);

        super.init();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String tableName = req.getParameter("tableName");
            String tableAction = req.getParameter("action");

            String url = req.getRequestURI();
            if (req.getQueryString() != null && req.getQueryString().length() >0)
                url += '?' + req.getQueryString();
            req.getSession().setAttribute("prevUrl", url);


            if (tableName == null) {
                index(req, resp);
            } else {
                if (tableAction == null) {
                    show(req, resp);
                } else {
                    switch (tableAction) {
                        case "update":
                            showRow(req, resp);
                            break;
                        case "delete":
                            delete(req, resp);
                            break;
                        default:
                            resp.sendError(404);
                    }
                }
            }
        } catch (SQLException e) {
            resp.sendError(500);
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        String url = req.getRequestURI();
        if (req.getQueryString() != null && req.getQueryString().length() >0)
            url += '?' + req.getQueryString();
        req.getSession().setAttribute("prevUrl", url);


        String action = req.getParameter("action");

        System.out.println("POST: " + action.toUpperCase());


        try {
            switch (action) {
                case "insert":
                    System.out.println("POST CALLING INSERT");
                    insert(req, resp);
                    break;
                case "update":
                    System.out.println("POST CALLING UPDATE");
                    update(req, resp);
                    break;
                default:
                    resp.sendError(404);
            }
        } catch (SQLException e) {
            resp.sendError(500);
            e.printStackTrace();
        }
    }

    private List<String> extractValuesFromReq(HttpServletRequest req) throws SQLException {

        String tableName = req.getParameter("tableName");

        List<String> columns = tableDAO.getColumnNames(tableName);
        List<String> values = new ArrayList<>();

        columns.forEach(c -> values.add(req.getParameter(c)));

        return values;
    }

    private void index(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException, SQLException {

        ArrayList<String> tableNames;

        tableNames = tableDAO.getTableNames();

        req.setAttribute("tableNames", tableNames);
        req.getRequestDispatcher("index.jsp").forward(req, resp);
    }

    private void show(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException, SQLException {

        String tableName = req.getParameter("tableName");

        ArrayList<String> columnNames = tableDAO.getColumnNames(tableName);

        ArrayList<ArrayList<String>> tableData = tableDAO.getTableData(tableName);

        req.setAttribute("action", "table");
        req.setAttribute("tableName", tableName);
        req.setAttribute("columnNames", columnNames);
        req.setAttribute("tableData", tableData);

        req.getRequestDispatcher("show.jsp").forward(req, resp);
    }

    private void insert(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException, SQLException {

        String tableName = req.getParameter("tableName");

        List<String> values = extractValuesFromReq(req);


        // it supposed to have different handling, but not now ;)
        if (tableDAO.insert(tableName, values))
            resp.sendRedirect("/table?tableName=" + tableName);
        else
            //resp.sendRedirect("/table?tableName=" + tableName);
            resp.sendRedirect("/table?tableName=" + tableName);
    }

    private void showRow(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException, SQLException {

        String tableName = req.getParameter("tableName");
        int id = Integer.parseInt(req.getParameter("id"));

        List<String> columnNames = tableDAO.getColumnNames(tableName);
        List<String> row = tableDAO.getRow(tableName, id);

        req.setAttribute("tableName", tableName);
        req.setAttribute("row", row);
        req.setAttribute("columnNames", columnNames);
        req.getRequestDispatcher("row.jsp").forward(req, resp);
    }

    private void update(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException, SQLException {
        String tableName = req.getParameter("tableName");
        int id = Integer.parseInt(req.getParameter("id"));

        List<String> columnNames = tableDAO.getColumnNames(tableName);
        List<String> row = columnNames.stream().map(col -> req.getParameter(col)).collect(Collectors.toList());


        if (tableDAO.update(tableName, row)) {
            resp.sendRedirect("/table?tableName=" + tableName);
        } else {
            showRow(req, resp);
        }
    }

    private void delete(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException, SQLException {
        String tableName = req.getParameter("tableName");
        int id = Integer.parseInt(req.getParameter("id"));

        if (tableDAO.delete(tableName, id))
            resp.sendRedirect("/table?tableName=" + tableName);
        else
            resp.sendRedirect("/table?tableName=" + tableName);
    }
}

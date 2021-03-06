package main.controller;

import main.dao.TableDAO;
import main.dao.TableDaoImpl;
import main.util.Constants;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;


public class ApplicationController extends HttpServlet {

    private TableDAO tableDAO;

    @Override
    public void init() throws ServletException {

        ServletContext context = getServletContext();
        context.setAttribute("queryNames", Constants.QUERIES.keySet());
        //set trigger names as well

        tableDAO =
                new TableDaoImpl(Constants.DATABASE_URL, Constants.USERNAME, Constants.PASSWORD);


        try {
            context.setAttribute("triggers", tableDAO.getTriggers());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        super.init();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.sendRedirect("/table");
    }
}

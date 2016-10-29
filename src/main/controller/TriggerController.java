package main.controller;

import main.dao.TableDAO;
import main.dao.TableDaoImpl;
import main.util.Constants;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by User on 29.10.2016.
 */
public class TriggerController extends HttpServlet {
    private TableDAO tableDAO;

    @Override
    public void init() throws ServletException {
        tableDAO =
                new TableDaoImpl(Constants.DATABASE_URL, Constants.USERNAME, Constants.PASSWORD);

        super.init();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String triggerName = req.getParameter("triggerName");
        boolean enable = !req.getParameter("enable").equalsIgnoreCase("true");

        tableDAO.changeTriggerState(triggerName, enable);
        try {
            req.getServletContext().setAttribute("triggers", tableDAO.getTriggers());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println(req.getSession().getAttribute("prevUrl").toString());
        resp.sendRedirect(req.getSession().getAttribute("prevUrl").toString());
    }
}

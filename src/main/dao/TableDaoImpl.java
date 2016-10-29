package main.dao;


import main.entity.Trigger;
import main.util.*;
import main.util.Collection;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

// Should i handle exceptions here on throw them to the caller???

public class TableDaoImpl implements TableDAO {

    private String databaseURL;
    private String username;
    private String password;

    private Connection connection;

    public TableDaoImpl(String databaseURL, String username, String password) {
        this.databaseURL = databaseURL;
        this.username = username;
        this.password = password;

        try {
            Class.forName("oracle.jdbc.OracleDriver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.err.println("NO DRIVER YOPT");
        }
    }

    private Connection getConnection() throws SQLException {
        if (connection != null && connection.isValid(1)) {
            return connection;
        } else {
            return connection = DriverManager.getConnection(databaseURL, username, password);
        }
    }

    @Override
    public void changeTriggerState(String triggerName, boolean enable) {

        try {
            connection = getConnection();

            Statement statement = connection.createStatement();

            String action = enable ? "ENABLE" : "DISABLE";

            String query = String.format("ALTER TRIGGER %s %s", triggerName, action);

            System.out.println(query);

            statement.executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Trigger> getTriggers() throws SQLException {
        List<Trigger> result = new ArrayList<>();
        connection = getConnection();

        Statement statement = connection.createStatement();

        ResultSet resultSet = statement.executeQuery("select distinct trigger_name, status from user_triggers");

        while (resultSet.next()) {
            String name = resultSet.getString(1);
            boolean active = resultSet.getString(2).equalsIgnoreCase("enabled");
            Trigger trigger = new Trigger();
            trigger.setName(name);
            trigger.setActive(active);
            result.add(trigger);
            System.out.println(name);
        }

        return result;
    }

    @Override
    public ArrayList<String> getTableNames() throws SQLException {
        ArrayList<String> tableNames = new ArrayList<>();

        connection = getConnection();
        Statement statement = connection.createStatement();
        ResultSet tableNamesSet = statement.executeQuery(Constants.Queries.TABLE_NAMES);

        while (tableNamesSet.next()) {
            tableNames.add(tableNamesSet.getString(1));
        }

        return tableNames;
    }

    @Override
    public ArrayList<String> getColumnNames(String tableName) throws SQLException {
        ArrayList<String> columnNames = new ArrayList<>();

        connection = getConnection();

        PreparedStatement statement = connection.prepareStatement(Constants.Queries.COLUMN_NAMES);
        statement.setString(1, tableName);

        ResultSet columnNamesSet = statement.executeQuery();

        while (columnNamesSet.next()) {
            columnNames.add(columnNamesSet.getString(1));
        }

        return columnNames;
    }

    @Override
    public ArrayList<ArrayList<String>> getTableData(String tableName) throws SQLException {
//        ArrayList<ArrayList<String>> tableData = new ArrayList<>();
//
//        connection = getConnection();
//        Statement statement = connection.createStatement();
//        ResultSet tableDataSet =
//                statement.executeQuery(String.format(Constants.Queries.TABLE_DATA, tableName));

        return getQueryData(String.format(Constants.Queries.TABLE_DATA, tableName));

//        int columnCount = tableDataSet.getMetaData().getColumnCount();
//
//        while (tableDataSet.next()) {
//            ArrayList<String> tableRow = new ArrayList<>();
//            for (int i = 1; i <= columnCount; ++i)
//                tableRow.add(tableDataSet.getString(i));
//            tableData.add(tableRow);
//        }
//
//        return tableData;
    }

    @Override
    public ArrayList<ArrayList<String>> getQueryData(String sqlQueryString) throws SQLException {
        ArrayList<ArrayList<String>> data = new ArrayList<>();

        connection = getConnection();
        Statement statement = connection.createStatement();
        System.out.println(sqlQueryString);
        ResultSet dataSet =
                statement.executeQuery(sqlQueryString);

        int columnCount = dataSet.getMetaData().getColumnCount();

        ArrayList<String> columnNames = new ArrayList<>();

        for (int i = 1; i <= columnCount; ++i)
            columnNames.add(dataSet.getMetaData().getColumnLabel(i));

        data.add(columnNames);

        while (dataSet.next()) {
            ArrayList<String> row = new ArrayList<>();
            for (int i = 1; i <= columnCount; ++i)
                row.add(dataSet.getString(i));
            data.add(row);
        }

        return data;
    }

    @Override
    public boolean insert(String tableName, List<String> row) {
        boolean result = false;

        try {
            connection = getConnection();
            Statement statement = connection.createStatement();
            System.out.println(String.format(Constants.Queries.INSERT, tableName, Collection.join(row, ", ")));
            row = row.stream().map(col -> "'" + col + "'").collect(Collectors.toList());
            if (statement.executeUpdate(String.format(Constants.Queries.INSERT, tableName, Collection.join(row, ", "))) > 0)
                result = true;
        } catch (SQLException e) {
            result = false;
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public boolean update(String tableName, List<String> row) {
        boolean result;

        try {
            connection = getConnection();
            int id = Integer.parseInt(row.get(0));

            List<String> columnNames = getColumnNames(tableName);

            List<String> setArray = new ArrayList<>();

            for (int i = 0; i < row.size(); ++i) {
                setArray.add(columnNames.get(i) + " = '" + row.get(i) + "'");
            }

            String setString = Collection.join(setArray, ",");

            String query = String.format(Constants.Queries.UPDATE, tableName, setString);

            System.out.println(query);

            PreparedStatement statement =
                    connection.prepareStatement(query);

            statement.setInt(1, id);

            int updated = statement.executeUpdate();

            result = updated > 0;
        } catch (SQLException e) {
            System.err.println("EXCEPTION IN UPDATE");
            e.printStackTrace();
            result = false;
        }

        return result;
    }

    @Override
    public boolean delete(String tableName, int id) {
        boolean result = false;

        try {
            connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(String.format(Constants.Queries.DELETE, tableName));
            statement.setInt(1, id);

            if (statement.executeUpdate() > 0)
                result = true;
        } catch (SQLException e) {
            result = false;
        }

        return result;
    }

    @Override
    public List<String> getRow(String tableName, int id) throws SQLException {
        List<String> row = new ArrayList<>();

        connection = getConnection();

        String query = String.format(Constants.Queries.SELECT_ROW, tableName);

        System.out.println(query);

        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, id);

        ResultSet rowSet = statement.executeQuery();

        if (rowSet.next())
            for (int i = 1; i <= rowSet.getMetaData().getColumnCount(); ++i)
                row.add(rowSet.getString(i));

        return row;
    }
}

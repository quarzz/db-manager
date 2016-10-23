package main.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public interface TableDAO {
    ArrayList<String> getTableNames() throws SQLException;
    ArrayList<String> getColumnNames(String tableName) throws SQLException;
    ArrayList<ArrayList<String>> getTableData(String tableName) throws SQLException;
    boolean insert(String tableName, List<String> row);
    boolean update(String tableName, List<String> row);
    boolean delete(String tableName, int id);
    List<String> getRow(String tableName, int id) throws SQLException;
}

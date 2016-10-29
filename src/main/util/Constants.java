package main.util;


import java.util.HashMap;
import java.util.Map;

public class Constants {

    //database connection data
    public static final String DATABASE_URL = "jdbc:oracle:thin:@localhost:1521:XE";
    public static final String USERNAME = "oli";
    public static final String PASSWORD = "imivrsagzp";

    //trigger data
    public static final String[] TRIGGERS = {
        "BIRTH_CONTROL",
        "CHECK_OPERATION_AGE",
        "CHECK_SURPLUS"
    };


    //trigger data (can be done from sql
    //query data
    public static final Map<String, String> QUERIES;
    static {
        QUERIES = new HashMap<>();
        QUERIES.put(
                "These year operations by category",
                "SELECT operation.* FROM operation, cost, cost_category\n" +
                "   WHERE cost_category.name = '&category' AND\n" +
                "   cost.category_id = cost_category.id AND\n" +
                "   operation.id = cost.operation_id AND\n" +
                "   EXTRACT(YEAR FROM (SYSDATE)) = EXTRACT(YEAR FROM (operation.time))"
        );
        QUERIES.put(
                "All debit operations",
                "SELECT * FROM operation WHERE total < 0"
        );
        QUERIES.put(
                "This year top 5 expensive operations",
                "SELECT * FROM\n" +
                "  (SELECT tmp1.* FROM \n" +
                "    (SELECT operation.*, currency.rate_exchange * operation.total as normolized_total \n" +
                "      FROM operation, currency\n" +
                "      WHERE operation.currency_id = currency.id) tmp1, cost\n" +
                "    WHERE cost.operation_id = tmp1.id AND\n" +
                "    EXTRACT(YEAR FROM (SYSDATE)) = EXTRACT(YEAR FROM(tmp1.time))\n" +
                "    ORDER BY tmp1.normolized_total)\n" +
                "  WHERE ROWNUM <= 5"
        );
    }

    public static class Queries {
        public static final String TABLE_NAMES =
                "SELECT TABLE_NAME FROM user_tables";
        public static final String COLUMN_NAMES =
                "SELECT COLUMN_NAME FROM USER_TAB_COLUMNS WHERE TABLE_NAME LIKE ?";
        public static final String TABLE_DATA =
                "SELECT * FROM %s";
        public static final String INSERT =
                "INSERT INTO %s VALUES(%s)";
        public static final String DELETE =
                "DELETE FROM %s WHERE id = ?";
        public static final String SELECT_ROW =
                "SELECT * FROM %s WHERE id = ?";
        public static final String UPDATE =
                "UPDATE %s SET %s WHERE id = ?";
    }
}

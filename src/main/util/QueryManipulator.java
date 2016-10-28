package main.util;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static main.util.Collection.extractRegexMatchesAsList;


public class QueryManipulator {
//    public static String ass

    public static String getNameFromRequest(HttpServletRequest request) {
        return request.getParameter("queryName");
    }

    public static Set<String> getQueryParameterNames(String queryName) {
        String sqlQueryString = Constants.QUERIES.get(queryName);
        List<String> parameterNames =
                extractRegexMatchesAsList(sqlQueryString, "'.*?&([0-9a-zA-Z_]+).*?'");
        return new HashSet<>(parameterNames);
    }
}

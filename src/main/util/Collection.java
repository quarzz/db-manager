package main.util;


import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Collection {
    public static String join(List<?> array, String separator) {
        StringJoiner joiner = new StringJoiner(separator);

        for (Object element: array)
            joiner.add(element.toString());

        return joiner.toString();
    }

    public static List<String> extractRegexMatchesAsList(String string, String regex) {
        List<String> matches = new ArrayList<>();

        Matcher matcher = Pattern.compile(regex).matcher(string);

        while(matcher.find()) {
            matches.add(matcher.group(1));
        }

        return matches;
    }
}

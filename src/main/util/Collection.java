package main.util;


import java.util.*;
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

    // https://ruby-doc.org/core-2.2.0/String.html#method-i-gsub
    public static String gsub(String string, String regex, Map<String, String> replacements) {
        Matcher matcher = Pattern.compile(regex).matcher(string);

        StringBuilder result = new StringBuilder();
        int beginCopyIndex = 0;

        while (matcher.find()) {
            result.append(string.substring(beginCopyIndex, matcher.start()));
            beginCopyIndex = matcher.end();
            result.append(replacements.get(matcher.group(1)));
        }

        result.append(string.substring(beginCopyIndex, string.length()));

        return result.toString();
    }

//    public static void main(String[] args) {
//        Map<String, String> map = new HashMap<>();
//        map.put("category", "Питание");
//
//        System.out.println(gsub(Constants.QUERIES.get("These year operations by category"), "&([a-zA-Z0-9_]+)", map));
//    }
}

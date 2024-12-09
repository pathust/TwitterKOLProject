package utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Double.parseDouble;

public class Math {
    static public int toInt(String followersCount) {
        int factor = 1;
        if (followersCount.endsWith("K")) {
            followersCount = followersCount.replace("K", "");
            factor = 1000;
        } else if (followersCount.endsWith("M")) {
            followersCount = followersCount.replace("M", "");
            factor = 1000_000;
        }
        else
            followersCount = followersCount.replace(",", "");
        return (int) parseDouble(followersCount) * factor;
    }

    static public int getLastInt(String text) {
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(text);
        int res = 0;
        while (matcher.find()) {
            res = Integer.parseInt(matcher.group());
        }
        return res;
    }
}

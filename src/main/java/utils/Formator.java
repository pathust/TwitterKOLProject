package utils;

public class Formator {
    public static String formatUserLink(String input) {
        if (input == null) {
            return null;
        }
        if (input.contains("/status/")) {
            int statusIndex = input.indexOf("/status/");
            System.out.println(input.substring(0, statusIndex));
            return input.substring(0, statusIndex);
        }
        return input;
    }

    public static String formatTweetLink(String input) {
        if (input == null) {
            return null;
        }
        if (input.contains("/status/")) {
            int statusIndex = input.indexOf("/status/");
            String afterStatus = input.substring(statusIndex);
            int nextSlashIndex = afterStatus.indexOf("/", "/status/".length());
            if (nextSlashIndex == -1) {
                return input;
            }
            System.out.println(input.substring(0, nextSlashIndex));
            return input.substring(0, statusIndex + nextSlashIndex);
        }

        return input;
    }
}

import java.util.ArrayList;
import java.util.List;

public class KMP {
    public static List<AhoCorasick.Match> searchAll(String text, List<String> patterns) {
        List<AhoCorasick.Match> matches = new ArrayList<>();
        for (int i = 0; i < patterns.size(); i++) {
            String pattern = patterns.get(i);
            int[] prefix = prefixFunction(pattern);
            int matched = 0;
            for (int j = 0; j < text.length(); j++) {
                while (matched > 0 && text.charAt(j) != pattern.charAt(matched)) {
                    matched = prefix[matched - 1];
                }
                if (text.charAt(j) == pattern.charAt(matched)) {
                    matched++;
                }
                if (matched == pattern.length()) {
                    matches.add(new AhoCorasick.Match(i, j - pattern.length() + 1));
                    matched = prefix[matched - 1];
                }
            }
        }
        return matches;
    }

    public static long countMatches(String text, List<String> patterns) {
        long count = 0;
        for (String pattern : patterns) {
            int[] prefix = prefixFunction(pattern);
            int matched = 0;
            for (int j = 0; j < text.length(); j++) {
                while (matched > 0 && text.charAt(j) != pattern.charAt(matched)) {
                    matched = prefix[matched - 1];
                }
                if (text.charAt(j) == pattern.charAt(matched)) {
                    matched++;
                }
                if (matched == pattern.length()) {
                    count++;
                    matched = prefix[matched - 1];
                }
            }
        }
        return count;
    }

    private static int[] prefixFunction(String pattern) {
        int[] prefix = new int[pattern.length()];
        int length = 0;
        for (int i = 1; i < pattern.length(); i++) {
            while (length > 0 && pattern.charAt(i) != pattern.charAt(length)) {
                length = prefix[length - 1];
            }
            if (pattern.charAt(i) == pattern.charAt(length)) {
                length++;
            }
            prefix[i] = length;
        }
        return prefix;
    }
}

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TestCases {
    private static void check(String text, List<String> patterns) {
        AhoCorasick ac = new AhoCorasick(patterns);
        List<String> actual = normalize(ac.search(text));
        List<String> expected = normalize(KMP.searchAll(text, patterns));
        if (!actual.equals(expected)) {
            throw new AssertionError("Different results for text=" + text + ", patterns=" + patterns);
        }
        if (actual.size() != ac.countMatches(text)) {
            throw new AssertionError("Count is not equal to number of matches");
        }
    }

    private static List<String> normalize(List<AhoCorasick.Match> matches) {
        List<String> result = new ArrayList<>();
        for (AhoCorasick.Match match : matches) {
            result.add(match.patternIndex + ":" + match.position);
        }
        Collections.sort(result);
        return result;
    }

    public static void main(String[] args) {
        check("ahishers", Arrays.asList("he", "she", "his", "hers"));
        check("aaaa", Arrays.asList("a", "aa", "aaa"));
        check("banana", Arrays.asList("ana", "na", "ban"));
        check("abc", Arrays.asList("abc", "abc", "bc"));
        check("xyz", Arrays.asList("a", "ab", "bc"));
        check("", Arrays.asList("a", "ab"));
        boolean rejectedEmpty = false;
        try {
            new AhoCorasick(Arrays.asList("a", ""));
        } catch (IllegalArgumentException exception) {
            rejectedEmpty = true;
        }
        if (!rejectedEmpty) {
            throw new AssertionError("Empty pattern should be rejected");
        }
        System.out.println("All 7 correctness tests passed.");
    }
}

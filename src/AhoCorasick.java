import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public class AhoCorasick {
    private static class Node {
        Map<Character, Node> children = new HashMap<>();
        Node failure;
        List<Integer> output = new ArrayList<>();
    }

    public static class Match {
        public final int patternIndex;
        public final int position;

        public Match(int patternIndex, int position) {
            this.patternIndex = patternIndex;
            this.position = position;
        }

        @Override
        public String toString() {
            return "pattern=" + patternIndex + ", position=" + position;
        }
    }

    private final Node root = new Node();
    private final List<String> patterns;

    public AhoCorasick(List<String> patterns) {
        this.patterns = new ArrayList<>(patterns);
        for (int i = 0; i < patterns.size(); i++) {
            String pattern = patterns.get(i);
            if (pattern.isEmpty()) {
                throw new IllegalArgumentException("Empty patterns are not supported");
            }
            Node current = root;
            for (char c : pattern.toCharArray()) {
                current = current.children.computeIfAbsent(c, key -> new Node());
            }
            current.output.add(i);
        }
        buildFailureLinks();
    }

    private void buildFailureLinks() {
        Queue<Node> queue = new ArrayDeque<>();
        root.failure = root;
        for (Node child : root.children.values()) {
            child.failure = root;
            queue.add(child);
        }
        while (!queue.isEmpty()) {
            Node current = queue.remove();
            for (Map.Entry<Character, Node> entry : current.children.entrySet()) {
                char c = entry.getKey();
                Node child = entry.getValue();
                Node fallback = current.failure;
                while (fallback != root && !fallback.children.containsKey(c)) {
                    fallback = fallback.failure;
                }
                child.failure = fallback.children.getOrDefault(c, root);
                child.output.addAll(child.failure.output);
                queue.add(child);
            }
        }
    }

    public List<Match> search(String text) {
        List<Match> matches = new ArrayList<>();
        scan(text, (patternIndex, position) -> matches.add(new Match(patternIndex, position)));
        return matches;
    }

    public long countMatches(String text) {
        long[] count = {0};
        scan(text, (patternIndex, position) -> count[0]++);
        return count[0];
    }

    private void scan(String text, MatchConsumer consumer) {
        Node current = root;
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            while (current != root && !current.children.containsKey(c)) {
                current = current.failure;
            }
            current = current.children.getOrDefault(c, root);
            for (int patternIndex : current.output) {
                consumer.accept(patternIndex, i - patterns.get(patternIndex).length() + 1);
            }
        }
    }

    private interface MatchConsumer {
        void accept(int patternIndex, int position);
    }
}

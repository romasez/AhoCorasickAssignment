import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class Benchmark {
    private static final int[] PATTERN_COUNTS = {10, 100, 1000};
    private static final int[] TEXT_LENGTHS = {1000, 10000, 100000};
    private static final int RUNS = 5;

    private static String randomString(Random random, int length) {
        StringBuilder builder = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            builder.append((char) ('a' + random.nextInt(26)));
        }
        return builder.toString();
    }

    private static List<String> makePatterns(Random random, int count) {
        List<String> patterns = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            patterns.add(randomString(random, 5 + random.nextInt(8)));
        }
        return patterns;
    }

    private static String makeText(Random random, int length, List<String> patterns) {
        StringBuilder text = new StringBuilder(randomString(random, length));
        for (int i = 0; i < Math.min(20, patterns.size()); i++) {
            String pattern = patterns.get(i);
            int position = random.nextInt(length - pattern.length() + 1);
            text.replace(position, position + pattern.length(), pattern);
        }
        return text.toString();
    }

    private static long runAho(String text, List<String> patterns) {
        AhoCorasick ac = new AhoCorasick(patterns);
        return ac.countMatches(text);
    }

    private static long runKmp(String text, List<String> patterns) {
        return KMP.countMatches(text, patterns);
    }

    public static void main(String[] args) throws IOException {
        File directory = new File("results");
        directory.mkdirs();
        try (FileWriter writer = new FileWriter(new File(directory, "benchmark.csv"))) {
            writer.write("patterns,text_length,aho_avg_ms,kmp_avg_ms,matches\n");
            System.out.println("Patterns | Text length | Aho avg (ms) | KMP avg (ms) | Matches");
            for (int count : PATTERN_COUNTS) {
                for (int length : TEXT_LENGTHS) {
                    Random random = new Random(2026L + count * 100000L + length);
                    List<String> patterns = makePatterns(random, count);
                    String text = makeText(random, length, patterns);
                    for (int i = 0; i < 3; i++) {
                        runAho(text, patterns);
                        runKmp(text, patterns);
                    }
                    long ahoTotal = 0;
                    long kmpTotal = 0;
                    long matches = -1;
                    for (int i = 0; i < RUNS; i++) {
                        long start = System.nanoTime();
                        long ahoMatches = runAho(text, patterns);
                        ahoTotal += System.nanoTime() - start;
                        start = System.nanoTime();
                        long kmpMatches = runKmp(text, patterns);
                        kmpTotal += System.nanoTime() - start;
                        if (ahoMatches != kmpMatches) {
                            throw new AssertionError("Result mismatch for " + count + ", " + length);
                        }
                        matches = ahoMatches;
                    }
                    double ahoMs = ahoTotal / (RUNS * 1000000.0);
                    double kmpMs = kmpTotal / (RUNS * 1000000.0);
                    String row = String.format(Locale.US, "%d,%d,%.4f,%.4f,%d%n", count, length, ahoMs, kmpMs, matches);
                    writer.write(row);
                    System.out.printf(Locale.US, "%8d | %11d | %12.4f | %12.4f | %d%n", count, length, ahoMs, kmpMs, matches);
                }
            }
        }
        System.out.println("Saved results/benchmark.csv");
    }
}

# Aho-Corasick Algorithm in Java

A simple second-year university project that searches for multiple patterns in one text and compares Aho-Corasick with KMP.

## Project files

- `src/AhoCorasick.java` — trie, failure links, outputs, and all match positions.
- `src/KMP.java` — KMP search for each pattern separately.
- `src/TestCases.java` — 7 correctness tests, including overlapping and duplicate patterns.
- `src/Benchmark.java` — 9 experiments, each run 5 times.
- `results/benchmark.csv` — actual benchmark results.
- `REPORT.md` — short experiment report.
- `HANDWRITTEN_EXPLANATION.md` — text to rewrite by hand in your own words.

## How to run

Requires Java 17 or newer. From the project folder:

```bash
javac -d out src/*.java
java -cp out TestCases
java -cp out Benchmark
```

The benchmark automatically rewrites `results/benchmark.csv` with results from your computer.

## How it works

1. **Trie:** Every pattern is inserted into the trie. Patterns that share prefixes share nodes.
2. **Failure links:** Breadth-first search builds links to the longest available suffix when a trie transition cannot continue.
3. **Outputs:** Each node stores pattern IDs that end there. A node also inherits output IDs from its failure node, so suffix matches are not missed.
4. **Search:** Scan the text once. Follow trie transitions or failure links and report all matching patterns and their starting indexes (zero-based).

For example, with text `ahishers` and patterns `he`, `she`, `his`, `hers`, the program can find all four matching occurrences, including the overlap between `she` and `he`.

## Complexity

Let `n` = text length, `p` = number of patterns, `S` = total length of all patterns, and `z` = number of matches.

- **Aho-Corasick search:** `O(n + z)` after preprocessing, using amortized failure transitions.
- **Trie/failure link preprocessing:** commonly described as `O(S)` for a fixed-size alphabet and standard construction; actual costs depend on how trie transitions and outputs are represented.
- **Memory:** `O(S)` for trie nodes and failure pointers, plus stored output lists. This implementation copies failure outputs into nodes; in the worst case, these duplicated output IDs can require `O(S * p)` additional storage. A separate output-link implementation can reduce this overhead.
- **KMP for all patterns:** `O(p * n + S)` total time, `O(maximum pattern length)` working memory when patterns are processed one by one, excluding the input and stored matches.

## Comparison

Aho-Corasick preprocesses all patterns together and scans the text once, making it useful for keyword filtering, dictionary matching, and detecting many terms in a document. KMP is simpler and can use less memory for one or a few patterns, but repeating KMP for every pattern scans the same text many times.

## Experimental setup

- Patterns: 10, 100, 1,000.
- Text lengths: 1,000, 10,000, 100,000 characters.
- Input: repeatable seeded random lowercase English letters; pattern lengths 5–12; up to 20 patterns inserted into each text.
- Runs: 3 unmeasured warm-up iterations, then 5 measured iterations per case.
- Timing: `System.nanoTime()`; averages reported in milliseconds.
- Timing includes pattern preprocessing and searching for both algorithms; printing and data generation are excluded.
- Correctness: matched-occurrence counts are compared during every benchmark run.

Results are machine-dependent and can vary because of JIT compilation, garbage collection, and operating system scheduling. See `REPORT.md` and `results/benchmark.csv`.

## Submission

Create your own GitHub repository, upload the project contents, and submit your repository link in Moodle. Write the handwritten explanation separately on paper and include it with the submission as instructed by your teacher.

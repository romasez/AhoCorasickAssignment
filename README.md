# Aho-Corasick Algorithm — Experiment Report

## 1. Objective

The goal is to implement the Aho-Corasick algorithm and compare it with the KMP algorithm. The program searches for all occurrences of multiple patterns in a text.

## 2. Implementation

I created a trie from all the patterns. Every node has child nodes, a failure link, and a list of pattern IDs that end at this node. Failure links are built with breadth-first search. During searching, the program reads the text one character at a time and follows failure links when necessary. For comparison, KMP searches the same text separately for each pattern.

## 3. Test Method

I used 10, 100, and 1,000 patterns and text lengths of 1,000, 10,000, and 100,000. The text and patterns were generated using a fixed random seed. Each test was warmed up 3 times and measured 5 times with `System.nanoTime()`. The average includes building the search structure and searching the text. Both implementations returned the same match counts.

## 4. Results

| Patterns | Text length | Aho-Corasick (ms) | KMP (ms) |
|---:|---:|---:|---:|
| 10 | 1,000 | 0.3927 | 0.6520 |
| 10 | 10,000 | 0.8889 | 0.4873 |
| 10 | 100,000 | 1.5223 | 3.9556 |
| 100 | 1,000 | 0.7547 | 0.4007 |
| 100 | 10,000 | 0.8234 | 3.9481 |
| 100 | 100,000 | 2.6900 | 42.4513 |
| 1,000 | 1,000 | 1.6618 | 3.8804 |
| 1,000 | 10,000 | 2.0729 | 44.3854 |
| 1,000 | 100,000 | 3.2356 | 411.6796 |

## 5. Complexity

If `n` is the text length, `p` is the number of patterns, `S` is their total length, and `z` is the number of matches, Aho-Corasick searching takes `O(n + z)` after preprocessing. Standard trie and failure construction is approximately `O(S)` for a fixed alphabet. Trie nodes require `O(S)` memory, but this program also copies match lists from failure nodes, which can use extra memory. Running KMP separately takes `O(p * n + S)` time.

## 6. Discussion and Conclusion

For a small number of patterns, the algorithms had similar results, and KMP was faster in two small tests. When the number of patterns increased, Aho-Corasick became much faster because it scans the text only once. For 1,000 patterns and 100,000 characters, Aho-Corasick took about 3.24 ms, while KMP took about 411.68 ms. Aho-Corasick is a good choice for many patterns, while KMP is easier and may be preferable for one pattern. Exact timings depend on the computer and JVM.

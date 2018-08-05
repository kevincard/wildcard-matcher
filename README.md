# Fast Wildcard Matcher
A fast implementation of wildcard matching basing on BackTracing algorithm

## Usage: 

### Class: FastWildcardMatcher 
* public static boolean match(final CharSequence source, final CharSequence pattern);
* Checks whether a string or path matches a given wildcard pattern. Possible patterns allow to match single characters
 ('?') or any count of characters ('*'). Wildcard characters can be escaped (by an '\\'). When matching path, deep tree
 wildcard also can be used ('**').
 
### Class: FastWildcardMatcherV2
* public static boolean match(final CharSequence source, final CharSequence pattern);
* Similar with FastWildcardMatcher, expect that '?' also matches zero character.

## Performance
 * Average time complexity: O(n)
 * Space complexity: O(1)

 * Performance comparing to similar implementations according to JMH, see [MyBenchmark](https://github.com/kevincard/wildcard-matcher/blob/master/wildcard-matcher-test/src/main/java/com/github/softhouse/utils/wildcard/MyBenchmark.java)

|Benchmark                 |              Mode | Cnt  |      Score |       Error | Units|
|-|-|-|-|-|-|
|MyBenchmark.wildcardMatcherFromJobb   | thrpt |   3 |  423263.765 ±  50034.316 || ops/s|
|MyBenchmark.wildcardMatcherMyVersion1 | thrpt  |  3 | 1981307.913 ±  94699.304 || ops/s|
|MyBenchmark.wildcardMatcherMyVersion2 | thrpt  |  3 | 2181474.899 ± 248855.801 || ops/s|
|MyBenchmark.wildcardMatcherSimpleBackTrace     | thrpt  |  3 | 1184048.548 ± 227573.587 || ops/s|

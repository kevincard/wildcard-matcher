package com.github.softhouse.utils.wildcard.utils.wildcard;

import org.testng.Assert;
import org.testng.annotations.Test;

public class FastWildcardMatcherTest {

    @Test
    public void matchStringVersion1() {
        Assert.assertTrue(FastWildcardMatcher.match("abc", "*"));
        Assert.assertTrue(FastWildcardMatcher.match("abc", "*c"));
        Assert.assertTrue(FastWildcardMatcher.match("abc", "a*"));
        Assert.assertTrue(FastWildcardMatcher.match("abc", "a*c"));

        Assert.assertTrue(FastWildcardMatcher.match("abc", "a?c"));
        Assert.assertTrue(FastWildcardMatcher.match("abcd", "a?c?"));
        Assert.assertTrue(FastWildcardMatcher.match("abcd", "a??d"));

        Assert.assertTrue(FastWildcardMatcher.match("abcde", "a*d?"));
        Assert.assertTrue(FastWildcardMatcher.match("abc.abcd", "a*c*bcd"));
        Assert.assertTrue(FastWildcardMatcher.match("abc.abab", "ab*.?bab"));

        Assert.assertFalse(FastWildcardMatcher.match("abc.abcd", "a*ab"));

        Assert.assertFalse(FastWildcardMatcher.match("abcde", "a*d"));
        Assert.assertFalse(FastWildcardMatcher.match("abcde", "a*x"));
        Assert.assertFalse(FastWildcardMatcher.match("abcde", "a*df"));

        Assert.assertFalse(FastWildcardMatcher.match("abcde", "?abcd"));

        Assert.assertTrue(FastWildcardMatcher.match("ab\\\\*cde", "ab\\\\*c*"));
        Assert.assertTrue(FastWildcardMatcher.match("ab\\\\*cde", "ab\\\\*?de"));

    }

    @Test
    public void matchStringVersion2() {
        Assert.assertTrue(FastWildcardMatcherV2.match("abc", "*?"));
        Assert.assertTrue(FastWildcardMatcherV2.match("abc", "*c?"));
        Assert.assertTrue(FastWildcardMatcherV2.match("abc", "???"));
        Assert.assertTrue(FastWildcardMatcherV2.match("abc", "????"));
    }

    @Test
    public void matchPath() {
        // matchOne
        Assert.assertEquals(FastWildcardMatcher.matchOne("abcde", "a*d?", "abde?"), 0);
        Assert.assertEquals(FastWildcardMatcher.matchOne("abcde", "?abcd", "a*d?"), 1);
        Assert.assertEquals(FastWildcardMatcher.matchOne("abcde", "?abcd", "xyz*"), -1);

        Assert.assertTrue(FastWildcardMatcher.matchPath("/a/b/dd", "**"));

        Assert.assertTrue(FastWildcardMatcher.matchPath("/a/b/dd", "**/dd"));
        Assert.assertTrue(FastWildcardMatcher.matchPath("/a/b/c/dd", "/a/**/dd"));
        Assert.assertTrue(FastWildcardMatcher.matchPath("/a/b/dd", "/a/*/dd"));
        Assert.assertTrue(FastWildcardMatcher.matchPath("/a/b/dd", "/a/*/d?"));
        Assert.assertTrue(FastWildcardMatcher.matchPath("/a/b/ddxxa", "/a/*/dd*"));
        Assert.assertTrue(FastWildcardMatcher.matchPath("/a/b/ddxxa", "/a/?/dd*"));
        Assert.assertTrue(FastWildcardMatcher.matchPath("a/b/ddxxa", "a/?/dd*"));
        Assert.assertTrue(FastWildcardMatcher.matchPath("a/b/dd", "**/dd"));

        Assert.assertFalse(FastWildcardMatcher.matchPath("/a/b/c/dd", "/a/*/dd"));
        Assert.assertFalse(FastWildcardMatcher.matchPath("\\a\\b\\c\\dd", "/a/*/dd"));
        Assert.assertFalse(FastWildcardMatcher.matchPath("/a/b/c/dd", "/a/*/dd"));

        // matchOne
        Assert.assertEquals(FastWildcardMatcher.matchPathOne("/a/b/c/dd", "/a/*/dd", "**/dd"), 1);
        Assert.assertEquals(FastWildcardMatcher.matchPathOne("/a/b/c/dd", "/a/**/dd", "**/dd"), 0);
        Assert.assertEquals(FastWildcardMatcher.matchPathOne("/a/b/c/dd", "/b/d", "/a/c/*"), -1);

        Assert.assertEquals(FastWildcardMatcher.matchPathOne("\\a\\b\\c\\dd", "/a/*/dd", "**\\dd"), 1);
        Assert.assertEquals(FastWildcardMatcher.matchPathOne("\\a\\b\\c\\dd", "\\a\\**\\dd", "**\\dd"), 0);
        Assert.assertEquals(FastWildcardMatcher.matchPathOne("\\a\\b\\c\\dd", "/b/d", "/a/c/*"), -1);

        Assert.assertEquals(FastWildcardMatcher.matchPathOne("/a/b/c/dd", "/a/*/dd", "**/dd"), 1);
        Assert.assertEquals(FastWildcardMatcher.matchPathOne("/a/b/c/dd", "/a/**/dd", "**/dd"), 0);
        Assert.assertEquals(FastWildcardMatcher.matchPathOne("/a/b/c/dd", "/b/d", "/a/c/*"), -1);
    }
    
}

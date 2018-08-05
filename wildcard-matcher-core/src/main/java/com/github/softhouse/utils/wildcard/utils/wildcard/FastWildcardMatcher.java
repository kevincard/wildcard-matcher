package com.github.softhouse.utils.wildcard.utils.wildcard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by kevin02.zhou on 2018/7/31.
 */
public class FastWildcardMatcher {

    public static boolean match(final CharSequence source, final CharSequence pattern) {
        int patternLength = pattern.length();
        if (patternLength == 1) {
            if (pattern.charAt(0) == '*') {
                return true;
            }
        }
        if (patternLength == 0) {
            return source.length() == 0;
        }
        int sourceLength = source.length();

        //try matching from the end
        int sIndex = sourceLength - 1;
        int pIndex = patternLength - 1;
        while (sIndex >= 0) {
            if (pIndex >= 0) {
                final char pChar = pattern.charAt(pIndex);
                if (pChar == '*') {
                    break;
                }
                else if (pChar == '?' || pChar == source.charAt(sIndex)) {
                    pIndex--;
                    sIndex--;
                }
                else {
                    return false;
                }
            }
            else {
                return false;
            }
        }
        patternLength = pIndex + 1;
        sourceLength = sIndex + 1;
        sIndex = 0;
        pIndex = 0;
        int pCheckPoint = -1;
        int sCheckPoint = -1;

        char pChar = 0;
        boolean nextIsNotWildcard = false;
        while (sIndex < sourceLength) {
            if (pIndex < patternLength) {
                pChar = pattern.charAt(pIndex);
                if (!nextIsNotWildcard) {
                    if (pChar == '*') {
                        pCheckPoint = ++pIndex;
                        sCheckPoint = sIndex + 1;
                        continue;
                    }
                    else if (pChar == '?') {
                        sIndex++;
                        pIndex++;
                        continue;
                    }
                    else if (pChar == '\\') {
                        pIndex++;
                        nextIsNotWildcard = true;
                        continue;
                    }
                }
                else {
                    nextIsNotWildcard = false;
                }

                if (pChar == source.charAt(sIndex)) {
                    sIndex++;
                    pIndex++;
                }
                else if (pCheckPoint != -1) {
                    if (pIndex > pCheckPoint) {
                        final int sNextIndex = sIndex + 1;
                        if (sNextIndex < sourceLength) {
                            final char sNextChar = source.charAt(sNextIndex);
                            int i = pIndex;
                            while (i >= pCheckPoint) {
                                if (sNextChar == pChar) {
                                    break;
                                }
                                pChar = pattern.charAt(--i);
                            }
                            sIndex = sCheckPoint + pIndex - i;
                            sCheckPoint = sIndex + 1;
                            pIndex = pCheckPoint;
                        }
                        else {
                            return false;
                        }
                    }
                    else {
                        pIndex = pCheckPoint;
                        sIndex = sCheckPoint++;
                    }
                }
                else {
                    return false;
                }
            }
            else {
                if (pChar == '*') {
                    return true;
                }
                else if (pCheckPoint != -1) {
                    pIndex = pCheckPoint;
                    sIndex = sourceLength - (patternLength - pCheckPoint);
                }
            }
        }

        while (pIndex < patternLength && pattern.charAt(pIndex) == '*') {
            ++pIndex;
        }

        return pIndex == patternLength;
    }

    /**
     * Matches string to at least one pattern. Returns index of matched pattern, or <code>-1</code> otherwise.
     */
    public static int matchOne(final String src, final String... patterns) {
        for (int i = 0; i < patterns.length; i++) {
            if (match(src, patterns[i])) {
                return i;
            }
        }
        return -1;
    }

    // ---------------------------------------------------------------- path

    private static final String PATH_MATCH = "**";
    private static final String PATH_SPLITTER_CHARS = "/\\";

    /**
     * Matches path to at least one pattern. Returns index of matched pattern or <code>-1</code> otherwise.
     * @see #matchPath(String, String)
     */
    public static int matchPathOne(final String platformDependentPath, final String... patterns) {
        for (int i = 0; i < patterns.length; i++) {
            if (matchPath(platformDependentPath, patterns[i])) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Matches path against pattern using *, ? and ** wildcards. Both path and the pattern are tokenized on path
     * separators (both \ and /). '**' represents deep tree wildcard, as in Ant. The separator should match the
     * corresponding path
     */
    public static boolean matchPath(final String path, final String pattern) {
        final List<String> pathElements = split(path, PATH_SPLITTER_CHARS);
        final List<String> patternElements = split(pattern, PATH_SPLITTER_CHARS);
        return matchTokens(pathElements.toArray(new String[0]), patternElements.toArray(new String[0]));
    }

    /**
     * Match tokenized string and pattern.
     */
    protected static boolean matchTokens(final String[] tokens, final String[] patterns) {
        int patNdxStart = 0;
        int patNdxEnd = patterns.length - 1;
        int tokNdxStart = 0;
        int tokNdxEnd = tokens.length - 1;

        while ((patNdxStart <= patNdxEnd) && (tokNdxStart <= tokNdxEnd)) { // find first **
            final String patDir = patterns[patNdxStart];
            if (patDir.equals(PATH_MATCH)) {
                break;
            }
            if (!match(tokens[tokNdxStart], patDir)) {
                return false;
            }
            patNdxStart++;
            tokNdxStart++;
        }
        if (tokNdxStart > tokNdxEnd) {
            for (int i = patNdxStart; i <= patNdxEnd; i++) { // string is finished
                if (!patterns[i].equals(PATH_MATCH)) {
                    return false;
                }
            }
            return true;
        }
        if (patNdxStart > patNdxEnd) {
            return false; // string is not finished, but pattern is
        }

        while ((patNdxStart <= patNdxEnd) && (tokNdxStart <= tokNdxEnd)) { // to the last **
            final String patDir = patterns[patNdxEnd];
            if (patDir.equals(PATH_MATCH)) {
                break;
            }
            if (!match(tokens[tokNdxEnd], patDir)) {
                return false;
            }
            patNdxEnd--;
            tokNdxEnd--;
        }
        if (tokNdxStart > tokNdxEnd) {
            for (int i = patNdxStart; i <= patNdxEnd; i++) { // string is finished
                if (!patterns[i].equals(PATH_MATCH)) {
                    return false;
                }
            }
            return true;
        }

        while ((patNdxStart != patNdxEnd) && (tokNdxStart <= tokNdxEnd)) {
            int patIdxTmp = -1;
            for (int i = patNdxStart + 1; i <= patNdxEnd; i++) {
                if (patterns[i].equals(PATH_MATCH)) {
                    patIdxTmp = i;
                    break;
                }
            }
            if (patIdxTmp == patNdxStart + 1) {
                patNdxStart++; // skip **/** situation
                continue;
            }
            // find the pattern between padIdxStart & padIdxTmp in str between strIdxStart & strIdxEnd
            final int patLength = (patIdxTmp - patNdxStart - 1);
            final int strLength = (tokNdxEnd - tokNdxStart + 1);
            int ndx = -1;
            strLoop: for (int i = 0; i <= strLength - patLength; i++) {
                for (int j = 0; j < patLength; j++) {
                    final String subPat = patterns[patNdxStart + j + 1];
                    final String subStr = tokens[tokNdxStart + i + j];
                    if (!match(subStr, subPat)) {
                        continue strLoop;
                    }
                }

                ndx = tokNdxStart + i;
                break;
                // this is a double-loop, cannot be refactor to break statement directly
            }

            if (ndx == -1) {
                return false;
            }

            patNdxStart = patIdxTmp;
            tokNdxStart = ndx + patLength;
        }

        for (int i = patNdxStart; i <= patNdxEnd; i++) {
            if (!patterns[i].equals(PATH_MATCH)) {
                return false;
            }
        }

        return true;
    }

    public static List<String> split(final String str, final String separatorChars) {
        return splitWorker(str, separatorChars, -1, false);
    }

    private static List<String> splitWorker(final String str, final String separatorChars, final int max, final boolean preserveAllTokens) {
        // Performance tuned for 2.0 (JDK1.4)
        // Direct code is quicker than StringTokenizer.
        // Also, StringTokenizer uses isSpace() not isWhitespace()

        if (str == null) {
            return null;
        }
        final int len = str.length();
        if (len == 0) {
            return Collections.emptyList();
        }
        final List<String> list = new ArrayList<>(10);
        int sizePlus1 = 1;
        int i = 0, start = 0;
        boolean match = false;
        boolean lastMatch = false;
        if (separatorChars == null) {
            // Null separator means use whitespace
            while (i < len) {
                if (Character.isWhitespace(str.charAt(i))) {
                    if (match || preserveAllTokens) {
                        lastMatch = true;
                        if (sizePlus1++ == max) {
                            i = len;
                            lastMatch = false;
                        }
                        list.add(str.substring(start, i));
                        match = false;
                    }
                    start = ++i;
                    continue;
                }
                lastMatch = false;
                match = true;
                i++;
            }
        } else if (separatorChars.length() == 1) {
            // Optimise 1 character case
            final char sep = separatorChars.charAt(0);
            while (i < len) {
                if (str.charAt(i) == sep) {
                    if (match || preserveAllTokens) {
                        lastMatch = true;
                        if (sizePlus1++ == max) {
                            i = len;
                            lastMatch = false;
                        }
                        list.add(str.substring(start, i));
                        match = false;
                    }
                    start = ++i;
                    continue;
                }
                lastMatch = false;
                match = true;
                i++;
            }
        } else {
            // standard case
            while (i < len) {
                if (separatorChars.indexOf(str.charAt(i)) >= 0) {
                    if (match || preserveAllTokens) {
                        lastMatch = true;
                        if (sizePlus1++ == max) {
                            i = len;
                            lastMatch = false;
                        }
                        list.add(str.substring(start, i));
                        match = false;
                    }
                    start = ++i;
                    continue;
                }
                lastMatch = false;
                match = true;
                i++;
            }
        }
        if (match || preserveAllTokens && lastMatch) {
            list.add(str.substring(start, i));
        }
        return list;
    }
}
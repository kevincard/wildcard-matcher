package com.github.softhouse.utils.wildcard.utils.wildcard;

/**
 * '*' matches any number of chars
 * '?' matches zero or single char
 * Created by kevin02.zhou on 2018/7/31.
 */
public class FastWildcardMatcherV2 {

    public static boolean match(final CharSequence source, final CharSequence pattern) {
        final int patternLength = pattern.length();
        if (patternLength == 1) {
            if (pattern.charAt(0) == '*') {
                return true;
            }
        }
        final int sourceLength = source.length();
        if (patternLength == 0) {
            return false;
        }

        int sIndex = sourceLength - 1;
        int pIndex = patternLength - 1;
        char pChar;
        while (sIndex >= 0) {
            if (pIndex >= 0) {
                pChar = pattern.charAt(pIndex);
                if (pChar == '*' || pChar == '?') {
                    break;
                }
                else if (pChar == source.charAt(sIndex)) {
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
        return match(source, pattern, 0, 0, sIndex + 1, pIndex + 1);
    }

    public static boolean match(final CharSequence source, final CharSequence pattern, final int sBeginIndex, final int pBeginIndex, final int sourceLength, final int patternLength) {
        int sIndex = sBeginIndex;
        int pIndex = pBeginIndex;
        int pCheckPoint = -1;
        int sCheckPoint = -1;

        char pChar = 0;
        boolean nextIsNotWildcard = false;
        while (sIndex < sourceLength) {
            if (pIndex < patternLength) {
                pChar = pattern.charAt(pIndex);
                if (nextIsNotWildcard) {
                    nextIsNotWildcard = false;
                }
                else {
                    if (pChar == '\\') {
                        nextIsNotWildcard = true;
                        pIndex++;
                        continue;
                    }
                    else if (pChar == '*') {
                        pCheckPoint = ++pIndex;
                        sCheckPoint = sIndex + 1;
                        continue;
                    }
                    else if (pChar == '?') {
                        // if '?' follows the checkpoint, just more forward the checkpoint
                        if (pCheckPoint == pIndex) {
                            pCheckPoint++;
                            pIndex++;
                        }
                        else {
                            //check whether substring(index) or substring(index+1) matches the rest of the pattern
                            final int pNext = pIndex + 1;
                            final boolean match = pNext >= patternLength ||
                                    match(source, pattern, sIndex, pNext, sourceLength, patternLength) ||
                                    match(source, pattern, sIndex + 1, pNext, sourceLength, patternLength);
                            if (match) {
                                return true;
                            }
                            else if (pCheckPoint != -1) {
                                pIndex = pCheckPoint;
                                sIndex = sCheckPoint++;
                            }
                            else {
                                return false;
                            }
                        }
                        continue;
                    }
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
                            while (i>=pCheckPoint) {
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
                    sIndex = sCheckPoint++;
                }
                else {
                    return false;
                }
            }
        }

        while (pIndex < patternLength && ((pChar = pattern.charAt(pIndex)) == '*' || (pChar == '?'))) {
            ++pIndex;
        }

        return pIndex == patternLength;
    }

}
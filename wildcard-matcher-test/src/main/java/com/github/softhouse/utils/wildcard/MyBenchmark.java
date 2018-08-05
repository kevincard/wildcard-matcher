/*
 * Copyright (c) 2014, Oracle America, Inc.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  * Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 *  * Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *
 *  * Neither the name of Oracle nor the names of its contributors may be used
 *    to endorse or promote products derived from this software without
 *    specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
 * THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.github.softhouse.utils.wildcard;

import com.github.softhouse.utils.wildcard.utils.wildcard.FastWildcardMatcher;
import com.github.softhouse.utils.wildcard.utils.wildcard.FastWildcardMatcherV2;
import com.vip.vjtools.vjkit.text.WildcardMatcher;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.infra.Blackhole;

public class MyBenchmark {

    public final static String TEST_STRING = "com.github.softhouse.softhouse";
    public final static String TEST_PATTERN1 = "*.github.softhouse";
    public final static String TEST_PATTERN2 = "com.github.softhouse.*";
    public final static String TEST_PATTERN3 = "com.*.softhouse.softhouse";
    public final static String TEST_PATTERN4 = "*.*?softhouse.*";
    public final static String TEST_PATTERN5 = "com.github.*house";
    public final static String TEST_PATTERN6 = "com.github?so??house.softhouse";
    public final static String TEST_PATTERN7 = "*?so??soft.house";
    public final static String TEST_PATTERN8 = "com.github.softhouse.utils";

    @Benchmark
    public void wildcardMatcherFromJobb(final Blackhole blackhole) {
        blackhole.consume(WildcardMatcher.match(TEST_STRING, TEST_PATTERN1));
        blackhole.consume(WildcardMatcher.match(TEST_STRING, TEST_PATTERN2));
        blackhole.consume(WildcardMatcher.match(TEST_STRING, TEST_PATTERN3));
        blackhole.consume(WildcardMatcher.match(TEST_STRING, TEST_PATTERN4));
        blackhole.consume(WildcardMatcher.match(TEST_STRING, TEST_PATTERN5));
        blackhole.consume(WildcardMatcher.match(TEST_STRING, TEST_PATTERN6));
        blackhole.consume(WildcardMatcher.match(TEST_STRING, TEST_PATTERN7));
        blackhole.consume(WildcardMatcher.match(TEST_STRING, TEST_PATTERN8));
    }

    @Benchmark
    public void wildcardMatcherSimple(final Blackhole blackhole) {
        blackhole.consume(SimpleWildcardMatcher.match(TEST_STRING, TEST_PATTERN1));
        blackhole.consume(SimpleWildcardMatcher.match(TEST_STRING, TEST_PATTERN2));
        blackhole.consume(SimpleWildcardMatcher.match(TEST_STRING, TEST_PATTERN3));
        blackhole.consume(SimpleWildcardMatcher.match(TEST_STRING, TEST_PATTERN4));
        blackhole.consume(SimpleWildcardMatcher.match(TEST_STRING, TEST_PATTERN5));
        blackhole.consume(SimpleWildcardMatcher.match(TEST_STRING, TEST_PATTERN6));
        blackhole.consume(SimpleWildcardMatcher.match(TEST_STRING, TEST_PATTERN7));
        blackhole.consume(SimpleWildcardMatcher.match(TEST_STRING, TEST_PATTERN8));
    }

    @Benchmark
    public void wildcardMatcherMyVersion1(final Blackhole blackhole) {
        blackhole.consume(FastWildcardMatcher.match(TEST_STRING, TEST_PATTERN1));
        blackhole.consume(FastWildcardMatcher.match(TEST_STRING, TEST_PATTERN2));
        blackhole.consume(FastWildcardMatcher.match(TEST_STRING, TEST_PATTERN3));
        blackhole.consume(FastWildcardMatcher.match(TEST_STRING, TEST_PATTERN4));
        blackhole.consume(FastWildcardMatcher.match(TEST_STRING, TEST_PATTERN5));
        blackhole.consume(FastWildcardMatcher.match(TEST_STRING, TEST_PATTERN6));
        blackhole.consume(FastWildcardMatcher.match(TEST_STRING, TEST_PATTERN7));
        blackhole.consume(FastWildcardMatcher.match(TEST_STRING, TEST_PATTERN8));
    }

    @Benchmark
    public void wildcardMatcherMyVersion2(final Blackhole blackhole) {
        blackhole.consume(FastWildcardMatcherV2.match(TEST_STRING, TEST_PATTERN1));
        blackhole.consume(FastWildcardMatcherV2.match(TEST_STRING, TEST_PATTERN2));
        blackhole.consume(FastWildcardMatcherV2.match(TEST_STRING, TEST_PATTERN3));
        blackhole.consume(FastWildcardMatcherV2.match(TEST_STRING, TEST_PATTERN4));
        blackhole.consume(FastWildcardMatcherV2.match(TEST_STRING, TEST_PATTERN5));
        blackhole.consume(FastWildcardMatcherV2.match(TEST_STRING, TEST_PATTERN6));
        blackhole.consume(FastWildcardMatcherV2.match(TEST_STRING, TEST_PATTERN7));
        blackhole.consume(FastWildcardMatcherV2.match(TEST_STRING, TEST_PATTERN8));
    }
}

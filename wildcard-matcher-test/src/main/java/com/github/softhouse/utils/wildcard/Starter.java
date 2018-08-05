package com.github.softhouse.utils.wildcard;

import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

public class Starter {

    public static void main(final String[] args) throws RunnerException {
        final Options opt = new OptionsBuilder().include(MyBenchmark.class.getSimpleName())
                .forks(1).warmupIterations(2)
                .measurementIterations(3).build();
        new Runner(opt).run();
    }
}

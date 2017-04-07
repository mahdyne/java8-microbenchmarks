package com.mahdyne.microbenchmarks;

import org.openjdk.jmh.annotations.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@State(Scope.Benchmark)
public class MyBenchmark {

    volatile int LIST_SIZE = 10_000_000;
    volatile List<Integer> list = null;

    public static void main(String[] args) {
        MyBenchmark benchmark = new MyBenchmark();
        benchmark.setup();

        System.out.println("for loop sum is: " + benchmark.forSum());
        System.out.println("stream sum is: " + benchmark.streamSum());
        System.out.println("parallel stream sum is: " + benchmark.parStreamSum());
    }

    @Setup
    public void setup() {
        list = IntStream.rangeClosed(1, LIST_SIZE).boxed().collect(Collectors.toList());
    }

    @TearDown
    public void cleanUp() {

    }

    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    @Fork(2)
    @Warmup(iterations = 2)
    @Measurement(iterations = 3)
    public long forSum() {
        long sum = 0;
        for (Integer i : list) {
            sum += i;
        }
        return sum;
    }

    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    @Fork(2)
    @Warmup(iterations = 2)
    @Measurement(iterations = 3)
    public long streamSum() {
        long sum = list.stream().mapToLong(i -> i).sum();
        return sum;
    }

    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    @Fork(2)
    @Warmup(iterations = 2)
    @Measurement(iterations = 3)
    public long parStreamSum() {
        long sum = list.parallelStream().mapToLong(i -> i).sum();
        return sum;
    }
}

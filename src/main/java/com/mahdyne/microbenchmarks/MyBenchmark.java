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

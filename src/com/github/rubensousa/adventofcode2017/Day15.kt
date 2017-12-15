package com.github.rubensousa.adventofcode2017

import java.io.IOException

object Day15 {

    @Throws(IOException::class)
    @JvmStatic
    fun main(args: Array<String>) {
        val factorA = 16807L
        val factorB = 48271L
        val a = 277L
        val b = 349L
        part1(a, b, factorA, factorB)
        part2(a, b, factorA, factorB)
    }

    private fun part1(a: Long, b: Long, factorA: Long, factorB: Long) {
        var count = 0
        var currentA = a
        var currentB = b
        repeat(40000000) {
            currentA = generate(currentA, factorA)
            currentB = generate(currentB, factorB)
            if (currentA.and(0xffff) == currentB.and(0xffff)) {
                count++
            }
        }
        println(count)
    }

    private fun part2(a: Long, b: Long, factorA: Long, factorB: Long) {
        var count = 0
        var currentA = a
        var currentB = b
        repeat(5000000) {
            while (currentA.rem(4L) != 0L) {
                currentA = generate(currentA, factorA)
            }
            while (currentB.rem(8L) != 0L) {
                currentB = generate(currentB, factorB)
            }
            if (currentA.and(0xffff) == currentB.and(0xffff)) {
                count++
            }
            currentA = generate(currentA, factorA)
            currentB = generate(currentB, factorB)
        }
        println(count)
    }

    private fun generate(previousValue: Long, factor: Long): Long {
        return (previousValue * factor) % Integer.MAX_VALUE
    }
}
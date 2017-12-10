package com.github.rubensousa.adventofcode2017

import java.io.BufferedReader
import java.io.FileReader


object Day01 {

    @JvmStatic
    fun main(args: Array<String>) {
        val bufferedReader = BufferedReader(FileReader("inputday01.txt"))
        val line: String? = bufferedReader.readLine()
        if (line != null) {
            println(getSumMatches(line))
            println(getSumMatchesSteps(line))
        }
    }

    private fun getSumMatches(number: String): Int {
        var sum = 0

        for (i in 0 until number.length) {
            var nextIndex = i + 1
            if (nextIndex >= number.length) {
                nextIndex -= number.length
            }
            val n1 = number[i] - '0'
            val n2 = number[nextIndex] - '0'
            if (n1 == n2) {
                sum += n1
            }
        }

        return sum
    }

    private fun getSumMatchesSteps(number: String): Int {
        var sum = 0
        val steps = number.length / 2

        for (i in 0 until number.length) {
            var nextIndex = i + steps
            if (nextIndex >= number.length) {
                nextIndex -= number.length
            }
            val n1 = number[i] - '0'
            val n2 = number[nextIndex] - '0'
            if (n1 == n2) {
                sum += n1
            }
        }

        return sum
    }
}
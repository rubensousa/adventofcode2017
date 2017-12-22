package com.github.rubensousa.adventofcode2017


import java.io.BufferedReader
import java.io.FileReader
import java.io.IOException
import java.util.ArrayList

object Day05 {

    @Throws(IOException::class)
    @JvmStatic
    fun main(args: Array<String>) {
        val bufferedReader = BufferedReader(FileReader("inputday05.txt"))
        var line: String? = bufferedReader.readLine()
        val integers = ArrayList<Int>()
        val integersSecond = ArrayList<Int>()
        while (line != null) {
            integers.add(line.toInt())
            integersSecond.add(line.toInt())
            line = bufferedReader.readLine()
        }
        println(countSteps(integers))
        println(countStepsPart2(integersSecond))
    }

    private fun countSteps(array: ArrayList<Int>): Int {
        var steps = 0
        var index = 0
        while (index < array.size) {
            val jumps = array[index]
            array[index]++
            index += jumps
            steps++
        }
        return steps
    }

    private fun countStepsPart2(array: ArrayList<Int>): Int {
        var steps = 0
        var index = 0
        while (index < array.size) {
            val jumps = array[index]
            if (jumps >= 3) {
                array[index]--
            } else {
                array[index]++
            }
            index += jumps
            steps++
        }
        return steps
    }

}
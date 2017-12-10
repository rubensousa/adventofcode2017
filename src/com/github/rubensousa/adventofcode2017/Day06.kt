package com.github.rubensousa.adventofcode2017


import java.io.BufferedReader
import java.io.FileReader
import java.io.IOException
import kotlin.collections.ArrayList
import kotlin.collections.HashSet

object Day06 {

    @Throws(IOException::class)
    @JvmStatic
    fun main(args: Array<String>) {
        val bufferedReader = BufferedReader(FileReader("inputday06.txt"))
        val line = bufferedReader.readLine()
        val data = line.split("\t")
        val array = ArrayList<Int>()
        val secondArray = ArrayList<Int>()

        for (number in data) {
            array.add(number.toInt())
            secondArray.add(number.toInt())
        }

        println(getSteps(array))
        println(getStepsLoop(secondArray))
    }

    private fun getSteps(array: ArrayList<Int>): Int {
        var steps = 0
        var seen = false
        val configs = HashSet<String>()
        while (!seen) {
            balanceArray(array)
            steps++

            val builder = StringBuilder()

            for (i in array.indices) {
                builder.append(array[i])
            }

            val config = builder.toString()
            seen = configs.contains(config)

            if (!seen) {
                configs.add(config)
            }
        }
        return steps
    }

    private fun getStepsLoop(array: ArrayList<Int>): Int {
        var steps = 0
        var seen = false
        val configs = HashSet<String>()
        var firstConfig = ""
        var firstConfigSteps = 0
        while (!seen) {
            balanceArray(array)
            steps++

            val builder = StringBuilder()

            for (i in array.indices) {
                builder.append(array[i])
            }

            val config = builder.toString()
            if (configs.contains(config)) {
                if (firstConfig.equals(config) && firstConfigSteps != 0) {
                    return steps - firstConfigSteps
                }
                if (firstConfig.isEmpty()) {
                    firstConfig = config
                    firstConfigSteps = steps
                }
            } else {
                configs.add(config)
            }
        }
        return steps
    }

    private fun balanceArray(array: ArrayList<Int>) {
        var max = Integer.MIN_VALUE
        var maxIndex = 0
        for (i in array.indices) {
            if (array[i] > max) {
                max = array[i]
                maxIndex = i
            }
        }
        array[maxIndex] = 0
        var index = maxIndex + 1
        while (max > 0) {
            if (index >= array.size) {
                index = 0
            }
            array[index]++
            max--
            index++
        }
    }
}
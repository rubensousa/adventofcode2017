package com.github.rubensousa.adventofcode2017

import java.io.BufferedReader
import java.io.FileReader


object Day02 {

    @JvmStatic
    fun main(args: Array<String>) {
        val bufferedReader = BufferedReader(FileReader("inputday02.txt"))
        var line: String? = bufferedReader.readLine()
        val array = ArrayList<ArrayList<Int>>()

        while (line != null) {
            val tempArray = ArrayList<Int>()
            val numbers = line.split(" ")
            for (number in numbers) {
                tempArray.add(number.toInt())
            }
            array.add(tempArray)
            line = bufferedReader.readLine()
        }
        println(getCheckSum(array))
        println(getSum(array))
    }

    fun getCheckSum(array: ArrayList<ArrayList<Int>>): Int {
        var sum = 0
        for (i in array.indices) {
            var min = Integer.MAX_VALUE
            var max = Integer.MIN_VALUE
            for (j in 0 until array[i].size) {
                min = Math.min(array[i][j], min)
                max = Math.max(array[i][j], max)
            }
            sum += max - min
        }
        return sum
    }

    private fun getSum(array: ArrayList<ArrayList<Int>>): Int {
        var sum = 0
        for (i in array.indices) {
            for (j in 0 until array[i].size - 1) {
                val num = array[i][j]
                for (k in j + 1 until array[i].size) {
                    val num2 = array[i][k]
                    val max = Math.max(num2, num)
                    val min = Math.min(num2, num)
                    if (max % min == 0) {
                        sum += max / min
                        break
                    }
                }
            }
        }
        return sum
    }
}
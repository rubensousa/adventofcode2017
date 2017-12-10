package com.github.rubensousa.adventofcode2017


import java.io.BufferedReader
import java.io.FileReader
import java.io.IOException
import kotlin.collections.ArrayList

object Day10 {

    @Throws(IOException::class)
    @JvmStatic
    fun main(args: Array<String>) {
        val bufferedReader = BufferedReader(FileReader("input.txt"))
        var line: String? = bufferedReader.readLine()
        val data = line?.toCharArray()
        val lengths = ArrayList<Int>()
        if (data != null) {
            for (number in data) {
                lengths.add(number.toInt())
            }
            lengths.add(17)
            lengths.add(31)
            lengths.add(73)
            lengths.add(47)
            lengths.add(23)
            //println(getResult(lengths))
            println(getHash(lengths))
        }
    }

    private fun generateArray() : IntArray{
        val array = IntArray(256)

        for (i in 0 until array.size) {
            array[i] = i
        }

        return array
    }

    private fun getHash(lengths: ArrayList<Int>): String {
        val array = generateArray()
        var position = 0
        var skip = 0

        for (i in 0 until 64) {
            for (length in lengths) {
                reverse(position, length, array)
                position += length + skip
                while (position >= array.size) {
                    position -= array.size
                }
                skip++
            }
        }

        var index = 0
        val builder = StringBuilder()

        for (i in 0 until 16) {
            var value = array[index]
            index++
            for (j in 1 until 16) {
                value = value.xor(array[index])
                index++
            }
            var hex = Integer.toHexString(value)
            if (hex.length < 2) {
                hex = "0" + hex
            }
            builder.append(hex)
        }

        return builder.toString()
    }

    private fun getResult(lengths: ArrayList<Int>): Int {
        val array = generateArray()
        var position = 0
        var skip = 0

        for (i in 0 until array.size) {
            array[i] = i
        }

        for (length in lengths) {
            reverse(position, length, array)
            position += length + skip
            while (position >= array.size) {
                position -= array.size
            }
            skip++
        }

        return array[0] * array[1]
    }

    private fun reverse(position: Int, length: Int, array: IntArray) {
        val reversed = IntArray(length)
        var index = position
        for (i in 0 until length) {
            reversed[reversed.size - 1 - i] = array[index]
            index++
            if (index == array.size) {
                index = 0
            }
        }
        index = position
        for (i in 0 until length) {
            array[index] = reversed[i]
            index++
            if (index == array.size) {
                index = 0
            }
        }
    }

}
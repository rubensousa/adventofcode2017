package com.github.rubensousa.adventofcode2017


import java.io.BufferedReader
import java.io.FileReader
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList

object Day9 {

    @Throws(IOException::class)
    @JvmStatic
    fun main(args: Array<String>) {
        val bufferedReader = BufferedReader(FileReader("input.txt"))
        var line: String? = bufferedReader.readLine()
        if (line != null) {
            println(getScore(line))
        }
    }

    private fun getScore(line: String): Int {
        val list = ArrayDeque<Int>()
        var garbage = 0
        var current = 0
        var score = 0
        var i = 0

        while (i < line.length){
            val c = line[i]
            if (garbage == 0) {
                when (c) {
                    '{' -> {
                        list.addFirst(current + 1)
                        current++
                    }
                    '}' -> {
                        score += list.removeFirst()
                        current--
                    }
                    '<' -> garbage++
                }
            } else if (c == '!') {
                i++
            } else if (c == '>') {
                garbage--
            }
            i++
        }

        return score
    }

    private fun getGarbage(line: String): Int {
        val list = ArrayDeque<Int>()
        var garbage = 0
        var current = 0
        var score = 0
        var i = 0
        var garbageCount = 0

        while (i < line.length){
            val c = line[i]
            if (garbage == 0) {
                when (c) {
                    '{' -> {
                        list.addFirst(current + 1)
                        current++
                    }
                    '}' -> {
                        score += list.removeFirst()
                        current--
                    }
                    '<' -> garbage++
                }
            } else if (c == '!') {
                i++
            } else if (c == '>') {
                garbage--
            }else{
                garbageCount++
            }
            i++
        }

        return garbageCount
    }

}
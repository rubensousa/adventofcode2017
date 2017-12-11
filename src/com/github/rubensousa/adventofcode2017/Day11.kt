package com.github.rubensousa.adventofcode2017

import java.io.BufferedReader
import java.io.FileReader
import java.io.IOException
import kotlin.collections.ArrayList

object Day11 {

    val NORTH = 0
    val SOUTH = 1
    val NORTHWEST = 2
    val NORTHEAST = 3
    val SOUTHWEST = 4
    val SOUTHEAST = 5

    @Throws(IOException::class)
    @JvmStatic
    fun main(args: Array<String>) {
        val bufferedReader = BufferedReader(FileReader("inputday11.txt"))
        var line: String? = bufferedReader.readLine()
        if (line != null) {
            val directions = parseDirections(line)
            println(getSteps(directions))
            println(getFurthestSteps(directions))
        }
    }

    private fun getFurthestSteps(directions: ArrayList<Int>): Int {
        var x = 0.0
        var y = 0.0
        var steps = 0
        for (direction in directions) {
            when (direction) {
                NORTH -> {
                    y++
                }
                SOUTH -> {
                    y--
                }
                NORTHEAST -> {
                    x += 0.5f
                    y += 0.5f
                }
                NORTHWEST -> {
                    x -= 0.5f
                    y += 0.5f
                }
                SOUTHWEST -> {
                    x -= 0.5f
                    y -= 0.5f
                }
                SOUTHEAST -> {
                    x += 0.5f
                    y -= 0.5f
                }
            }
            steps = Math.max(steps.toDouble(), Math.floor(Math.abs(y) + Math.abs(x))).toInt()
        }
        return steps
    }

    private fun getSteps(directions: ArrayList<Int>): Int {
        var x = 0.0
        var y = 0.0
        for (direction in directions) {
            when (direction) {
                NORTH -> {
                    y++
                }
                SOUTH -> {
                    y--
                }
                NORTHEAST -> {
                    x += 0.5f
                    y += 0.5f
                }
                NORTHWEST -> {
                    x -= 0.5f
                    y += 0.5f
                }
                SOUTHWEST -> {
                    x -= 0.5f
                    y -= 0.5f
                }
                SOUTHEAST -> {
                    x += 0.5f
                    y -= 0.5f
                }
            }
        }
        return Math.floor(Math.abs(y) + Math.abs(x)).toInt()
    }

    private fun parseDirections(data: String): ArrayList<Int> {
        val directions = data.split(",")
        val array = ArrayList<Int>()
        for (direction in directions) {
            val value = when (direction) {
                "n" -> NORTH
                "s" -> SOUTH
                "nw" -> NORTHWEST
                "ne" -> NORTHEAST
                "sw" -> SOUTHWEST
                "se" -> SOUTHEAST
                else -> NORTH
            }
            array.add(value)
        }
        return array
    }

}
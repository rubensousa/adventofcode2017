package com.github.rubensousa.adventofcode2017

import java.io.BufferedReader
import java.io.FileReader
import kotlin.collections.ArrayList

object Day21 {

    data class Pattern(val input: Array<CharArray>, val output: Array<CharArray>) {

        fun rotate() {
            val size = input.size
            for (layer in 0 until size / 2) {
                val last = size - 1 - layer
                for (i in 0 until last) {
                    val offset = i - layer
                    val top = input[layer][i]
                    input[layer][i] = input[last - offset][layer]
                    input[last - offset][layer] = input[last][last - offset]
                    input[last][last - offset] = input[i][last]
                    input[i][last] = top
                }
            }
        }

        fun flip() {
            val half = Math.floor((input.size / 2.0) + 1).toInt()
            for (j in 0 until half) {
                for (i in 0 until half) {
                    val temp = input[input.size - 1 - i][j]
                    input[input.size - 1 - i][j] = input[input.size - 1 - i][input.size - 1 - j]
                    input[input.size - 1 - i][input.size - 1 - j] = temp
                }
            }
        }
    }

    private fun parseGrid(data: String): Array<CharArray> {
        val size = when (data.length) {
            5 -> 2
            11 -> 3
            else -> 4
        }
        val grid = Array(size, { CharArray(size) })
        val lines = data.split("/")
        for (i in lines.indices) {
            grid[i] = lines[i].toCharArray()
        }
        return grid
    }

    private fun parsePatterns(): ArrayList<Pattern> {
        val reader = BufferedReader(FileReader("inputday21.txt"))
        val particles = ArrayList<Pattern>()
        var line = reader.readLine()
        while (line != null) {
            val data = line.split(" => ")
            val input = parseGrid(data[0])
            val output = parseGrid(data[1])
            line = reader.readLine()
            particles.add(Pattern(input, output))
        }
        return particles
    }

    /**
     * .#.
     * ..#
     * ###
     */
    private fun getInitialState(): Array<CharArray> {
        val grid = Array(3, { CharArray(3) })
        grid[0][0] = '.'
        grid[0][1] = '#'
        grid[0][2] = '.'
        grid[1][0] = '.'
        grid[1][1] = '.'
        grid[1][2] = '#'
        grid[2][0] = '#'
        grid[2][1] = '#'
        grid[2][2] = '#'
        return grid
    }

    @JvmStatic
    fun main(args: Array<String>) {
        val patterns = parsePatterns()
        println(getPixelsOn(patterns, getInitialState(), 5))
        println(getPixelsOn(patterns, getInitialState(), 18))
    }

    private fun getPixelsOn(patterns: ArrayList<Pattern>, grid: Array<CharArray>, iterations: Int): Int {
        var count = 0
        var currentGrid = grid
        repeat(iterations) {
            currentGrid = expand(patterns, currentGrid)
        }
        for (i in currentGrid.indices) {
            for (j in currentGrid.indices) {
                if (currentGrid[i][j] == '#') {
                    count++
                }
            }
        }
        return count
    }

    private fun expand(patterns: ArrayList<Pattern>, grid: Array<CharArray>): Array<CharArray> {
        val newSize = if (grid.size % 2 == 0) {
            grid.size / 2 * 3
        } else {
            grid.size / 3 * 4
        }
        val childSize = 2 + grid.size % 2
        val newChildSize = 3 + grid.size % 2
        val totalChilds = (grid.size * grid.size) / (childSize * childSize)
        var currentChild = 0
        val newGrid = Array(newSize, { CharArray(newSize) })
        val slicedGrid = Array(childSize, { CharArray(childSize) })
        var slicedI = 0
        var slicedJ = 0
        var newI = 0
        var newJ = 0
        while (currentChild < totalChilds) {
            for (i in 0 until childSize) {
                for (j in 0 until childSize) {
                    slicedGrid[i][j] = grid[i + slicedI][j + slicedJ]
                }
            }
            for (pattern in patterns) {
                if (doesPatternMatch(pattern, slicedGrid)) {
                    for (i in 0 until newChildSize) {
                        for (j in 0 until newChildSize) {
                            newGrid[newI + i][newJ + j] = pattern.output[i][j]
                        }
                    }
                    break
                }
            }
            slicedJ += childSize
            newJ += newChildSize
            if (slicedJ >= grid.size) {
                slicedJ = 0
                slicedI += childSize
            }
            if (newJ >= newSize) {
                newJ = 0
                newI += newChildSize
            }
            currentChild++
        }

        return newGrid
    }

    private fun doesPatternMatch(pattern: Pattern, grid: Array<CharArray>): Boolean {
        if (pattern.input.size != grid.size) {
            return false
        }
        if (isPatternTheSameAs(pattern, grid)) {
            return true
        }
        repeat(4) {
            pattern.rotate()
            if (isPatternTheSameAs(pattern, grid)) {
                return true
            }
        }
        repeat(4) {
            pattern.flip()
            if (isPatternTheSameAs(pattern, grid)) {
                return true
            }
            pattern.flip()
            pattern.rotate()
        }
        return false
    }

    private fun isPatternTheSameAs(pattern: Pattern, grid: Array<CharArray>): Boolean {
        for (i in pattern.input.indices) {
            for (j in pattern.input.indices) {
                if (pattern.input[i][j] != grid[i][j]) {
                    return false
                }
            }
        }
        return true
    }
}
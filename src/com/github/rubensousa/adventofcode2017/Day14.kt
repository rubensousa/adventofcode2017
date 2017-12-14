package com.github.rubensousa.adventofcode2017

import java.io.IOException
import java.util.*

object Day14 {

    data class Direction(val i: Int, val j: Int)

    @Throws(IOException::class)
    @JvmStatic
    fun main(args: Array<String>) {
        val builder = StringBuilder()
        for (i in 0 until 128) {
            val hash = Day10.getHash("amgozmfv-" + i.toString())
            val binary = getBinaryString(hash)
            builder.append(binary)
        }
        println(findOnes(builder.toString()))
        val grid = buildGrid(builder.toString())
        println(findRegions(grid))
    }

    private fun findRegions(grid: Array<IntArray>): Int {
        var count = 0
        for (i in grid.indices) {
            for (j in grid.indices) {
                if (grid[i][j] == 1) {
                    val directions = getValidDirections(i, j, grid)
                    count++
                    while (!directions.empty()) {
                        val direction = directions.pop()
                        grid[direction.i][direction.j] = 0
                        directions.addAll(getValidDirections(direction.i, direction.j, grid))
                    }
                }
            }
        }
        return count
    }

    private fun getValidDirections(i: Int, j: Int, grid: Array<IntArray>): Stack<Direction> {
        val directions = Stack<Direction>()
        if (i > 0 && grid[i - 1][j] == 1) {
            directions.add(Direction(i - 1, j))
        }
        if (j > 0 && grid[i][j - 1] == 1) {
            directions.add(Direction(i, j - 1))
        }
        if (i < grid.size - 1 && grid[i + 1][j] == 1) {
            directions.add(Direction(i + 1, j))
        }
        if (j < grid.size - 1 && grid[i][j + 1] == 1) {
            directions.add(Direction(i, j + 1))
        }
        return directions
    }

    private fun buildGrid(binary: String): Array<IntArray> {
        val grid = Array(128, { IntArray(128) })
        val chars = binary.toCharArray()
        var index = 0
        for (i in 0 until 128) {
            for (j in 0 until 128) {
                grid[i][j] = Character.getNumericValue(chars[index])
                index++
            }
        }
        return grid
    }

    private fun findOnes(binary: String): Int {
        var count = 0
        var chars = binary.toCharArray()
        for (i in chars.indices) {
            if (chars[i] == '1') {
                count++
            }
        }
        return count
    }

    private fun getBinaryString(hash: String): String {
        val builder = StringBuilder()
        for (i in 0 until hash.length) {
            builder.append(getBinaryDigit(hash.substring(i, i + 1)))
        }
        return builder.toString()
    }

    private fun getBinaryDigit(hex: String): String {
        val number = Integer.parseInt(hex, 16)
        var result = Integer.toBinaryString(number)
        for (i in result.length until 4) {
            result = "0" + result
        }
        return result
    }
}
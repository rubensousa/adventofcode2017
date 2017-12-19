package com.github.rubensousa.adventofcode2017

import java.io.BufferedReader
import java.io.FileReader

object Day19 {

    val DIRECTION_NORTH = 0
    val DIRECTION_SOUTH = 1
    val DIRECTION_EAST = 2
    val DIRECTION_WEST = 3

    val CHAR_CORNER = '+'
    val CHAR_VERTICAL = '|'
    val CHAR_HORIZONTAL = '-'

    data class NavigationResult(val x: Int, val y: Int, val direction: Int, val data: Char, val steps: Int)

    private fun parseGrid(): Array<CharArray> {
        val reader = BufferedReader(FileReader("inputday19.txt"))
        val lines = ArrayList<String>()
        var line = reader.readLine()
        while (line != null) {
            lines.add(line)
            line = reader.readLine()
        }

        val grid = Array(lines.size, { CharArray(lines.size) })

        for (i in lines.indices) {
            grid[i] = lines[i].toCharArray()
        }

        return grid
    }

    @JvmStatic
    fun main(args: Array<String>) {
        val grid = parseGrid()
        println(navigateGrid(grid))
        println(navigateGridSteps(grid))
    }

    private fun navigateGrid(grid: Array<CharArray>): String {
        val builder = StringBuilder()
        var currentX = 0
        var currentY = 0

        // Find start position
        for (j in 0 until grid.size) {
            if (grid[0][j] == CHAR_VERTICAL) {
                currentX = j
                break
            }
        }

        var navigationResult = navigate(grid, currentX, currentY, DIRECTION_SOUTH)

        while (navigationResult.data.isLetter()) {
            builder.append(navigationResult.data)
            currentX = navigationResult.x
            currentY = navigationResult.y
            when (navigationResult.direction) {
                DIRECTION_SOUTH -> currentY++
                DIRECTION_NORTH -> currentY--
                DIRECTION_EAST -> currentX++
                DIRECTION_WEST -> currentX--
            }
            if (!isCharFromPath(grid[currentY][currentX])) {
                break
            }
            navigationResult = navigate(grid, currentX, currentY, navigationResult.direction)
        }

        return builder.toString()
    }

    private fun navigateGridSteps(grid: Array<CharArray>): Int {
        var steps = 1
        var currentX = 0
        var currentY = 0

        // Find start position
        for (j in 0 until grid.size) {
            if (grid[0][j] == CHAR_VERTICAL) {
                currentX = j
                break
            }
        }

        var navigationResult = navigate(grid, currentX, currentY, DIRECTION_SOUTH)

        while (navigationResult.data.isLetter()) {
            steps += navigationResult.steps
            currentX = navigationResult.x
            currentY = navigationResult.y
            when (navigationResult.direction) {
                DIRECTION_SOUTH -> currentY++
                DIRECTION_NORTH -> currentY--
                DIRECTION_EAST -> currentX++
                DIRECTION_WEST -> currentX--
            }
            if (!isCharFromPath(grid[currentY][currentX])) {
                break
            }
            steps++
            navigationResult = navigate(grid, currentX, currentY, navigationResult.direction)
        }

        return steps
    }

    private fun navigate(grid: Array<CharArray>, x: Int, y: Int, direction: Int): NavigationResult {
        var currentDirection = direction
        var currentX = x
        var currentY = y
        var steps = 0
        while (true) {
            if (grid[currentY][currentX].isLetter()) {
                return NavigationResult(currentX, currentY, currentDirection, grid[currentY][currentX], steps)
            } else if (grid[currentY][currentX] == CHAR_CORNER) {
                val result = getNextDirection(grid, currentX, currentY, currentDirection)
                if (result.x == currentX && result.y == currentY) {
                    return NavigationResult(currentX, currentY, currentDirection, grid[currentY][currentX], steps)
                }
                currentDirection = result.direction
                currentX = result.x
                currentY = result.y
                steps++
            } else {
                when (currentDirection) {
                    DIRECTION_SOUTH -> currentY++
                    DIRECTION_NORTH -> currentY--
                    DIRECTION_EAST -> currentX++
                    DIRECTION_WEST -> currentX--
                }
                steps++
            }
        }
    }

    private fun getNextDirection(grid: Array<CharArray>, x: Int, y: Int, currentDirection: Int): NavigationResult {
        if (currentDirection == DIRECTION_EAST || currentDirection == DIRECTION_WEST) {
            if (isCharFromPath(grid[y - 1][x])) {
                return NavigationResult(x, y - 1, DIRECTION_NORTH, grid[y - 1][x], 0)
            }
            if (isCharFromPath(grid[y + 1][x])) {
                return NavigationResult(x, y + 1, DIRECTION_SOUTH, grid[y + 1][x], 0)
            }
        } else {
            if (isCharFromPath(grid[y][x + 1])) {
                return NavigationResult(x + 1, y, DIRECTION_EAST, grid[y][x + 1], 0)
            }
            if (isCharFromPath(grid[y][x - 1])) {
                return NavigationResult(x - 1, y, DIRECTION_WEST, grid[y][x - 1], 0)
            }
        }
        return NavigationResult(x, y, DIRECTION_SOUTH, grid[y][x], 0)
    }

    private fun isCharFromPath(char: Char): Boolean {
        return char == CHAR_VERTICAL || char == CHAR_HORIZONTAL
    }
}
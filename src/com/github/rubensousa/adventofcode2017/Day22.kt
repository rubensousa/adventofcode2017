package com.github.rubensousa.adventofcode2017

import java.io.BufferedReader
import java.io.FileReader


object Day22 {

    val DIRECTION_UP = 1
    val DIRECTION_DOWN = -1
    val DIRECTION_LEFT = 2
    val DIRECTION_RIGHT = -2


    data class Coordinate(var x: Long, var y: Long) {

        companion object {
            val STATE_CLEAN = 0
            val STATE_WEAKENED = 1
            val STATE_INFECTED = 2
            val STATE_FLAGGED = 3
        }

        var state = STATE_CLEAN
    }

    private fun parseGrid(): Array<CharArray> {
        val reader = BufferedReader(FileReader("inputday22.txt"))
        var line = reader.readLine()
        val grid = Array(line.length, { CharArray(line.length) })
        var index = 0
        while (line != null) {
            for (i in line.indices) {
                grid[index][i] = line[i]
            }
            index++
            line = reader.readLine()
        }

        return grid
    }

    @JvmStatic
    fun main(args: Array<String>) {
        val grid = parseGrid()
        println(getTotalBursts(grid))
        println(getTotalBurstsPart2(grid))
    }

    private fun getTotalBursts(grid: Array<CharArray>): Long {
        var bursts = 0L
        val infected = HashSet<Coordinate>()
        var currentCoordinate = Coordinate(grid.size / 2L, grid.size / 2L)
        var currentDirection = DIRECTION_UP

        for (i in grid.indices) {
            for (j in grid.indices) {
                if (grid[i][j] == '#') {
                    infected.add(Coordinate(j.toLong(), i.toLong()))
                }
            }
        }
        repeat(10000) {
            if (infected.contains(currentCoordinate)) {
                infected.remove(currentCoordinate)
                currentCoordinate = Coordinate(currentCoordinate.x, currentCoordinate.y)
                currentDirection = moveRight(currentCoordinate, currentDirection)
            } else {
                infected.add(currentCoordinate)
                currentCoordinate = Coordinate(currentCoordinate.x, currentCoordinate.y)
                currentDirection = moveLeft(currentCoordinate, currentDirection)
                bursts++
            }
        }
        return bursts
    }


    private fun getTotalBurstsPart2(grid: Array<CharArray>): Long {
        var bursts = 0L
        val infected = HashSet<Coordinate>()
        val weakened = HashSet<Coordinate>()
        val flagged = HashSet<Coordinate>()
        var currentCoordinate = Coordinate(grid.size / 2L, grid.size / 2L)
        var currentDirection = DIRECTION_UP

        for (i in grid.indices) {
            for (j in grid.indices) {
                if (grid[i][j] == '#') {
                    val coordinate = Coordinate(j.toLong(), i.toLong())
                    coordinate.state = Coordinate.STATE_INFECTED
                    infected.add(coordinate)
                }
            }
        }
        repeat(10000000) {
            when {
                weakened.contains(currentCoordinate) -> {
                    weakened.remove(currentCoordinate)
                    infected.add(currentCoordinate)
                    currentCoordinate.state = Coordinate.STATE_INFECTED
                    currentCoordinate = Coordinate(currentCoordinate.x, currentCoordinate.y)
                    move(currentCoordinate, currentDirection)
                    bursts++
                }
                infected.contains(currentCoordinate) -> {
                    infected.remove(currentCoordinate)
                    flagged.add(currentCoordinate)
                    currentCoordinate.state = Coordinate.STATE_FLAGGED
                    currentCoordinate = Coordinate(currentCoordinate.x, currentCoordinate.y)
                    currentDirection = moveRight(currentCoordinate, currentDirection)
                }
                flagged.contains(currentCoordinate) -> {
                    flagged.remove(currentCoordinate)
                    currentCoordinate.state = Coordinate.STATE_CLEAN
                    currentCoordinate = Coordinate(currentCoordinate.x, currentCoordinate.y)
                    currentDirection = reverse(currentCoordinate, currentDirection)
                }
                else -> { // Clean state
                    weakened.add(currentCoordinate)
                    currentCoordinate.state = Coordinate.STATE_WEAKENED
                    currentCoordinate = Coordinate(currentCoordinate.x, currentCoordinate.y)
                    currentDirection = moveLeft(currentCoordinate, currentDirection)
                }
            }
        }
        return bursts
    }

    private fun move(coordinate: Coordinate, direction: Int) {
        when (direction) {
            DIRECTION_UP -> coordinate.y--
            DIRECTION_DOWN -> coordinate.y++
            DIRECTION_LEFT -> coordinate.x--
            DIRECTION_RIGHT -> coordinate.x++
        }
    }

    private fun reverse(coordinate: Coordinate, direction: Int): Int {
        val newDirection = direction * -1
        move(coordinate, newDirection)
        return newDirection
    }

    private fun moveRight(coordinate: Coordinate, direction: Int): Int {
        when (direction) {
            DIRECTION_UP -> {
                coordinate.x++
                return DIRECTION_RIGHT
            }
            DIRECTION_DOWN -> {
                coordinate.x--
                return DIRECTION_LEFT
            }
            DIRECTION_RIGHT -> {
                coordinate.y++
                return DIRECTION_DOWN
            }
            DIRECTION_LEFT -> {
                coordinate.y--
                return DIRECTION_UP
            }
            else -> return DIRECTION_UP
        }
    }

    private fun moveLeft(coordinate: Coordinate, direction: Int): Int {
        when (direction) {
            DIRECTION_UP -> {
                coordinate.x--
                return DIRECTION_LEFT
            }
            DIRECTION_DOWN -> {
                coordinate.x++
                return DIRECTION_RIGHT
            }
            DIRECTION_RIGHT -> {
                coordinate.y--
                return DIRECTION_UP
            }
            DIRECTION_LEFT -> {
                coordinate.y++
                return DIRECTION_DOWN
            }
            else -> return DIRECTION_DOWN
        }
    }

}
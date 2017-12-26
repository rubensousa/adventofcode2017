package com.github.rubensousa.adventofcode2017


object Day25 {

    enum class State {
        A, B, C, D, E, F
    }

    data class WriteResult(val x: Long, val value: Int, val state: State)

    @JvmStatic
    fun main(args: Array<String>) {
        println(getChecksum())
    }

    private fun getChecksum(): Long {
        val coordinates = HashMap<Long, Int>()
        var currentState = State.A
        var currentCoordinate = 0L
        coordinates.put(0, 0)

        repeat(12459852) {
            var value = 0
            if (coordinates[currentCoordinate] != null) {
                value = coordinates[currentCoordinate]!!
            }
            val result = writeTape(currentState, currentCoordinate, value)
            coordinates.put(currentCoordinate, result.value)
            currentCoordinate = result.x
            currentState = result.state
        }

        var ones = 0L

        for (value in coordinates.values) {
            if (value == 1) {
                ones++
            }
        }
        return ones
    }

    private fun writeTape(state: State, coordinate: Long, value: Int): WriteResult {
        when (state) {
            State.A -> {
                return if (value == 0) {
                    WriteResult(coordinate + 1, 1, State.B)
                } else {
                    WriteResult(coordinate - 1, 1, State.E)
                }
            }
            State.B -> {
                return if (value == 0) {
                    WriteResult(coordinate + 1, 1, State.C)
                } else {
                    WriteResult(coordinate + 1, 1, State.F)
                }
            }
            State.C -> {
                return if (value == 0) {
                    WriteResult(coordinate - 1, 1, State.D)
                } else {
                    WriteResult(coordinate + 1, 0, State.B)
                }
            }
            State.D -> {
                return if (value == 0) {
                    WriteResult(coordinate + 1, 1, State.E)
                } else {
                    WriteResult(coordinate - 1, 0, State.C)
                }
            }
            State.E -> {
                return if (value == 0) {
                    WriteResult(coordinate - 1, 1, State.A)
                } else {
                    WriteResult(coordinate + 1, 0, State.D)
                }
            }
            State.F -> {
                return if (value == 0) {
                    WriteResult(coordinate + 1, 1, State.A)
                } else {
                    WriteResult(coordinate + 1, 1, State.C)
                }
            }
        }
    }
}
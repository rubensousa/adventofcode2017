package com.github.rubensousa.adventofcode2017


object Day17 {

    @JvmStatic
    fun main(args: Array<String>) {
        println(spin(337))
        println(spinPart2(337))
    }

    private fun spin(times: Int): Int {
        val array = ArrayList<Int>()
        array.add(0)
        var position = 0
        for (i in 1 until 2018) {
            position = getStepPosition(position, i, times)
            array.add(position, i)
        }
        return array[position + 1]
    }

    private fun spinPart2(times: Int): Int {
        var position = 0
        var valueAtFirstPosition = 0
        for (i in 1 until 50000000) {
            position = getStepPosition(position, i, times)
            if (position == 1) {
                valueAtFirstPosition = i
            }
        }
        return valueAtFirstPosition
    }

    private fun getStepPosition(currentPosition: Int, size: Int, times: Int): Int {
        return (currentPosition + times) % size + 1
    }

}
package com.github.rubensousa.adventofcode2017


object Day03 {

    @JvmStatic
    fun main(args: Array<String>) {
        val input = 325489
        println(getDistanceSpiral(input))
        println(getSpiralGreatest(input))
    }

    private fun getDistanceSpiral(number: Int): Int {
        var squareRoot = Math.ceil(Math.sqrt(number.toDouble())).toInt()
        if (squareRoot % 2 == 0) {
            squareRoot++
        }
        val sideDistance = squareRoot + 1
        val bottomRight = squareRoot * squareRoot
        val bottomLeft = bottomRight - sideDistance
        val topLeft = bottomLeft - sideDistance
        val topRight = topLeft - sideDistance
        val distanceToMiddle = squareRoot / 2
        val middle: Int
        middle = when {
            number < topRight -> topRight - distanceToMiddle
            number < topLeft -> topLeft - distanceToMiddle
            number < bottomLeft -> bottomLeft - distanceToMiddle
            else -> bottomRight - distanceToMiddle
        }
        return Math.abs(number - middle) + distanceToMiddle
    }

    private fun getSpiralGreatest(number: Int): Int {
        var squareRoot = Math.ceil(Math.sqrt(number.toDouble())).toInt()
        if (squareRoot % 2 == 0) {
            squareRoot++
        }
        val array = Array(squareRoot) { IntArray(squareRoot) }
        array[squareRoot / 2][squareRoot / 2] = 1
        var currentRoot = 3
        var i = squareRoot / 2
        var j = squareRoot / 2 + 1
        var num = 2
        while (currentRoot <= squareRoot) {
            val bottomRight = currentRoot * currentRoot
            val bottomLeft = bottomRight - currentRoot + 1
            val topLeft = bottomLeft - currentRoot + 1
            val topRight = topLeft - currentRoot + 1
            while (num <= bottomRight) {
                val sum = sumNeighbors(array, i, j)
                array[i][j] = sum
                when {
                    num < topRight -> i--
                    num < topLeft -> j--
                    num < bottomLeft -> i++
                    else -> j++
                }
                if (sum > number) {
                    return sum
                }
                num++
            }
            currentRoot += 2
        }
        return 0
    }

    private fun sumNeighbors(array: Array<IntArray>, i: Int, j: Int): Int {
        var sum = 0

        // Sum elements before
        if (i >= 1) {
            sum += array[i - 1][j]
            if (j >= 1) {
                sum += array[i - 1][j - 1]
            }
            if (j < array.size - 1) {
                sum += array[i - 1][j + 1]
            }
        }

        // Sum elements in the same line
        if (j >= 1) {
            sum += array[i][j - 1]
        }

        if (j < array.size - 1) {
            sum += array[i][j + 1]
        }

        // Sum elements after
        if (i < array.size - 1) {
            sum += array[i + 1][j]
            if (j >= 1) {
                sum += array[i + 1][j - 1]
            }
            if (j < array.size - 1) {
                sum += array[i + 1][j + 1]
            }
        }

        return sum
    }
}